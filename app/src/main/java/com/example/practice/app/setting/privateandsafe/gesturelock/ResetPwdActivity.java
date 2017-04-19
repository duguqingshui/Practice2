package com.example.practice.app.setting.privateandsafe.gesturelock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.app.setting.privateandsafe.gesturelock.utils.ToastUtils;
import com.example.practice.app.setting.privateandsafe.gesturelock.view.Drawl;
import com.example.practice.app.setting.privateandsafe.gesturelock.view.GuestureLockView;


/**
 * Created by Administrator on 2016/5/29 0029.
 */
public class ResetPwdActivity extends Activity {

    Context context;

    private FrameLayout mFrameLayout;
    private GuestureLockView mGuestureLockView;
    private SharedPreferences sp;
    private String pwd;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guesture_lock);

        sp=getSharedPreferences("GuestureLockSP",MODE_PRIVATE);
        context=this;

        initView();
    }

    private void initView() {

        //系统保存的密码
        pwd = sp.getString("pwd",null);

        mTextView= (TextView) findViewById(R.id.textview);
        mTextView.setText("重置密码，请输入密码校验");

        mFrameLayout= (FrameLayout) findViewById(R.id.framelayout);
        mGuestureLockView=new GuestureLockView(context,  new Drawl.GestureCallBack() {
            @Override
            public void checkedSuccess(String password) {
                //password是用户输入的密码，如果两个和系统保存的密码一样直接重置
                if(pwd.equals(password)){
                    sp.edit().putString("pwd","").commit();
                    Intent intent=new Intent(ResetPwdActivity.this, GuestureLockActivity.class);
                    startActivity(intent);
                    ToastUtils.showToast(context,"密码重置成功，请设置新密码");
                    finish();
                }else {
                    ToastUtils.showToast(context,"密码不正确，重置失败");
                    refresh();
                }
            }

            @Override
            public void checkedFail() {

            }
        });

        mGuestureLockView.setParentView(mFrameLayout);
    }

    public void refresh() {
        onCreate(null);
    }
}
