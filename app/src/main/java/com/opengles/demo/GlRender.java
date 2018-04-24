package com.opengles.demo;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/3/7.
 */

public class GlRender implements GLSurfaceView.Renderer{
    private Context context;
    private List<SixStar> starList;
    private int nCount = 6;
    private CirBell bell;
    public GlRender(Context context)
    {
        this.context = context;
        starList = new ArrayList<SixStar>();
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
//        for(int i = 0; i < nCount; i++)
//        {
//            starList.add(new SixStar(context, (-i / 5)));
//        }
        bell = new CirBell(context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0,width,height);
        float usize = 0.3f;
        float rate = (float)width / height;
        MatrixSet.setProjectMatrix(-usize * rate, usize * rate , -usize, usize , 0.2f, 20f);
        MatrixSet.setLookAtM(0.0f, 1.0f, 1.5f, 0, 0, 0, 0, 1, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        MatrixSet.setRotateMatrix(30, 0, 0, 1);
//        for(int i = 0; i < 5; i++) {
//            SixStar triangle = starList.get(i);
//            MatrixSet.translateMatrix(i* 0.8f, 0f, 0f);
//            triangle.draw(gl);
//        }
        bell.draw(gl);
    }
}
