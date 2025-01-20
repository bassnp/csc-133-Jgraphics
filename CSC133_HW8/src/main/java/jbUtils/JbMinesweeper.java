package jbUtils;

import java.util.Random;

public class JbMinesweeper {
    private Random rand = new Random();
    private int rows;
    private int cols;
    private int mine_count;
    private int points;
    private int total;
    private String game_state = "";

    /*
    Increment by 2 when revealed instead of needing extra data.

    0 == Safe - Hidden - G
    1 == Mine - Hidden - M

    2 == Safe - Clicked - G
    3 == Mine - Clicked - M
    */

    private int[][] points_board;
    private int[][] state_board;

    public JbMinesweeper(int _rows, int _cols, int _mines) {
        rows = _rows;
        cols = _cols;

        points_board = new int[rows][cols];
        state_board = new int[rows][cols];

        mine_count = _mines;
        points = 0;
        total = 0;
        game_state = "";

        resetBoard();
    }

    public String getGameState() {
        return game_state;
    }

    public int getScore() {
        return points;
    }

    public int[][] get_board() {
        return state_board;
    }

    public int getState(int _row, int _col) {
        return state_board[_row][_col];
    }

    public void setState(int _row, int _col, int _val) {
        state_board[_row][_col] = _val;
    }

    public int getPoints(int _row, int _col) {
        return points_board[_row][_col];
    }

    public void setPoints(int _row, int _col, int _val) {
        points_board[_row][_col] = _val;
    }

    public boolean invokeClick(int[] pos) {
        if (pos[0] >= 0 && pos[0] < rows && pos[1] >= 0 && pos[1] < cols) {
            if (getState(pos[0], pos[1]) >= 2) // Already clicked
                return false;

            int state = getState(pos[0], pos[1]);
            setState(pos[0], pos[1], state + 2);

            if (state == 1) { // Clicked on a mine
                game_state = "lost";
                return true;
            } else {
                points += getPoints(pos[0], pos[1]);
                total += 1;

                if(total == (rows * cols) - mine_count) {
                    game_state = "won";
                    return true; // no more golds to click
                }

                return false;
            }
        }
        return false;
    }
    
    public void resetBoard() {
        // Clear Board
        points = 0;
        total = 0;
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++) {
                setState(x, y, 0);
                setPoints(x, y, 0);
            }

        // Fill with mines
        for (int i = 0; i < mine_count; i++) {
            boolean valid_pos = false;
            while(!valid_pos) { // Ensure placement
                int row = rand.nextInt(0, rows);
                int col = rand.nextInt(0, cols);
                if(getState(row, col) == 0) {
                    setState(row, col, 1);
                    valid_pos = true;
                }
            }
        }

        // Calculate points
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if(getState(x, y) % 2 == 0) {
                    int mines = scanCell(x, y, 1);
                    int mine_points = mines * 10;
                    int safe_points = (8 - mines) * 5;
                    setPoints(x, y, mine_points + safe_points);
                }
            }
        }
    }

    public void reveal_cells() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                int state = getState(x, y);
                if(state < 2) {
                    setState(x, y, state + 2); // Turnover 2 states to reveal
                }
            }
        }
    }

    public static float[] mapStateToColor(int state) {
        if(state < 2)
            return new float[]{0.4f, 0.4f, 0.4f, 1f}; // gray
        else {
            if (state == 2)
                return new float[]{0.9f, 0.9f, 0.9f, 1f}; // safe
            else if (state == 3)
                return new float[]{0.9f, 0.1f, 0.1f, 1f}; // mine
            else
                return new float[]{0, 0, 0, 1f}; // black
        }
    }

    // Scan distance helper
    private int getDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.floor(Math.sqrt((x2 - x1)^2 + (y2 - y1)^2));
    }

    // Scan cell for nearby alive cells given a distance and search style
    private int scanCell(int x, int y, int distance) {
        int ones_counter = 0;
        for (int i_x = -distance; i_x <= distance; i_x++) {
            for (int i_y = -distance; i_y <= distance; i_y++) {
                int n_x = x + i_x;
                int n_y = y + i_y;

                int i_distance = getDistance(x, y, n_x, n_y);
                int[] pointer = new int[]{(n_x + rows) % rows, (n_y + cols) % cols};

                if(i_distance <= distance && !(n_x == 0 && n_y == 0))
                    if(getState(pointer[0], pointer[1]) % 2 == 1) // odd is a mine
                        ones_counter++;
            }
        }
        return ones_counter;
    }

    public void printStates() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                System.out.print((getState(x, y) % 2 == 0 ? "G" : "M") + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printPoints() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++)
                System.out.printf("%02d ", getPoints(x, y));
            System.out.println();
        }
        System.out.println();
    }
}
