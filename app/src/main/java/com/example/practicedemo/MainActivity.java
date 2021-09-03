package com.example.practicedemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.example.practicedemo.base.BaseActivity;
import com.example.practicedemo.widget.Rotate3DLayout;


public class MainActivity extends BaseActivity {
    private Button btRotate;
    private Rotate3DLayout rlRotate;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btRotate = (Button) findViewById(R.id.bt_rotate);
        rlRotate = (Rotate3DLayout) findViewById(R.id.rl_rotate);
        //点击后翻转
        btRotate.setOnClickListener(v -> rlRotate.rotate3D());
    }
}