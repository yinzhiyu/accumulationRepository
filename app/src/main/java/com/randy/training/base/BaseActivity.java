package com.randy.training.base;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.randy.training.R;


public class BaseActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private FrameLayout parentLinearLayout;//把父类activity和子类activity的view都add到这里


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initContentView(R.layout.activity_base);
        findViewById(R.id.cl_bg).setBackground(getResources().getDrawable(R.mipmap.dog2));
    }

    /**
     * 初始化contentview
     */

    private void initContentView(int layoutResID) {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);

        viewGroup.removeAllViews();

        parentLinearLayout = new FrameLayout(this);

//        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);

        viewGroup.addView(parentLinearLayout);

        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

    }

    @Override

    public void setContentView(View view) {
        parentLinearLayout.addView(view);

    }

    @Override

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        parentLinearLayout.addView(view, params);

    }
}