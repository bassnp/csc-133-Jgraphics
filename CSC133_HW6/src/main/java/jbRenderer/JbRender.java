package jbRenderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import jbUtils.JbWindowManager;
import java.util.Random;

public abstract class JbRender {
    protected Random rand;
    public JbRender() {
        rand = new Random();
    }

    protected JbWindowManager windowManager = null;
    public void initOpenGL(JbWindowManager _windowManager) {
        windowManager = _windowManager;
    }

    public boolean isActive() {
        return !windowManager.isClosed();
    }

    public void startFrame(boolean randomColor) {
        // Main loop

        GLFW.glfwPollEvents();

        // Clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Set background
        GL11.glClearColor(0f, 0f, 0f, 1f);

        if (randomColor)
            GL11.glColor4f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1f);
        else
            GL11.glColor4f(0.8f, 0.8f, 0.8f, 1f); // I like gray more than white
    }

    public void completeFrame() {
        completeFrame(-1);
    }

    public void completeFrame(int DELAY) {
        windowManager.swapBuffers();

        // Delay
        if(DELAY > 0)
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        else if (DELAY == -1) {
            while ( isActive() ) {} // Hold
            windowManager.closeWindow();
        }

    }
}

