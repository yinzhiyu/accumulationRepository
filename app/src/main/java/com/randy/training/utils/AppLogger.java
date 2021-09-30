package com.randy.training.utils;

import java.util.Map;

/**
 * 日志工具类 User: pliliang Date: 13-7-4 Time: 下午5:26
 */
public class AppLogger {

    private static final String TAG = "ZongHengReader";

    private AppLogger() {
    }

    public static void v(String msg) {
        if (DebugConfig.DEBUG) {
            android.util.Log.v(TAG, buildMessage(msg));
        }
    }

    public static void d(String msg) {
        if (DebugConfig.DEBUG) {
            android.util.Log.d(TAG, buildMessage(msg));
        }
    }

    public static void i(String msg) {
        if (DebugConfig.DEBUG) {
            android.util.Log.i(TAG, buildMessage(msg));
        }
    }

    public static void w(String msg) {
        if (DebugConfig.DEBUG) {
            android.util.Log.w(TAG, buildMessage(msg));
        }
    }

    public static void w(String tag, String msg) {
        if (DebugConfig.DEBUG) {
            android.util.Log.w(tag, buildMessage(msg));
        }
    }

    public static void e(String msg) {
        if (DebugConfig.DEBUG) {
            android.util.Log.e(TAG, buildMessage(msg));
        }
    }

    public static void i(String tag, String msg) {
        if (DebugConfig.DEBUG) {
            android.util.Log.i(tag, buildMessage(msg));
        }
    }

    public static void e(String tag, String msg) {
        if (DebugConfig.DEBUG) {
            android.util.Log.e(tag, buildMessage(msg));
        }
    }

    public static void e(Class clazz, String msg) {
        if (DebugConfig.DEBUG && clazz != null) {
            android.util.Log.e(clazz.getSimpleName(), msg);
        }
    }

    public static void es(String... msg) {
        StringBuilder sb = new StringBuilder();
        for (String s : msg) {
            sb.append(s).append("=");
        }
        String result = "";
        if (sb.length() > 1) {
            result = sb.substring(0, sb.length() - 1);
        }
        android.util.Log.e(TAG, buildMessage(result));
    }

    public static void log_request(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (String s : params.keySet()) {
            sb.append(s).append("=").append(params.get(s)).append("&");
        }
        String s = sb.substring(0, sb.length() - 1);
        if (DebugConfig.DEBUG) {
            android.util.Log.w(TAG, s);
        }
    }

    private static String buildMessage(String msg) {
		/*StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

		return new StringBuilder()
		        .append(caller.getClassName())
		        .append(".")
		        .append(caller.getMethodName())
		        .append("(): ")
		        .append(msg).toString();*/
        return msg;
    }
}
