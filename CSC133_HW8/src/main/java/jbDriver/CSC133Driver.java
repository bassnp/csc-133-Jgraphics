// Jaden Bruha, Assignment 8, CSC 133-09, Fall 2024

package jbDriver;

import jbRender.JbRenderCells;
import jbRender.JbTextureObject;
import jbUtils.JbKeyListener;
import jbUtils.JbMinesweeper;
import jbUtils.JbMouseListener;
import jbUtils.JbWindowManager;

import static jbDriver.JbSpot.*;
import static org.lwjgl.glfw.GLFW.*;

public class CSC133Driver {
    public static void main(String[] args) {
        // Create a minesweeper board
        int board_rows = 7, board_cols = 9, mine_count = 14;
        JbMinesweeper game_board = new JbMinesweeper(board_rows, board_cols, mine_count);
        game_board.printStates();
        game_board.printPoints();

        // Create window with Programmable Pipeline
        JbRenderCells cell_board = new JbRenderCells();
        JbWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, "CSUS CSC133");
        cell_board.initOpenGL(JbWindowManager.get());


        // Load and Cache textures
        cell_board.setTextureIndex(new int[]{0, 1},
                new JbTextureObject("assets/images/GreenGrassORG.jpg"));
        cell_board.setTextureIndex(2,
                new JbTextureObject("assets/images/Mario1.png"));
        cell_board.setTextureIndex(3,
                new JbTextureObject("assets/images/MarioWithGun2.png"));

        int frame_delay = 10;
        boolean exit = false;
        boolean game_over = false;
        boolean revealed = false;
        while (!exit) { // Game loop
            if (game_over) { // Prerender check for coloring
                String game_state = game_board.getGameState();
                if(game_state.equals("lost"))
                    cell_board.startFrame(new float[]{0.2f, 0.1f, 0.1f, 1f});
                else if(game_state.equals("won"))
                    cell_board.startFrame(new float[]{0.1f, 0.7f, 0.1f, 1f});
                else
                    cell_board.startFrame();
            }
            else
                cell_board.startFrame();
            cell_board.renderCells(game_board.get_board());
            cell_board.endFrame(frame_delay);

            // Keybindings
            if(JbKeyListener.keyClicked(GLFW_KEY_ESCAPE))
                exit = true;
            if(JbKeyListener.keyClicked(GLFW_KEY_R)) {
                game_board.resetBoard();
                game_board.printStates();
                game_board.printPoints();
                game_over = false;
                revealed = false;
                System.out.println("Reset the board");
            }

            if(game_over) { // Game end
                if(!revealed) { // End only once
                    game_board.reveal_cells();
                    revealed = true;

                    System.out.printf("Game Over! You %s!\nYour Score: %d\n\n",
                            game_board.getGameState(), game_board.getScore());
                }
            } else if(JbMouseListener.mouseButtonClick(GLFW_MOUSE_BUTTON_1)) { // Game is running
                int mx = JbMouseListener.getX();
                int my = JbMouseListener.getY();

                // Clicked on the screen
                if(mx >= 0 && my >= 0 && mx <= WIN_WIDTH && my <= WIN_HEIGHT) {
                    float nx = (float) mx / WIN_WIDTH;
                    float ny = (float) my / WIN_HEIGHT;
                    int[] pos = cell_board.getCell(nx, ny, game_board.get_board());

                    // Complete player action
                    game_over = game_board.invokeClick(pos); // returns true if clicked a mine
                    System.out.printf("Mouse click at: (%d, %d) worth: %d\n",
                            pos[0], pos[1], game_board.getPoints(pos[0], pos[1]));
                }
            }
        }

        // Clean
        JbWindowManager.get().closeWindow();
    }
}
