package jbUtils;

import java.util.Random;

public class JbPingPongBoard {
    private Random rand = new Random();
    private int rows;
    private int cols;
    private int upper_bound;
    private int lower_bound;

    private int[][] live_board;
    private int[][] next_board;

    private void init(int _rows, int _cols) {
        rows = _rows;
        cols = _cols;

        live_board = new int[rows][cols];
        next_board = new int[rows][cols];
    }

    public JbPingPongBoard(int _rows, int _cols) {
        init(_rows, _cols);
        resetNextBoard(0, 1);
        swapBoards();
    }

    public JbPingPongBoard(int _rows, int _cols, int init) {
        init(_rows, _cols);
        resetNextBoard(init, init);
        swapBoards();
    }

    public JbPingPongBoard(int _rows, int _cols, int upper, int lower) {
        init(_rows, _cols);
        resetNextBoard(upper, lower);
        swapBoards();
    }

    public int[][] get() {
        return live_board;
    }
    public void setNext(int row, int col, int val) {
        next_board[row][col] = val;
    }
    public int getNext(int row, int col) {
        return next_board[row][col];
    }
    public void setLive(int row, int col, int val) {
        live_board[row][col] = val;
    }
    public int getLive(int row, int col) {
        return live_board[row][col];
    }

    public void swapBoards() {
        int[][] temp = live_board;
        live_board = next_board;
        next_board = temp;
    }

    public int countNearest(int x, int y) {
        return scanCell(x, y, 1, false);
    }

    public int countNextNearest(int x, int y) {
        return scanCell(x, y, 1, true);
    }

    private void resetNextBoard(int lower, int upper) {
        next_board = new int[rows][cols];
        for (int x = 0; x < rows; x++)
            for (int y = 0; y < cols; y++)
                setNext(x, y, rand.nextInt(upper - lower + 1) + lower);
    }
    public void resetNextBoard() {
        resetNextBoard(0, 1);
    }

    public void resetBoard() {
        resetNextBoard(0, 1);
        swapBoards();
        resetNextBoard(0, 1);
    }

    public void print() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++)
                System.out.print(getLive(x, y) + " ");
            System.out.println();
        }
        System.out.println();
    }

    // Touching only board generation
    public void generateNearest() {
        generateSpecific(1, false);
    }

    // Touching and adjacent generation
    public void generateNextNearest() {
        generateSpecific(1, true);
    }

    // Board generation helper
    public void generateSpecific(int distance, boolean fill) {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                setNext(x, y, scanCell(x, y, distance, fill));
            }
        }
    }

    // Cellular automata with the following rules
    public void mutateNextGeneration() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                int neighbor_count = getNext(x, y);
                boolean alive = getLive(x, y) > 0;
                if(alive) {
                    if (neighbor_count > 3) {
                        // die
                        setNext(x, y, 0);
                    } else if (neighbor_count < 2) {
                        // die
                        setNext(x, y, 0);
                    }
                } else if (neighbor_count == 3) {
                    setNext(x, y, 1);
                } else {
                    setNext(x, y, 0);
                }
            }
        }
    }

    // Scan distance helper
    private int getDistance(int x1, int y1, int x2, int y2, boolean fill) {
        if (!fill) // Used for non-fill
            return Math.abs(x2 - x1) + Math.abs(y2 - y1);
        else // Used for fill
            return (int) Math.floor(Math.sqrt((x2 - x1)^2 + (y2 - y1)^2));
    }

    // Scan cell for nearby alive cells given a distance and search style
    private int scanCell(int x, int y, int distance, boolean fillSearch) {
        int ones_counter = 0;
        for (int i_x = -distance; i_x <= distance; i_x++) {
            for (int i_y = -distance; i_y <= distance; i_y++) {
                int n_x = x + i_x;
                int n_y = y + i_y;

                int i_distance = getDistance(x, y, n_x, n_y, fillSearch);
                int[] pointer = new int[]{(n_x + rows) % rows, (n_y + cols) % cols};

                if(i_distance <= distance && !(n_x == 0 && n_y == 0))
                    if(getLive(pointer[0], pointer[1]) > 0)
                        ones_counter++;
            }
        }
        return ones_counter;
    }
}
