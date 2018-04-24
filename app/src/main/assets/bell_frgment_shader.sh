varying vec4 textureColor;
uniform sampler2D sTexture;
varying vec2 textureCornidate;
void main()
{
    gl_FragColor = texture2D(sTexture, textureCornidate);
}