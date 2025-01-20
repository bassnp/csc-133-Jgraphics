package jbRender;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static jbDriver.JbSpot.*;

public class JbCamera {
    private Vector3f lf = new Vector3f(0f, 0f, 0f);
    private Vector3f la = new Vector3f(0f, 0f, -1.0f);
    private Vector3f uv = new Vector3f(0f, 1.0f, 0f);

    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f projMatrix = new Matrix4f();

    public JbCamera() {
        viewMatrix.identity();
        viewMatrix.lookAt(lf, la.add(lf), uv);

        projMatrix.identity();
        projMatrix.ortho(0f, 1f, 0f, 1f, 0f, 100f);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjMatrix() {
        return projMatrix;
    }
}
