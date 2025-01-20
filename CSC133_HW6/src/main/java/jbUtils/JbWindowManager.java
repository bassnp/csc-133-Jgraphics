package jbUtils;

import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class JbWindowManager {
    private static JbWindowManager self = new JbWindowManager();
    private long winId = NULL;

    private JbWindowManager() {}
    public static JbWindowManager get() {
        return self;
    }

    public void initGLFWWindow(int win_width, int win_height, String win_name) {
        // Ensure init
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Lock bounds
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // Pop up window
        winId = glfwCreateWindow(win_width, win_height, win_name, NULL, NULL);
        if (winId == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Make current
        glfwMakeContextCurrent(winId);
        GL.createCapabilities();
    }

    public int getKey(int key) {
        return org.lwjgl.glfw.GLFW.glfwGetKey(self.getWinId(), key);
    }

    public boolean isClosed() {
        return glfwWindowShouldClose(winId);
    }

    public void swapBuffers() {
        glfwSwapBuffers(winId);
    }

    public void closeWindow() {
        glfwDestroyWindow(winId);
    }

    public long getWinId() {
        return winId;
    }
}