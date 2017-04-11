package com.example.practice.app.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.example.practice.R;
import com.example.practice.app.home.contacts.AddressBookFragment;
import com.example.practice.doman.Account;
import com.example.practice.doman.Message;
import com.example.practice.app.home.sessionrecord.SessionRecordFragment;
import com.example.practice.fragment.MeFragment;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCToast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMOBBS on 2016/11/4.
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rb_message)
    RadioButton message;
    @BindView(R.id.rb_addressbook)
    RadioButton addressbook;
    @BindView(R.id.rb_setting)
    RadioButton setting;

    @BindView(R.id.containerMenu)
    FrameLayout containerMenu;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    //通讯录
    private  AddressBookFragment addressBookFragment;
    //会话记录
    private SessionRecordFragment sessionRecordFragment;
    //设置
    private MeFragment meFragment;
    private String account;
    private String nickname;
    private Intent intent;
    private ServiceConnection mConnection;
    private ReceiveService.sendBinder sendMsg;
    private MenuFragment menuFragment;
    private HomeFragment homeFragment;

    //布局管理器
    private FragmentManager fManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        fManager = getSupportFragmentManager();

        String settTag = String.valueOf(R.id.containerMenu);
        if (savedInstanceState != null) {
            menuFragment = (MenuFragment) fManager.findFragmentByTag(settTag);
        }
        if (homeFragment == null) {
            menuFragment = new MenuFragment();
            fManager.beginTransaction()
                    .add(R.id.containerMenu, menuFragment, settTag)
                    .commit();
        }
        clickMenu(addressbook);
        //获取当前登录的账号和昵称
        account = SpUtils.getString(getApplicationContext(), Constant.LOGIN_ACCOUNT, "");
        nickname = SpUtils.getString(getApplicationContext(), Constant.LOGIN_NICKNAME, "");

        //绑定服务
        intent = new Intent(this, ReceiveService.class);
        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sendMsg = (ReceiveService.sendBinder) service;
                //初始化数据
                getFriendsData();
                //获取会话记录
                getSessionRecord();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    private void getSessionRecord() {
        Account acc = new Account(account, null, nickname, 0);
        Message msg = new Message(Constant.CMD_SESSIONRECORD, acc, null, null, new Date(), Constant.CHAT);
        sendMsg.sendMessage(msg);
    }

    /**
     * 初始化Listview布局，添加好友数据
     */
    private void getFriendsData() {
        Account acc = new Account(account, null, nickname, 0);
        Message msg = new Message(Constant.CMD_GETFRIEND_INFO, acc, null, null, new Date(), Constant.CHAT);
        sendMsg.sendMessage(msg);
    }

    /**
     * 点击底部菜单事件
     * @param v
     */
    public void clickMenu(View v){
        FragmentTransaction trans = fManager.beginTransaction();
        int vID = v.getId();
        // 隐藏所有的fragment
        hideFrament(trans);
        // 设置Fragment
        setFragment(vID,trans);
        trans.commit();
    }
    /**
     * 隐藏所有的fragment(编程初始化状态)
     * @param trans
     */
    private void hideFrament(FragmentTransaction trans) {
        if(sessionRecordFragment!=null){
            trans.hide(sessionRecordFragment);
        }
        if(addressBookFragment!=null){
            trans.hide(addressBookFragment);
        }
        if(meFragment!=null){
            trans.hide(meFragment);
        }
    }
    /**
     * 设置Fragment
     * @param vID
     * @param trans
     */
    private void setFragment(int vID, FragmentTransaction trans) {
        switch (vID){
            case R.id.rb_message:
                if (sessionRecordFragment==null){
                    sessionRecordFragment=new SessionRecordFragment();
                    trans.add(R.id.containerMain,sessionRecordFragment);
                }
                else {
                    trans.show(sessionRecordFragment);
                }
                break;
            case R.id.rb_addressbook:
                if (addressBookFragment==null){
                    addressBookFragment=new AddressBookFragment();
                    trans.add(R.id.containerMain,addressBookFragment);
                }
                else {
                    trans.show(addressBookFragment);
                }
                break;
            case R.id.rb_setting:
                if (meFragment==null){
                    meFragment=new MeFragment();
                    trans.add(R.id.containerMain,meFragment);
                }
                else {
                    trans.show(meFragment);
                }
                break;
        }
    }
    /*
      *点击back事件
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            exitBy2Click();
        }
    }
    private long lastClickTime;
    private void exitBy2Click() {
        long now = System.currentTimeMillis();
        if (now - lastClickTime > 2_000) {
            lastClickTime = now;
            MCToast.show(R.string.common_exit_app, this);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(mConnection);
        //localBroadcastManager.unregisterReceiver(mReceiver);
    }
}
