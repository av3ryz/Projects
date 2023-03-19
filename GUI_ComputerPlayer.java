import javax.swing.*;
import java.lang.Math; //java libraries to get random numbers
import java.util.ArrayList;
import java.util.Random;

/**
 * Class ComputerPlayer inherits class Player and creates the AI player with attributes from class Player.
 * It will have methods to help the computer make an easy/medium/hard move against the human player.
 */
public class GUI_ComputerPlayer extends GUI_Players {

    //variable unique to ComputerPlayer, a mode number for easy, medium, or hard,
    private int numMode;

    //declare variables for medium mode
    private int colRandNum;
    static private int closeWin; //declare variable to check for 3 in a row (computer's move would be to either win or block)
    static private ArrayList<Integer> rowList = new ArrayList<>(); //rowList and colList add the row and col of the 3 connecting pieces
    static private ArrayList<Integer> colList = new ArrayList<>();

    ArrayList<Integer> availableX = new ArrayList<>(); //availableX and availableY................
    ArrayList<Integer> availableY = new ArrayList<>();
    int currentScore;
    int winner;
    //static char [] searchResult = new char[4];
    ArrayList<Integer> scores = new ArrayList<>();

    //computer player constructor
    public GUI_ComputerPlayer(String defaultName, char defaultColour, int playNum, int defaultScore, ImageIcon defaultChip, int numM) {
        super(defaultName, defaultColour, playNum, defaultScore, defaultChip); //super allows ComputerPlayer to inherit attributes from Player
        numMode = numM;
    }

    //getter and setter method for computer player's mode number
    public int getNumMode() {
        return numMode;
    }
    public void setNumMode(int numM) {
        numMode = numM;
    }


    /**
     * easyDrop method will generate a random column number, checks
     * if the column is full, and will repeat until a piece is dropped
     */
    public void easyDrop(int r, char[][] gridBoard) {
        boolean dropped = false;

        //while loop generates new column number if column is full
        while (dropped == false) {
            colRandNum = (int) (Math.random() * 7) + 1;

            //for loop goes through each row and the same column that randomly generated
            for (int i = r-1; i >= 0; i--) {
                if (gridBoard[i][colRandNum-1] == '_') { //if column is available to drop in,
                    gridBoard[i][colRandNum-1] = Connect4_GUI.aiPlayer.getColour();
                    Connect4_GUI.gridButtons[i][colRandNum-1].setIcon(Connect4_GUI.aiPlayer.getChip());
                    System.out.println("\n" + name + " chose column " + colRandNum);
                    dropped = true;
                    break;
                }
            }

        } // end while
    }

    /**
     * hBlock will check for 3 in a rows, and will add a piece on the
     * left or right side of the connecting 3 to block/win. Returns
     * boolean variable determined by whether a piece was placed
     */
    public static boolean hBlock(char[][] gridBoard, GUI_Players player) {
        boolean dropped = false;

        //nested for loop goes through col index 0 to 6 and row index 5 to 0
        for (int r1 = GUI_Board.ROWS-1; r1 >= 0 ; r1--) {
            closeWin = 0; //reset closeWin to 0 after each row check
            for (int c1 = 0; c1 < gridBoard[r1].length; c1++) {
                if (gridBoard[r1][c1] == player.getColour()) {
                    closeWin++;
                    rowList.add(r1); //add the rows and cols that are in the connecting 3
                    colList.add(c1);
                } else {
                    closeWin = 0;
                    rowList.clear();
                    colList.clear();
                }

                if (closeWin == 3) {
                    int len = rowList.size();
                    int rF = rowList.get(0); //first row
                    int rL = rowList.get(len-1); //last row
                    int cF = colList.get(0); //first col
                    int cL = colList.get(len-1); //last col
                    int filled1 = 0;
                    int filled2 = 0;

                    //if the first and last piece of the connecting 3 aren't in index col 0 or 6, then pieces can be added to block the left or right
                    if (cF > 0 && cL < 6) {
                        //for loop starts from bottom row, goes up, checking if the column is filled enough to add piece blocking the third
                        for (int i = 5; i >= rL; i--) { //int i stops when it reaches the row of the connecting 3
                            if (gridBoard[i][cF-1] != '_') {
                                filled1++; //increment if spots to the left are filled
                            }
                            if (gridBoard[i][cL+1] != '_') {
                                filled2++; //increment if spots to the right are filled
                            }
                        }

                        //if the right amount of spots are filled, a piece can be added if the position left of piece is empty
                        if (filled1 == (5-rF) && gridBoard[rF][cF-1] == '_') {
                            gridBoard[rF][cF-1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rF][cF-1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cF)+">");
                            dropped = true;
                            return dropped;
                        }

                        //if the right amount of spots are filled, a piece can be added if the position right of piece is empty
                        if (dropped == false && filled2 == (5-rL) && gridBoard[rL][cL+1] == '_') {
                            gridBoard[rL][cL+1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rL][cL+1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cL+2)+">");
                            dropped = true;
                            return dropped;
                        }

                    } else if (cF == 0) { //if connecting 3 starts from column index 0, it can only be blocked on the right
                        for (int i = 5; i >= rL; i--) { //int i stops when it reaches the row of the connecting 3
                            if (gridBoard[i][cL+1] != '_') { //check how many positions are filled in column to the right
                                filled1++;
                            }
                        }

                        //if the right amount of spots are filled, a piece can be added if the position right of ending piece is empty
                        if (filled1 == (5-rL) && gridBoard[rL][cL+1] == '_') {
                            gridBoard[rL][cL+1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rL][cL+1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cL+2)+">");
                            dropped = true;
                            return dropped;
                        }

                    } else if (cL == 6) { //if connecting 3 end at column index 6, it can only be blocked on the left
                        for (int i = 5; i >= rF; i--) { //int i stops when it reaches the row of the connecting 3
                            if (gridBoard[i][cF-1] != '_') { //check how many positions are filled in column to the left
                                System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cF)+">");
                                filled1++;
                            }
                        }

