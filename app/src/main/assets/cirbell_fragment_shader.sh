precision mediump float;
varying vec4 aFragColor;
varying vec2 aFragTexturePosition;
uniform sampler2D sTexture;
void main()
{
gl_FragColor = texture2D(sTexture, aFragTexturePosition);

}