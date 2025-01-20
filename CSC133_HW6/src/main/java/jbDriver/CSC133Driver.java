// Jaden Bruha, Assignment 6, CSC 133-09, Fall 2024

package jbDriver;

import jbRenderer.JbRenderCells;
import jbUtils.JbPingPongBoard;
import jbUtils.JbWindowManager;
import org.lwjgl.glfw.GLFW;

import static jbDriver.JbSpot.*;

public class CSC133Driver {
    public static void main(String[] args) {
        // Create Cell Renderer
        JbRenderCells my_cell_board = new JbRenderCells();
        JbWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, "CSUS CSC133");
        my_cell_board.initOpenGL(JbWindowManager.get());

        // Create a boolean ping-pong array
        int board_width = 100, board_height = 100;
        JbPingPongBoard game_board = new JbPingPongBoard(board_width, board_height);

        game_board.resetNextBoard();

        int frame_delay = 300;
        boolean exit = false;

        // Display Generation
        while (!exit) {
            // First in frame priority
            if (JbWindowManager.get().getKey(GLFW.GLFW_KEY_ESCAPE) == 1)
                exit = true;

            if (JbWindowManager.get().getKey(GLFW.GLFW_KEY_R) == 1 ) {
                System.out.println("The board has been reset");
                game_board.resetBoard();
            }

            if (JbWindowManager.get().getKey(GLFW.GLFW_KEY_I) == 1 ) {
                System.out.println("The delay has increased");
                frame_delay += 500;
            }

            if (JbWindowManager.get().getKey(GLFW.GLFW_KEY_D) == 1 ) {
                System.out.println("The  delay has decreased");
                frame_delay -= 500;
            }

            // Generate neighbor count for each cell (prepare for next generation)
            game_board.generateNextNearest();
            // Mutate into the next generation of the game of life
            game_board.mutateNextGeneration();
            // Ping || Pong
            game_board.swapBoards();
            // Show live game board
            my_cell_board.renderCells(frame_delay, game_board.get());

            frame_delay = Math.max(frame_delay, 0);
        }
    }
}
