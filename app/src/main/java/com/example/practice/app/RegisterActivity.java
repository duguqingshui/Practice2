package com.example.practice.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.adapter.ImageAdapter;
import com.example.practice.doman.Account;
import com.example.practice.doman.Message;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;

import java.util.Date;

/**
 * Created by AMOBBS on 2016/11/7.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText et_account;
    private EditText et_pass;
    private EditText et_repass;
    private EditText et_nickname;
    private ImageView iv_headimg;
    private GridView gridview;

    private Intent intent;
    private ServiceConnection mConnection;
    private ReceiveService.sendBinder sendMsg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    private  boolean isOrNot=false;
    public Integer[] mThumbIds={//显示的图片数组
            R.mipmap.ig1, R.mipmap.camera, R.mipmap.folder, R.mipmap.ic_launcher, R.mipmap.music, R.mipmap.picture, R.mipmap.video,
            R.mipmap.i3, R.mipmap.i4,R.mipmap.i5,R.mipmap.i6,R.mipmap.i7, R.mipmap.i8
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.register_user);
        if (getSupportActionBar() != null) {
            // 是否显示应用程序标题，默认为true
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            //显示系统的返回键
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        gridview=(GridView)findViewById(R.id.gridview_img);
        iv_headimg = (ImageView) findViewById(R.id.iv_headimg);
        et_account = (EditText) findViewById(R.id.et_account);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_repass = (EditText) findViewById(R.id.et_repass);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        Button bt_register = (Button) findViewById(R.id.bt_register);

        //默认头像
        SpUtils.putInt(getApplicationContext(),"USER_IMG",mThumbIds[0]);
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

        gridview.setAdapter(new ImageAdapter(this));//调用ImageAdapter.java
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){//监听事件
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getApplicationContext(), ""+position,Toast.LENGTH_SHORT).show();//显示信息;
                iv_headimg.setBackgroundResource(mThumbIds[position]);
                SpUtils.putInt(getApplicationContext(),"USER_IMG",mThumbIds[position]);
                System.out.println("发送者头像R"+mThumbIds[position]);
                gridview.setVisibility(View.GONE);
            }
        });
        iv_headimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOrNot==true){
                    gridview.setVisibility(View.VISIBLE);
                    isOrNot=false;
                }else
                {
                    gridview.setVisibility(View.GONE);
                    isOrNot=true;
                }
            }
        });

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String account = et_account.getText().toString();
                String password = et_pass.getText().toString();
                String rePassword = et_repass.getText().toString();
                String nickname = et_nickname.getText().toString();
                int  headimg=SpUtils.getInt(getApplicationContext(),"USER_IMG",0);
                if (!TextUtils.isEmpty(nickname)) {
                    if (password.equals(rePassword)) {
                        Account acc = new Account(account, password, nickname, 0,headimg);
                        //Log.i("信息", acc.getAccount()+":"+acc.getNickname());
                        Message msg = new Message(Constant.CMD_REGISTER, acc, null, null, new Date(), Constant.CHAT);
                        sendMsg.sendMessage(msg);
                    } else {
                        et_repass.setText("");
                        Toast.makeText(getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "昵称不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
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
    protected void onStop() {
        super.onStop();
        //结束广播
        localBroadcastManager.unregisterReceiver(mReceiver);
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

    /**
     * 获取后台服务ReceiveService发过来的数据
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String receiveMsg = intent.getStringExtra("backMsg");
            Log.i("收到的消息RegisterActivity", receiveMsg);
            if("注册成功".equals(receiveMsg)){
                Toast.makeText(getApplicationContext(), receiveMsg, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }else if("注册失败".equals(receiveMsg)){
                Toast.makeText(getApplicationContext(), receiveMsg, Toast.LENGTH_SHORT).show();
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
