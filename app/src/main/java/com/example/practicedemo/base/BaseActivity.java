package com.example.practicedemo.base;

import android.os.Build;
import android.os.Bundle;

import com.example.practicedemo.databinding.ActivityBaseBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.practicedemo.R;

public class BaseActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
//    private ActivityBaseBinding binding;
    //父类 activity
    private FrameLayout parentLinearLayout;//把父类activity和子类activity的view都add到这里
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityBaseBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_base);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_base);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityBaseBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        initContentView(getResources().getIdentifier("cl_bg","id",getPackageName()));
//        binding.getRoot().setBackground(getResources().getDrawable(R.mipmap.dog2));

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