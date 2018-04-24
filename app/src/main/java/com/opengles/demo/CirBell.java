package com.opengles.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SocketHandler;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/3/12.
 */

public class CirBell {
    private int aPositionHandle;
    private int aColorHandle;
    private int aCordinateHandle;
    private int aMVPHandle;

    private int program;
    private ByteBuffer vertexBuffer;
    private ByteBuffer colorBuffer;
    private ByteBuffer cordinateBuffer;
    private int textureId = 0;

    private Bitmap mBitmap;

    private float RR = 1.0f;
    private float r = 0.8f;

    private int smallSpan = 24;
    private int bigSapn = 72;
    private int vCount = 0;
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

    private float[] vertexTexture;

    public CirBell(Context context)
    {
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.aa);
        initVertexData();
        initFragData(context);
        initTextureData();
    }

    private void initTextureData()
    {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
    }

    private void initVerTexData1()
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
    }
    private void initVertexData()
    {
        float smallR = (RR - r ) / 2;
        float smallEdgeSpan = (float)(360.0 / smallSpan);
        float bigEdgeSpan = (float)(360.0 / bigSapn);
        vCount = 3 * smallSpan * bigSapn * 2;
        float D = r + smallR;
        List<Float> vertexs= new ArrayList<Float>();
        List<Float> textures = new ArrayList<Float>();
        int textureIndex = 0;
        for(float tmpSmallEdgeSpan = 0; tmpSmallEdgeSpan < 360 + smallEdgeSpan; tmpSmallEdgeSpan += smallEdgeSpan)
        {
            double smallRadians = Math.toRadians(tmpSmallEdgeSpan);
            for(float tmpBigEdgeSpan = 0; tmpBigEdgeSpan < 360 + bigEdgeSpan; tmpBigEdgeSpan += bigEdgeSpan)
            {
                double bigRadians = Math.toRadians(tmpBigEdgeSpan);
                float y = (float)(smallR * Math.cos(smallRadians));

                float x = (float)((D + smallR * Math.sin(smallRadians)) * Math.sin(bigRadians));
                float z = (float)((D + smallR * Math.sin(smallRadians)) * Math.cos(bigRadians));

                vertexs.add(x);
                vertexs.add(y);
                vertexs.add(z);

                textures.add(tmpBigEdgeSpan / (float)360);
                textures.add(tmpSmallEdgeSpan / (float)360);
            }
        }

        int[] vertexArray = new int[vCount];
        int index = 0;
        for(int i = 0; i < smallSpan; i++)
        {
            for (int j = 0; j < bigSapn; j++)
            {
                int nFirst = i * (bigSapn) + j;
                vertexArray[index++] = nFirst + 1;
                vertexArray[index++] = nFirst + bigSapn + 1;
                vertexArray[index++] = nFirst + bigSapn + 2;

                vertexArray[index++] = nFirst + 1;
                vertexArray[index++] = nFirst;
                vertexArray[index++] = nFirst + bigSapn + 1;
            }
        }


        float[] vertexData = new float[vCount * 3];
        float[] colorData = new float[vCount * 4];
        float[] textureData = new float[vCount * 2];

        index = 0;
        int colorIndex = 0;
        int texIndex = 0;

        for(int aIndex = 0; aIndex < vertexArray.length; aIndex++)
        {
            vertexData[index++] = vertexs.get(3 * vertexArray[aIndex]);
            vertexData[index++] = vertexs.get(3 * vertexArray[aIndex] + 1);
            vertexData[index++] = vertexs.get(3 * vertexArray[aIndex] + 2);

            textureData[texIndex++] = textures.get(2 * vertexArray[aIndex]);
            textureData[texIndex++] = textures.get(2 * vertexArray[aIndex] + 1);

//            if(aIndex % 3 == 0)
//            {
//                colorData[colorIndex++] = 1.0f;
//                colorData[colorIndex++] = 0.0f;
//                colorData[colorIndex++] = 0.0f;
//                colorData[colorIndex++] = 1.0f;
//            }else if(aIndex % 3 == 1)
//            {
//                colorData[colorIndex++] = 1.0f;
//                colorData[colorIndex++] = 0.0f;
//                colorData[colorIndex++] = 0.0f;
//                colorData[colorIndex++] = 1.0f;
//            }else{
//                colorData[colorIndex++] = 1.0f;
//                colorData[colorIndex++] = 0.0f;
//                colorData[colorIndex++] = 0.0f;
//                colorData[colorIndex++] = 1.0f;
//            }
        }

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4);
        vertexBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = vertexBuffer.asFloatBuffer();
        floatBuffer.put(vertexData);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(colorData.length * 4);
        colorBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer colorfloatBuffer = colorBuffer.asFloatBuffer();
        colorfloatBuffer.put(colorData);
        colorBuffer.position(0);


        cordinateBuffer = ByteBuffer.allocateDirect(textureData.length * 4);
        cordinateBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer textureBuffer = cordinateBuffer.asFloatBuffer();
        textureBuffer.put(textureData);
        cordinateBuffer.position(0);
        Log.i("kk", "vertexs size = " + vertexs.size() + " size = " + vertexArray.length);

    }

    private void initFragData(Context context)
    {
        String vertexSource = ShaderUtils.getShaderStr(context, "ceirbell_vertex_shader.sh");
        String fragSource = ShaderUtils.getShaderStr(context, "cirbell_fragment_shader.sh");
        program = ShaderUtils.createProgram(vertexSource, fragSource);

        aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        aColorHandle = GLES20.glGetAttribLocation(program, "aColor");
        aCordinateHandle = GLES20.glGetAttribLocation(program, "aTexturePosition");
        aMVPHandle = GLES20.glGetUniformLocation(program, "aMVPHandle");
    }

    public void draw(GL10 gl)
    {
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glEnableVertexAttribArray(aColorHandle);
        GLES20.glEnableVertexAttribArray(aMVPHandle);
        GLES20.glEnableVertexAttribArray(aCordinateHandle);
        GLES20.glEnable(GLES20.GL_TEXTURE0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glVertexAttribPointer(aCordinateHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, cordinateBuffer);
        GLES20.glUniformMatrix4fv(aMVPHandle, 1, false, MatrixSet.getFinalMatrix(MatrixSet.getCurMMMatrix()), 0);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES20.glVertexAttribPointer(aColorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, colorBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

}
