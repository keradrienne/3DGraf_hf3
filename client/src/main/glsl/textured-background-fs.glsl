#version 300 es

precision highp float;

in vec4 rayDir;
out vec4 fragmentColor;

// kell egy sampler uniform
uniform struct {
    samplerCube envTexture;
} material;

void main() {
    fragmentColor = texture ( material.envTexture, rayDir.xyz);
}
