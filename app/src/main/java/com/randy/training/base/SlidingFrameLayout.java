package com.randy.training.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.randy.training.R;
import com.randy.training.utils.Utils;
import com.randy.training.utils.ViewUtils;

/**
 * Created by sunbo on 2014/01/14.
 */
public class SlidingFrameLayout extends FrameLayout {

    private Activity mActivity;

    private ViewDragHelper mViewDragHelper;
    private Paint mPaint;

    /**
     * 上层页面DecorView下的LinearLayout
     */
    private View mContentView;

    private int mContentLeft;
    private int mContentTop;

    boolean mInLayout;

    /**
     * 屏幕的宽和高
     */
    private int mContentWidth;
    private int mContentHeight;

    /**
     * 触发退出当前Activity的宽度
     */
    private float mSlideWidth;

    /**
     * 用于记录当前滑动距离
     */
    private int curSlideX;

    /**
     * 是否可以滑动
     */
    private boolean mSlidingEnable = true;

    /**
     * 上层页面滑动百分比
     */
    private float mScrollPercent;

    /**
     * 是否绘制背景页面
     */
    private boolean canDrawBehinds;

    private final float BEHIND_MOVE_PERCENT = 0.33f; // 背景移动临界点百分比
//    private final float FADE_DEGREE = 0.66f; // 背景透明度初始值（默认为0.66）

    private int mBehindTranslateX; // 背景view平移距离

    private Bitmap mBehindBitmap;

    private Activity mBehindActivity;

    public void setBehindActivity(Activity behindActivity) {
        this.mBehindActivity = behindActivity;
    }

    public void setSlidingEnable(boolean slidingEnable) {
        mSlidingEnable = slidingEnable;
    }


    /**
     * 构造方法
     *
     * @param context
     */
    public SlidingFrameLayout(Context context) {
        this(context, null);
    }

