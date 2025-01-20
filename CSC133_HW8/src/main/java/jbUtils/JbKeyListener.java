package jbUtils;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class JbKeyListener {
    private static JbKeyListener self = new JbKeyListener();
    private boolean keys_pressed[] = new boolean[350];
    private boolean keys_clicked[] = new boolean[350];

    public static JbKeyListener get() {
        return self;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            self.keys_pressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            self.keys_pressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return self.keys_pressed[keyCode];
    }

    public static boolean keyClicked(int keyCode) {
        if (isKeyPressed(keyCode)) { // Single frame check
            if(!self.keys_clicked[keyCode]) {
                self.keys_clicked[keyCode] = true;
                return true;
            }
            return false;
        } else
            self.keys_clicked[keyCode] = false;

        return false;
    }
}