package com.randy.training.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import androidx.collection.LruCache;
import androidx.core.content.ContextCompat;

/**
 * Created with IntelliJ IDEA.
 * User: pliliang
 * Date: 13-7-2
 * Time: 上午10:31
 */
public class Utils {

    public static DecimalFormat sDecimalFormat = new DecimalFormat("#.#");


    /**
     * 针对webview的Url添加公共上行参数
     */
    public static String addUrlParams(String webUrl, HashMap<String, String> params) {
        if (!TextUtils.isEmpty(webUrl)) {
            HashMap<String, String> formParams = new HashMap<String, String>();
            if (params != null) {
                formParams.putAll(params);
            }
            StringBuilder sb = new StringBuilder(webUrl);
            sb.append((webUrl.indexOf("?") > 0 ? "&" : "?"));
            for (String s : formParams.keySet()) {
                sb.append(s).append("=").append(formParams.get(s)).append("&");
            }
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }


    /**
     * 时间格式化
     *
     * @param ms 需要转化为时间的变量
     * @param s  转化前基准： 1 - 秒级别 ; 1000 - 毫秒级别
     * @return
     */
    public static String formatTime(long ms, int s) {
        int ss = s;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        StringBuilder sb = new StringBuilder();
        sb.append(Integer.parseInt(strDay) == 0 ? "" : strDay + "天");
        sb.append(Integer.parseInt(strHour) == 0 ? "" : strHour + "'");
        sb.append(Integer.parseInt(strMinute) == 0 ? "" : strMinute + "'");
        sb.append(strSecond + "''");
        sb.append(s != 1000 ? "" : strMilliSecond + "'''");
//        sb.append(Integer.parseInt(strSecond) == 0 ? "" : strSecond + "''");
//        sb.append(Integer.parseInt(strMilliSecond) == 0 ? "" : strMilliSecond + "'''");
        return sb.toString();
    }

    /**
     * 过滤键盘emoji
     *
     * @param source
     * @return
     */
    public static String filterEmojiString(String source) {
        StringBuilder sb = new StringBuilder();
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                sb.append(codePoint);
            }
        }
        return sb.toString();
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF)));
    }


    /**
     * 格式化内容
     *
     * @param raw
     * @return
     */
    public static String getFormatedContent(String raw) {
        raw = raw.replaceAll("(\\\\n|\\\\r|</p>|<p>|<br/>|<br />)+", "\n        ");
        raw = raw.replaceAll("[\\r\\n　| ]+\\z", "");
        if (raw.equals(""))
            return "";
        else if (!raw.startsWith("\n        "))
            return "\n        " + raw;
        else
            return raw;
    }

    /**
     * 判断桌面是否已添加快捷方式
     * ps: not always working on different Android system
     *
     * @param context context
     * @return if shortcut exist on screen
     */
    public static boolean hasShortcut(Context context) {
        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isShortCutExist(context, title);
    }

    /**
     * 判断桌面是否已添加快捷方式
     *
     * @param context
     * @param shortcutName   快捷方式标题
     * @return
     */

    private static String AUTHORITY = null;

    public static boolean isShortCutExist(Context context, String title) {

        boolean isInstallShortcut = false;

        if (null == context || TextUtils.isEmpty(title))
            return isInstallShortcut;

        if (TextUtils.isEmpty(AUTHORITY))
            AUTHORITY = getAuthorityFromPermission(context);

        final ContentResolver cr = context.getContentResolver();

        if (!TextUtils.isEmpty(AUTHORITY)) {
            try {
                final Uri CONTENT_URI = Uri.parse(AUTHORITY);

                Cursor c = cr.query(CONTENT_URI, new String[]{"title",
                                "iconResource"}, "title=?", new String[]{title},
                        null);

                // XXX表示应用名称。
                if (c != null && c.getCount() > 0) {
                    isInstallShortcut = true;
                }
                if (null != c && !c.isClosed())
                    c.close();
            } catch (Exception e) {
                AppLogger.e("isShortCutExist", e.getMessage());
            }

        }
        return isInstallShortcut;

    }

    public static String getCurrentLauncherPackageName(Context context) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = context.getPackageManager()
                .resolveActivity(intent, 0);
        if (res == null || res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }

    public static String getAuthorityFromPermissionDefault(Context context) {

        return getThirdAuthorityFromPermission(context,
                "com.android.launcher.permission.READ_SETTINGS");
    }

    public static String getThirdAuthorityFromPermission(Context context,
                                                         String permission) {
        if (TextUtils.isEmpty(permission)) {
            return "";
        }

        try {
            List<PackageInfo> packs = context.getPackageManager()
                    .getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs == null) {
                return "";
            }
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission)
                                || permission.equals(provider.writePermission)) {
                            if (!TextUtils.isEmpty(provider.authority)// 精准匹配launcher.settings，再一次验证
                                    && (provider.authority)
                                    .contains(".launcher.settings"))
                                return provider.authority;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAuthorityFromPermission(Context context) {
        // 获取默认
        String authority = getAuthorityFromPermissionDefault(context);
        // 获取特殊第三方
        if (authority == null || authority.trim().equals("")) {
            String packageName = getCurrentLauncherPackageName(context);
            packageName += ".permission.READ_SETTINGS";
            authority = getThirdAuthorityFromPermission(context, packageName);
        }
        // 还是获取不到，直接写死
        if (TextUtils.isEmpty(authority)) {
            int sdkInt = Build.VERSION.SDK_INT;
            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                authority = "com.android.launcher.settings";
            } else if (sdkInt < 19) {// Android 4.4以下
                authority = "com.android.launcher2.settings";
            } else {// 4.4以及以上
                authority = "com.android.launcher3.settings";
            }
        }
        authority = "content://" + authority + "/favorites?notify=true";
        return authority;

    }

    /**
     * 改变按钮点击事件
     *
     * @param view
     * @param isEnable
     */
    public static void setButtonEnabled(View view, boolean isEnable) {
        view.setEnabled(isEnable);
        view.setClickable(isEnable);
    }

    public static String ListToString(int[] list) {
        StringBuilder idStr = new StringBuilder();
        for (int id : list) {
            idStr.append(String.valueOf(id)).append(",");
        }
        if (idStr.length() > 0)
            idStr.delete(idStr.length() - 1, idStr.length());

        return idStr.toString();
    }

    public static Bitmap getBitmap(Context mContext, int resId) {
        Bitmap mBgBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
        return mBgBitmap;
    }

    public static void recycleBitmap(Bitmap mBitmap) {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    /**
     * InputMethodManager持有的mCurRootView会持有Activity，导致泄漏
     *
     * @param context
     */
    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) {
            return;
        }

        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }

            Field f_mCurRootView = imm.getClass().getDeclaredField("mCurRootView");
            Field f_mServedView = imm.getClass().getDeclaredField("mServedView");
            Field f_mNextServedView = imm.getClass().getDeclaredField("mNextServedView");

            f_mCurRootView.setAccessible(true);
            f_mCurRootView.set(imm, null);

            f_mServedView.setAccessible(true);
            f_mServedView.set(imm, null);

            f_mNextServedView.setAccessible(true);
            f_mNextServedView.set(imm, null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void fixInputMethodManagerLeak1(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;

        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


    public static boolean isRunningInMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static boolean isValidatedBitmap(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled();
    }

    public static boolean isValidatedSoftRefBitmap(Reference<Bitmap> bitmap) {
        return bitmap != null && bitmap.get() != null && !bitmap.get().isRecycled();
    }

    public static boolean isValidatedSoftRefBitmap(SoftReference<Bitmap> bitmap) {
        return bitmap != null && bitmap.get() != null && !bitmap.get().isRecycled();
    }

    public static boolean isValidatedSoftRefBitmap(WeakReference<Bitmap> bitmap) {
        return bitmap != null && bitmap.get() != null && !bitmap.get().isRecycled();
    }

    public static float getFloatPoint2(Object data) {
        float temp = 0;
        if (data == null)
            return temp;
        if (data instanceof String) {
            String tempString = data.toString();
            temp = Float.parseFloat(tempString);
        } else if (data instanceof Long) {
            temp = Float.valueOf((long) data);
        } else if (data instanceof Integer) {
            temp = Float.valueOf((int) data);
        } else if (data instanceof Float) {
            temp = (float) data;
        } else {
            new IllegalArgumentException("don't contain this type ");
        }
        DecimalFormat df = new DecimalFormat("#.0");
        temp = Float.parseFloat(df.format(temp));
        return temp;
    }

    public static boolean isBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            return !topActivity.getPackageName().equals(context.getPackageName());
        }
        return false;
    }


    public static boolean isCpuX86ByCommand() {
        boolean isX86 = false;
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String cpuInfo = ""; // 1-cpu型号 //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo = cpuInfo + arrayOfString[i] + " ";
            }
            localBufferedReader.close();
            AppLogger.e("cpuinfo is :" + cpuInfo);
            isX86 = cpuInfo != null && (cpuInfo.toLowerCase().contains("x86"));
        } catch (IOException e) {
        }
        return isX86;
    }


    public static boolean isCpuX86ByBuild() {
        String cpuInfo = Build.CPU_ABI;
//        cpuInfo = "x86";
        return cpuInfo != null && (cpuInfo.toLowerCase().contains("x86"));
    }

    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static int[] getScreen(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        int screen[] = {screenHeight, screenWidth};
        return screen;
    }

    public static int getScreenWidth(Context context) {
        return getScreen(context)[1];
    }


    public static void setViewHeight(View v, int height) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
        layoutParams.height = height;
        v.setLayoutParams(layoutParams);
    }


    public static String getNumW(long num) {
        if (num > 10000) {
            return num / 10000 + "W";
        }
        return num + "";
    }

    /**
     * Window 级别开启硬件加速
     */
    public static void setLayerHardwareEnable(Window window) {
        if (window != null && Build.VERSION.SDK_INT >= 11) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            try {
                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                Field fd = WindowManager.LayoutParams.class.getField("FLAG_HARDWARE_ACCELERATED");
                int hardware_accelerated = fd.getInt(lParams);
                window.setFlags(hardware_accelerated, hardware_accelerated);
            } catch (Exception e) {
                AppLogger.e("** Older OS, no HW acceleration anyway! **");
            }
        }
    }

    /**
     * 关闭输入法
     *
     * @param activity
     */
    public static void hiddenKeyboard(Activity activity) {
        if (activity != null) {
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public static boolean isValidatedActivity(Activity activity) {
        if (activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return false;
        }
        return true;
    }

    public static boolean isValidatedContext(Context context) {
        if (context instanceof Activity) {
            return isValidatedActivity((Activity) context);
        }
        return context != null;
    }

    public static void runMainThreadToDelayedTime(Runnable runnable, long delayTime) {
        if (runnable == null)
            return;
        if (delayTime <= 0)
            new Handler(Looper.getMainLooper()).post(runnable);
        else
            new Handler(Looper.getMainLooper()).postDelayed(runnable, delayTime);
    }

    /**
     * 运行在UI主线程
     *
     * @param runnable
     */
    public static void runMainThread(Runnable runnable) {
        if (isRunningInMainThread()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    /**
     * 运行在UI主线程
     *
     * @param context
     * @param runnable
     */
    public static void runMainThread(Context context, Runnable runnable) {
        if (isRunningInMainThread()) {
            runnable.run();
        } else {
            if (context instanceof Activity && Utils.isValidatedActivity((Activity) context)) {
                ((Activity) context).runOnUiThread(runnable);
            } else {
                new Handler(Looper.getMainLooper()).post(runnable);
            }
        }
    }


    public static View addLoadingView(Context context) {
        if (context instanceof Activity) {
            return addLoadingView((Activity) context);
        } else {
            AppLogger.e(Utils.class.getSimpleName(), " 当前登录context不支持进度条显示  " + context.getClass().getSimpleName());
        }
        return null;
    }

    public static View addLoadingView(Activity activity) {
        return addLoadingView(activity, null);
    }

    public static View addLoadingView(Context context, View view) {
        if (context instanceof Activity) {
            return addLoadingView((Activity) context, view);
        } else {
            AppLogger.e(Utils.class.getSimpleName(), " 当前登录context不支持进度条显示  " + context.getClass().getSimpleName());
        }
        return null;
    }

    public static void removeLoadingView(View view) {
        try {
            if (view != null && view.getParent() != null) {
                ((ViewGroup) (view.getParent())).removeView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int TIME_INTERVAL = 500; //ms
    private static long LAST_CLICK_TIME;

    /**
     * 判断是否是快速点击
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        return isFastClick(TIME_INTERVAL);
    }

    /**
     * 判断是否是快速点击
     *
     * @param timeInterval 时间间隔
     * @return
     */
    public synchronized static boolean isFastClick(int timeInterval) {
        long time = System.currentTimeMillis();
        if (time - LAST_CLICK_TIME < timeInterval) {
            return true;
        }
        LAST_CLICK_TIME = time;
        return false;
    }


    private static long lastActionTime;
    public static final int INTERVAL = 300;

    public static boolean isActionEnable(int interval) {
        long current = System.currentTimeMillis();
        // 使用绝对值避免用户修改系统时间
        if (Math.abs(current - lastActionTime) > interval) {
            lastActionTime = current;
            return true;
        }
        return false;
    }

    static LruCache<Integer, Long> sActionRecord = new LruCache<Integer, Long>(20);

    /**
     * 判断两次事件间隔大于interval的为可用，否则不能用，最多记录20条事件
     *
     * @param actionId 事件ID
     * @param interval 事件间隔
     * @return true可用，false不可用
     */
    public static boolean isActionEnable(int actionId, int interval) {
        boolean flag = true;
        Long lastRecordTime = sActionRecord.get(actionId);
        long currentTimeMillis = System.currentTimeMillis();
        if (lastRecordTime != null) {
            flag = Math.abs(currentTimeMillis - lastRecordTime) > interval;
        } else {
            flag = true;
        }
        if (flag) {
            sActionRecord.put(actionId, currentTimeMillis);
        }
        return flag;
    }


    //软键盘弹出问题，版本小于6.0且魅族
    public static boolean isFlyme() {
        try {
//            final Method method = Build.class.getMethod("hasSmartBar");
            String name = Build.BRAND;
            return name.equalsIgnoreCase("Meizu") &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
        } catch (final Exception e) {
            return false;
        }
    }

    //软键盘弹出问题，版本小于6.0且oppo
    public static boolean isOppo() {
        try {
//            final Method method = Build.class.getMethod("hasSmartBar");
            String name = Build.BRAND;
            return name.equalsIgnoreCase("OPPO") &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
        } catch (final Exception e) {
            return false;
        }
    }

    //两个颜色叠加
    public static int overlayColor(int top, int base) {
        int resultAlpha = (int) ((1 - (1 - Color.alpha(top) / 255f) * (1 - Color.alpha(base) / 255f)) * 255f);
        int red = (int) (Color.red(top) * (1 - Color.alpha(top) / 255f) + Color.red(base) * Color.alpha(top) / 255f);
        int green = (int) (Color.green(top) * (1 - Color.alpha(top) / 255f) + Color.green(base) * Color.alpha(top) / 255f);
        int blue = (int) (Color.blue(top) * (1 - Color.alpha(top) / 255f) + Color.blue(base) * Color.alpha(top) / 255f);
        return Color.argb(resultAlpha, red, green, blue);
    }


    /**
     * 判断手机中是否存在虚拟导航
     */
    public static boolean isNaviBar(Activity activity) {
        boolean isBar = false;
        String navi = "navigationBarBackground";//虚拟导航栏字段名称----statusBarBackground状态栏字段名称，
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            int counts = vp.getChildCount();
            for (int i = 0; i < counts; i++) {
                int id = vp.getChildAt(i).getId();
                String name = null;
                try {
                    name = activity.getResources().getResourceEntryName(id);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                if (id != -1 && navi.equals(name)) {
                    isBar = true;
                    break;
                }
            }
        }
        return isBar;
    }

    /**
     * 复制帖子内容
     *
     * @param context
     * @param content
     */
    public static void copyToClipboard(Context context, String content, List<String> userNames) {
        if (userNames != null && userNames.size() > 0) {
            String[] splitStrs = content.split("\\[zh_@\\]", -1);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < splitStrs.length; i++) {
                if (i < userNames.size()) {
                    sb.append(splitStrs[i]).append("@").append(userNames.get(i)).append(" ");
                } else {
                    sb.append(splitStrs[i]);
                }
            }
            copyToClipboard(context, sb.toString());
        } else {
            copyToClipboard(context, content);
        }
    }

    /**
     * 复制文本
     *
     * @param context
     * @param text
     */
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(text);
    }

    @SuppressLint("WrongConstant")
    public static void installApk(Context context, File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean judgeAPK(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".") + 1;
        String fileType = fileName.substring(index);
        return "apk".equalsIgnoreCase(fileType);
    }

    public static boolean fileExist(File file) {
        return file.exists();
    }


    /**
     * 8.0 ,26
     *
     * @return
     */
    public static boolean hasO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 千字转万字
     *
     * @param book_size
     * @return
     */
    public static String formatWordcount(float book_size) {
        book_size = book_size / 10;
        sDecimalFormat.setRoundingMode(RoundingMode.DOWN);
        String format = sDecimalFormat.format(book_size);
        sDecimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        if (format.endsWith(".0")) {
            format = format.substring(0, format.length() - 2);
        }
        format += "万字";
        return format;
    }


    /**
     * 包名判断是否为主进程
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getProcessName(context));
    }

    /**
     * 获取进程名称
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }


    public static boolean readPermissionGranted(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writePermissionGranted(Context context) {
        try {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void enableAutoBrightness(ContentResolver aContentResolver) {
        Settings.System.putInt(aContentResolver, "screen_brightness_mode", 1);
    }
}
