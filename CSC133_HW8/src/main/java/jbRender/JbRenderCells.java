// Jaden Bruha, Assignment 8, CSC 133-09, Fall 2024

package jbRender;

import com.sun.jdi.PrimitiveValue;
import jbUtils.JbMinesweeper;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class JbRenderCells extends JbRender {
    public int[] getCell(float normal_x, float normal_y, int[][] board) {
        final int WIDTH = board.length;
        final int HEIGHT = board[0].length;

        final float CELL_SIZEX = 1f / WIDTH;
        final float CELL_SIZEY = 1f / HEIGHT;

        final float PADX = CELL_SIZEX * 0.1f;
        final float PADY = CELL_SIZEY * 0.1f;

        final float OFFSET_SIZEX = 1f / (WIDTH + (WIDTH * PADX));
        final float OFFSET_SIZEY = 1f / (HEIGHT + (HEIGHT * PADY));

        // Determine which cell the click corresponds to
        int row = (int) (normal_x / OFFSET_SIZEX);
        int col = (int) (normal_y / OFFSET_SIZEY);

        return new int[]{row, (HEIGHT - col - 1)};
    }

    public void renderCells(int[][] board) {
        final int WIDTH = board.length;
        final int HEIGHT = board[0].length;

        final float CELL_SIZEX = 1f / WIDTH;
        final float CELL_SIZEY = 1f / HEIGHT;

        final float PADX = CELL_SIZEX * 0.1f;
        final float PADY = CELL_SIZEY * 0.1f;

        final float OFFSET_SIZEX = 1f / (WIDTH + (WIDTH * PADX));
        final float OFFSET_SIZEY = 1f / (HEIGHT + (HEIGHT * PADY));

        for (int row = 0; row < WIDTH; row++) {
            for (int col = 0; col < HEIGHT; col++) {
                // Load color from state
                useTextureAtIndex(board[row][col]);
                shaders.loadVector4f("COLOR_FACTOR", new Vector4f(
                        JbMinesweeper.mapStateToColor(board[row][col])
                ));

                // Create the square cell
                float sizex = CELL_SIZEX - PADX;
                float sizey = CELL_SIZEY - PADY;
                float x = PADX + (row * OFFSET_SIZEX);
                float y = PADY + (col * OFFSET_SIZEY);
                float[] vertices = new float[]{ // square
                        x + sizex, y,        0f,  /**/ 1f, 1f, //br
                        x,        y + sizey, 0f,  /**/ 0f, 0f, //tl
                        x + sizex, y + sizey, 0f,  /**/ 1f, 0f, //tr
                        x,        y,        0f,  /**/ 0f, 1f, //bl
                };

                // Designate triangles
                int[] indices = new int[]{ 3, 1, 0, 0, 1, 2 };

                // Create buffers for allocated data
                FloatBuffer vertices_buffer = BufferUtils.createFloatBuffer(vertices.length);
                vertices_buffer.put(vertices).flip();

                IntBuffer indices_buffer = BufferUtils.createIntBuffer(indices.length);
                indices_buffer.put(indices).flip();

                // Connect buffers to opengl
                int vao_id = glGenVertexArrays();
                glBindVertexArray(vao_id);

                int vbo_id = glGenBuffers();
                glBindBuffer(GL_ARRAY_BUFFER, vbo_id);
                glBufferData(GL_ARRAY_BUFFER, vertices_buffer, GL_DYNAMIC_DRAW);

                int ebo_id = glGenBuffers();
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo_id);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices_buffer, GL_DYNAMIC_DRAW);

                int pos_stride = 3;
                int tex_stride = 2;
                int pos_bytes = (pos_stride + tex_stride) * 4; // 4 bytes for size of float

                glVertexAttribPointer(0, pos_stride, GL_FLOAT, false, pos_bytes, 0);
                glEnableVertexAttribArray(0);

                glVertexAttribPointer(1, tex_stride, GL_FLOAT, false, pos_bytes, pos_stride * 4);
                glEnableVertexAttribArray(1);

                glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

                // Cleanup
                glDisableVertexAttribArray(0);
                glDisableVertexAttribArray(1);
                glBindVertexArray(0);
            }
        }
    }
}

