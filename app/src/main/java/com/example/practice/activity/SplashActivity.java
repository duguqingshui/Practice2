package com.example.practice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.practice.R;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;

/**
 * Created by AMOBBS on 2016/11/15.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //开启服务
        startService(new Intent(this, ReceiveService.class));

        ImageView iv_start = (ImageView) findViewById(R.id.iv_start);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2500);
        aa.setFillAfter(true);

        //开启动画，给图片添加透明度的变化，从0到1
        iv_start.startAnimation(aa);

        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束后跳转到登录界面
                String account = SpUtils.getString(getApplicationContext(), Constant.LOGIN_ACCOUNT, "");
                if(!"".equals(account)){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
