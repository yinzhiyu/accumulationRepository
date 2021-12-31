package com.randy.training.skin;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.randy.training.R;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;

/**
 * @author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/12/2914:35
 * desc   :
 */
public class SkinFactory implements LayoutInflater.Factory2 {
    private AppCompatDelegate mDelegate;
    private List<SkinView> cacheSkinView = new ArrayList<SkinView>();

    public void setDelegate(AppCompatDelegate delegate) {
        this.mDelegate = delegate;
    }

    static final Class<?>[] constructors = new Class[]{Context.class, Attributes.class};

    final Object[] constructorArgs = new Object[2];

    private static final HashMap<String, Constructor<? extends View>> constructorMap = new HashMap<>();

    static final String[] prefixes = new String[]{"android.widget.", "android.view.", "android.webkit"};

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        View view = mDelegate.createView(parent, name, context, attributeSet);
        if (view == null) {
            constructorArgs[0] = context;
            try {
                if (-1 == name.indexOf(".")) {
                    //如果 View 的 name 中不包含 “.” ，则说明是系统控件，会在接下来的调用链在 name 前面加上 “Android”
//                    view = LayoutInflater.createView(context, name, prefixes, attributeSet);
                } else {
                    //如果 name 中包含 “.” 则直接调用 createView 方法，onCreateView 后续也是调用了 createView
//                    view = createView(context, name, prefixes, attributeSet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        collectSkinView(context, attributeSet, view);
        return view;
    }

    private void collectSkinView(Context context, AttributeSet attrs, View view) {
        //获取我们自己定义的属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Skinable);
        boolean isSupport = a.getBoolean(R.styleable.Skinable_isSupport, false);
        if (isSupport) {
            final int len = attrs.getAttributeCount();
            HashMap<String, String> attrmap = new HashMap<String, String>();
            for (int i = 0; i < len; i++) {
                String attrName = attrs.getAttributeName(i);
                String attrVale = attrs.getAttributeValue(i);

                attrmap.put(attrName, attrVale);
            }
            SkinView skinView = new SkinView();
            skinView.view = view;
            skinView.attrsMap = attrmap;
            cacheSkinView.add(skinView);
        }
    }

    public void changeSkin() {
        for (SkinView skinView : cacheSkinView) {
            skinView.changeSkin();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String s, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        return null;
    }
}
