attribute vec3 aPosition;
attribute vec4 aColor;
attribute vec2 aTexturePosition;
uniform mat4 aMVPHandle;
varying vec4 aFragColor;
varying vec2 aFragTexturePosition;

void main()
{
gl_Position = aMVPHandle * vec4(aPosition, 1);
aFragColor = aColor;
aFragTexturePosition = aTexturePosition;
}