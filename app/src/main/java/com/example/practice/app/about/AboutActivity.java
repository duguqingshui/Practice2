package com.example.practice.app.about;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.practice.BuildConfig;
import com.example.practice.R;


/**
 * Created by AMOBBS on 2017/2/21.
 */

public class AboutActivity extends AppCompatActivity {
    private TextView tv_version_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setContentView(R.layout.activity_about);
        if (getSupportActionBar() != null){
            getSupportActionBar().show();
        }
        setTitle(R.string.common_about);

        initView();
    }
    private void initView() {
        tv_version_number=(TextView)findViewById(R.id.tv_version_number);
        tv_version_number.setText(" 版本号 ：V ");
        tv_version_number.append(BuildConfig.VERSION_NAME);
        if (BuildConfig.DEBUG) {
            tv_version_number.append("");
        }
    }
}
