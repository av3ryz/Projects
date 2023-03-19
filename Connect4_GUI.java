import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

//class Connect4_GUI creates the variables for the JFrame
public class Connect4_GUI extends JFrame implements ActionListener {

    static final int COLS = 7;
    static final int ROWS = 6;

    //game logo components (image/image icon)
    JLabel gameLabel = new JLabel();
    ImageIcon gameIcon;
    Image gameImg;

    //white circle images
    static ImageIcon whiteIcon;
    Image whiteImg;

    //red chip images
    static ImageIcon redIcon;
    Image redImg;

    //yellow chip images
    static ImageIcon yellowIcon;
    Image yellowImg;

    //bg music button icon
    ImageIcon musicIcon;
    Image musicImg;

    //bg music off icon
    ImageIcon muteIcon;
    Image muteImg;

    static AudioInputStream audioInputStream;
    static String musicFilePath = "C:\\Users\\visss\\OneDrive\\Documents\\GitHub\\Connect4_GUI\\src\\music.wav";
    static Clip music;

    JButton[] colButtons = new JButton[COLS];  //column buttons

    static JButton[][] gridButtons = new JButton[ROWS][COLS];  //grid board

    static JLabel playerTurnLabel = new JLabel("Welcome to Connect 4!");  //player info


    //play, help, quit, and music buttons
    JButton playButton = new JButton("Play Game");
    JButton helpButton = new JButton("Help");
    JButton quitButton = new JButton("Quit");
    JButton musicButton = new JButton();

    JPanel mainPanel = new JPanel(); //panel all the other panels will be added in
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JPanel panel3 = new JPanel();
    JPanel panel4 = new JPanel();
    JPanel panel5 = new JPanel();
    JPanel panel6 = new JPanel();

    //declare class variables needed to run Connect4 program

    //create stopwatch var to keep track of time
    /*static Connect4_GUI timer;

    static {
        try {
            timer = new Connect4_GUI();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }*/

    //static boolean timerPaused; //check if timer was paused in checkSave method

    //long variables to show nanoseconds in seconds and minute to use in timer methods
    private final long nanoSecondsPerSecond = 1000000000;
    private final long nanoSecondsPerMinute = 60000000000L;

    //initialize timer variables to show start and end time
    private long timerStartTime = 0;
    private long timerStopTime = 0;
    private boolean timerRunning = false;

    //get previous time in sec and min
    static long savePassedSec = 0;
    static long savePassedMin = 0;

    //get total time by adding any previous game time + current game time, initialize as 0
    static long totalTimeInSec;
    static long totalTimeInMin;

    //global variable sc to allow user to enter input
    static Scanner sc = new Scanner(System.in);

    //initialize default data for player attributes to use temporarily in player constructor method
    static String playerName = "default1";
    static char playerColour = '_';
    static ImageIcon playerChip;
    static int score1 = 0; //keep track of scores in the game
    static int score2 = 0;
    static int scoreAI = 0;

    //access player1 outside of main
    static GUI_Players player1 = new GUI_Players(playerName, playerColour, 1, score1, playerChip);        //create player1 and 2 objects with attributes playName, playerColour, and playNum
    static GUI_Players player2 = new GUI_Players(playerName, playerColour, 2, score2, playerChip);
    static GUI_ComputerPlayer aiPlayer = new GUI_ComputerPlayer(playerName, playerColour, 2, scoreAI,playerChip, 0);        //create aiPlayer object with attributes playerName, playerColour, playNum, and AI's level (numM)

    //declare and initialize game variables
    static boolean playGame = true;
    static int colNum;
    static int continueNum;
    static int playAgainst = 0;
    static int modeNum = 0;  //AI level
    static int turnCounter = 0;
    static int countMoves = 0;
    static char[]searchResult = {'a', 'a', 'a', 'a'};
    static ArrayList<GUI_Players> playerList = new ArrayList<GUI_Players>();

    //store which mode the user wants to play in -- 1) player Vs. player mode or 2) player Vs. AI mode
    static int storeMode = 0;
    static int []playersTurnMode = new int [3];
    static long fileLength;

    //create grid
    static char[][] gridBoard = new char[GUI_Board.ROWS][GUI_Board.COLS];
    static GUI_Board grid = new GUI_Board(gridBoard);

    static boolean gameOver = false;

    static int game1;

    public Connect4_GUI() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        //make box layout for main panel
        BoxLayout boxlayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

        /*GridBagLayout gBLayout = new GridBagLayout();
        mainPanel.setLayout(gBLayout);*/

        //add game image icon to game label, adding it to panel1 which has a flowlayout
        FlowLayout titleLayout = new FlowLayout();
        panel1.setLayout(titleLayout);
        gameIcon = new ImageIcon("C:\\Users\\visss\\OneDrive\\Documents\\GitHub\\Connect4_GUI\\src\\logo1.png");
        gameImg = (gameIcon).getImage().getScaledInstance(350, 110, Image.SCALE_SMOOTH); //resize the image
        gameIcon = new ImageIcon(gameImg); //set imgIcon to resized image
        gameLabel.setIcon(gameIcon);
        panel1.add(gameLabel);

        //add game to main
        mainPanel.add(panel1);

        //create and add empty space in main panel to separate components (panels)
        mainPanel.add(Box.createRigidArea(new Dimension(2000, 10)));

        //set grid layout for buttons, 1 row, 7 cols
        GridLayout buttonLayout = new GridLayout(1,COLS);
        panel2.setLayout(buttonLayout);

        //creating the new buttons with labelled column numbers
        for (int c = 0; c < COLS; c++) {
            colButtons[c] = new JButton(Integer.toString(c + 1));
            colButtons[c].setFont(new Font("Arial", Font.BOLD, 18));
            colButtons[c].setBackground(Color.WHITE);
            colButtons[c].setActionCommand(""+(c+1));
            /*colButtons[c].addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (int c = 0; c < COLS; c++) {
                        if (e.getActionCommand().equals(Integer.toString(c+1))) {
                            colNum = c+1;
                            System.out.println("colNum is "+colNum);
                        }
                    }
                }
            });*/
            panel2.add(colButtons[c]); //add column buttons into the panel
        }
        panel2.setPreferredSize(new Dimension(700, 50)); //set row of column buttons width and height

