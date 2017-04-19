package com.example.practice.app.setting.privateandsafe.gesturelock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.view.MCToast;

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
            tvSetPwd.setText(R.string.set_lock);
        }else {
            tvSetPwd.setText(R.string.input_psw);
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
                    MCToast.show(R.string.before_set_pass, context);
                }else {
                    Intent intent=new Intent(CreateGestureLockActivity.this, ResetPwdActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
