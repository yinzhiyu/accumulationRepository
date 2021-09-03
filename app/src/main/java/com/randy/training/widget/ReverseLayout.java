package com.randy.training.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/8/2518:44
 * desc   :
 */
public class ReverseLayout extends FrameLayout {

    private Camera mCamera;

    public ReverseLayout(@NonNull Context context) {
        this(context,null);
    }

    public ReverseLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ReverseLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCamera = new Camera();
        setVisibility(GONE);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //将整个页面水平翻转，目的是为了抵消外布局翻转后的左右倒置现象
        mCamera.save();
        canvas.save();
        //镜像反转
        mCamera.rotateY(180);
        Matrix matrix = new Matrix();
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-getWidth()/2,-getHeight()/2);
        matrix.postTranslate(getWidth()/2,getHeight()/2);
        canvas.setMatrix(matrix);
        mCamera.restore();
        super.dispatchDraw(canvas);
        canvas.restore();
    }
}
