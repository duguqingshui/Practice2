package com.example.practice.app;

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
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.doman.Account;
import com.example.practice.doman.Messages;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMOBBS on 2017/4/28.
 */

public class FriendDetailActivity extends AppCompatActivity{
    @BindView(R.id.friend_img)
    ImageView friend_img;
    @BindView(R.id.friend_name)
    TextView friend_name;
    @BindView(R.id.friend_sex)
    ImageView friend_sex;
    @BindView(R.id.friend_age)
    TextView friend_age;
    @BindView(R.id.friend_account)
    TextView friend_account;
    @BindView(R.id.friend_birthday)
    TextView friend_birthday;
    @BindView(R.id.friend_sign)
    TextView friend_sign;

    private String account,nickname,birthday,sign;
    private int sex,img;

    private Intent intent;
    private ServiceConnection mConnection;
    private ReceiveService.sendBinder sendMsg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frienddetails);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.detail);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
        intent = new Intent(this, ReceiveService.class);

        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sendMsg = (ReceiveService.sendBinder) service;
                getFriendsData();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }
    /**
     * 获取指定好友数据
     */
    private void getFriendsData() {
        Account acc = new Account(account, null, nickname, 0);
        Messages msg = new Messages(Constant.CMD_DETAIL_FRIEND, acc, null, null, new Date(), Constant.CHAT);
        sendMsg.sendMessage(msg);
    }
    public String getSex(int a){
        String sex = null;
        if (a==1){
            sex="女";
        }
        else if (a==0){
            sex="男";
        }
        return sex;
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
        private String account,nickname,birthday,sign;
        private int sex,img;
        @Override
        public void onReceive(Context context, Intent intent) {

            String receiveMsg = intent.getStringExtra("backMsg");
            Log.i("FriendDetailActivity", receiveMsg);
            account = receiveMsg.split(",")[0];
            nickname = receiveMsg.split(",")[1];
            String headinmg = receiveMsg.split(",")[2];
            img = Integer.parseInt(headinmg);
            String loginsex= receiveMsg.split(",")[3];
            sex= Integer.parseInt(loginsex);
            birthday= receiveMsg.split(",")[4];
            sign= receiveMsg.split(",")[5];

            friend_img.setImageResource(img);
            friend_name.setText(nickname);
            if (sex==0){
                friend_sex.setImageResource(R.drawable.man);
            }
            else {
                friend_sex.setImageResource(R.drawable.woman);
            }
            friend_account.setText(account);
            friend_birthday.setText(birthday);
            friend_sign.setText(sign);
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
}
