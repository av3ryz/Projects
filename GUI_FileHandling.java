import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class GUI_FileHandling {

    static Scanner sc = new Scanner(System.in);

    //determine whether it is the first time the user is opening this game by checking if the playerInfoFile text file is empty
    public static long checkEmpty() {
        File file = new File ("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\playerInfoFile.txt");
        if (file.length() == 0) {
            return 0;
        }
        return file.length();
    }

    //determine whether it is  the first time the timer stops by checking if the timerFile text file is empty
    /*public static long checkTimeFileEmpty() {
        File file = new File("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\timerFile.txt");
        if (file.length() == 0) {
            return 0;
        } else {
            return file.length();
        }
    }*/

    //allow the user to quit the game (and be able to continue from it later) anytime during the game
    public static void checkSave(char[][] gridBoard, int turnCounter, int storeMode, int modeNum) {
        //long timerFileLength = checkTimeFileEmpty();

        if (Connect4_GUI.gameOver == false) {
            //prompt user to either quit and save game, or just quit without saving
            System.out.println("\nPlease enter 1 if you would like to quit your game now. If you quit, your game will be saved and you can continue later. Else enter 2 to exit without saving.");
            //int choice = sc.nextInt();
            int saveOption = JOptionPane.showConfirmDialog(null, "Would you like to quit and save the game\nTo continue later?", "Quit and Save", JOptionPane.YES_NO_OPTION);

            if (saveOption == 0) { //user chooses to save
                //stop the timer when the user quits the game
                //Connect4_GUI.timer.stop();
                //write time elapsed in sec and min to text file
                //readTimeFromFile();

                /*Connect4_GUI.totalTimeInSec = Connect4_GUI.timer.getElapsedSeconds() + Connect4_GUI.savePassedSec;
                Connect4_GUI.totalTimeInMin = Connect4_GUI.timer.getElapsedMinutes() + Connect4_GUI.savePassedMin;*/

                //write grid and player info to files
                //writeTimerToFile();
                writeGridToFile(gridBoard);
                writePlayersToFile(turnCounter, storeMode, modeNum);
                //Connect4_GUI.timerPaused = true;
                Connect4_GUI.gameOver = true; //game ends

            } else { //user chooses to end game without saving
                clearFiles();
                //clearTimerFile();
                //Connect4_GUI.timerPaused = true;
                Connect4_GUI.gameOver = true; //game ends

            }
        }

    }

    //if the user chooses to quit the game, clear the text files
    public static void clearFiles() {
        try { //clear currentGameFile
            PrintWriter writer1 = new PrintWriter("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\currentGameFile.txt");
            writer1.print("");
            writer1.close();
        } catch (Exception e) {
            System.out.println("File error");
        }

        try { //clear playerInfoFile
            PrintWriter writer2 = new PrintWriter("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\playerInfoFile.txt");
            writer2.print("");
            writer2.close();
        } catch (Exception e) {
            System.out.println("File error");
        }

        try { //clear scoreFile
            PrintWriter writer4 = new PrintWriter("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\out\\scoreFile.txt");
            writer4.print("");
            writer4.close();
        } catch (Exception e) {
            System.out.println("File error");
        }

    }

    //if user has completely finished the game
    public static void clearWinningGameFiles() {
        try { //clear currentGameFile
            PrintWriter writer1 = new PrintWriter("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\currentGameFile.txt");
            writer1.print("");
            writer1.close();
        } catch (Exception e) {
            System.out.println("File error");
        }
    }

   /* public static void clearTimerFile() {
        try { //clear timerFile
            PrintWriter writer3 = new PrintWriter("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\timerFile.txt");
            writer3.print("");
            writer3.close();
        } catch (Exception e) {
            System.out.println("File error");
        }
    }*/

    //if the user chooses to quit the game, save their progress by writing their moves on the grid to the currentGameFile text file
    public static void writeGridToFile(char[][] gridBoard) {
        char[][] currentGame = new char [Connect4_GUI.ROWS][Connect4_GUI.COLS];

        for (int i = 0; i < gridBoard.length; i++) {
            for (int j = 0; j < gridBoard[0].length; j++) {
                currentGame [i][j] = gridBoard[i][j];
            }
        }
        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\currentGameFile.txt", false);
            for (int i = 0; i < currentGame.length; i++) {
                for (int j = 0; j < currentGame[0].length; j++) {
                    myWriter.write(currentGame[i][j]);
                    myWriter.append(" ");
                }
                myWriter.append("\n");
            }
            myWriter.close();
            System.out.println("Your game is saved");
        }
        catch (Exception e) {
            System.out.println("File error");
        }

    }

    //save player1 & 2's info (name, colour, turn number, mode, level) to the playerInfoFile text file
    public static void writePlayersToFile (int turnCounter, int storeMode, int modeNum) {
        try {
            FileWriter writer = new FileWriter ("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\playerInfoFile.txt", false);
            writer.write(Connect4_GUI.player1.getName() + "\n");
            writer.write(Connect4_GUI.player1.getColour() + "\n");
            if (storeMode == 1) {
                writer.write(Connect4_GUI.player2.getName() + "\n");
                writer.write(Connect4_GUI.player2.getColour() + "\n");
            }
            else {
                writer.write(Connect4_GUI.aiPlayer.getName()+"\n");
                writer.write(Connect4_GUI.aiPlayer.getColour()+"\n");
            }
            writer.write(String.valueOf(turnCounter));
            writer.append("\n");
            String srStoreMode = Integer.toString(storeMode);
            writer.write(srStoreMode);
            if (storeMode == 2) {   //if the user chooses Player Vs. AI mode, write the AI level to the playerInfoFile file
                writer.append("\n");
                String srModeNum = Integer.toString(modeNum);
                writer.write(srModeNum);
            }
            writer.close();
            System.out.println("Players' info is saved.");
        }
        catch (Exception e) {
            System.out.println("File error");
        }
    }

    //when the user wants to resume their game, read their moves on the grid from the currentGameFile text file and save them in the lastSaved 2D array
    public static char[][] readGridFromFile () {
        char[][] lastSaved = new char[Connect4_GUI.ROWS][Connect4_GUI.COLS];
        try {
            File savedGridFile = new File ("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\currentGameFile.txt");
            Scanner scan = new Scanner (savedGridFile);
            while (scan.hasNextLine()) {
                for (int i = 0; i < lastSaved.length; i++) {
                    String[] line = scan.nextLine().trim().split(" ");
                    for (int j = 0; j < lastSaved[0].length; j++) {
                        char temp = line[j].charAt(0);  //parse the String at line[j] to a char
                        lastSaved[i][j] = temp;
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not find");
        }
        catch (Exception e) {
            System.out.println("File error");
        }
        return lastSaved;
    }

    //when the user wants to resume their game, read both players' name and color from the playerInfoFile text file and store them in their respective objects' attributes
    public static void readPlayersNameColour (int storeMode) {
        //Players player1, Players player2, ComputerPlayer aiPlayer,
        try {
            File savedNameColour = new File("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\playerInfoFile.txt");
            Scanner scan = new Scanner (savedNameColour);

            Connect4_GUI.player1.setName(scan.nextLine());
            Connect4_GUI.player1.setColour(scan.nextLine().charAt(0));

            //if the user chose Player Vs. Player mode, read and store player2's name and colour
            if (storeMode == 1) {
                Connect4_GUI.player2.setName(scan.nextLine());
                Connect4_GUI.player2.setColour(scan.nextLine().charAt(0));
            }
            //if the user chose Player Vs. AI mode, read and store aiPlayer's name and colour
            else {
                Connect4_GUI.aiPlayer.setName(scan.nextLine());
                Connect4_GUI.aiPlayer.setColour(scan.nextLine().charAt(0));
            }

        }
        catch (FileNotFoundException e) {
            System.out.println("File not find");
        }
        catch (Exception e) {
            System.out.println("File error");
        }
    }

    //when the user wants to resume the game, read the turnCounter and mode from the playerInfoFile text file and store them in the playersTurnMode 1D array
    public static int[] readTurnMode (int []playersTurnMode) {
        String temp = "";

        try {
            File savedTurnMode = new File ("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\playerInfoFile.txt");
            Scanner scan = new Scanner (savedTurnMode);

            int line = 0;
            for (int i = 0; i < 4; i++) {
                temp = scan.nextLine();
            }
            playersTurnMode[0] = scan.nextInt();  //read the turnCounter
            playersTurnMode[1] = scan.nextInt();  //read the mode

            if (playersTurnMode[1] == 2) {      //if the user chose Player Vs. AI mode
                playersTurnMode[2] = scan.nextInt();  //read the AI level
            }
        }
        catch (Exception e) {
            System.out.println("File error");
        }
        return playersTurnMode;
    }

    //write time in sec and min to file, so we can read it again once a game is resumed
    public static void writeTimerToFile () {
        try {
            FileWriter writer = new FileWriter("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\timerFile.txt", false);
            writer.write(Long.toString(Connect4_GUI.totalTimeInSec));
            writer.append("\n");
            writer.write(Long.toString(Connect4_GUI.totalTimeInMin));

            writer.close();
            System.out.println("Time elapsed in second and minute are saved.");
        }
        catch (Exception e) {
            System.out.println("File error");
        }
    }

    //read time from file in sec and min and set values to their respective long variables
    public static void readTimeFromFile() {
        int count = 1;

        try {
            File savedTimes = new File("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\timerFile.txt");
            Scanner scan = new Scanner(savedTimes);

            while(count <=2 && scan.hasNextLong()) {
                if(count == 1) {
                    Connect4_GUI.savePassedSec = scan.nextLong();
                    System.out.println("time saved seconds is "+Connect4_GUI.savePassedSec);
                    count++;
                }
                if(count == 2) {
                    Connect4_GUI.savePassedMin = scan.nextLong();
                    System.out.println("time saved min is "+Connect4_GUI.savePassedMin);
                    count++;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not find");
        } catch (Exception e) {
            System.out.println("File error readSecFromFile");
        }
    }

    //write scores to scoreFile.txt
    public static void writeScoresToFile (int storeMode) {
        try {
            FileWriter writer = new FileWriter("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\out\\scoreFile.txt", false);
            writer.write(Integer.toString(storeMode));
            writer.append("\n");
            writer.write(Integer.toString(Connect4_GUI.score1));
            writer.append("\n");
            writer.write(Integer.toString(Connect4_GUI.score2));
            writer.append("\n");
            writer.write(Integer.toString(Connect4_GUI.scoreAI));

            writer.close(); //close so nothing else is written to file
            System.out.println("\nScores are saved.");
        }
        catch (Exception e) {
            System.out.println("File error");
        }
    }

    //create scanner to read set player scores
    public static int readScoresFromFile (int storeMode) {
        try {
            File savedScores = new File("C:\\Users\\kwu38\\OneDrive\\Documents\\GitHub\\C4_2.0\\out\\scoreFile.txt");
            Scanner scan = new Scanner(savedScores);
            storeMode = scan.nextInt();
            Connect4_GUI.score1 = scan.nextInt();
            Connect4_GUI.player1.setPlayerScore(Connect4_GUI.score1);
            System.out.println("Player 1        score: " + Connect4_GUI.score1);
            Connect4_GUI.score2 = scan.nextInt();
            Connect4_GUI.player2.setPlayerScore(Connect4_GUI.score2);
            Connect4_GUI.scoreAI = scan.nextInt();
            System.out.println("Player 2        score: " + Connect4_GUI.score2);
            Connect4_GUI.aiPlayer.setPlayerScore(Connect4_GUI.scoreAI);
            System.out.println("Bob     score: " + Connect4_GUI.scoreAI);

        } catch (FileNotFoundException e) {
            System.out.println("File not find");
        }
        return storeMode;
    }

} //End of class Connect4

