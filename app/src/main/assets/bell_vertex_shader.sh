attribute vec3 aPosition;
attribute vec4 aColor;
attribute vec2 aCornidate;
uniform mat4 aMVPMatrix;
varying vec4 textureColor;
varying vec2 textureCornidate;
void main()
{
    gl_Position = aMVPMatrix * vec4(aPosition, 1);
    textureColor = aColor;
    textureCornidate = aCornidate;
}