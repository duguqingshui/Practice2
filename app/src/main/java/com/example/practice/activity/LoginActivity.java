package com.example.practice.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.doman.Account;
import com.example.practice.doman.Message;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private EditText et_account;
    private EditText et_password;
    private Button bt_login;
    private Intent intent;
    private ServiceConnection mConnection;
    private ReceiveService.sendBinder sendMsg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);

        //绑定服务
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

        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册广播接收器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.practice.activity.MyBroadcastReceiver");
        localBroadcastManager.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //结束广播
        localBroadcastManager.unregisterReceiver(mReceiver);
    }

    private void initData() {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = et_account.getText().toString();
                password = et_password.getText().toString();
                Account acc = new Account(account, password, null, 0);

                Message msg = new Message(Constant.CMD_LOGIN, acc, null, null, new Date(), Constant.CHAT);
                //调用服务的方法登录账号
                sendMsg.sendMessage(msg);
            }
        });

    }

    /**
     * 获取后台服务ReceiveService发过来的数据
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String receiveMsg = intent.getStringExtra("backMsg");
            Log.i("收到的消息LoginActivity123", receiveMsg);
            String nickname = receiveMsg.split(",")[0];
            Toast.makeText(getApplicationContext(), receiveMsg.split(",")[2], Toast.LENGTH_SHORT).show();

            if("登录成功".equals(receiveMsg.split(",")[2])){
                //记录登录账号、密码、昵称
                String headinmg = receiveMsg.split(",")[1];

                int img = Integer.parseInt(headinmg);
                System.out.println("发送者头像L"+img);
                SpUtils.putString(getApplicationContext(), Constant.LOGIN_ACCOUNT, account);
                SpUtils.putString(getApplicationContext(), Constant.LOGIN_PASSWORD, password);
                SpUtils.putString(getApplicationContext(), Constant.LOGIN_NICKNAME, nickname);
                SpUtils.putInt(getApplicationContext(), "headimg", img);
                //登陆成功，进入主页面
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
            }else if("密码错误".equals(receiveMsg.split(",")[2])){
                et_password.setText("");
            }else if("你还没有账号请注册...".equals(receiveMsg.split(",")[2])){
                et_account.setText("");
                et_password.setText("");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(mConnection);
    }
}
