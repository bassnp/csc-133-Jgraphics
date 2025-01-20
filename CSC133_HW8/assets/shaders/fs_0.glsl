#version 430 core

uniform vec4 COLOR_FACTOR;
uniform sampler2D TEX_SAMPLER;

out vec4 color;
in vec2 fTexCoords;

void main()
{
    color = texture(TEX_SAMPLER, fTexCoords);
}
