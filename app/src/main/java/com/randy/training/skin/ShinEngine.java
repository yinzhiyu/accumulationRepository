package com.randy.training.skin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/12/3010:37
 * desc   :
 */
public class ShinEngine {
    private ShinEngine() {

    }

    public static ShinEngine getInstance() {
        return ShinEngineHolder.sInstance;
    }

    /**
     * 静态内部类
     */
    private static class ShinEngineHolder {
        private static final ShinEngine sInstance = new ShinEngine();
    }

    private Resources outResources;
    private Context context;
    private String outPkgName;

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 加载外部资源包
     */
    public void load(final String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        PackageManager packageManager = this.context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        outPkgName = packageInfo.packageName;
        AssetManager assetManager;
        try {
            //通过反射获取 AssetManager 用来加载外面的资源包
            assetManager = AssetManager.class.newInstance();
            //addAssetPath 方法可以加载外部的资源包
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager,path);

            outResources = new Resources(assetManager, this.context.getResources().getDisplayMetrics(), this.context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getColor(int resId){
        if(outResources==null) {
            return resId;
        }
        String resName = outResources.getResourceEntryName(resId);
        int outResId = outResources.getIdentifier(resName, "color", outPkgName);
        if(outResId==0){
            return resId;
        }
        return outResources.getColor(outResId);
    }

    public Drawable getDrawable(int resId) {
        if(outResources==null) {
            return ContextCompat.getDrawable(this.context, resId);
        }
        String resName = outResources.getResourceEntryName(resId);
        int outResId = outResources.getIdentifier(resName, "drawable", outPkgName);
        if(outResId==0){
            return ContextCompat.getDrawable(this.context, resId);
        }
        return outResources.getDrawable(outResId);
    }
}
