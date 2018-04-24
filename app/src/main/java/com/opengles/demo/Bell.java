package com.opengles.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/3/9.
 */

public class Bell {
    private int aPositonHandle;
    private int aColorHandle;
    private int aMVPHandle;
    private int aCornidateHandle;
    private int program;
    private ByteBuffer vertexBuffer;
    private ByteBuffer colorBuffer;
    private ByteBuffer textureBuffer;
    private int span = 9;

    private float RR = 0.8f;
    private float r = 0.5f;

    private Bitmap bitmap;
    private int textureId;

    public Bell(Context context)
    {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.aa);
        initVertexData();
        initShader(context);
        initTexture();
    }

    private void initTexture()
    {
        int[] gens = new int[1];
        GLES20.glGenTextures(1, gens, 0);
        textureId = gens[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }

    private void initVertexData()
    {
        int vertexCount = 2 * span + 2;
        float angelSpan = (float)(90.0 / span);
        float[] vertexs = new float[vertexCount * 3];
        int index = 0;
        for(float angel = 0; angel <= 90; angel += angelSpan)
        {
            double radians = Math.toRadians(angel);
            float x1 = (float)(-RR * Math.sin(radians));
            float y1 = (float)(RR * Math.cos(radians));
            float z1 = 0;
            vertexs[index++] = x1;
            vertexs[index++] = y1;
            vertexs[index++] = z1;

            float x2 = (float)(-r * Math.sin(radians));
            float y2 = (float)(r * Math.cos(radians));
            float z2 = 0;
            vertexs[index++] = x2;
            vertexs[index++] = y2;
            vertexs[index++] = z2;
        }

        vertexBuffer = ByteBuffer.allocateDirect(vertexs.length * 4);
        vertexBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = vertexBuffer.asFloatBuffer();
        floatBuffer.put(vertexs);
        vertexBuffer.position(0);

        int colorsCount = vertexCount * 4;
        float[] colors = new float[colorsCount];
        index = 0;
        for(int i = 0; i < vertexCount; i++)
        {
            if(i % 2 != 0)
            {
                colors[index++] = 1.0f;
                colors[index++] = 1.0f;
                colors[index++] = 1.0f;
                colors[index++] = 0.0f;
            }else
            {
                colors[index++] = 0.0f;
                colors[index++] = 1.0f;
                colors[index++] = 1.0f;
                colors[index++] = 0.0f;
            }
        }
        colorBuffer = ByteBuffer.allocateDirect(colorsCount * 4);
        colorBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = colorBuffer.asFloatBuffer();
        buffer.put(colors);
        colorBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(vertexCount * 2 * 4);
        float[] textures = new float[vertexCount * 2];
        index=0;
        for(int i = 0; i < vertexCount; i++)
        {
            textures[index++] = i / (float)vertexCount;
            textures[index++] = i / (float)vertexCount;
        }
        textureBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer1 = textureBuffer.asFloatBuffer();
        floatBuffer1.put(textures);
        textureBuffer.position(0);
     }

    private void initShader(Context context)
    {
        String vertexSource = ShaderUtils.getShaderStr(context, "bell_vertex_shader.sh");
        String fragSource = ShaderUtils.getShaderStr(context, "bell_frgment_shader.sh");
        program = ShaderUtils.createProgram(vertexSource, fragSource);
        aPositonHandle = GLES20.glGetAttribLocation(program, "aPosition");
        aColorHandle = GLES20.glGetAttribLocation(program, "aColor");
        aMVPHandle = GLES20.glGetUniformLocation(program, "aMVPMatrix");
        aCornidateHandle = GLES20.glGetAttribLocation(program, "aCornidate");
    }

    public void draw(GL10 gl)
    {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPositonHandle);
        GLES20.glEnableVertexAttribArray(aColorHandle);
        GLES20.glEnableVertexAttribArray(aCornidateHandle);
        GLES20.glEnable(GLES20.GL_TEXTURE0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glVertexAttribPointer(aCornidateHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, textureBuffer);
        GLES20.glUniformMatrix4fv(aMVPHandle, 1,false, MatrixSet.getFinalMatrix(MatrixSet.getCurMMMatrix()), 0);
        GLES20.glVertexAttribPointer(aPositonHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aColorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 2 * span + 2);
    }
}
