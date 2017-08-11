package com.github.butterknifedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.BindString;
import com.github.butterknifelib.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindString
    String mAppName;

    @BindString
    String appName;

    @BindString(R.string.app_name)
    String app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.e("MainActivity", "mAppName="+mAppName+" appName="+appName+" app_name="+app_name);
    }
}