        //add column buttons to main
        mainPanel.add(panel2);
        mainPanel.add(Box.createRigidArea(new Dimension(1500, 0)));

        //set grid layout for grid board, 6 rows, 7 cols
        GridLayout boardLayout = new GridLayout(ROWS,COLS);
        panel3.setLayout(boardLayout);

        //resize white circle png and initialize button icons with this image icon
        whiteIcon = new ImageIcon("C:\\Users\\visss\\OneDrive\\Documents\\GitHub\\Connect4_GUI\\src\\white.png");
        whiteImg = (whiteIcon).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); //resize the image
        whiteIcon = new ImageIcon(whiteImg); //set imgIcon to resized image

        redIcon = new ImageIcon("C:\\Users\\visss\\OneDrive\\Documents\\GitHub\\Connect4_GUI\\src\\red.png");
        redImg = (redIcon).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); //resize the image
        redIcon = new ImageIcon(redImg); //set imgIcon to resized image

        yellowIcon = new ImageIcon("C:\\Users\\visss\\OneDrive\\Documents\\GitHub\\Connect4_GUI\\src\\yellow.png");
        yellowImg = (yellowIcon).getImage().getScaledInstance(100, 86, Image.SCALE_SMOOTH); //resize the image
        yellowIcon = new ImageIcon(yellowImg); //set imgIcon to resized image

        //add grid buttons to the 2D array board
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                gridButtons[r][c] = new JButton(whiteIcon); //initialize grid board with white circles
                gridButtons[r][c].setBackground(Color.BLUE);
                panel3.add(gridButtons[r][c]); //add each button to panel3
            }
        }
//        panel3.setPreferredSize(new Dimension(700,600)); //set size of grid board, aligned with column buttons
        panel3.setPreferredSize(new Dimension(700,570)); //set size of grid board, aligned with column buttons
        mainPanel.add(panel3); //add grid of buttons to main panel
        mainPanel.add(Box.createRigidArea(new Dimension(1500, 0)));


        FlowLayout playerInfoLayout = new FlowLayout(FlowLayout.CENTER, 700,8); //center panel 4 label
        panel4.setLayout(playerInfoLayout);
        playerTurnLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        panel4.add(playerTurnLabel);
        mainPanel.add(panel4); //add label to main panel
        mainPanel.add(Box.createRigidArea(new Dimension(1500, 0)));

        FlowLayout playBLayout = new FlowLayout(FlowLayout.CENTER, 700,0);
        panel5.setLayout(playBLayout);
        playButton.addActionListener(this);
        panel5.add(playButton);
        mainPanel.add(panel5); //add play button to main panel
        mainPanel.add(Box.createRigidArea(new Dimension(1500, 0)));

        FlowLayout menuLayout = new FlowLayout(FlowLayout.CENTER,10,5);
        panel6.setLayout(menuLayout);

        //display instructions when user clicks help button
        helpButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Welcome to Connect 4, where you connect 4 tokens to win!\n\n"
                        +"Press Play Game to begin a new game, or resume your last game played.\n\n"
                        +"You can choose between Player vs. Player or Player vs. Computer mode.\n"
                        +"In Player vs. Computer mode, you can choose easy, medium, or hard mode.\n\n"
                        +"Enter your player info and colour, and when it is your turn,\n"
                        +"enter a number to drop your token into that column.\n\n"
                        +"When a game is finished, press Play New Game to play again.\n\n"
                        +"Best of Luck! :D");
            }
        });
        panel6.add(helpButton);

        //don't forget to add file handling to this
        quitButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUI_FileHandling.checkSave(gridBoard, turnCounter, storeMode, modeNum); //allow the user to quit the game anytime
                dispose(); //exits the GUI frame
            }
        });
        panel6.add(quitButton);

        //music on icon
        musicIcon = new ImageIcon("C:\\Users\\visss\\OneDrive\\Documents\\GitHub\\Connect4_GUI\\src\\speaker.png");
        musicImg = (musicIcon).getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
        musicIcon = new ImageIcon(musicImg);
        musicButton.setIcon(musicIcon);

        //music off icon
        muteIcon = new ImageIcon("C:\\Users\\visss\\OneDrive\\Documents\\GitHub\\Connect4_GUI\\src\\mute.png");
        muteImg = (muteIcon).getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
        muteIcon = new ImageIcon(muteImg);

        musicButton.setBorderPainted(false); //make button transparent except for icon
        musicButton.setOpaque(false);
        musicButton.setContentAreaFilled(false);

        //initialize clip reference called music
        music = AudioSystem.getClip();

        //call method to restart music
        resetMusic();

        //play or mute music when music button is pressed
        musicButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (musicButton.getIcon() == musicIcon) {
                    musicButton.setIcon(muteIcon); //change icon
                    music.stop();
                    music.close(); //close music
                } else {
                    musicButton.setIcon(musicIcon);
                    try {
                        resetMusic(); //music was muted, call method to reset audio stream
                    } catch (UnsupportedAudioFileException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (LineUnavailableException ex) {
                        ex.printStackTrace();
                    }
                    music.start(); //play music
                } //end if

            }
        });
        panel6.add(musicButton); //add music button

        panel6.setBorder(new EmptyBorder(new Insets(0, 400,10,400))); //sets empty border for panel 6
        mainPanel.add(panel6); //add buttons to main panel

        //add mainPanel which includes all other panels, in frame
        add(mainPanel);

        setTitle("Connect 4");
        setSize(1500, 1050);