                        //if the right amount of spots are filled, a piece can be added if the position left of starting piece is empty
                        if (filled1 == (5-rF) && gridBoard[rF][cF-1] == '_') {
                            gridBoard[rF][cF-1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rF][cF-1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cF)+">");
                            dropped = true;
                            return dropped;
                        }
                    }
                }
            }
        }
        return dropped;
    }

    /**
     * vBlock will check for 3 in a rows vertically, and will add a piece on the
     * top of the connecting 3 to block/win. Returns boolean variable
     * determined by whether a piece was placed
     */
    public static boolean vBlock(char[][] gridBoard, GUI_Players player) {
        boolean dropped = false;

        //nested for loop goes through each row in the same one column to check vertically for connecting 3
        for (int c2 = 0; c2 < GUI_Board.COLS; c2++) {
            for (int r2 = GUI_Board.ROWS-1; r2 >= 0; r2--) {
                if (gridBoard[r2][c2] == player.getColour()) {
                    closeWin++;
                    rowList.add(r2);
                    colList.add(c2);
                } else {
                    closeWin = 0;
                    rowList.clear();
                    colList.clear();
                }

                if (closeWin == 3) {
                    int len = rowList.size();
                    int rL = rowList.get(len-1); //last row
                    int cL = colList.get(len-1); //last col

                    //if the last index position at the top is not in index row 0 (top row), a token can be added
                    if (rL > 0 && gridBoard[rL-1][cL] == '_') {
                        gridBoard[rL-1][cL] = Connect4_GUI.aiPlayer.getColour();
                        Connect4_GUI.gridButtons[rL-1][cL].setIcon(Connect4_GUI.aiPlayer.getChip());
                        System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cL+1)+">");
                        dropped = true;
                        return dropped;
                    }
                }
            }
        } //end of nested for loop
        return dropped;
    }

    /**
     * diagBottomUpBlock will take in each row and column a player's piece is found in.
     * From there, it will check if there are connecting 3 from the bottom to the
     * top left of the piece, each row up and each column to the right.
     * Method will block/win with the diagonal connecting 3. Returns boolean variable
     * determined by whether a piece was placed
     */
    public static boolean diagBottomUpBlock(char[][] gridBoard, GUI_Players player, int r, int c) {
        boolean dropped = false;
        closeWin = 0;
        rowList.clear();
        colList.clear();

        //for loop will loop a maximum of 3 times
        for (int j = c; j <= c+2; j++) {
            if (r >= 0 && j <= 6) { //if column and row are valid
                if (gridBoard[r][j] == player.getColour()) {
                    closeWin++;
                    rowList.add(r);
                    colList.add(j);
                } else {
                    closeWin = 0;
                    rowList.clear();
                    colList.clear();
                }

                if (closeWin == 3) {
                    int len = rowList.size();
                    int rF = rowList.get(0); //first row
                    int rL = rowList.get(len-1); //last row
                    int cF = colList.get(0); //first col
                    int cL = colList.get(len-1); //last col
                    int filled1 = 0;
                    int filled2 = 0;

                    //if connecting 3 is BETWEEN row 1 and 5, piece can be added either at the start OR end of the connecting 3
                    if (rF < 5 && rL > 0 && cL < 6 && cF > 0) {

                        //for loop checks if a piece can be added to the right (end)
                        for (int i = 5; i >= rL; i--) {
                            if (gridBoard[i][cL+1] != '_') { //check how many positions in the column to the right is filled
                                filled1++;
                            }
                        }

                        //if filled up enough to add a valid piece to the end of the diagonal (one row up, one column right)
                        if (filled1 == (5-rL+1) && gridBoard[rL-1][cL+1] == '_') { //if empty
                            gridBoard[rL-1][cL+1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rL-1][cL+1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cL+2)+">");
                            dropped = true;
                            return dropped;
                        }

                        //for loop checks if a piece can be added to the left (start)
                        for (int k = 5; k >= rF; k--) {
                            if (gridBoard[k][cF-1] != '_') { //check how many positions in the column to the left is filled
                                filled2++;
                            }
                        }

                        //if filled up enough to add a valid piece to the start of the diagonal (one row down, one column left)
                        if (filled2 == (5-rF-1) && gridBoard[rF+1][cF-1] == '_') { //if empty
                            gridBoard[rF+1][cF-1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rF+1][cF-1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cF)+">");
                            dropped = true;
                            return dropped;
                        }

                    } else if ((rF == 5 && cL < 6) || (cF == 0 && rL > 0)) { //else if connecting 3 starts at last row and before column 7 or starts column 1 and ends before top row
                        //can only add to piece after end of connected 3

                        for (int i = 5; i >= rL; i--) { //checks filled to the right of ending piece
                            if (gridBoard[i][cL+1] != '_') {
                                filled1++;
                            }
                        }

                        //if filled enough, add piece one row up and one column to the right of the ending piece
                        if (filled1 == (5-rL+1) && gridBoard[rL-1][cL+1] == '_') {
                            gridBoard[rL-1][cL+1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rL-1][cL+1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cL+2)+">");
                            dropped = true;
                            return dropped;
                        }

                    } else if ((rL == 0 && cF > 0) || (cL == 6 && rF < 5)) { //else if connecting 3 ends at top row and starting piece column is not at col 1, or if it starts before last row and ends at column 6
                        //can only add to piece before start of connected 3

                        for (int k = 5; k >= rF; k--) { //checks filled to the left of starting piece
                            if (gridBoard[k][cF-1] != '_') {
                                filled2++;
                            }
                        }

                        //if filled enough, add piece one row down and one column to the left of the starting piece
                        if (filled2 == (5-rF-1) && gridBoard[rF+1][cF-1] == '_') {
                            gridBoard[rF+1][cF-1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rF+1][cF-1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cF)+">");
                            dropped = true;
                            return dropped;
                        }
                    }
                }

                r--;
            }
        }
        return dropped;
    }

    /**
     * diagTopDownBlock will take in each row and column a player's piece is found in.
     * From there, it will check if there are connecting 3 from the top of the piece to the
     * bottom right, each row down and each column to the right.Method will block/win with the
     * diagonal connecting 3. Returns boolean variable determined by whether a piece was placed
     */
    public static boolean diagTopDownBlock(char[][] gridBoard, GUI_Players player, int r, int c) {
        boolean dropped = false;
        closeWin = 0;
        rowList.clear();
        colList.clear();

        //for loop will loop a maximum of 3 times
        for (int j = c; j <= c+2; j++) {
            if (r <=5 && j <= 6) {
                if (gridBoard[r][j]== player.getColour()) {
                    closeWin++;
                    rowList.add(r);
                    colList.add(j);
                } else {
                    closeWin = 0;
                    rowList.clear();
                    colList.clear();
                }

                if (closeWin == 3) {
                    int len = rowList.size();
                    int rF = rowList.get(0); //first row
                    int rL = rowList.get(len-1); //last row
                    int cF = colList.get(0); //first col
                    int cL = colList.get(len-1); //last col
                    int filled1 = 0;
                    int filled2 = 0;

                    //if connecting 3 is BETWEEN row 1 and 5, piece can be added either at the start OR end of the connecting 3
                    if (rF > 0 && rL < 5 && cF > 0 && cL < 6) {

                        for (int i = 5; i >= rL; i--) { //check amount filled in the column to the right of ending piece
                            if (gridBoard[i][cL+1] != '_') {
                                filled1++;
                            }
                        }

                        //if filled enough and position is empty, add piece down one row and one column to the right of the ending piece
                        if (filled1 == (5-rL-1) && gridBoard[rL+1][cL+1] == '_') {
                            gridBoard[rL+1][cL+1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rL+1][cL+1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cL+2)+">");
                            dropped = true;
                            return dropped;
                        }

                        for (int k = 5; k >= rF; k--) { //check filled in the column to the left of the starting piece
                            if (gridBoard[k][cF-1] != '_') {
                                filled2++;
                            }
                        }

                        //if filled enough and position is empty, add piece up one row and one column to the left of the starting piece
                        if (filled2 == (5-rF+1) && gridBoard[rF-1][cF-1] == '_') {
                            gridBoard[rF-1][cF-1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rF-1][cF-1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cF)+">");
                            dropped = true;
                            return dropped;
                        }

                    } else if ((rL == 5 && cF > 0) || (rF > 0 && cL == 6)) { //else if the connecting 3 start after column 1 and ends in the last row, or starts before first row and ends in last column
                        //piece can only be added to the left of starting piece, one row up and one column to the left

                        for (int i = 5; i >= rF; i--) {
                            if (gridBoard[i][cF-1] != '_') {
                                filled1++;
                            }
                        }

                        //if filled enough and position is empty, add piece down one row and one column to the left of the starting piece
                        if (filled1 == (5-rF+1) && gridBoard[rF-1][cF-1] == '_') {
                            gridBoard[rF-1][cF-1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rF-1][cF-1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cF)+">");
                            dropped = true;
                            return dropped;
                        }

                    } else if ((cF == 0 && rL > 5) || (rF == 0 && cL < 6)) { //else if the connecting 3 start in column 1 and ends before last row, or starts at top row and ends before last column
                        //piece can only be added to the right of ending piece, one row down and one column to the right

                        for (int k = 5; k >= rL; k--) {
                            if (gridBoard[k][cL+1] != '_') {
                                filled2++;
                            }
                        }

                        //if filled enough and position is empty, add piece up one row and one column to the right of the ending piece
                        if (filled2 == (5-rL-1) && gridBoard[rL+1][cL+1] == '_') {
                            gridBoard[rL+1][cL+1] = Connect4_GUI.aiPlayer.getColour();
                            Connect4_GUI.gridButtons[rL+1][cL+1].setIcon(Connect4_GUI.aiPlayer.getChip());
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(cL+2)+">");
                            dropped = true;
                            return dropped;
                        }
                    }
                }
                r++;
            }
        }
        return dropped;
    }

    /**
     * block method will call all the other block methods, horizontal, vertical, diagonals
     * and will block/win if returned true, meaning AI successfully blocked, or played the winning move
     */
    public boolean block(char[][] gridBoard, ArrayList<GUI_Players> blockList) {
        boolean done = false;

        //for loop goes through each player's pieces to find connecting 3
        for (int i = 0; i < blockList.size(); i++) {
            closeWin = 0;
            rowList.clear();
            colList.clear();
            done = hBlock(gridBoard, blockList.get(i));
            if (done == true) {
                Connect4_GUI.countMoves++;
                return done;
            }
            done = vBlock(gridBoard, blockList.get(i));
            if (done == true) {
                Connect4_GUI.countMoves++;
                return done;
            }
            //nested for loop accesses each element in gridBoard starting from bottom row
            for (int r = 5; r >= 0; r--) {
                for (int c = 0; c < GUI_Board.COLS; c++) {
                    if (gridBoard[r][c] == blockList.get(i).getColour()) {
                        //check diagonal bottom to top right
                        done = diagBottomUpBlock(gridBoard, blockList.get(i), r, c);
                        if (done == true) {
                            Connect4_GUI.countMoves++;
                            return done;
                        }
                        done = diagTopDownBlock(gridBoard, blockList.get(i), r, c);
                        if (done == true) {
                            Connect4_GUI.countMoves++;
                            return done;
                        }
                    }
                }
            }
        }
        return done;
    }

    /**
     * mediumDrop will start their turn placing piece in middle column. In other turns, it will place a calculated move which is
     * placing a piece to connect a random AI player piece. If pieces are blocked, it will place it in a random column. This method
     * will call the block method before placing a piece so computer can place a move to block or win
     */
    public void mediumDrop(char playerColour, int rows, int cols, char[][] gridBoard, int turnCounter) {
        ArrayList<Integer> row = new ArrayList<Integer>(); //list to add piece in row in between row 1 and 6
        ArrayList<Integer> rowBorder = new ArrayList<Integer>(); //list to add piece on row 1 and 6
        ArrayList<Integer> col = new ArrayList<Integer>(); //list to add piece in col in between row 1 and 7
        ArrayList<Integer> colBorder = new ArrayList<Integer>(); //list to add piece on col 1 and 7

        ArrayList<GUI_Players> blockList = new ArrayList<GUI_Players>();
        blockList.add(Connect4_GUI.aiPlayer);
        blockList.add(Connect4_GUI.player1);

        Random rand = new Random();
        int tempR = 0;
        int tempC = 0;
        int randIndex1 = 1;
        int randIndex2 = 1;
        int randomMove;
        int counter = 0;
        int countFilled = 0;
        int tempRow = rows-1; //index position 5
        int middleCol = 3; //index position 3
        int randNum;
        boolean done = false;
        boolean dropped = false;


        if (done == false) {
            //go through all the positions on the grid and add the ones that are the computer's colour to its respective row and column list
            for (int r = rows-1; r >= 0; r--) { //begins at index 5, goes up
                for (int c = 0; c < cols ; c++) { //begins at index 0, goes left to right
                    if (gridBoard[r][c] == playerColour) {
                        //add the positions that are on the border of the grid to another list
                        if (r == 0 || c == 0 || c == 6) {
                            rowBorder.add(r);
                            colBorder.add(c);
                        } else { //positions that are not located on the borders are added to another list
                            row.add(r);
                            col.add(c);
                        }
                    }
                }
            }
        }

        done = block(gridBoard, blockList); //calls block method to check if piece was already placed to block/win

        //goes through the middle positions in the first round until it can put one in an empty spot
        for (int k = 0; k < 2; k++) {
            if (turnCounter == 2 && counter == 0) { //if they haven't placed it in the middle column yet, and it's their first turn
                if (gridBoard[tempRow][middleCol] == '_') {
                    gridBoard[tempRow][middleCol] = playerColour; //place in middle
                    System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(middleCol+1)+">");
                    Connect4_GUI.countMoves++;
                    done = true;
                    counter = 1;
                } else {
                    tempRow--;
                }
            }//end if statement
        }//end for loop

        //while piece has not been placed in middle, or turn has not been used to block or win
        while (done == false) {

            countFilled = 0;
            //generate a random number between 1-6 and anything <= 4 will find the computer's piece within the grid's borders, else
            randNum = (int)(Math.random()*6) + 1;

            //get random index position to access this position in arraylist row and col
            if (row.size() > 0) { //if list is not empty
                //generate random index number
                randIndex1 = rand.nextInt(row.size()); //row size min can only be 1, 1-1=0, index 0 is used
            }

            //get random index position to access this position in arraylist rowBorder and colBorder
            if (rowBorder.size() > 0) { //if list is not empty
                //generate random index number
                randIndex2 = rand.nextInt(rowBorder.size()); //row size min can only be 1, 1-1=0, index 0 is used
            }

            if (randNum <= 4 && row.size() != 0) { //finds random piece within the border and accesses its row and col
                tempR = row.get(randIndex1); //access this row and col
                tempC = col.get(randIndex1);
                randomMove = (int)(Math.random()*3) + 1; //generate random move for 3 different scenarios

                //if statement runs when a space(s) around the random piece chosen is empty
                if (gridBoard[tempR][tempC-1] == '_' || gridBoard[tempR-1][tempC-1] == '_' || gridBoard[tempR-1][tempC] == '_' || gridBoard[tempR-1][tempC+1] == '_' || gridBoard[tempR][tempC+1] == '_') {

                    //scenario 1 will place piece on top
                    if (randomMove == 1) {
                        //check the position on top of this piece
                        if (gridBoard[tempR-1][tempC] == '_') {
                            gridBoard[tempR-1][tempC] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC+1)+">");
                            dropped = true;
                        }

                    } else if (randomMove == 2) { //scenario 2 will place piece to the left, either next to or one row up to the left

                        for (int r = 5; r >= (tempR-1); r--) { //checks the positions filled in column to the left
                            if (gridBoard[r][tempC-1] != '_') {
                                countFilled++;
                            }
                        }

                        if (countFilled == (5-tempR) && gridBoard[tempR][tempC-1] == '_') { //adds piece to the left
                            gridBoard[tempR][tempC-1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC)+">");
                            dropped = true;
                        } else if (countFilled == (5-tempR+1) && gridBoard[tempR-1][tempC-1] == '_') { //adds piece to the left, one row up
                            gridBoard[tempR-1][tempC-1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC)+">");
                            dropped = true;
                        }

                    } else if (randomMove == 3) { //scenario 3 will place piece to the right, either next to or one row up to the right

                        for (int r = 5; r >= (tempR-1); r--) { //checks the positions filled in column to the right
                            if (gridBoard[r][tempC+1] != '_') {
                                countFilled++;
                            }
                        }

                        if (countFilled == (5-tempR) && gridBoard[tempR][tempC+1] == '_') { //adds piece to the right
                            gridBoard[tempR][tempC+1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC+2)+">");
                            dropped = true;
                        } else if (countFilled == (5-tempR+1) && gridBoard[tempR-1][tempC+1] == '_') { //adds piece to the right, one row up
                            gridBoard[tempR-1][tempC+1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC+2)+">");
                            dropped = true;
                        }

                    }
                }

            } else if (randNum == 5 && rowBorder.size() != 0) { //finds random piece on the border and accesses its row and col
                tempR = rowBorder.get(randIndex2);
                tempC = colBorder.get(randIndex2);
                randomMove = (int)(Math.random()*2) + 1; //generate random move for 2 different scenarios

                //random border piece chosen is in first column, and a space(s) around it is/are empty
                if (tempC == 0 && (gridBoard[tempR-1][tempC] == '_' || gridBoard[tempR-1][tempC+1] == '_' || gridBoard[tempR][tempC+1] == '_')) {

                    if (randomMove == 1) { //first scenario, piece gets put on top
                        if (gridBoard[tempR-1][tempC] == '_') {
                            gridBoard[tempR-1][tempC] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC+1)+">");
                            dropped = true;
                        }
                    } else if (randomMove == 2) { //second scenario, piece gets put one column to the right

                        for (int r = 5; r >= (tempR-1); r--) { //checks amount of filled columns to the right
                            if (gridBoard[r][tempC+1] != '_') {
                                countFilled++;
                            }
                        }

                        if (countFilled == (5-tempR) && gridBoard[tempR][tempC+1] == '_') { //adds piece one column right
                            gridBoard[tempR][tempC+1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC+2)+">");
                            dropped = true;
                        } else if (countFilled == (5-tempR+1) && gridBoard[tempR-1][tempC+1] == '_') { //adds piece to the right, one row up
                            gridBoard[tempR-1][tempC+1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC+2)+">");
                            dropped = true;
                        }
                    }
                } else if (tempC == 6 && (gridBoard[tempR][tempC-1] == '_' || gridBoard[tempR-1][tempC-1] == '_' || gridBoard[tempR-1][tempC] == '_')) { //else if random border piece chosen is in last column, and a space(s) around it is/are empty

                    if (randomMove == 1) { //scenario 1, piece gets put on top of border piece
                        if (gridBoard[tempR-1][tempC] == '_') {
                            gridBoard[tempR-1][tempC] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC+1)+">");
                            dropped = true;
                        }
                    } else if (randomMove == 2) { //scenario 2, piece gets put in the left column of the piece chosen

                        for (int r = 5; r >= (tempR-1); r--) { //checks amount of filled columns to the left
                            if (gridBoard[r][tempC-1] != '_') {
                                countFilled++;
                            }
                        }

                        if (countFilled == (5-tempR) && gridBoard[tempR][tempC-1] == '_') { //adds piece to the left
                            gridBoard[tempR][tempC-1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC)+">");
                            dropped = true;
                        } else if (countFilled == (5-tempR+1) && gridBoard[tempR-1][tempC-1] == '_') { //adds piece to the left one row up
                            gridBoard[tempR-1][tempC-1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC)+">");
                            dropped = true;
                        }
                    }

                } else if (tempR == 0 && tempC > 0 && tempC < 6) { //else if on last row but BETWEEN first and last column

                    if (randomMove == 1) { //scenario 1, a piece gets put to the left of chosen random piece

                        for (int r = 5; r >= tempR; r--) {
                            if (gridBoard[r][tempC-1] != '_') {
                                countFilled++;
                            }
                        }

                        if (countFilled == (5-tempR) && gridBoard[tempR][tempC-1] == '_') { //adds piece to the left
                            gridBoard[tempR][tempC-1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC)+">");
                            dropped = true;
                        }

                    } else if (randomMove == 2) { //scenario 2, a piece gets put to the right of the chosen random piece

                        for (int r = 5; r >= tempR; r--) {
                            if (gridBoard[r][tempC+1] != '_') {
                                countFilled++;
                            }
                        }

                        if (countFilled == (5-tempR) && gridBoard[tempR][tempC+1] == '_') { //adds piece to the right
                            gridBoard[tempR][tempC+1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+(tempC+2)+">");
                            dropped = true;
                        }

                    }
                }

            } else if (randNum == 6) { //if randNum is 6, a random column is found to drop the piece in
                while (dropped == false) {
                    colRandNum = (int) (Math.random() * 7) + 1;
                    //checks if column is full, otherwise, loops until it finds an available column
                    for (int i = GUI_Board.ROWS-1; i >= 0; i--) {
                        if (gridBoard[i][colRandNum-1] == '_') {
                            gridBoard[i][colRandNum-1] = playerColour;
                            System.out.println("\n"+Connect4_GUI.aiPlayer.getName()+" chooses column number <"+colRandNum+">");
                            dropped = true;
                            break;
                        }
                    }

                }
            }

            if (dropped == true) { //if dropped is true, end while(!done) loop
                Connect4_GUI.countMoves++;
                done = true;
            } else {
                done = false;
            }

        }//end while

    } //End of mediumDrop


    //This void type method gets the available positions to drop on the board
    public void getAvailablePos(char[][]gridBoard){

        //this for loop repeats 7 times, starting from the left side of the grid
        for (int i = 0; i < 7; i++) {
            //this for loop repeats 6 times, starting from the bottoms of the grid
            for(int j = 5; j >= 0; j--) {
                //if the position is available
                if (gridBoard[j][i]== '_') {
                    // adding the row and column indices into two different array lists
                    availableX.add(j);
                    availableY.add(i);
                    break;
                }
            }

        }

    } // end method

    //this return type method returns the score of every directions in an available position gathered in the getAvailablePos method
    public static int getPositionScore(char AIColour, char[]searchResult) {
        //initializing positionScore
        int positionScore = 0;
        // if AIplayer is playing as O
        if (AIColour == 'O') {
            // set the available position to O
            searchResult[0] = 'O';

            //connect four disks; wins
            if (searchResult[0] == searchResult[1] && searchResult[1] == searchResult[2] && searchResult[2]== searchResult[3] && searchResult[3] == 'O') {
                positionScore  += 1000;
            }

            //connect three disks
            if (searchResult[0] == searchResult[1] && searchResult[1] == searchResult[2] && searchResult[2]== 'O') {
                positionScore  += 20;
            }

            //connect 2 disks
            if(searchResult[0] == searchResult[1] && searchResult[1] == 'O') {
                positionScore  += 15;
            }

            //place a disk
            if(searchResult[0] == 'O') {
                positionScore  += 2;
            }

            //block 3 disks from opponent
            if(searchResult[1] == searchResult[2] && searchResult[2] == searchResult[3] && searchResult[3]== 'X') {
                positionScore  += 80;
            }
            //block 2 disks from opponent
            if(searchResult[1] == searchResult[2] && searchResult[2] == 'X') {
                positionScore  += 11;
            }
            //block 1 disk
            if(searchResult[1] == 'X') {
                positionScore  += 1;
            }


        } else if (AIColour == 'X') {

            searchResult[0] = 'X';

            //connect 4 disks; wins
            if (searchResult[0] == searchResult[1] && searchResult[1] == searchResult[2] && searchResult[2]== searchResult[3] && searchResult[3] == 'X') {
                positionScore  += 1000;
            }

            //connect 3 disks
            if (searchResult[0] == searchResult[1] && searchResult[1] == searchResult[2] && searchResult[2]== 'X') {
                positionScore  += 20;
            }

            //connect 2 disks
            if(searchResult[0] == searchResult[1] && searchResult[1] == 'X') {
                positionScore  += 15;
            }

            //place a disk
            if(searchResult[0] == 'X') {
                positionScore  += 2;
            }

            //block 3 disks from opponent
            if(searchResult[1] == searchResult[2] && searchResult[2] == searchResult[3] && searchResult[3]== 'O') {
                positionScore  += 80;
            }

            //block 2 disks from opponent
            if(searchResult[1] == searchResult[2] && searchResult[2] == 'O') {
                positionScore  +=11;
            }

            //block 1 disk
            if(searchResult[1] == 'O') {
                positionScore  += 1;
            }

        } // end if/else

        return positionScore;
    } // end method

    //this void type method sets every value in the searchResult array to 0
    public static void clearSearchResult(char[]searchResult) {
        for(int i = 0; i < 4; i ++) {
            //setting the value to 0
            searchResult[i] = 0;
        } // end for
    } // end clearSearchResult method

    //this return type method return the score of available positions
    public static int calculateScore(char[][] gridBoard, char player, int r, int c, char[]searchResult) {

        int positionScore = 0;

        //horizontal forward
        for (int i = 0; i < 4 ; i++) {
            if(r >= 0 && r < 6 && c+i >= 0 && c+i < 7) {
                searchResult[i] = gridBoard[r][c+i];
                positionScore += getPositionScore(player,searchResult);
            } // end if
        } // end for
        //call clearSearchResult method
        clearSearchResult(searchResult);

        //horizontal backward
        for (int i = 0; i < 4 ; i++) {
            if(r >= 0 && r < 6 && c-i >= 0 && c-i < 7) {
                searchResult[i] = gridBoard[r][c-i];
                positionScore += getPositionScore(player,searchResult);
            }
        }
        //call clearSearchResult method
        clearSearchResult(searchResult);

        //vertical upward
        for (int i = 0; i < 4 ; i++) {
            if(r+i >= 0 && r+i < 6 && c >= 0 && c < 7) {
                searchResult[i] = gridBoard[r+i][c];
                positionScore += getPositionScore(player, searchResult);
            }

        }
        //call clearSearchResult method
        clearSearchResult(searchResult);

        //vertical downward
        for (int i = 0; i < 4 ; i++) {
            if(r-i >= 0 && r-i < 6 && c >= 0 && c < 7) {
                searchResult[i] = gridBoard[r-i][c];
                positionScore += getPositionScore(player, searchResult);
            }

        }
        //call clearSearchResult method
        clearSearchResult(searchResult);

        //diagonal up right
        for (int i = 0; i < 4 ; i++) {
            if(r+i >= 0 && r+i < 6 && c+i >= 0 && c+i < 7) {
                searchResult[i] = gridBoard[r+i][c+i];
                positionScore += getPositionScore(player, searchResult);
            }

        }
        //call clearSearchResult method
        clearSearchResult(searchResult);

        //diagonal up left
        for (int i = 0; i < 4 ; i++) {
            if(r-i >= 0 && r-i < 6 && c-i >= 0 && c-i < 7) {
                searchResult[i] = gridBoard[r-i][c-i];
                positionScore += getPositionScore(player, searchResult);
            }

        }
        //call clearSearchResult method
        clearSearchResult(searchResult);

        //diagonal down right
        for (int i = 0; i < 4 ; i++) {
            if(r+i >= 0 && r+i < 6 && c-i >= 0 && c-i < 7) {
                searchResult[i] = gridBoard[r+i][c-i];
                positionScore += getPositionScore(player, searchResult);
            }

        }
        //call clearSearchResult method
        clearSearchResult(searchResult);

        //diagonal down left
        for (int i = 0; i < 4 ; i++) {
            if(r-i >= 0 && r-i < 6 && c+i >= 0 && c+i < 7) {
                searchResult[i] = gridBoard[r-i][c+i];
                positionScore += getPositionScore(player, searchResult);
            }

        }
        //call clearSearchResult method
        clearSearchResult(searchResult);

        return positionScore;

    }

    //this void type method places the disk in calculated position
    public void hardDrop(char AIColour, int r, char[][] gridBoard, int turnTracker, char[]searchResult) {
        int maxXindex = 0;
        int maxYindex = 0;
        int score;
        int maxScore = 0;
        int posScore[] = {0,0,0,0,0,0,0};

        int randCol = 3;
        Random rand = new Random();

        //call getAvailablePos() method
        getAvailablePos(gridBoard);

        //use first move to drop disk into the middle column
        //AI goes first
        if (turnTracker == 1) {
            gridBoard[r-1][3] = AIColour;
            Connect4_GUI.gridButtons[r-1][3].setIcon(Connect4_GUI.aiPlayer.getChip());
            Connect4_GUI.countMoves++;
            //AI goes second
        } else if (turnTracker == 2){
            // if the middle column is blank
            if (gridBoard[r-1][3] == '_'){
                gridBoard[r-1][3] = AIColour;
                Connect4_GUI.gridButtons[r-1][3].setIcon(Connect4_GUI.aiPlayer.getChip());
                Connect4_GUI.countMoves++;

                maxXindex = r-1;
                maxYindex = 3;

            } else {// if the player places on the middle column
                while(randCol == 3){
                    //generates a random number other than 3
                    randCol = rand.nextInt(7);
                }
                //places the disk
                gridBoard[r-1][randCol] = AIColour;
                Connect4_GUI.gridButtons[r-1][randCol].setIcon(Connect4_GUI.aiPlayer.getChip());
                Connect4_GUI.countMoves++;
                maxXindex = r-1;
                maxYindex = randCol;
            }

        } else {
            maxScore = posScore[0];
            for(int i = 0; i < 7; i++) {

                score = calculateScore(gridBoard, AIColour,availableX.get(i),availableY.get(i), searchResult);
                posScore[i] = score;
                if(posScore[i]>maxScore) {
                    maxScore = posScore[i];
                    maxXindex = availableX.get(i);
                    maxYindex = availableY.get(i);
                }
            }

            //if the score is 0
            if (maxScore == 0){
                //if AI can place a disk on 0,0
                if(gridBoard[1][0] != '_'){
                    maxXindex = 0;
                    maxYindex = 0;
                } else {
                    //generate a random column
                    maxYindex = rand.nextInt(7);
                    //this while loop checks if the column is full; if yes, it generates a new column number
                    while (gridBoard[0][maxYindex] != '_'){
                        maxYindex = rand.nextInt(7);
                    }
                    //checking for available row rumber
                    for (int i = 5; i >= 0; i --){
                        if (gridBoard[i][maxYindex] == '_'){
                            maxXindex = i;
                            break;
                        } // end if
                    } // end for
                } // end if/else
            } // end if statement

            // places the disk
            gridBoard[maxXindex][maxYindex] = AIColour;
            Connect4_GUI.gridButtons[maxXindex][maxYindex].setIcon(Connect4_GUI.aiPlayer.getChip());
            Connect4_GUI.countMoves++;
        }

        System.out.println(name+" chooses column number <"+ (maxYindex+1) + ">");

        //clear posScoreArray
        for (int i = 0; i < 7; i ++) {
            posScore[i] = 0;
        }
        //clear the array lists
        availableX.clear();
        availableY.clear();

    } //End of hardDrop

} //End of class ComputerPlayer
