package com.example.practice.app.setting.privateandsafe.changepss;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.practice.R;
import com.example.practice.app.home.MainActivity;
import com.example.practice.app.setting.privateandsafe.gesturelock.CreateGestureLockActivity;
import com.example.practice.app.setting.user.UserEditActivity;
import com.example.practice.doman.Account;
import com.example.practice.doman.Message;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCIntent;
import com.example.practice.view.MCToast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AMOBBS on 2017/4/19.
 */

public class ChangePassActicity extends AppCompatActivity{
    @BindView(R.id.old_pass)
    EditText oldPass;
    @BindView(R.id.new_pass)
    EditText newPass;
    @BindView(R.id.old_clear)
    Button oldClear;
    @BindView(R.id.new_clear)
    Button newClear;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.show_pass)
    CheckBox showPass;

    private Intent intent;
    private ServiceConnection mConnection;
    private ReceiveService.sendBinder sendMsg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    private String account;
    private  boolean isOrNotTrue=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
        setTitle(R.string.change_pass);
        initView();
        intent = new Intent(this, ReceiveService.class);

        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sendMsg = (ReceiveService.sendBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    private void initView() {
        ButterKnife.bind(this);
        account=SpUtils.getString(getApplicationContext(), Constant.LOGIN_ACCOUNT, null);
    }
    @OnClick(R.id.submit)
    public void OnSubmitCLick(){
        String password= SpUtils.getString(getApplicationContext(), Constant.LOGIN_PASSWORD,null);
        String oldpassword=oldPass.getText().toString();
        String newpassword=newPass.getText().toString();
        if (TextUtils.isEmpty(oldpassword)||TextUtils.isEmpty(newpassword)){
            MCToast.show("密码不能为空(⊙o⊙)哦！", ChangePassActicity.this);
        }else {
            if (oldpassword.equals(password)){
                Account acc=new Account(account,oldpassword,null,0);
                Message msg = new Message(Constant.CMD_CHANGE_PASS, acc, null, newpassword, new Date(), Constant.CHAT);
                sendMsg.sendMessage(msg);
                SpUtils.putString(getApplicationContext(), Constant.LOGIN_PASSWORD,newpassword);
                String pass= SpUtils.getString(getApplicationContext(), Constant.LOGIN_PASSWORD,null);
                if (pass.equals(newpassword)){
                    MCToast.show("修改密码成功！", ChangePassActicity.this);
                    MCIntent.sendIntentFromAnimLeft(this, MainActivity.class);
                    finish();
                }
            }else {
                MCToast.show("请输入正确的密码!", ChangePassActicity.this);
            }
        }

    }
    /**
     * 获取后台服务ReceiveService发过来的数据
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receiveMsg = intent.getStringExtra("backMsg");
            Log.i("收到的消息UserEditActivity", receiveMsg);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //注册广播接收器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.practice.app.MyBroadcastReceiver");
        localBroadcastManager.registerReceiver(mReceiver, filter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        //结束广播
        localBroadcastManager.unregisterReceiver(mReceiver);
    }
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(mConnection);
    }
    /**
     * 点击返回按钮，返回上一个页面
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}