package com.example.practice.app.setting.accountManage;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.app.LoginActivity;
import com.example.practice.doman.Account;
import com.example.practice.doman.Messages;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCIntent;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AMOBBS on 2017/4/24.
 */

public class AccountManageActivity extends AppCompatActivity{
    @BindView(R.id.item_image)
    RoundedImageView user_headimage;
    @BindView(R.id.name)
    TextView edit_user_name;
    @BindView(R.id.account)
    TextView user_account;
    @BindView(R.id.exitButton)
    TextView exit_login;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date date = new Date();
    String todayTime = sdf.format(date);

    private String account,nickname;
    private int img;
    private Intent intent;
    private ServiceConnection mConnection;
    private ReceiveService.sendBinder sendMsg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountmanage);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.account_manage);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
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
        account= SpUtils.getString(getApplicationContext(), Constant.LOGIN_ACCOUNT, null);
        nickname=SpUtils.getString(getApplicationContext(), Constant.LOGIN_NICKNAME, null);
        img=SpUtils.getInt(getApplicationContext(), Constant.LOGIN_HEADIMAGE, 0);
        edit_user_name.setText(nickname);
        user_account.setText(account);
        user_headimage.setImageResource(img);
    }

    @OnClick(R.id.exitButton)
    public void OnExitLoginClick(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.common_reminder)
                .setMessage(R.string .exit_login_content)
                .setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Account acc = new Account(null, null, nickname, 0);
                        Messages msg = new Messages(Constant.CMD_EXIT, acc, null, nickname, todayTime, Constant.CHAT);
                        sendMsg.sendMessage(msg);
                        SpUtils.putString(getApplicationContext(),Constant.LOGIN_ACCOUNT, "");
                        MCIntent.sendIntentFromAnimLeft(AccountManageActivity.this, LoginActivity.class);
                        finish();
                    }
                })
                .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();

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