    public SlidingFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (Activity) context;
        //构造ViewDragHelper
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new DragCallback());
        //设置从左边缘捕捉View
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void bind() {
        canDrawBehinds = true;
        // 初始化rootView
        ViewGroup mDecorView = (ViewGroup) mActivity.getWindow().getDecorView();
        mContentView = mDecorView.getChildAt(0);
        mDecorView.removeView(mContentView);
        this.addView(mContentView);
        mDecorView.addView(this);

        //计算屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mContentWidth = dm.widthPixels;
        mContentHeight = dm.heightPixels;
        mSlideWidth = dm.widthPixels * 0.28f;
    }


    public Bitmap createBehindBitmap() {
        if (canDrawBehinds && mSlidingEnable && mBehindBitmap == null && mBehindActivity != null && mBehindBitmap == null) {
            View behindView = mBehindActivity.getWindow().getDecorView().getRootView();

            try {
                mBehindBitmap = ViewUtils.viewToBitmap(behindView);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                if (!Utils.isValidatedBitmap(mBehindBitmap)) {
                    mBehindBitmap = ViewUtils.viewToBitmap(behindView, null);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return mBehindBitmap;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mSlidingEnable) {
            return super.onInterceptTouchEvent(event);
        }
        try {
            return mViewDragHelper.shouldInterceptTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mSlidingEnable) {
            return false;
        }
        try {
            mViewDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        try {
            mInLayout = true;
            if (mContentView != null)
                mContentView.layout(mContentLeft, mContentTop,
                        mContentLeft + mContentView.getMeasuredWidth(),
                        mContentTop + mContentView.getMeasuredHeight());
            mInLayout = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    private class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false; // child == mContentView; //只有mContentView可以移动
        }

        /**
         * 滑动位置改变时 触发
         *
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            curSlideX = left;

            //当滚动位置到达屏幕最右边，则关掉Activity
            if (changedView == mContentView && left >= mContentWidth) {
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.none, R.anim.none);
            }

            mScrollPercent = Math.abs((float) left / mContentWidth);
            float scrimOpacity = 1 - mScrollPercent; // 背景（不透明度／平移）百分比

//            float degree = FADE_DEGREE * scrimOpacity + mScrollPercent;
//            setColor(degree);
//            ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(mColorMatrix);
//            mPaint.setColorFilter(colorFilter);

            mBehindTranslateX = (int) (-getWidth() * BEHIND_MOVE_PERCENT * scrimOpacity);

            mContentLeft = left;
            mContentTop = top;

            //当滑动位置改变时，刷新View,绘制新的阴影位置
            invalidate();
        }

        /**
         * 松开手时 触发
         *
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //当前回调，松开手时触发，比较触发条件和当前的滑动距离
            int left = releasedChild.getLeft();
            if (left <= mSlideWidth) {
                //缓慢滑动的方法,小于触发条件，滚回去
                mViewDragHelper.settleCapturedViewAt(0, 0);
            } else {
                //大于触发条件，滚出去...
                mViewDragHelper.settleCapturedViewAt(mContentWidth, 0);
            }
            //需要手动调用更新界面的方法
            invalidate();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            //限制左右拖拽的位移
//            left = left >= 0 ? left : 0;
//            return left;
            //这样的话，页面就只能向右mContentWidth滑动了！
            return Math.min(mContentWidth, Math.max(left, 0));
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mContentWidth;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            //触发边缘时，主动捕捉mRootView
            mViewDragHelper.captureChildView(mContentView, pointerId);
        }
    }

//    private float[] mColorMatrix = new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};
//
//    private void setColor(float degree) {
//        final float[] a = mColorMatrix;
//        a[0] = a[6] = a[12] = degree;
//    }

    /**
     * 使用settleCapturedViewAt方法时，必须重写computeScroll方法，传入true
     */
    @Override
    public void computeScroll() {
        //持续滚动期间，不断刷新ViewGroup
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true))
            invalidate();
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
//        onDraw（）方法在ViewGroup中不一定会执行
        try {
            if (mSlidingEnable && canDrawBehinds) {
                drawBehindView(canvas);
            }
            drawShadow(canvas);
            super.dispatchDraw(canvas);
        } catch (StackOverflowError error) {
            error.printStackTrace();
        }

    }

    /**
     * 画底层activity
     *
     * @param canvas
     */
    private void drawBehindView(Canvas canvas) {
        int height = canvas.getHeight();
        canvas.saveLayer(0, 0, curSlideX, height, mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.translate(mBehindTranslateX, 0);
//        Log.e(S, " drawBehindView  mBehindBitmap = " + (mBehindBitmap == null));
//        if (mBehindBitmap == null) {
//            mBehindBitmap = createBehindBitmap();
//        }
        if (mBehindBitmap != null && !mBehindBitmap.isRecycled()) {
            canvas.drawBitmap(mBehindBitmap, 0, 0, mPaint);
        }
        canvas.restore();
    }

    /**
     * 进行阴影绘制
     *
     * @param canvas
     */
    private void drawShadow(Canvas canvas) {
        canvas.save();
        //构造一个渐变
        Shader mShader = new LinearGradient(curSlideX - 40, 0, curSlideX, 0, Color.parseColor("#00000000"), Color.parseColor("#30000000"), Shader.TileMode.REPEAT);
//        Shader mShader = new LinearGradient(curSlideX - 20, 0, curSlideX, 0, new int[]{Color.parseColor("#1edddddd"), Color.parseColor("#6e666666"), Color.parseColor("#9e666666")}, null, Shader.TileMode.REPEAT);
        //设置着色器
        mPaint.setShader(mShader);
        //绘制时，注意向左边偏移
        RectF rectF = new RectF(curSlideX - 40, 0, curSlideX, mContentHeight);
        canvas.drawRect(rectF, mPaint);
        canvas.restore();
    }

    public void recycleBitmap() {
        if (mBehindBitmap != null && !mBehindBitmap.isRecycled()) {
            mBehindBitmap.recycle();
            mBehindBitmap = null;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(false);
    }
}

