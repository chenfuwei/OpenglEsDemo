package com.opengles.demo;

import android.opengl.Matrix;
import android.util.Log;

import java.util.Stack;

/**
 * Created by Administrator on 2018/3/8.
 */

public class MatrixSet {
    private static final String TAG = "MatrixSet";
    private static float[] mVMMatrix = new float[16];
    private static float[] mProjMatrix = new float[16];
    private static float[] mMVPMatrix = new float[16];
    private static float[] mMMMatrix = new float[16];
    private static float[] mCurMMatrix = new float[16];

    private static  Stack<float[]> stack;

    public static void initStack()
    {
        stack = new Stack<float[]>();
    }

    public static void setProjectMatrix(float left, float right, float bottom, float top, float near, float far)
    {
        mProjMatrix = new float[16];
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
        for(int i = 0; i < 16; i++)
        {
            Log.i(TAG, "setProjectMatrix = " + mProjMatrix[i]);
        }

    }

    public static void setRotateMatrix(float angle, float x, float y , float z)
    {
        mMMMatrix = new float[16];
        Matrix.setRotateM(mMMMatrix,0, angle, x, y, z);
        mCurMMatrix = mMMMatrix;
    }

    public static void translateMatrix(float x, float y, float z)
    {
        float[] tmp = new float[16];
        for(int i = 0; i < 16; i++)
        {
            tmp[i] = mMMMatrix[i];
        }
        stack.push(tmp);

        mCurMMatrix = mMMMatrix;
        Matrix.translateM(mCurMMatrix, 0, x, y ,z );
        mMMMatrix =stack.pop();
        for(int index = 0; index < 16; index++)
        {
            Log.i("mmatrix", "mmatrix value = " + mMMMatrix[index]);
        }
    }

    public static float[] getCurMMMatrix()
    {
        return mCurMMatrix;
    }

    public static void setLookAtM(float eyex, float eyey, float eyez, float centerx, float centery, float centerz,
                                  float upx, float upy, float upz)
    {
        mVMMatrix = new float[16];
        Matrix.setLookAtM(mVMMatrix, 0, eyex, eyey, eyez,centerx, centery, centerz, upx, upy, upz);
        for(int i = 0; i < 16; i++)
        {
            Log.i(TAG, "setLookAtM = " + mVMMatrix[i]);
        }
    }

    public static float[] getFinalMatrix(float[] spec)
    {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
//        for(int i = 0; i < 16; i++)
//        {
//            Log.i(TAG, "mMVPMatrix = " + mMVPMatrix[i]);
//        }
        return mMVPMatrix;
    }

}
