package com.opengles.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/3/7.
 */

public class Triangle {
    public static final String NO_FILTER_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}";

    private int aPositionHandler;
    private int aColorHandler;
    private int program;
    private int aCornidateHandle;

    private int textureId;
    private Bitmap bitmap = null;

    private ByteBuffer vertexBuffer;
    private ByteBuffer colorBuffer;
    private ByteBuffer cordinateBuffer;
    private float[] vertex = new float[]{
            -1f, 0f, 0f,
            1f, 0f, 0f,
            0f, 1f, 0f
    };
    private float[] color = new float[]{
            1f, 0f, 0f ,1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
    };

   public Triangle(Context context)
    {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.aa);
        initVerTexData();
        initShader(context);
        initTexture();
    }

    private void initTexture()
    {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0,bitmap,0);
    }



    private void initVerTexData()
    {
        vertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4);
        vertexBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = vertexBuffer.asFloatBuffer();
        floatBuffer.put(vertex);
        floatBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(color.length * 4);
        colorBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer colorFloatBuffer = colorBuffer.asFloatBuffer();
        colorFloatBuffer.put(color);
        colorFloatBuffer.position(0);

        float[] codinates = new float[]{
                0f, 1f,
                1f, 1f,
                0.5f,0f};
        cordinateBuffer = ByteBuffer.allocateDirect(codinates.length * 4);
        cordinateBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffer1 = cordinateBuffer.asFloatBuffer();
        buffer1.put(codinates);
        cordinateBuffer.position(0);
    }

    private void initShader(Context context)
    {
        String vertextSource = ShaderUtils.getShaderStr(context, "vertex_shader.sh");
        String fragmentSource = ShaderUtils.getShaderStr(context, "fragment_shader.sh");
        program = ShaderUtils.createProgram(vertextSource, fragmentSource);
        aPositionHandler = GLES20.glGetAttribLocation(program, "aPosition");
        aColorHandler = GLES20.glGetAttribLocation(program, "aColor");
        aCornidateHandle = GLES20.glGetAttribLocation(program, "textureCornidate");
    }

    public void draw(GL10 gl)
    {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPositionHandler);
        GLES20.glEnableVertexAttribArray(aColorHandler);
        GLES20.glEnableVertexAttribArray(aCornidateHandle);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glVertexAttribPointer(aCornidateHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, cordinateBuffer);
        GLES20.glVertexAttribPointer(aPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aColorHandler, 4, GLES20.GL_FLOAT, false, 4 * 4, colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDisableVertexAttribArray(aPositionHandler);
        GLES20.glDisableVertexAttribArray(aColorHandler);
    }

}
