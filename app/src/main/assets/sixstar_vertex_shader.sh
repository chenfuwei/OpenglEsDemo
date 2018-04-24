attribute vec3 aPosition;
attribute vec4 aColor;
varying vec4 aFragColor;
uniform mat4 aMVPMatrix;
void main()
{
    gl_Position = aMVPMatrix * vec4(aPosition, 1.0);
    aFragColor = aColor;
}