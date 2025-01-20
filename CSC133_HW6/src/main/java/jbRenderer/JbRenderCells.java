// Jaden Bruha, Assignment 6, CSC 133-09, Fall 2024

package jbRenderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class JbRenderCells extends JbRender {
    public void renderCells(int delay, int[][] board) {
        startFrame(true);
        renderCellArray(board);
        completeFrame(delay);
    }

    // Auxiliary array storage is inefficient
    public void renderCellArray(int[][] board) {
        // Draw the circles
        final int ROWS = board.length;
        final int COLS = board[0].length;
        final float GAP = 0.1f; // fraction of size

        final float size = 2f / Math.min(ROWS, COLS);

        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++) {
                // Set color
                if(board[row][col] == 0) // Dead
                    GL11.glColor4f(0.1f, 0.1f, 0.1f, 1f);
                else // Alive
                    GL11.glColor4f(0.2f, 0.8f, 0.2f, 1f);

                GL11.glBegin(GL11.GL_TRIANGLE_FAN);

                // Keep inside window
                float gap_adjust = size * GAP;

                float xoffset = -1f;
                float xpad = (row * size);

                float yoffset = 1f;
                float ypad = (col * size);

                float _x = xoffset + xpad + gap_adjust;
                float _y = yoffset - ypad - gap_adjust;

                // Get a circle based on Fan draw style // tl
                FloatBuffer vertices = createCell(_x, _y, size - gap_adjust);

                // Draw Triangles from Fan
                for (int i = 0; i < vertices.limit() / 2; i++) {
                    float x = vertices.get(i * 2);
                    float y = vertices.get(i * 2 + 1);
                    GL11.glVertex2f(x, y);
                }
                GL11.glEnd();
            }
        }
    }

    // 1. set the radius of the polygon,
    // 2. set the number of sides of the polygons,
    private FloatBuffer createCell(float x, float y, float size) {
        FloatBuffer vertices = BufferUtils.createFloatBuffer(8);

        vertices.put(x).put(y); // tl
        vertices.put(x+size).put(y); // tr
        vertices.put(x+size).put(y-size); // br
        vertices.put(x).put(y-size); // bl

        vertices.flip();
        return vertices;
    }
}

