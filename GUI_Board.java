/**
 * Class Board will be the template for the grid board and will have methods for accessing the grid
 */
public class GUI_Board {

    //constants
    public final static int ROWS = 6;
    public final static int COLS = 7;

    //instance variable
    private char[][] grid;

    //board constructor method
    public GUI_Board(char[][] gridBoard) {
        grid = gridBoard;
    }

    /**
     * initializes grid with underscores "_"
     */
    public void initialGrid() {

        //for loop goes through each row and column in the grid
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = '_';
                Connect4_GUI.gridButtons[r][c].setIcon(Connect4_GUI.whiteIcon);
            }
        }

    } //end of initialGrid

    /**
     * prints out each grid element with borders to resemble an actual grid
     */
    public void printGrid() {

        //for loop goes through each row and column and displays the updated board, also creating a grid layout
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                System.out.print("  |  " + grid[r][c]);
                if (grid[r][c] == 'X') {
                    //gridButtons[r][c].setOpaque(true);
                    Connect4_GUI.gridButtons[r][c].setIcon(Connect4_GUI.redIcon);
                } else if (grid[r][c] == 'O'){
                    //gridButtons[r][c].setOpaque(true);
                    Connect4_GUI.gridButtons[r][c].setIcon(Connect4_GUI.yellowIcon);
                }
            }
            System.out.println("  |");

            if (r < grid.length - 1) {
                System.out.println("  +-----+-----+-----+-----+-----+-----+-----+");
            } else {
                System.out.println("     1     2     3     4     5     6     7  "); //column numbers
            }
        }

    } //end of printGrid method

} //end of class Board

