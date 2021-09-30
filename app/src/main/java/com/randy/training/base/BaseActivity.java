package com.randy.training.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/9/3015:43
 * desc   :
 */
public class BaseActivity extends AppCompatActivity {
    private SlidingFrameLayout slidingFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getAppManager().addActivity(this);
        slidingFrameLayout = new SlidingFrameLayout(this);
        slidingFrameLayout.bind();
        Activity lastActivity = ActivityStackManager.getAppManager().lastActivity();
        slidingFrameLayout.setBehindActivity(lastActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (slidingFrameLayout != null) {
            slidingFrameLayout.createBehindBitmap();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (slidingFrameLayout != null) {
            slidingFrameLayout.recycleBitmap();
        }
    }

    /**
     * 设置是否支持横向拖拽
     *
     * @param slidingEnable
     */
    public void setSlidingEnable(boolean slidingEnable) {
        if (slidingFrameLayout != null) {
            slidingFrameLayout.setSlidingEnable(slidingEnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getAppManager().finishActivity();
    }
}