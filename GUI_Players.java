import javax.swing.*;
import java.util.Scanner;

/**
 * Class Players will be the template for players 1 and 2, and each will have its unique attributes affiliated to its object.
 * It will have a drop method to allow player to drop its piece into the grid board
 */
public class GUI_Players {

    //player attributes - instance variables
    protected String name;
    protected char colour;
    protected int playerNum;
    protected int playerScore;
    protected ImageIcon chip;
    private boolean full = false;

    //create scanner variable to get input from keyboard
    static Scanner sc = new Scanner(System.in);

    //Players constructor method
    public GUI_Players(String playerName, char playerColour, int playNum, int score, ImageIcon playerChip) {
        name = playerName;
        colour = playerColour;
        playerNum = playNum;
        playerScore = score;
        chip = playerChip;
    }

    /**
     * drop method catches and prevents user error from entering column number that is out of range.
     * It gets a valid column number and updates gridBoard with the player's move, returning true when
     * a piece is dropped and gridBoard is updated
     */
    public void drop(int r, char[][] gridBoard, int turnCounter, int storeMode, int modeNum) {
        boolean full = true;

        //illegal move check, do while loop runs until a valid number is entered
        do {
            //prompt user to enter a valid column number
            System.out.println("\n"+name+", please enter a column number from 1-7 which will be the column you drop your token into. Else enter 0 to save your game: ");
            Connect4_GUI.colNum = Integer.parseInt(JOptionPane.showInputDialog("Please enter a column number from 1-7 which will be the column\nyou drop your token into. Else enter 0 to save your game: "));
            //Connect4_GUI.colNum = sc.nextInt();


            //if colNum is a valid column number, and check if column is full
            if (Connect4_GUI.colNum >= 1 && Connect4_GUI.colNum <=7) {
                if (gridBoard[0][Connect4_GUI.colNum-1] != '_') {
                    full = true;
                    // let user know column is full, and the loop will prompt them to enter another column number
                    System.out.println("\nColumn "+Connect4_GUI.colNum+" is full/out of range!");
                    JOptionPane.showMessageDialog(null,"Column "+Connect4_GUI.colNum+" is full!\nPlease choose another column from 1-7.");
                } else {
                    full = false;
                }
            } else if (Connect4_GUI.colNum == 0) { //else if user chose to save game, break out of while loop
                full = false;
            }

        } while (Connect4_GUI.colNum < 0 || Connect4_GUI.colNum > 7 || full == true); //while colNum is not a valid column number

        if (Connect4_GUI.colNum == 0) {
            //set gameOver equal to
            GUI_FileHandling.checkSave(gridBoard, turnCounter, storeMode, modeNum); //allow the user to quit the game anytime
        } else {
            //check which positions in column have been filled and put in the first open position
            for (int i = r-1; i >= 0 ; i--) {
                if (gridBoard[i][Connect4_GUI.colNum-1] == '_') {
                    gridBoard[i][Connect4_GUI.colNum-1] = colour;
                    System.out.println("\n"+name+" chose column "+(Connect4_GUI.colNum));
                    Connect4_GUI.gridButtons[i][Connect4_GUI.colNum-1].setIcon(chip);
                    Connect4_GUI.countMoves++;
                    break;
                }
            }
        }

    } //end of drop method

    //accessor method for player name
    public String getName() {
        return name;
    }

    //setter method for setting player name
    public void setName(String playerName) {
        name = playerName;
    }

    //accessor method for player colour
    public char getColour() {
        return colour;
    }

    //setter method for player colour
    public void setColour(char playerColour) {
        colour = playerColour;
    }

    //getter method for getting the player's number (either 1 or 2)
    public int getPlayerNum() { return playerNum; }

    //getter and setter methods to get/set player's score
    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int score) {
        playerScore = score;
    }

    public ImageIcon getChip() { return chip; }

    public void setChip(ImageIcon playerChip) { chip = playerChip; }

}
