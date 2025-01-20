package jbUtils;

import jbRender.JbShaders;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
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
        // Ensure ready
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Setup restraints
        //glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // Create window
        winId = glfwCreateWindow(win_width, win_height, win_name, NULL, NULL);
        if (winId == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Callbacks
        glfwSetCursorPosCallback(winId, JbMouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(winId, JbMouseListener::mouseButtonCallback);
        glfwSetKeyCallback(winId, JbKeyListener::keyCallback);

        // Complete
        glfwMakeContextCurrent(winId);
        glfwSwapInterval(1);
        GL.createCapabilities();
    }

    public void swapBuffers() {
        glfwSwapBuffers(winId);
    }

    public void closeWindow() {
        glfwDestroyWindow(winId);
        glfwFreeCallbacks(winId);
        glfwTerminate();
    }

    public long getWinId() {
        return winId;
    }
}