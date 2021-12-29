package com.randy.training.utils.screenshot;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.util.Log;


/**
 * 截屏工具类
 *
 * @author admin
 */
public class ScreenshotHelper {
    private ContentResolver contentResolver;
    private IScreenshotCallBack screenshotCallBack;
    private ContentObserver  internalObserver;
    private ContentObserver  externalObserver;

    /**
     * 读取媒体数据库时需要读取的列
     */
    private static final String[] MEDIA_PROJECTIONS = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
    };

    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap", "snap", "截屏"
    };

    private ScreenshotHelper() {

    }

    public static ScreenshotHelper getInstance() {
        return ScreenshotHolder.S_INSTANCE;
    }

    /**
     * 静态内部类
     */
    private static class ScreenshotHolder {
        private static final ScreenshotHelper S_INSTANCE = new ScreenshotHelper();
    }


    public void init(ContentResolver contentResolver, IScreenshotCallBack screenshotCallBack) {
        this.contentResolver = contentResolver;
        this.screenshotCallBack = screenshotCallBack;
        HandlerThread handlerThread = new HandlerThread("Screenshot_Observer");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        Uri limitedCallLogUri = CallLog.Calls.CONTENT_URI.buildUpon()
                .appendQueryParameter(CallLog.Calls.LIMIT_PARAM_KEY, "1").build();


        internalObserver = new MediaControllerObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, handler);
        externalObserver = new MediaControllerObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, handler);
    }

    //添加监听
    public void register() {
        contentResolver.registerContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, true, internalObserver);
        contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, externalObserver);
    }

    public void unregister() {
        contentResolver.unregisterContentObserver(internalObserver);
        contentResolver.unregisterContentObserver(externalObserver);
    }

    /**
     * 媒体内容观察者 (观察媒体数据库的改变)
     */
    public class MediaControllerObserver extends ContentObserver {
        private final Uri contentUri;

        public MediaControllerObserver(Uri contentUri, Handler handler) {
            super(handler);
            this.contentUri = contentUri;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            handleMediaControllerChange(contentUri);
        }

        //数据改变时查询数据库中最后加入的一条数据，处理获取到的第一行数据。
        private void handleMediaControllerChange(Uri contentUri) {
            Cursor cursor = null;
            try {
                // 数据改变时查询数据库中最后加入的一条数据
                cursor = contentResolver.query(
                        contentUri,
                        MEDIA_PROJECTIONS,
                        null,
                        null,
                        MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
                );
                if (cursor == null) {
                    return;
                }
                if (!cursor.moveToFirst()) {
                    return;
                }
                // 获取各列的索引
                int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
                // 获取行数据
                final String data = cursor.getString(dataIndex);
                long dateTaken = cursor.getLong(dateTakenIndex);
                handleMediaRowData(data, dateTaken);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }


        }

        private void handleMediaRowData(String data, long dateTaken) {
            long duration = 0;
            long step = 100;
            //设置最大等待时间 500ms （因为某些魅族手机保存有延迟）
            while (!checkScreenShot(data) && duration <= 500) {
                try {
                    duration += step;
                    Thread.sleep(step);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (checkScreenShot(data)) {
                if (screenshotCallBack != null) {
                    screenshotCallBack.screenshotTaken(data);
                }
            }else{
                Log.d("xxxxxxxxxxxxxxx", "xxxxxxxxxxxx"+dateTaken);
            }
        }

        /**
         * 判断是否是截屏
         */
        private boolean checkScreenShot(String data) {
            if (data == null) {
                return false;
            }
            data = data.toLowerCase();
            //
            for (String keyWord : KEYWORDS) {
                if (data.contains(keyWord)) {
                    return true;
                }
            }
            return false;
        }
    }
}
