package jbRender;

import jbUtils.JbWindowManager;
import java.util.Random;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class JbRender {
    protected Random rand;
    protected JbWindowManager windowManager = null;
    protected JbCamera camera = null;

    protected JbShaders shaders = null;
    protected int shader_program;

    private JbTextureObject[] textures = null;
    private int texture_in_use = -1;

    public JbRender() {
        rand = new Random();
    }

    public void initOpenGL(JbWindowManager _windowManager) {
        windowManager = _windowManager;
        camera = new JbCamera();
        shaders = new JbShaders();
        shader_program = shaders.createShaderProgram();
        textures = new JbTextureObject[5]; // Max of 5 textures at a time should be loaded
    }

    public void startFrame(float[] n_clr) {
        glfwPollEvents();
        glClearColor(n_clr[0], n_clr[1], n_clr[2], 1f);
        doFrame();
    }

    public void startFrame() {
        glfwPollEvents();
        glClearColor(0.05f, 0.05f, 0.05f, 1f);
        doFrame();
    }

    public void doFrame() {
        glClear(GL_COLOR_BUFFER_BIT);
        shaders.loadMatrix4f("uProjMatrix", camera.getProjMatrix());
        shaders.loadMatrix4f("uViewMatrix", camera.getViewMatrix());
        glUseProgram(shader_program);
    }

    public void endFrame(int DELAY) {
        windowManager.swapBuffers();
        glUseProgram(0);

        // Delay
        if(DELAY > 0)
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        else if (DELAY == -1) {
            windowManager.closeWindow();
        }
    }

    // Overload for easier loading
    public void setTextureIndex(int[] states, JbTextureObject texture) {
        for (int state : states)
            setTextureIndex(state, texture);
    }

    public void setTextureIndex(int state, JbTextureObject texture) {
        if(state >= 0 && state < textures.length)
            textures[state] = texture; // Create local pointer to texture object
        else
            throw new IndexOutOfBoundsException("Texture index is not supported.");
    }

    // Binding
    public void useTextureAtIndex(int state) {
        if(texture_in_use >= 0)
            textures[state].unbind_texture();

        textures[state].bind_texture();
        texture_in_use = state;
    }
}

