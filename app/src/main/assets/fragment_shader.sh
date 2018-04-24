precision mediump float;
varying vec4 textrueColor;
varying vec2 cornidate;
uniform sampler2D sTexture;
void main()
{
    gl_FragColor = texture2D(sTexture, cornidate);
}