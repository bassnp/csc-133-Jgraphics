#type vertex
#version 430 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;

uniform mat4 uProjMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;

void main()
{
    fColor = aColor;
    gl_Position = uProjMatrix * uViewMatrix * vec4(aPos, 1.0);
}

#type fragment
#version 430 core


in vec4 fColor;

out vec4 color;

void main()
{
    color = fColor;
}
