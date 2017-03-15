package com.example.practice.app.setting.statistics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.practice.R;


/**
 * Created by AMOBBS on 2017/2/28.
 */

public class StatisticalFlowActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statisticalflow);
        getSupportActionBar().show();
        setTitle(R.string.statisticalflow);
    }
}
