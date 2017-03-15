package com.example.practice.app.setting.privateandsafe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.practice.R;

/**
 * Created by AMOBBS on 2017/2/28.
 */

public class PrivateAndSafe extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privateandsafe);
        getSupportActionBar().show();
        setTitle(R.string.privateandsafe);
    }
}
