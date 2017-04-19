package com.example.practice.app.setting.privateandsafe.gesturelock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.app.setting.privateandsafe.gesturelock.utils.ToastUtils;

/**
 * Created by AMOBBS on 2017/4/19.
 */

public class CreateGestureLockActivity extends AppCompatActivity{

    private SharedPreferences sp;

    private TextView tvSetPwd;
    private TextView tvResetPwd;
    private String pwd;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gesture);
        context=this;
        sp=getSharedPreferences("GuestureLockSP",MODE_PRIVATE);
        pwd = sp.getString("pwd", null);

        tvSetPwd= (TextView) findViewById(R.id.tv);
        tvResetPwd= (TextView) findViewById(R.id.tv2);

        if(TextUtils.isEmpty(pwd)){
            tvSetPwd.setText("设置密码");
        }else {
            tvSetPwd.setText("输入密码");
        }

        tvSetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreateGestureLockActivity.this,GuestureLockActivity.class);
                startActivity(intent);
                finish();
            }
        });


        tvResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(pwd)){
                    ToastUtils.showToast(context,"请先设置密码");
                }else {
                    Intent intent=new Intent(CreateGestureLockActivity.this, ResetPwdActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
