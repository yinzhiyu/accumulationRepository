package com.randy.training.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/9/610:40
 * desc   : activity 跳转工具类
 */
public class IntentUtil {
    public static final String SETTINGS_ACTION = "android.settings.APPLICATION_DETAILS_SETTINGS";


    public static void go(Context context, Class clzz) {
        goBundle(context, clzz, null);
    }


    /**
     * @param context
     * @param clzz
     * @param params  传递以key和value的形式，key必须为String类型
     */
    public static void go(Context context, Class clzz, Object... params) {
        Bundle bundle = createBundle(params);
        goBundle(context, clzz, bundle);
    }

    /**
     * 创建bundle对象
     *
     * @param params
     * @return
     */
    private static Bundle createBundle(Object[] params) {
        if (params == null) return null;

        if (params.length % 2 != 0) throw new IllegalArgumentException("缺少键所对应的值");

        int length = params.length / 2;
        Bundle bundle = new Bundle();
        for (int i = 0; i < length; i++) {
            putValue(bundle, (String) params[i * 2], params[i * 2 + 1]);
        }
        return bundle;
    }

    /**
     * 往bundle里存值
     *
     * @param bundle
     * @param key
     * @param value
     */
    private static void putValue(Bundle bundle, String key, Object value) {
        if (value == null) throw new IllegalArgumentException("键所对应的值不能为空");

        if (value instanceof String) {
            bundle.putString(key, (String) value);
            return;
        }

        if (value instanceof Integer) {
            bundle.putInt(key, (int) value);
            return;
        }

        if (value instanceof Long) {
            bundle.putLong(key, (Long) value);
            return;
        }

        if (value instanceof Float) {
            bundle.putFloat(key, (float) value);
            return;
        }

        if (value instanceof Boolean) {
            bundle.putBoolean(key, (boolean) value);
            return;
        }

        if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
            return;
        }

        if (value instanceof Serializable) {
            bundle.putParcelable(key, (Parcelable) value);
            return;
        }


        if (value instanceof int[]) {
            bundle.putIntArray(key, (int[]) value);
            return;
        }

        if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
            return;
        }

        if (value instanceof long[]) {
            bundle.putLong(key, (Long) value);
            return;
        }

        if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
            return;
        }

        if (value instanceof Parcelable[]) {
            bundle.putParcelableArray(key, (Parcelable[]) value);
            return;
        }

        if (value instanceof List) {
            List list = (List) value;
            if (list.size() == 0) return;


            if (list.get(0) instanceof String) {
                bundle.putStringArrayList(key, (ArrayList<String>) value);
                return;
            }

            if (list.get(0) instanceof Integer) {
                bundle.putIntegerArrayList(key, (ArrayList<Integer>) value);
                return;
            }

            if (list.get(0) instanceof Parcelable) {
                bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                return;
            }
        }

        throw new IllegalArgumentException("未处理的数据类型");
    }

    /**
     * 带bundle传递进行跳转
     *
     * @param context
     * @param clzz
     * @param bundle
     */
    public static void goBundle(Context context, Class clzz, Bundle bundle) {
        Intent intent = new Intent(context, clzz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 在fragment中使用
     *
     * @param fragment
     * @param clzz
     * @param requestCode
     * @param params
     */
    public void goForResult(Fragment fragment, Class clzz, int requestCode, Object... params) {
        Bundle bundle = createBundle(params);
        goBundleForResult(fragment, clzz, requestCode, bundle);
    }

    public void goForResult(Fragment fragment, Class clzz, int requestCode) {
        goBundleForResult(fragment, clzz, requestCode, null);
    }


    public void goBundleForResult(Fragment fragment, Class clzz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(fragment.getContext(), clzz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 在activity中使用
     *
     * @param activity
     * @param clzz
     * @param requestCode
     * @param params
     */
    public void goForResult(Activity activity, Class clzz, int requestCode, Object... params) {
        Bundle bundle = createBundle(params);
        goBundleForResult(activity, clzz, requestCode, bundle);
    }

    public void goForResult(Activity activity, Class clzz, int requestCode) {
        goBundleForResult(activity, clzz, requestCode, null);
    }


    public void goBundleForResult(Activity activity, Class clzz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, clzz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 跳转到打电话界面
     *
     * @param context
     * @param phoneNum
     */
    public static void goCall(Context context, String phoneNum) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel://" + phoneNum));
        context.startActivity(intent);
    }


    /**
     * 跳转到浏览器页面
     *
     * @param context
     * @param url
     */
    public static void goBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }


    /**
     * 跳转到app的设置页面(包括通知管理、应用权限等)
     */
    public static void goSetting(Context context) {
        Intent intent = new Intent()
                .setAction(SETTINGS_ACTION)
                .setData(Uri.fromParts("package",
                        context.getApplicationContext().getPackageName(), null));
        context.startActivity(intent);
    }

    /**
     * 跳转系统的蓝牙设置界面
     *
     * @param context
     */
    public static void goBluetoothSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到无限网络界面
     *
     * @param context
     */
    public static void goWifiSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到移动网络设置界面
     *
     * @param context
     */
    public static void goRoamingSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转位置服务界面【管理已安装的应用程序】
     *
     * @param context
     */
    public static void goLocationSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }
}
