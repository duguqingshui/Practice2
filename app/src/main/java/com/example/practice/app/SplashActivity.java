package com.example.practice.app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.practice.R;
import com.example.practice.app.home.MainActivity;
import com.example.practice.app.setting.privateandsafe.gesturelock.GuestureLockActivity;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCIntent;

/**
 * Created by AMOBBS on 2016/11/15.
 */

public class SplashActivity extends AppCompatActivity {
    private FrameLayout fl_root;
    private Button bt_to_login,bt_to_newuser;
    private   String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //隐藏actionBar
        getSupportActionBar().hide();
        //开启服务
        startService(new Intent(this, ReceiveService.class));
        // 初始化UI
        initUI();
        //初始化数据
        initData();
        // 初始化动画
        initAnimation();
    }
    /**
     * 添加淡入的动画效果
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        fl_root.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(!"".equals(account)){
                }else {
                  bt_to_newuser.setVisibility(View.GONE);
                    bt_to_login.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束后跳转到登录界面
                account = SpUtils.getString(getApplicationContext(), Constant.LOGIN_ACCOUNT, "");
                if(!"".equals(account)){
                    MCIntent.sendIntentFromAnimLeft(SplashActivity.this, GuestureLockActivity.class);
                }else {
                    MCIntent.sendIntentFromAnimLeft(SplashActivity.this, LoginActivity.class);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initData() {
        bt_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(SplashActivity.this, LoginActivity.class);
                finish();
            }
        });
        bt_to_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(SplashActivity.this, RegisterActivity.class);
                Intent it=new Intent(SplashActivity.this,RegisterActivity.class);
                finish();
            }
        });
    }

    private void initUI() {
        fl_root = (FrameLayout) findViewById(R.id.fl_root);
        bt_to_login = (Button) findViewById(R.id.bt_to_login);
        bt_to_newuser = (Button) findViewById(R.id.bt_to_newuser);
    }
    /**
     * 返回当前程序版本名
     */
    public  String getAppVersionName() {
        // 1,包管理者对象packageManager
        PackageManager pm = getPackageManager();
        // 2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            // 3,获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 返回版本号
     *
     * @return 非0 则代表获取成功
     */
    private int getVersionCode() {
        // 1,包管理者对象packageManager
        PackageManager pm = getPackageManager();
        // 2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            // 3,获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
