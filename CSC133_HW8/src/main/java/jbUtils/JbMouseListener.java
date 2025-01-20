package jbUtils;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class JbMouseListener {
    private static JbMouseListener self = new JbMouseListener();
    private double x_pos, y_pos;
    private boolean mouse_btn_holding[] = new boolean[3];
    private boolean mouse_btn_clicked[] = new boolean[3];
    
    private JbMouseListener() {
        x_pos = 0.0;
        y_pos = 0.0;
    }

    public static JbMouseListener get() {
        return self;
    }

    public static void mousePosCallback(long window, double x_pos, double y_pos) {
        self.x_pos = x_pos;
        self.y_pos = y_pos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS)
            if (button < self.mouse_btn_holding.length)
                self.mouse_btn_holding[button] = true;

        if (action == GLFW_RELEASE)
            if (button < self.mouse_btn_holding.length)
                self.mouse_btn_holding[button] = false;

    }

    public static int getX() {
        return (int) self.x_pos;
    }

    public static int getY() {
        return (int) self.y_pos;
    }
    
    public static boolean mouseButtonDown(int button) {
        if (button < self.mouse_btn_holding.length) {
            return self.mouse_btn_holding[button];
        } else {
            return false;
        }
    }

    public static boolean mouseButtonClick(int button) {
        if (mouseButtonDown(button)) { // Single frame check
            if(!self.mouse_btn_clicked[button]) {
                self.mouse_btn_clicked[button] = true;
                return true;
            }
            return false;
        } else
            self.mouse_btn_clicked[button] = false;

        return false;
    }
}