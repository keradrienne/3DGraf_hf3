#version 300 es

in vec4 vertexPosition; //#vec4# A four-element vector [x,y,z,w].; We leave z and w alone.; They will be useful later for 3D graphics and transformations. #vertexPosition# attribute fetched from vertex buffer according to input layout spec
out vec4 rayDir;

uniform struct{
    mat4 rayDirMatrix;
} camera;

void main(void) {
    gl_Position = vertexPosition;
    gl_Position.z = 0.99999f;
    rayDir = vertexPosition * camera.rayDirMatrix;
}