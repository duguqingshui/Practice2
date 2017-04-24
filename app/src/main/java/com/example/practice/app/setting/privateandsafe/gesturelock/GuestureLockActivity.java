package com.example.practice.app.setting.privateandsafe.gesturelock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.app.LoginActivity;
import com.example.practice.app.home.MainActivity;
import com.example.practice.app.setting.privateandsafe.gesturelock.utils.Variate;
import com.example.practice.app.setting.privateandsafe.gesturelock.view.Drawl;
import com.example.practice.app.setting.privateandsafe.gesturelock.view.GuestureLockView;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCIntent;
import com.example.practice.view.MCToast;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/5/29 0029.
 */
public class GuestureLockActivity extends Activity {
    @BindView(R.id.hint)
    TextView hint;
    Context context;

    private FrameLayout mFrameLayout;
    private GuestureLockView mGuestureLockView;
    private SharedPreferences sp;
    String pwd;
    private boolean gesture_lock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guesture_lock);
        context=this;
        sp=getSharedPreferences("GuestureLockSP",MODE_PRIVATE);

        //从sp中获取保存的密码，判断用户是否已经设置密码
        pwd=sp.getString("pwd",null);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        mFrameLayout= (FrameLayout) findViewById(R.id.framelayout);

        mGuestureLockView=new GuestureLockView(context, new Drawl.GestureCallBack() {
            @Override
            public void checkedSuccess(String password) {

                //首先判断一下用户是否已经设置密码
                if(TextUtils.isEmpty(pwd)){
                    hint.setText(R.string.first_set_pass);
                    //如果为空，代码没有设置密码，需要设置新的密码；
                    // 设置新密码需要设置两遍，防止用户误操作；
                    // 第一遍设置的新密码保存在Variate类的一个变量中，这个变量默认为null
                    if(TextUtils.isEmpty(Variate.PASSWORD)){
                        //如果这个变量为null，第一次将密码保存在Variate.PASSWORD提示再次输入密码，
                        Variate.PASSWORD=password;
                        hint.setText(R.string.again_input_pass);
                        MCToast.show(R.string.again_input_pass, context);
                        // 并且刷新当前页面
                        refresh();
                    }else {
                        //如果Variate.PASSWORD不为空代表是第二次输入新密码，判断两次输入密码是否相同
                        if(password.equals(Variate.PASSWORD)){
                            //如果相同，将密码保存在当地sp中
                            sp.edit().putString("pwd",password).commit();
                            // 进入主页面，点击输入密码，输入设置的密码进入“搏击爱好者”页面
                            hint.setText(R.string.input_new_pass);
                            SpUtils.putBoolean(getApplicationContext(), Constant.GESTURE_SWITCH,true);
                            gesture_lock=SpUtils.getBoolean(getApplicationContext(), Constant.GESTURE_SWITCH, false);
                            System.out.println("手势锁开关状态:"+gesture_lock);
                            MCToast.show(R.string.input_new_pass_remind, context);
                            refresh();
                        }else {
                            //如果两次输入密码不一样，将Variate.PASSWORD设为null,提示密码设置失败
                            Variate.PASSWORD=null;
                            MCToast.show(R.string.set_pass_lose, context);
                            SpUtils.putBoolean(getApplicationContext(), Constant.GESTURE_SWITCH,false);
                            // 跳回主页面需重新设置密码
                            MCIntent.sendIntentFromAnimLeft(GuestureLockActivity.this, CreateGestureLockActivity.class);
                            finish();
                        }
                    }

                }else{
                    //如果已经设置密码，判断输入密码和保存密码是否相同
                    hint.setText(R.string.input_new_pass);
                    if(pwd.equals(password)){
                        //如果相同，密码正确，进入”搏击爱好者“页面
                        MCIntent.sendIntentFromAnimLeft(GuestureLockActivity.this, MainActivity.class);
                        finish();
                    }else {
                        //如果不相同，密码错误，刷新当前activity，需重新输入密码
                        Toast.makeText(context,R.string.pass_error, Toast.LENGTH_SHORT).show();
                        refresh();
                    }
                }
            }

            @Override
            public void checkedFail() {

            }
        });

        mGuestureLockView.setParentView(mFrameLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当前页面关闭时将Variate.PASSWORD设为null；防止用户第二次输入密码的时候退出当前activity
        Variate.PASSWORD=null;
    }

    public void refresh() {
        onCreate(null);
    }
}
