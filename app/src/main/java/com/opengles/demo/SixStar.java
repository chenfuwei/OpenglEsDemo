package com.opengles.demo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/3/8.
 */

public class SixStar {
    private int program;
    private int aPositoinHandle;
    private int aColorHandle;
    private int aMVPMatrixHandle;
    private float[] sixStarVertex;

    private float[] mmMatrix;

    private ByteBuffer vertexBuffer;
    private ByteBuffer colorBuffer;

    private static final float R = 0.5f;
    private static final float r = 0.2f;

    private float z = 0;

    public SixStar(Context context, float z)
    {
        this.z = z;
        initVertexData();
        initShader(context);
    }

    private void initVertexData()
    {
        sixStarVertex = new float[108];
        int angleSpan = 60;
        int index = 0;
        for(int angle = 0; angle < 360; angle += angleSpan)
        {
            float x0 = 0;
            float y0 = 0;
            float z0 = 0;
            sixStarVertex[index++] = x0;
            sixStarVertex[index++] = y0;
            sixStarVertex[index++] = z0;

            double radian = Math.toRadians(angle);
            float x1 = (float)(-r * Math.sin(radian));
            float y1 = (float)(r * Math.cos(radian));
            float z1 = z;
            sixStarVertex[index++] = x1;
            sixStarVertex[index++] = y1;
            sixStarVertex[index++] = z1;

            double radian30 = Math.toRadians(angle + angleSpan / 2);
            float x2 = (float)(-R * Math.sin(radian30));
            float y2 = (float)(R * Math.cos(radian30));
            float z2 = z;
            sixStarVertex[index++] = x2;
            sixStarVertex[index++] = y2;
            sixStarVertex[index++] = z2;

            float x3 = 0;
            float y3 = 0;
            float z3 = z;
            sixStarVertex[index++] = x3;
            sixStarVertex[index++] = y3;
            sixStarVertex[index++] = z3;

            float x4 = x2;
            float y4 = y2;
            float z4 = z;
            sixStarVertex[index++] = x4;
            sixStarVertex[index++] = y4;
            sixStarVertex[index++] = z4;

            double radian60 = Math.toRadians(angle + angleSpan);
            float x5 = (float) (-r * Math.sin(radian60));
            float y5 = (float)(r * Math.cos(radian60));
            float z5 = z;
            sixStarVertex[index++] = x5;
            sixStarVertex[index++] = y5;
            sixStarVertex[index++] = z5;
        }
        vertexBuffer = ByteBuffer.allocateDirect(sixStarVertex.length * 4);
        vertexBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = vertexBuffer.asFloatBuffer();
        floatBuffer.put(sixStarVertex);
        floatBuffer.position(0);

        int vCount = sixStarVertex.length / 3;
        int nCount = vCount * 4;
        float[] colors = new float[nCount];
        int colorIndex = 0;
        for(int i = 0; i < vCount; i++)
        {
            if(i % 3 == 0)
            {
                colors[colorIndex++] = 1.0f;
                colors[colorIndex++] = 1.0f;
                colors[colorIndex++] = 1.0f;
                colors[colorIndex++] = 0.0f;
            }
            else
            {
                colors[colorIndex++] = 0.0f;
                colors[colorIndex++] = 1.0f;
                colors[colorIndex++] = 1.0f;
                colors[colorIndex++] = 0.0f;
            }
        }
        colorBuffer = ByteBuffer.allocateDirect(colors.length * 4);
        colorBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = colorBuffer.asFloatBuffer();
        buffer.put(colors);
        colorBuffer.position(0);
    }

    private void initShader(Context context)
    {
        String vertexSource = ShaderUtils.getShaderStr(context, "sixstar_vertex_shader.sh");
        String colorSource = ShaderUtils.getShaderStr(context, "sixstar_fragment_shader.sh");
        program = ShaderUtils.createProgram(vertexSource, colorSource);
        aPositoinHandle = GLES20.glGetAttribLocation(program, "aPosition");
        aColorHandle = GLES20.glGetAttribLocation(program, "aColor");
        aMVPMatrixHandle = GLES20.glGetUniformLocation(program, "aMVPMatrix");


        mmMatrix = new float[16];
        Matrix.setRotateM(mmMatrix, 0, 30, 0, 0, 1);
        for(int i = 0; i < 16; i++)
        {
            Log.i("kkkkk1", "mmMatrix = " + mmMatrix[i]);
        }
//        Matrix.translateM(mmMatrix, 0, 0.6f, 0, 0);
//        for(int i = 0; i < 16; i++)
//        {
//            Log.i("kkkkk", "mmMatrix = " + mmMatrix[i]);
//        }
       // Matrix.setRotateM(mmMatrix, 0, 30, 0, 1, 0);
        MatrixSet.initStack();
    }

    public void draw(GL10 gl)
    {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPositoinHandle);
        GLES20.glEnableVertexAttribArray(aColorHandle);
        GLES20.glEnableVertexAttribArray(aMVPMatrixHandle);

        GLES20.glUniformMatrix4fv(aMVPMatrixHandle, 1, false, MatrixSet.getFinalMatrix(MatrixSet.getCurMMMatrix()), 0);
        GLES20.glVertexAttribPointer(aPositoinHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aColorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }
}
