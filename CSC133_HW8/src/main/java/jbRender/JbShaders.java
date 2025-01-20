package jbRender;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL30.*;

public class JbShaders {
    private int shader_program;

    private String loadShaderStr(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader: " + filePath);
        }
    }

    private int compileShader(int shader_type, String shader_str) {
        int shader = glCreateShader(shader_type);
        glShaderSource(shader, shader_str);
        glCompileShader(shader);

        int success = glGetShaderi(shader, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
            System.out.println("Shader failed:\n" + glGetShaderInfoLog(shader,
                    glGetShaderi(shader, GL_INFO_LOG_LENGTH)));

        return shader;
    }

    public int createShaderProgram() {
        int vertex_shader = compileShader(GL_VERTEX_SHADER,
                loadShaderStr("assets/shaders/vs_0.glsl"));

        int fragment_shader = compileShader(GL_FRAGMENT_SHADER,
                loadShaderStr("assets/shaders/fs_0.glsl"));

        shader_program = glCreateProgram();
        glAttachShader(shader_program, vertex_shader);
        glAttachShader(shader_program, fragment_shader);
        glLinkProgram(shader_program);

        int success = glGetProgrami(shader_program, GL_LINK_STATUS);
        if (success == GL_FALSE)
            System.out.println("Linking of shaders failed:\n" + glGetProgramInfoLog(shader_program,
                    glGetProgrami(shader_program, GL_INFO_LOG_LENGTH)));

        glUseProgram(shader_program);
        return shader_program;
    }

    public void loadVector4f(String str_name, Vector4f vec4) {
        glUniform4fv(
                glGetUniformLocation(shader_program, str_name),
                new float[]{vec4.x, vec4.y, vec4.z, vec4.w}
        );
    }

    public void loadMatrix4f(String str_name, Matrix4f my_mat4) {
        int var_location = glGetUniformLocation(shader_program, str_name);
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        my_mat4.get(matrixBuffer);
        glUniformMatrix4fv(var_location, false, matrixBuffer);
    }

    public void loadTexture(String str_name, int texture_id) {
        glUniform1i(glGetUniformLocation(shader_program, str_name), texture_id);
    }
}
