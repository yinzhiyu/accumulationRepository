package com.randy.training.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;

@SuppressLint({"WrongCall", "NewApi"})
public class ViewUtils {
    public static final int DRAWABLE_LEFT = 1;
    public static final int DRAWABLE_TOP = 2;
    public static final int DRAWABLE_RIGHT = 3;
    public static final int DRAWABLE_BOTTOM = 4;
    public static final int DENSITY_NUM_PX = 160;
    public static final int PIXEL_DENSITY_240 = 240;
    public static final int PIXEL_DENSITY_320 = 320;
    public static final int PIXEL_DENSITY_480 = 480;
    public static final int PIXEL_DENSITY_640 = 640;

    /**
     * 使用软件加速,在打开硬件加速的情况下,由于某些View并不支持硬件加速,因此需要设置为软件加速
     *
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void useSoftware(View view, Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// Android3.0开始有此方法
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        }
    }

    /**
     * 打开当前Window的硬件加速
     *
     * @param window
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void openHardWare(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// Android3.0开始有此方法,但加速效果太差,所以只在4.0以上版本开启
            window.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
    }

    /**
     * 视图是否支持硬件加速
     *
     * @param view
     */
    public static boolean isHardwareAccelerated(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// Android3.0开始有此方法
            return view.isHardwareAccelerated();
        }
        return false;
    }

    /**
     * Canvas是否支持硬件加速
     */
    public static boolean isHardwareAccelerated(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// Android3.0开始有此方法
            return canvas.isHardwareAccelerated();
        }
        return false;
    }

    /**
     * 设置View背景
     *
     * @param view
     * @param drawable
     */
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 全局布局监听器 通过GlobalLayoutListener.onLayout()返回flase将保持监听,返回true将自动移除监听器
     */
    public static abstract class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private View view;

        @Override
        final public void onGlobalLayout() {
            if (onLayout() && view != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                view = null;
            }
        }

        abstract public boolean onLayout();

    }

    /**
     * 添加全局布局监听器,主要用于需要在布局完成后获取视图高宽等信息
     *
     * @param listener
     */
    public static void addGlobalLayoutListener(View view, GlobalLayoutListener listener) {
        listener.view = view;
        view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    /**
     * 用于在视图未确定高宽前"估算"视图的width以及height,注意此方法必须是使用LayoutInflater.inflate(resid,
     * container, false),并且container!=null的情况才有效
     *
     * @param child
     */
    public static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 修复PopupWindow在Android3.0之前因为绑定到ViewTreeObserver.
     * OnScrollChangedListener后没有判断null导致的错误
     */
    public static void popupWindowFix(final PopupWindow popupWindow) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            try {
                final Field fAnchor = PopupWindow.class.getDeclaredField("mAnchor");
                fAnchor.setAccessible(true);
                Field listener = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
                listener.setAccessible(true);
                final ViewTreeObserver.OnScrollChangedListener originalListener = (ViewTreeObserver.OnScrollChangedListener) listener
                        .get(popupWindow);
                ViewTreeObserver.OnScrollChangedListener newListener = new ViewTreeObserver.OnScrollChangedListener() {
                    public void onScrollChanged() {
                        try {
                            View mAnchor = (View) fAnchor.get(popupWindow);
                            if (mAnchor == null) {
                                return;
                            } else {
                                originalListener.onScrollChanged();
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                };
                listener.set(popupWindow, newListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置当前Activity是否是全屏模式，需要在Oncreate中调用才可生效
     */
    public static void fullScreen(Context context, boolean enable) {
        Activity activity = (Activity) context;
        Window window = activity.getWindow();
        if (enable) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = window.getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(attr);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public static int getBottomBarHeight(Activity activity) {
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);

        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return dm.heightPixels - rectangle.bottom;
    }

    public static int getTopBarHeight(Activity activity) {
        Rect rect = new Rect();
        Window win = activity.getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * <b>dip转px</b>
     *
     * @param context the application environment
     * @param dip     int值
     * @return px像素值
     */
    public static int convertDIP2PX(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * <b>px转dip</b>
     *
     * @param context the application environment
     * @param px      像素值
     * @return dip值
     */
    public static int convertPX2DIP(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    public static int getStatusBarHeight() {
        int id = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        return Resources.getSystem().getDimensionPixelSize(id);
    }

    public static void setListViewScrollThumb(Context context, ListView listView, int id) {
        try {
            Field f;
            try {
                f = AbsListView.class.getDeclaredField("mFastScroller");
            } catch (Exception e) {
                f = AbsListView.class.getDeclaredField("mFastScroll");
            }
            f.setAccessible(true);
            Object o = f.get(listView);
            try {
                int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
                if (targetSdkVersion >= Build.VERSION_CODES.KITKAT &&
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        setFastScrollReflectImageView(context, f, o, id);
                    } catch (Exception e) {
                        setFastScrollReflectDrawable(context, f, o, id);
                    }
                } else {
                    setFastScrollReflectDrawable(context, f, o, id);
                }
            } catch (Exception e) {
                setFastScrollReflectImageView(context, f, o, id);
            }
        } catch (Exception e) {
            AppLogger.e("" + e.getMessage());
        }
    }

    private static void setFastScrollReflectDrawable(Context context, Field f, Object o, int id)
            throws Exception {
        Field f1 = f.getType().getDeclaredField("mThumbDrawable");
        f1.setAccessible(true);
        Drawable drawable = (Drawable) f1.get(o);
        drawable = context.getResources().getDrawable(id);
        f1.set(o, drawable);

        Field f2 = f.getType().getDeclaredField("mTrackDrawable");
        f2.setAccessible(true);
        f2.set(o, null);
    }

    private static void setFastScrollReflectImageView(Context context, Field f, Object o, int id)
            throws Exception {
        Field f1 = f.getType().getDeclaredField("mThumbImage");
        f1.setAccessible(true);
        ImageView imageView = (ImageView) f1.get(o);
        imageView.setImageDrawable(context.getResources().getDrawable(id));

        Field f2 = f.getType().getDeclaredField("c");
        f2.setAccessible(true);
        ImageView trackImageView = (ImageView) f2.get(o);
        trackImageView.setVisibility(View.GONE);
    }

    public static Bitmap getDrawingCache(RelativeLayout view) {
        view.setDrawingCacheEnabled(true);
        view.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }


    /**
     * 设置drawable背景
     *
     * @param view
     * @param drawableDirection 设置方向
     * @param drawable
     */
    public static void setViewDrawable(TextView view, int drawableDirection, Drawable drawable) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            if (drawableDirection == DRAWABLE_LEFT) {
//                view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
//            } else if (drawableDirection == DRAWABLE_TOP) {
//                view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
//            } else if (drawableDirection == DRAWABLE_RIGHT) {
//                view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
//            } else if (drawableDirection == DRAWABLE_BOTTOM) {
//                view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, drawable);
//            }
//        } else {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        if (drawableDirection == DRAWABLE_LEFT) {
            view.setCompoundDrawables(drawable, null, null, null);
        } else if (drawableDirection == DRAWABLE_TOP) {
            view.setCompoundDrawables(null, drawable, null, null);
        } else if (drawableDirection == DRAWABLE_RIGHT) {
            view.setCompoundDrawables(null, null, drawable, null);
        } else if (drawableDirection == DRAWABLE_BOTTOM) {
            view.setCompoundDrawables(null, null, null, drawable);
        }
//        }
    }


    /**
     * 回收ImageView 资源
     *
     * @param mGuideImage
     */
    public static void recycleGuideImageView(ImageView mGuideImage) {
        try {
            Drawable drawable = mGuideImage.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
                System.gc();
            }
            mGuideImage.setImageBitmap(null);
            if (drawable != null) {
                drawable.setCallback(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final int PORTRAIT = 0;
    private static final int LANDSCAPE = 1;
    @NonNull
    private volatile static Point[] mRealSizes = new Point[2];

    /**
     * 得到屏幕的高度，像素
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(@Nullable Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

    /**
     * 获取当前 dpi
     * 160dpi 的屏幕其屏幕密度为 1
     *
     * @param context
     * @return
     */
    public static int getScreenDPI(Context context) {
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return (int) (dm.density * DENSITY_NUM_PX);
        }
        return 0;
    }

    /**
     * 判断虚拟导航栏是否显示
     *
     * @param context 上下文对象
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     */
    public static boolean checkNavigationBarShow(@NonNull Activity context) {
        boolean show;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);

        View decorView = context.getWindow().getDecorView();
        Configuration conf = context.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            show = (point.x != contentView.getWidth());
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            show = (rect.bottom != point.y);
        }
        return show;
    }

    /**
     * 获取虚拟键盘高度
     *
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Context activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

    public static void removeViewFromParent(View view) {
        try {
            if (view != null && view.getParent() != null) {
                ViewParent parent = view.getParent();
                if (parent instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) parent;
                    group.removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到屏幕的宽度，像素
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
//获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
// pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//listView.getDividerHeight()获取子项间分隔符占用的高度
//params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 对TextView中某一段文字进行变色处理
     *
     * @param tv
     * @param colorString 需要变色的文字
     * @param color
     */
    public static void setColorText(TextView tv, String colorString, int color) {
        if (tv == null || colorString == null) {
            return;
        }
        String text = tv.getText().toString();
        if (!text.contains(colorString)) {
            return;
        }
        Spannable word = new SpannableString(text);
        int start = text.indexOf(colorString);
        int end = start + colorString.length();
        word.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(word);
    }

    /**
     * 对TextView中多段文字进行变色处理
     *
     * @param tv
     * @param maps 要变色的文字及其颜色的键值对集合
     */
    public static void setColorTexts(TextView tv, HashMap<String, Integer> maps) {
        if (tv == null || maps == null || maps.size() == 0) {
            return;
        }
        String text = tv.getText().toString();
        Spannable word = new SpannableString(text);
        for (String key : maps.keySet()) {
            if (key == null || !text.contains(key) || maps.get(key) == null) {
                continue;
            }
            int start = text.indexOf(key);
            int end = start + key.length();
            word.setSpan(new ForegroundColorSpan(maps.get(key)), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        tv.setText(word);
    }


    public static Bitmap viewToBitmap(View v, Bitmap tempBitmap) {
        try {
            if (v == null || v.getWidth() == 0 || v.getHeight() == 0)
                return null;
            Bitmap bitmap = tempBitmap;

            if (!Utils.isValidatedBitmap(bitmap) || bitmap.getWidth() != v.getWidth() ||
                    bitmap.getHeight() != v.getHeight()) {
                bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
            }
            v.measure(MeasureSpec.makeMeasureSpec(v.getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(v.getHeight(), MeasureSpec.EXACTLY));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            v.draw(canvas);
            return bitmap;
        } catch (Throwable t) {
//            AppLogger.e(NewSlideView.TAG, " viewToBitmap is error " + t.getMessage());
        }
        return tempBitmap;
    }

    /**
     * 把View绘制到Bitmap上
     *
     * @param view 需要绘制的View
     * @return 返回Bitmap对象
     */
    public static Bitmap viewToBitmap(View view) {
        Bitmap bitmap = null;
        if (view != null) {
            view.clearFocus();
            view.setPressed(false);

            boolean willNotCache = view.willNotCacheDrawing();
            view.setWillNotCacheDrawing(false);

            // Reset the drawing cache background color to fully transparent
            // for the duration of this operation
            int color = view.getDrawingCacheBackgroundColor();
            view.setDrawingCacheBackgroundColor(0);
            float alpha = view.getAlpha();
            view.setAlpha(1.0f);

            if (color != 0) {
                view.destroyDrawingCache();
            }
            int w = view.getWidth();
            int h = view.getHeight();

            int widthSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
            view.measure(widthSpec, heightSpec);
            view.layout(0, 0, w, h);

            view.buildDrawingCache();
            Bitmap cacheBitmap = view.getDrawingCache();
            if (cacheBitmap == null) {
//                Log.e("view.ProcessImageToBlur", "failed getViewBitmap(" + view + ")",
//                        new RuntimeException());
                return null;
//                throw new RuntimeException();
            }
            bitmap = Bitmap.createBitmap(cacheBitmap);
            // Restore the view
            view.setAlpha(alpha);
            view.destroyDrawingCache();
            view.setWillNotCacheDrawing(willNotCache);
            view.setDrawingCacheBackgroundColor(color);
        }
        return bitmap;
    }

    public static View getViewByPosition(int pos, ListView listView, int headerViewCount) {
        final int firstListItemPosition = listView.getFirstVisiblePosition() - headerViewCount;
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static Bitmap scrollView2Bitmap(ScrollView scrollView, @ColorInt int colorBg) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            if (colorBg != -1)
                scrollView.getChildAt(i).setBackgroundColor(colorBg);
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    public static class ScreenInfo {

        public int x;

        public int y;

        public View parentView = null;
    }


    public static ScreenInfo getScreenInfo(View v, View parent) {
        int XY[] = {0, 0};
        View mainView = null;
        while (v != null) {
            XY[0] += v.getLeft();
            XY[1] += v.getTop();
            if (parent != null && v == parent || v.toString().contains("DecorView")) {
                mainView = v;
                break;
            }
            v = (View) v.getParent();
        }

        ScreenInfo screenInfo = new ScreenInfo();
        screenInfo.x = XY[0];
        screenInfo.y = XY[1];
        screenInfo.parentView = mainView;

        return screenInfo;
    }

    public static int getNameMaxLength(Context context) {
        int maxNameLength = 12;
        if (Utils.getScreenWidth(context) <= 480) {
            maxNameLength = 4;
        } else if (Utils.getScreenWidth(context) <= 1080) {
            maxNameLength = 8;
        }
        return maxNameLength;
    }
}
