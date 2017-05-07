package com.example.practice.app.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.example.practice.R;
import com.example.practice.app.home.contacts.AddressBookFragment;
import com.example.practice.doman.Account;
import com.example.practice.app.home.sessionrecord.SessionRecordFragment;
import com.example.practice.doman.Messages;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCToast;
import com.nineoldandroids.view.ViewHelper;

import java.text.SimpleDateFormat;
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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date date = new Date();
    String todayTime = sdf.format(date);
    //布局管理器
    private FragmentManager fManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //解决 Honeycomb SDK（3.0）以上的版本 数据发送问题
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        getSupportActionBar().hide();
        fManager = getSupportFragmentManager();
        initView();
        initEvents();
        clickMenu(addressbook);
        //获取当前登录的账号和昵称
        account = SpUtils.getString(getApplicationContext(), Constant.LOGIN_ACCOUNT, null);
        nickname = SpUtils.getString(getApplicationContext(), Constant.LOGIN_NICKNAME, null);

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
        Messages msg = new Messages(Constant.CMD_SESSIONRECORD, acc, null, null, todayTime, Constant.CHAT);
        sendMsg.sendMessage(msg);
    }

    /**
     * 初始化Listview布局，添加好友数据
     */
    private void getFriendsData() {
        Account acc = new Account(account, null, nickname, 0);
        Messages msg = new Messages(Constant.CMD_GETFRIEND_INFO, acc, null, null, todayTime, Constant.CHAT);
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

    @Override
    protected void onResume() {
        super.onResume();
        String state=SpUtils.getString(getApplicationContext(),Constant.LOGIN_ACCOUNT, null);
        if (state.equals("")){
            Account acc = new Account(account, null, nickname, 1);
            Messages msg = new Messages(Constant.CMD_EXIT, acc, null, nickname, todayTime, Constant.CHAT);
            sendMsg.sendMessage(msg);
        }

    }
    private void initEvents()
    {
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerStateChanged(int newState)
            {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                View mContent = drawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT"))
                {

                    float leftScale = 1 - 0.3f * scale;

                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent,
                            mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                } else
                {
                    ViewHelper.setTranslationX(mContent,
                            -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                drawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }

    private void initView()
    {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
    }

}
