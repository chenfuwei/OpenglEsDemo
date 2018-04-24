attribute vec3 aPosition;
attribute vec4 aColor;
attribute vec2 textureCornidate;
varying vec4 textrueColor;
varying vec2 cornidate;
void main()
{
    gl_Position = vec4(aPosition, 1);
    textrueColor = aColor;
    cornidate = textureCornidate;
}