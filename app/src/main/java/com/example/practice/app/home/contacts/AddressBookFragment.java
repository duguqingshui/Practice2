package com.example.practice.app.home.contacts;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.practice.R;
import com.example.practice.adapter.ImageAdapter;
import com.example.practice.app.ChatActivity;
import com.example.practice.app.LoginActivity;
import com.example.practice.doman.Account;
import com.example.practice.doman.Message;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCToast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AMOBBS on 2017/2/7.
 */

public class AddressBookFragment extends Fragment {
    private View view;
    private ImageView iv_headimg;
    private GridView gridview;
    private TextView tv_nickname;
    private TextView tv_onlinecount;
    private TextView tv_friendscount;
    private ListView lv_friends;
    private LinearLayout ll_info;
    private String account;
    private String nickname;
    private AddressBookAdapter mAdapter;
    private  int  headimg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    private List<Account> list;
    private  boolean isOrNot=false;
    private ArrayList<Account> mOnlineList;//在线集合
    private ArrayList<Account> mUnonlineList;//离线集合
    private ReceiveService.sendBinder sendMsg;
    public Integer[] mThumbIds={//显示的图片数组
            R.mipmap.ig1, R.mipmap.camera, R.mipmap.folder, R.mipmap.ic_launcher, R.mipmap.music, R.mipmap.picture, R.mipmap.video,
            R.mipmap.i3, R.mipmap.i4,R.mipmap.i5,R.mipmap.i6,R.mipmap.i7, R.mipmap.i8
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //注册广播接收器
        view = inflater.inflate(R.layout.fragment_addressbook, container, false);
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        ll_info = (LinearLayout)view.findViewById(R.id.ll_info);
        tv_nickname = (TextView)view.findViewById(R.id.tv_nickname);
        tv_onlinecount = (TextView)view.findViewById(R.id.tv_onlinecount);
        tv_friendscount = (TextView)view.findViewById(R.id.tv_friendscount);
        lv_friends = (ListView)view. findViewById(R.id.lv_friends);

        //获取当前登录的账号和昵称
        account = SpUtils.getString(getContext().getApplicationContext(), Constant.LOGIN_ACCOUNT, "");
        nickname = SpUtils.getString(getContext().getApplicationContext(), Constant.LOGIN_NICKNAME, "");
        tv_nickname.setText(nickname);
        tv_nickname.setTextColor(Color.BLUE);
        iv_headimg=(ImageView)view.findViewById(R.id.iv_headimg);

        headimg=SpUtils.getInt(getContext().getApplicationContext(),"headimg",1);
        iv_headimg.setImageResource(headimg);
        System.out.println("发送者头像M"+headimg);
        gridview=(GridView)view.findViewById(R.id.gridview_img);
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
        gridview.setAdapter(new ImageAdapter(getContext()));//调用ImageAdapter.java
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){//监听事件
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getContext().getApplicationContext(), ""+position,Toast.LENGTH_SHORT).show();//显示信息;
                iv_headimg.setBackgroundResource(mThumbIds[position]);
                // SpUtils.putInt(getApplicationContext(),"USER_IMG",mThumbIds[position]);
                gridview.setVisibility(View.GONE);
            }
        });

        //响应listview条目点击事件
        lv_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String friend_nickname = list.get(position).getNickname();
                SpUtils.putString(getContext().getApplicationContext(), Constant.CHAT_NICKNAME, friend_nickname);
                int friend_headimg=list.get(position).getHeadimg();
                SpUtils.putInt(getContext().getApplicationContext(), "friend_headimg", friend_headimg);
                startActivity(new Intent(getContext().getApplicationContext(), ChatActivity.class));
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        //注册广播接收器
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.practice.app.MyBroadcastReceiver");
        localBroadcastManager.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        //结束广播
        localBroadcastManager.unregisterReceiver(mReceiver);
    }
    /**
     * 获取后台服务ReceiveService发过来的数据
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String receiveMsg = intent.getStringExtra("backMsg");
            Gson gson = new Gson();
            list = gson.fromJson(receiveMsg, new TypeToken<List<Account>>(){}.getType());
            mOnlineList = new ArrayList<Account>();
            mUnonlineList = new ArrayList<Account>();
            for (Account acc : list) {
                Log.i("好友信息MainActivity", acc.getNickname() + ":" + acc.getState());
                if (acc.getState() == 1) {//在线
                    mOnlineList.add(acc);
                } else {
                    mUnonlineList.add(acc);
                }
            }
                mAdapter = new AddressBookAdapter(list);
                lv_friends.setAdapter(mAdapter);
                tv_friendscount.setText("好友人数 :" + list.size());
                int count = mOnlineList.size() + 1;
                tv_onlinecount.setText("在线人数 ：" + count);
        }
    }


}