//        setSize(1300, 800);
        getContentPane().setBackground(Color.BLACK);
        //setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("Play Game") || command.equals("Play New Game")) {
            playGame = true;
            System.out.println("Welcome to Connect 4!");
        }

        while (playGame) {

            long fileLength;

            fileLength = GUI_FileHandling.checkEmpty(); //get file length to allow resuming a game

            //display option to either play new game or resume, or option to only play new game
            if (fileLength != 0) {
                //custom button text for JOptionPane when play button is pressed and a previous game was played
                Object[] gameOptions = {"Player Vs. Player", "Player Vs. Computer", "Resume Last Game"};
                //game1 is returned the index value of the object pressed in list gameOptions
                game1 = JOptionPane.showOptionDialog(null, "Please choose a player mode, or resume your previous game.\n\n", "Play Game", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameOptions, gameOptions[0]);
            } else {
                //custom button text for JOptionPane when play button is pressed and a previous game was played
                Object[] gameOptions = {"Player Vs. Player", "Player Vs. Computer"};
                game1 = JOptionPane.showOptionDialog(null, "Press below to begin playing!\n\n", "Play Game", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameOptions, gameOptions[0]);
            }

            playAgainst = game1 + 1; //get int value from whether user chose player vs. player, player vs. computer, or resume game, which are 0, 1, 2, respectively


            if (fileLength != 0) {
                gridBoard = GUI_FileHandling.readGridFromFile();                    //read the players' moves saved from the last game from currentGameFile
                grid = new GUI_Board(gridBoard);                                    //store the gridBoard to grid
                playersTurnMode = GUI_FileHandling.readTurnMode(playersTurnMode);   //read the turnCounter and mode from last game
                turnCounter = playersTurnMode[0];                          //store the number of turn the players have taken in the last game to turnCounter.
                storeMode = playersTurnMode[1];                                 //store the chosen mode for the last game to storeMode
                GUI_FileHandling.readPlayersNameColour(storeMode);                  //read players' name and colour from currentGameFile

                if (storeMode == 2) {                   //if the user played in the Player Vs. AI mode, also store the chosen level
                    modeNum = playersTurnMode[2];       //store the AI level from the last game to modeNum
                    aiPlayer.setNumMode(modeNum);       //set AI level to 3rd number in the playersTurnMode array
                }
            }

            gameOver = false;
            //timerPaused = false;

            if (playAgainst != 3) {
                storeMode = playAgainst;    //store the player's chosen mode -- player Vs. player or player Vs. AI. Resume from last game is not a mode, it is an option, so we do not store it in storeMode
                System.out.println("\nHere is your Connect 4 grid!\n");     //if it is a new game, display grid board (blank)
                GUI_FileHandling.clearFiles(); //initiaize values to default and clear files
                //GUI_FileHandling.clearTimerFile();
                totalTimeInSec = 0;
                totalTimeInMin = 0;
                grid.initialGrid();
                grid.printGrid();
            }

            //user chooses player vs. player
            if (storeMode == 1) {

                if (playAgainst != 3) {   //if it is a new game

                    System.out.println("Welcome to Player Vs. Player Mode.");
                    //call method p1Info to initialize player 1's attributes
                    player1 = p1Info();
                    //call method p2Info to initialize player 2's attributes
                    player2 = p2Info();

                    Object[] playerTurn = {"1", "2"};
                    turnCounter = JOptionPane.showOptionDialog(null, "Please choose which player goes first.\nPress 1 for Player 1, or 2 for Player 2:\n\n", "Who Goes First", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, playerTurn, playerTurn[0]);
                    turnCounter += 1; //since index of "1" is 0, and "2" is 1, and the index is what gets returned
                    System.out.println("\nPlease choose which player goes first. Enter 1 for Player 1, or 2 for Player 2:");
                    System.out.println("Players chose Player "+turnCounter+" to go first!");
                    //turnCounter = sc.nextInt();
                    countMoves = 0;

                } else {  //if the user chooses to resume the last game

                    grid.printGrid();  //display the grid saved from the last game
                    JOptionPane.showMessageDialog(null, "Player 1    Name: " + player1.getName() + "   Colour: " + player1.getColour() + "\nPlayer 2    Name: " + player2.getName() + "   Colour: " + player2.getColour());

                    //display both players' info saved from the last game
                    System.out.println("Player 1    Name: " + player1.getName() + "   Colour: " + player1.getColour());
                    System.out.println("Player 2    Name: " + player2.getName() + "   Colour: " + player2.getColour());

                    if (turnCounter % 2 == 1) {
                        JOptionPane.showMessageDialog(null, "Since " + player2.getName() + " made the last move, \n" + player1.getName() + " will go first when the game is resumed\n\nYour game will resume in Player Vs. Player mode.");
                        System.out.println("Since " + player2.getName() + " made the last move, " + player1.getName() + " will go first when the game is resumed");
                    } else {
                        JOptionPane.showMessageDialog(null, "Since " + player1.getName() + " made the last move, \n" + player2.getName() + " will go first when the game is resumed\n\nYour game will resume in Player Vs. Player mode.");
                        System.out.println("Since " + player1.getName() + " made the last move, " + player2.getName() + " will go first when the game is resumed");
                    }
                    System.out.println("Your game will resume in Player Vs. Player mode");
                }

                playerList.add(player1);
                playerList.add(player2);

                //variable for stop watch (timer)
                //timer.start();
                while (gameOver == false) {

                    //check if any player wins yet after 7 moves
                    if (turnCounter >= 7) {
                        gameOver = winner(gridBoard, playerList, storeMode);
                    }

                    if (countMoves == 42) {
                        JOptionPane.showMessageDialog(null, "It's a tie!\nYou've both filled up the board!\n\nClick the Play button for a new game,\nor quit to exit.");
                        System.out.println("\nIt's a tie! You've both filled up the board! Click the button for a new game, or press quit to exit.");
                        gameOver = true;
                    }

                    if (gameOver == false) {
                        //call alternateTurns method to switch player turns accordingly and track number of moves
                        turnCounter = alternateTurns(turnCounter, grid, gridBoard, storeMode, modeNum);
                    }

                }

                //stop timer
                //timer.stop();

                //long timerFileLength = GUI_FileHandling.checkTimeFileEmpty();

                /*if (timerFileLength != 0 && timerPaused == false) {
                    GUI_FileHandling.readTimeFromFile();
                    totalTimeInSec = timer.getElapsedSeconds() + savePassedSec; //add up total time in seconds
                    totalTimeInMin = timer.getElapsedMinutes() + savePassedMin;
                    JOptionPane.showMessageDialog(null, "This game lasted " + totalTimeInSec + " seconds in total");
                    JOptionPane.showMessageDialog(null, "This game lasted " + totalTimeInMin + " minutes in total");
                    System.out.println("\nThis game lasted " + totalTimeInSec + " seconds in total!");
                    System.out.println("This game lasted " + totalTimeInMin + " minutes in total!");
                } else {
                    //display time player took to play the game
                    JOptionPane.showMessageDialog(null, "This game lasted " + timer.getElapsedSeconds() + " seconds!");
                    JOptionPane.showMessageDialog(null, "This game lasted " + timer.getElapsedMinutes() + " minutes!");
                    System.out.println("\nThis game lasted " + timer.getElapsedSeconds() + " seconds!");
                    System.out.println("This game lasted " + timer.getElapsedMinutes() + " minutes!");
                }*/

                //clear players list so a new game can restart with new players
                playerList.clear();
            }

            if (storeMode == 2) {
                //if it is a new game
                if (playAgainst != 3) {
                    modeNum = AIMessage();  //ask the user to choose a level -- easy, medium, hard

                    player1 = p1Info();  //ask the user to enter player 1's info
                    aiPlayer = aiPlayerInfo(modeNum);   //initialize info for the AI player (name, colour, level)

                    Object[] playerTurn = {"1", "2"};
                    turnCounter = JOptionPane.showOptionDialog(null, "Please choose which player goes first.\nPress 1 for Player 1, or 2 for Player 2:\n\n", "Who Goes First", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, playerTurn, playerTurn[0]);
                    turnCounter += 1; //since index of "1" is 0, and "2" is 1, and the index is what gets returned
                    System.out.println("\nPlease choose which player goes first. Enter 1 for Player 1, or 2 for Player 2:");
                    System.out.println("Players chose Player "+turnCounter+" to go first!");
                    //turnCounter = sc.nextInt();
                    countMoves = 0;

                } else { //if the user chooses to resume the last game

                    grid.printGrid();  //display the grid saved from the last game
                    JOptionPane.showMessageDialog(null, "Player 1    Name: " + player1.getName() + "   Colour: " + player1.getColour() + "\nPlayer 2    Name: " + aiPlayer.getName() + "   Colour: " + aiPlayer.getColour());

                    System.out.println("\nPlayer 1    Name: " + player1.getName() + "   Colour: " + player1.getColour());   //display both players' info saved from the last game
                    System.out.println("AI Player    Name: " + aiPlayer.getName() + "   Colour: " + aiPlayer.getColour());

                    if (turnCounter % 2 == 1) {
                        JOptionPane.showMessageDialog(null, "Since " + aiPlayer.getName() + " made the last move, \n" + player1.getName() + " will go first when the game is resumed\n\nYour game will resume in Player Vs. Player mode.");
                        System.out.println("Since " + aiPlayer.getName() + " made the last move, " + player1.getName() + " will go first when the game is resumed");
                    } else {
                        JOptionPane.showMessageDialog(null, "Since " + player1.getName() + " made the last move, \n" + aiPlayer.getName() + " will go first when the game is resumed\n\nYour game will resume in Player Vs. Player mode.");
                        System.out.println("Since " + player1.getName() + " made the last move, " + aiPlayer.getName() + " will go first when the game is resumed");
                    }

                    if (aiPlayer.getNumMode() == 1) {
                        JOptionPane.showMessageDialog(null, "Your chosen difficulty level: Easy");
                        System.out.println("\nYour chosen AI level: Easy");
                    } else if (aiPlayer.getNumMode() == 2) {
                        JOptionPane.showMessageDialog(null, "Your chosen difficulty level: Medium");
                        System.out.println("Your chosen AI level: Medium");
                    } else {
                        JOptionPane.showMessageDialog(null, "Your chosen difficulty level: Hard");
                        System.out.println("Your chosen AI level: Hard");
                    }

                }

                playerList.add(player1);
                playerList.add(aiPlayer);

                //variable for stop watch (timer)
                //timer.start();
                while (gameOver == false) {

                    //check if any player wins yet after 7 moves
                    if (turnCounter >= 7) {
                        gameOver = winner(gridBoard, playerList, storeMode);
                    }

                    if (countMoves == 42) {
                        JOptionPane.showMessageDialog(null, "It's a tie!\nYou've both filled up the board!\n\nClick the Play button for a new game,\nor quit to exit.");
                        System.out.println("\nIt's a tie! You've both filled up the board! Click the button for a new game, or press quit to exit.");
                        gameOver = true;
                    }

                    if (gameOver == false) {
                        //call alternateTurns method to switch player turns accordingly and track number of moves
                        turnCounter = aiAltTurns(turnCounter, grid, gridBoard, storeMode, modeNum, searchResult);
                    }


                }
                //stop timer and display the time the game lasted for in seconds and minutes
                //timer.stop();

                //long timerFileLength = GUI_FileHandling.checkTimeFileEmpty();

                /*if (timerFileLength != 0 && timerPaused == false) {
                    GUI_FileHandling.readTimeFromFile();
                    totalTimeInSec = timer.getElapsedSeconds() + savePassedSec; //add up total time in seconds
                    totalTimeInMin = timer.getElapsedMinutes() + savePassedMin;
                    JOptionPane.showMessageDialog(null, "This game lasted " + totalTimeInSec + " seconds in total");
                    JOptionPane.showMessageDialog(null, "This game lasted " + totalTimeInMin + " minutes in total");
                    System.out.println("\nThis game lasted " + totalTimeInSec + " seconds in total!");
                    System.out.println("This game lasted " + totalTimeInMin + " minutes in total!");
                } else {
                    //display time player took to play the game
                    JOptionPane.showMessageDialog(null, "This game lasted " + timer.getElapsedSeconds() + " seconds!");
                    JOptionPane.showMessageDialog(null, "This game lasted " + timer.getElapsedMinutes() + " minutes!");
                    System.out.println("\nThis game lasted " + timer.getElapsedSeconds() + " seconds!");
                    System.out.println("This game lasted " + timer.getElapsedMinutes() + " minutes!");
                }*/

                //clear players list so a new game can restart with new players
                playerList.clear();
            }
            //prompt user to continue playing a new game or exit
            playerTurnLabel.setText("");
            int newGame = JOptionPane.showConfirmDialog(null, "Game Over! Play Again? ", "Press 'Play Game' to play again", JOptionPane.YES_NO_OPTION);
            //JOptionPane.showMessageDialog(null,"Game Over! Press 'Play Game' to play again!");
            System.out.println("\nWould you like to play again?");
            playGame = false;
            //startNum = sc.nextInt();
            if (newGame == JOptionPane.NO_OPTION) {
                int saveOption = JOptionPane.showConfirmDialog(null, "Quit and save progress? If you choose no, your scores will not be saved.", "Quit and Save", JOptionPane.YES_NO_OPTION);
                if (saveOption == JOptionPane.NO_OPTION) {
                    GUI_FileHandling.clearFiles();
                }
                JOptionPane.showMessageDialog(null, "Thank you for playing Connect 4! :D");
                System.out.println("Thank you for playing Connect 4! :D");
                dispose(); //exits gui frame
            } else {
                playGame = false;
            }

        } //end while (playGame) loop
        //GUI_FileHandling.clearTimerFile();

    }

    public static void updateBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (gridBoard[r][c] == 'X') {
                    //gridButtons[r][c].setOpaque(true);
                    gridButtons[r][c].setIcon(redIcon);
                    gridButtons[r][c].setVisible(true);
                } else if (gridBoard[r][c] == 'O'){
                    //gridButtons[r][c].setOpaque(true);
                    gridButtons[r][c].setIcon(yellowIcon);
                    gridButtons[r][c].setVisible(true);
                }
            }
        }
    }

    /**
     * void start method will initialize the current nanoseconds and will check if the timer is running (true)
     */
    /*public void start() {
        this.timerStartTime = System.nanoTime();
        this.timerRunning = true;
    }*/

    /**
     * void stop method will initialize the stop time and will stop timer from running, setting it to false
     */
    /*public void stop() {
        this.timerStopTime = System.nanoTime();
        this.timerRunning = false;
    }*/

    /**
     * this function will store the value of the elapsed time in nanoseconds,
     * then it will convert it to seconds by dividing it by nanoseconds per second
     * returns time in seconds
     */
    /*public long getElapsedSeconds() {
        long elapsedTime;

        if (timerRunning) {
            elapsedTime = (System.nanoTime() - timerStartTime);
        } else {
            elapsedTime = (timerStopTime - timerStartTime);
        }

        long timeInSeconds = elapsedTime / nanoSecondsPerSecond; //convert to seconds

        return timeInSeconds;
    }*/

    /**
     * this function will store the value of the elapsed time in nanoseconds,
     * then it will convert it to minutes by dividing it by nanoseconds per minute
     * returns time in minutes
     */
    /*public long getElapsedMinutes() {
        long elapsedTime;

        if (timerRunning) {
            elapsedTime = (System.nanoTime() - timerStartTime);
        } else {
            elapsedTime = (timerStopTime - timerStartTime);
        }

        long timeInMinutes = elapsedTime / nanoSecondsPerMinute; //convert to minutes

        return timeInMinutes;
    }*/

    /**
     * Resets audio stream and restarts the music. It creates audioInputStream
     * object and opens audioInputStream to the clip named music.
     */
    public void resetMusic() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(musicFilePath).getAbsoluteFile());
        music.open(audioInputStream);
        music.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * display the menu in the main method
     */
    public static void menu() {
        System.out.println("Welcome to Connect 4!");
        //prompt user to begin game by entering 1
        System.out.println("To start the game, please enter 1");
    }

    /**
     * display the welcome menu for player vs. computer option
     * returns an int, the number of the level they choose
     */
    public static int AIMessage() {
        //prompt user to choose either easy, medium, or hard mode in player vs. AI mode
        System.out.println("\nWelcome to Player Vs. AI mode! Please enter a number that corresponds with your desired difficulty level:");
        System.out.println("\n1: Easy\n2: Medium\n3: Hard");
        //int num = sc.nextInt(); //modeNum will be assigned this returned value

        Object[] levelOptions = {"Easy", "Medium", "Hard"};
        int num = JOptionPane.showOptionDialog(null, "Please choose the difficulty level\nyou would like to play against.\n\n", "Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, levelOptions, levelOptions[0]);
        num++;
        System.out.println("\nPlayer chose difficulty level: "+num);
        return num;
    }

    /**
     * method prompts user to enter player 1's information
     * returns player object to assign to global variable player1
     */
    public static GUI_Players p1Info() {
        //prompts player1 to enter their name and to choose a colour
        System.out.println("\nPlease enter a name for Player 1:");
        playerName = JOptionPane.showInputDialog("Please enter Player 1's name");
        //String playerName = sc.next();

        System.out.println("\nWelcome " + playerName + ", you are Player 1. Please enter either red or yellow as the colour of your tokens: (X or O)");
        //String tempPlayerColour = sc.next().toUpperCase();
        //playerColour = tempPlayerColour.charAt(0);

        Object[] colourOptions = {"Red", "Yellow"};
        int j = JOptionPane.showOptionDialog(null, "Please choose a colour for your tokens:\n\n", "Choose your colour!", JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, null, colourOptions, colourOptions[0]);
        if (j == JOptionPane.YES_OPTION) {
            playerColour = 'X';
            playerChip = redIcon;
        } else {
            playerColour = 'O';
            playerChip = yellowIcon;
        }

        //create player object to return created object to player1 in main method
        GUI_Players player = new GUI_Players(playerName, playerColour, 1, score1, playerChip);

        //display player's name and colour
        if (player.getColour() == 'X') {
            JOptionPane.showMessageDialog(null,"Welcome "+playerName+"! The colour of your tokens will be RED.");
            System.out.println("Player 1 is RED (X)");
        } else {
            JOptionPane.showMessageDialog(null,"Welcome "+playerName+"! The colour of your tokens will be YELLOW.");
            System.out.println("Player 1 is YELLOW (O)");
        }

        return player;
    }

    /**
     * method prompts user to enter player 2's information
     * returns player object to assign to global variable player2
     */
    public static GUI_Players p2Info() {
        char tempColour = 't';
        GUI_Players player = player2; //temporarily set player object inside this method as default player2

        playerName = JOptionPane.showInputDialog("Please enter Player 2's name");
        System.out.println("\nPlease enter a name for Player 2:");
        //String playerName = sc.next();

        if (player1.getColour() == 'X') { //if player1 is red

            playerColour = 'O'; //set player2's colour as yellow
            playerChip = yellowIcon;

            //create player object to return created object to player1 in main method
            player = new GUI_Players(playerName, playerColour,2, score2, playerChip);
            JOptionPane.showMessageDialog(null,"Welcome "+playerName+"! The colour of your tokens will be YELLOW.");
            System.out.println("\nWelcome " + playerName + ", you are Player 2.");
            System.out.println("Player 2 is YELLOW (O)");

        } else {

            playerColour = 'X';
            playerChip = redIcon;

            player = new GUI_Players(playerName, playerColour,2, score2, playerChip);
            JOptionPane.showMessageDialog(null,"Welcome "+playerName+"! The colour of your tokens will be RED.");
            System.out.println("\nWelcome " + playerName + ", you are Player 2.");
            System.out.println("Player 2 is RED (X)");
        }

        //set player 2's colour as the other open option since player 1 has already chosen their colour and inform them of their colour
        if (player1.getColour() == 'X') {
            player.setColour('O');

        } else {
            player.setColour('X');

        }

        return player;
    }

    /**
     * method will switch the turns according to the starting player
     * returns int var. turnCounter to continue switching players
     */
    public static int alternateTurns(int turnCounter, GUI_Board grid, char[][] gridBoard, int storeMode, int modeNum) {
        //if Player 1 goes first:
        if (turnCounter % 2 == 1) {

            if (player1.getColour() == 'X') {
                playerTurnLabel.setText("It is "+player1.getName()+"'s, Player 1's turn. (RED)");

            } else {
                playerTurnLabel.setText("It is "+player1.getName()+"'s, Player 1's turn. (YELLOW)");
            }

            //calls the drop method in player class to allow user to choose a col num
            player1.drop(GUI_Board.ROWS, gridBoard, turnCounter, storeMode, modeNum);
            grid.printGrid(); //display grid after they successfully drop a piece
        }

        //if Player 2 goes first:
        if (turnCounter % 2 == 0) {

            if (player2.getColour() == 'X') {
                playerTurnLabel.setText("It is "+player2.getName()+"'s, Player 2's turn. (RED)");
            } else {
                playerTurnLabel.setText("It is "+player2.getName()+"'s, Player 2's turn. (YELLOW)");
            }

            //player2 calls drop method in player class
            player2.drop(GUI_Board.ROWS, gridBoard, turnCounter, storeMode, modeNum);
            grid.printGrid();
        }

        turnCounter++;
        return turnCounter;
    }

    /**
     * initialize AI player's info and return ComputerPlayer
     * object to assign to global variable aiPlayer
     */
    public static GUI_ComputerPlayer aiPlayerInfo(int modeNum) { //get modeNum from user input from ai message method
        String aiName = "Bob"; //set a name for aiPlayer
        char tempColour = 't';
        System.out.println("\nPlayer 2's name is " + aiName);
        GUI_ComputerPlayer aiPlayer = new GUI_ComputerPlayer(aiName, tempColour, 2, scoreAI, playerChip, modeNum);

        //set player 2's colour as the other open option since player 1 has already chosen their colour
        if (player1.getColour() == 'X') {
            aiPlayer.setColour('O');
            aiPlayer.setChip(yellowIcon);
            JOptionPane.showMessageDialog(null,"Player 2's name, Computer Player, is "+aiName
                    +"\nThe colour of their tokens will be YELLOW");
            System.out.println("Player 2 is YELLOW (O)");
        } else {
            aiPlayer.setColour('X');
            aiPlayer.setChip(redIcon);
            JOptionPane.showMessageDialog(null,"Player 2's name, Computer Player, is "+aiName
                    +"\nThe colour of their tokens will be RED");
            System.out.println("Player 2 is RED (X)");
        }

        return aiPlayer;
    }

    /**
     * method will switch the turns according to the starting player
     */
    public static int aiAltTurns(int turnCounter, GUI_Board grid, char[][] gridBoard, int storeMode, int modeNum, char[]searchResult) {

        //if Player 1 goes first:
        if (turnCounter % 2 == 1) {
            if (player1.getColour() == 'X') {
                playerTurnLabel.setText("It is "+player1.getName()+"'s, Player 1's turn. (RED)");
            } else {
                playerTurnLabel.setText("It is "+player1.getName()+"'s, Player 1's turn. (YELLOW)");
            }
            player1.drop(GUI_Board.ROWS, gridBoard, turnCounter, storeMode, modeNum);
            grid.printGrid();
        }

        //if Player 2 goes first:
        if (turnCounter % 2 == 0) {

            //display computer player turn and colour
            if (aiPlayer.getColour() == 'X') {
                playerTurnLabel.setText("It is "+aiPlayer.getName()+"'s, Player 2's turn. (RED)");
            } else {
                playerTurnLabel.setText("It is "+aiPlayer.getName()+"'s, Player 2's turn. (YELLOW)");
            }

            //according to what difficulty level they chose, different drop methods will be used
            if (aiPlayer.getNumMode() == 1) {

                //calls easyDrop method in ComputerPlayer class to get randomized column from computer
                aiPlayer.easyDrop(GUI_Board.ROWS, gridBoard);
                grid.printGrid();

            } else if (aiPlayer.getNumMode() == 2) {

                /*calls mediumDrop method in ComputerPlayer class to get a random computer player's piece and add piece that connects, or if
                 the random piece that's chosen has been blocked, it will find another piece, or drop it into a random column*/
                aiPlayer.mediumDrop(aiPlayer.getColour(), GUI_Board.ROWS, GUI_Board.COLS, gridBoard, turnCounter);
                grid.printGrid();

            } else if (aiPlayer.getNumMode() == 3){

                aiPlayer.hardDrop(aiPlayer.getColour(), GUI_Board.ROWS, gridBoard, turnCounter, searchResult);
                grid.printGrid();

            }
        }

        turnCounter++; //gets incremented to switch turns
        return turnCounter;
    }

    /**
     * horizontalCheck takes in gridBoard as parameter and goes through each row
     * to check if there are connecting 4 in one row and will return true if there are
     */
    public static boolean horizontalCheck(char[][] gridBoard, GUI_Players player) {
        int count = 0;
        boolean four = false;

        //nested for loop iterates through each column and each row (checks each column in one row)
        for (int r = GUI_Board.ROWS-1; r >= 0 ; r--) {
            count = 0; //reset count to 0, so it doesn't end up checking and adding the next row and column
            for (int c = 0; c < GUI_Board.COLS; c++) {
                if (gridBoard[r][c] == player.getColour()) {
                    count++;
                } else {
                    count = 0;
                }

                if (count == 4) {
                    four = true;
                    return four;
                }
            }
        }
        return four;
    }

    /**
     * verticalCheck goes through each column in gridBoard to check if there are
     * connecting 4 in a column and will return true if there are
     */
    public static boolean verticalCheck(char[][] gridBoard, GUI_Players player) {
        int count = 0;
        boolean four = false;

        //nested for loop goes through each column vertically by going through each row in the same one column
        for (int c = 0; c < GUI_Board.COLS ; c++) {
            count = 0;
            for (int r = GUI_Board.ROWS-1; r >= 0; r--) {
                if (gridBoard[r][c] == player.getColour()) {
                    count++;
                } else {
                    count = 0;
                }

                if (count == 4) {
                    four = true;
                    return four;
                }
            }
        }
        return four;
    }

    /**
     * diagBottomUpCheck takes in the row and column a player's piece is found in.
     * From there, it will check if there are connecting 4 from the bottom
     * to the top right, each row up and each column to the right. Will return
     * true if there are connecting 4.
     */
    public static boolean diagBottomUpCheck(char[][] gridBoard, GUI_Players player, int r, int c) {
        int count = 0;
        boolean four = false;

        /* for loop will iterate a max of 4 times, starting from the leftmost column the piece is found in, and will
         increment the column to check the next column as the row gets decremented */
        for (int j = c; j <= c+3; j++) {
            if (r >= 0 && j <= 6) { //if statement only runs if row and column are in the range
                if (gridBoard[r][j] == player.getColour()) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == 4) {
                    four = true;
                    return four;
                }
                r--; //if row is still in the range, then decrement to check the next row
            }
        }
        return four;
    }

    /**
     * diagTopDownCheck takes in the row and column a player's piece is found in.
     * From there, it will check if there are connecting 4 from the top
     * to the bottom right, each row down and each column to the right.
     * Will return true if there are connecting 4.
     */
    public static boolean diagTopDownCheck(char[][] gridBoard, GUI_Players player, int r, int c) {
        int count = 0;
        boolean four = false;

        /* for loop will iterate a max of 4 times, starting from the leftmost column the piece is found in, and will
         increment the column to check the next column as the row gets incremented */
        for (int j = c; j <= c+3; j++) {
            if (r <= 5 && j <= 6) { //if statement only runs if row and column are in the range
                if (gridBoard[r][j] == player.getColour()) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == 4) {
                    four = true;
                    return four;
                }
                r++; //row gets incremented to check next row down
            }
        }
        return four;
    }

    /**
     * winner method will go through each check method to determine whether game is over or not
     * if a check method returns true, winner method returns true and game is over, else
     * returns false and game continues
     */
    public static boolean winner(char[][] gridBoard, ArrayList<GUI_Players> playerList, int storeMode) {
        boolean gameOver = false;

        //for loop goes through and checks each player's pieces to see if they won
        for (int i = 0; i < playerList.size(); i++) {
            //first check horizontally
            gameOver = horizontalCheck(gridBoard, playerList.get(i)); //call horizontalCheck method with current player for loop is going through
            if (gameOver) {
                GUI_FileHandling.clearWinningGameFiles();
                if (playerList.get(i).getColour() == 'X') {
                    JOptionPane.showMessageDialog(null,playerList.get(i).getName()+ " wins by connecting\n4 RED horizontally!");
                    System.out.println("\n"+playerList.get(i).getName()+ " wins by connecting 4 " +playerList.get(i).getColour()+ " horizontally!");
                } else {
                    JOptionPane.showMessageDialog(null,playerList.get(i).getName()+ " wins by connecting\n4 YELLOW horizontally!");
                    System.out.println("\n"+playerList.get(i).getName()+ " wins by connecting 4 " +playerList.get(i).getColour()+ " horizontally!");
                }
                grid.initialGrid();

                //if player 1 is the winner
                if (storeMode == 1) {
                    if (playerList.get(i).getPlayerNum() == 1) {
                        score1++; //add score to player1
                        player1.setPlayerScore(score1);

                    } else {  //player 2 is the winner
                        score2++;
                        player2.setPlayerScore(score2);
                    }
                    System.out.println("\n"+player1.getName()+"    score: "+score1);
                    System.out.println("\n"+player2.getName()+"    score: "+score2);
                } else { //storeMode = 2
                    if (playerList.get(i).getPlayerNum() == 1) {
                        score1++;
                        player1.setPlayerScore(score1);
                    } else {
                        scoreAI++;
                        aiPlayer.setPlayerScore(scoreAI);
                    }
                    System.out.println("\n"+player1.getName()+"    score: "+score1);
                    System.out.println("\n"+aiPlayer.getName()+"    score: "+scoreAI);
                }

                GUI_FileHandling.writeScoresToFile(storeMode);
                return gameOver;
            }

            //next check vertically
            gameOver = verticalCheck(gridBoard, playerList.get(i));
            if (gameOver) {
                GUI_FileHandling.clearWinningGameFiles();
                if (playerList.get(i).getColour() == 'X') {
                    JOptionPane.showMessageDialog(null,playerList.get(i).getName()+ " wins by connecting\n4 RED vertically!");
                    System.out.println("\n"+playerList.get(i).getName()+ " wins by connecting 4 " +playerList.get(i).getColour()+ " vertically!");
                } else {
                    JOptionPane.showMessageDialog(null,playerList.get(i).getName()+ " wins by connecting\n4 YELLOW vertically!");
                    System.out.println("\n"+playerList.get(i).getName()+ " wins by connecting 4 " +playerList.get(i).getColour()+ " vertically!");
                }
                grid.initialGrid();

                //if player 1 is the winner
                if (storeMode == 1) {
                    if (playerList.get(i).getPlayerNum() == 1) {
                        score1++; //add score to player1
                        player1.setPlayerScore(score1);

                    } else {  //player 2 is the winner
                        score2++;
                        player2.setPlayerScore(score2);
                    }
                    System.out.println("\n"+player1.getName()+"    score: "+score1);
                    System.out.println("\n"+player2.getName()+"    score: "+score2);
                }
                else { //storeMode = 2
                    if (playerList.get(i).getPlayerNum() == 1) {
                        score1++;
                        player1.setPlayerScore(score1);
                    } else {
                        scoreAI++; //add score to AI player
                        aiPlayer.setPlayerScore(scoreAI);
                    }
                    System.out.println("\n"+player1.getName()+"    score: "+score1);
                    System.out.println("\n"+aiPlayer.getName()+"    score: "+scoreAI);
                }

                GUI_FileHandling.writeScoresToFile(storeMode);
                return gameOver;
            }

            /* nested for loop accesses each element in gridBoard starting from bottom row
             and determines the row and column a player's piece is found in */
            for (int r = 5; r >= 0; r--) {
                for (int c = 0; c < gridBoard[r].length; c++) {
                    if (gridBoard[r][c] == playerList.get(i).getColour()) {
                        //check diagonal bottom to top right
                        gameOver = diagBottomUpCheck(gridBoard, playerList.get(i), r, c);
                        if (gameOver) {
                            GUI_FileHandling.clearWinningGameFiles();
                            if (playerList.get(i).getColour() == 'X') {
                                JOptionPane.showMessageDialog(null,playerList.get(i).getName()+ " wins by connecting\n4 RED diagonally!");
                                System.out.println("\n"+playerList.get(i).getName()+ " wins by connecting 4 " +playerList.get(i).getColour()+ " diagonally!");
                            } else {
                                JOptionPane.showMessageDialog(null,playerList.get(i).getName()+ " wins by connecting \n4 YELLOW diagonally!");
                                System.out.println("\n"+playerList.get(i).getName()+ " wins by connecting 4 " +playerList.get(i).getColour()+ " diagonally!");
                            }
                            grid.initialGrid();

                            //if player 1 is the winner
                            if (storeMode == 1) {
                                if (playerList.get(i).getPlayerNum() == 1) {
                                    score1++; //add score to player1
                                    player1.setPlayerScore(score1);

                                } else {  //player 2 is the winner
                                    score2++;
                                    player2.setPlayerScore(score2);
                                }
                                System.out.println("\n"+player1.getName()+"    score: "+score1);
                                System.out.println("\n"+player2.getName()+"    score: "+score2);
                            } else { //storeMode = 2
                                if (playerList.get(i).getPlayerNum() == 1) {
                                    score1++;
                                    player1.setPlayerScore(score1);
                                } else {
                                    scoreAI++; //add score to AI player
                                    aiPlayer.setPlayerScore(scoreAI);
                                }
                                System.out.println("\n"+player1.getName()+"    score: "+score1);
                                System.out.println("\n"+aiPlayer.getName()+"    score: "+scoreAI);
                            }

                            GUI_FileHandling.writeScoresToFile(storeMode);
                            return gameOver;
                        }

                        //check diagonal top to bottom right
                        gameOver = diagTopDownCheck(gridBoard, playerList.get(i), r, c);
                        if (gameOver) {
                            GUI_FileHandling.clearWinningGameFiles();
                            if (playerList.get(i).getColour() == 'X') {
                                JOptionPane.showMessageDialog(null,playerList.get(i).getName()+ " wins by\nconnecting 4 RED diagonally!");
                                System.out.println("\n"+playerList.get(i).getName()+ " wins by connecting 4 " +playerList.get(i).getColour()+ " diagonally!");
                            } else {
                                JOptionPane.showMessageDialog(null,playerList.get(i).getName()+ " wins by\nconnecting 4 YELLOW diagonally!");
                                System.out.println("\n"+playerList.get(i).getName()+ " wins by connecting 4 " +playerList.get(i).getColour()+ " diagonally!");
                            }
                            grid.initialGrid();

                            //if player 1 is the winner
                            if (storeMode == 1) {
                                if (playerList.get(i).getPlayerNum() == 1) {
                                    score1++; //add score to player1
                                    player1.setPlayerScore(score1);

                                } else {  //player 2 is the winner
                                    score2++;
                                    player2.setPlayerScore(score2);
                                }
                                System.out.println("\n"+player1.getName()+"    score: "+score1);
                                System.out.println("\n"+player2.getName()+"    score: "+score2);
                            } else { //storeMode = 2
                                if (playerList.get(i).getPlayerNum() == 1) {
                                    score1++;
                                    player1.setPlayerScore(score1);
                                } else {
                                    scoreAI++; //add score to AI player
                                    aiPlayer.setPlayerScore(scoreAI);
                                }
                                System.out.println("\n"+player1.getName()+"    score: "+score1);
                                System.out.println("\n"+aiPlayer.getName()+"    score: "+scoreAI);
                            }

                            GUI_FileHandling.writeScoresToFile(storeMode);
                            return gameOver;
                        }
                    }
                }
            } // end of for loop for diagonal checks

        } //end of for loop for winner check
        return gameOver;
    }

    public static void trackScores (int storeMode, ArrayList<GUI_Players>playerList, int i) {
        if (storeMode == 1) {
            if (playerList.get(i).getPlayerNum() == 1) {
                score1++;
                player1.setPlayerScore(score1);

            }
            else {      //player 2 is the winner
                score2++;
                player2.setPlayerScore(score2);
            }
            System.out.println(player1.getName()+"    score: "+score1);
            System.out.println(player2.getName()+"    score: "+score2);
        }
        else {
            if (playerList.get(i).getPlayerNum() == 1) {
                score1++;
                player1.setPlayerScore(score1);
            }
            else {
                scoreAI++;
                aiPlayer.setPlayerScore(scoreAI);
            }
            System.out.println(player1.getName()+"    score: "+score1);
            System.out.println(aiPlayer.getName()+"    score: "+scoreAI);
        }

        GUI_FileHandling.writeScoresToFile(storeMode);
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Connect4_GUI guiFrame = new Connect4_GUI();
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
