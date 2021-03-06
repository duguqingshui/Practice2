package com.example.practice.app.home.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.example.practice.R;
import com.example.practice.app.ChatActivity;
import com.example.practice.app.DetailsActicity;
import com.example.practice.doman.Account;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCIntent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMOBBS on 2017/2/7.
 */

public class AddressBookFragment extends Fragment {
    private View view;
    private ImageView iv_headimg;
    private TextView tv_nickname;
    private TextView tv_onlinecount;
    private TextView tv_friendscount;
    private TextView user_info;
    private ListView lv_friends;
    private RoundedImageView user_img;
    private LinearLayout ll_info;
    private String account;
    private String nickname;
    private AddressBookAdapter mAdapter;
    private  int  headimg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    private List<Account> list=new ArrayList<Account>();
    private  boolean isOrNot=false;
    private ArrayList<Account> mOnlineList;//在线集合
    private ArrayList<Account> mUnonlineList;//离线集合
    private ReceiveService.sendBinder sendMsg;
    private TextView title;
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
        ButterKnife.bind(getActivity());
        title=(TextView)view.findViewById(R.id.tv_title);
        user_info=(TextView)view.findViewById(R.id.user_info);
        ll_info = (LinearLayout)view.findViewById(R.id.ll_info);
        tv_nickname = (TextView)view.findViewById(R.id.tv_nickname);
        tv_onlinecount = (TextView)view.findViewById(R.id.tv_onlinecount);
        tv_friendscount = (TextView)view.findViewById(R.id.tv_friendscount);
        lv_friends = (ListView)view. findViewById(R.id.lv_friends);
        user_img=(RoundedImageView)view.findViewById(R.id.user_image);
        int img=SpUtils.getInt(getActivity(),Constant.LOGIN_HEADIMAGE,0);
        user_img.setImageResource(img);
        title.setText(R.string.addressBook);

        //获取当前登录的账号和昵称
        account = SpUtils.getString(getContext().getApplicationContext(), Constant.LOGIN_ACCOUNT, "");
        nickname = SpUtils.getString(getContext().getApplicationContext(), Constant.LOGIN_NICKNAME, "");
        tv_nickname.setText(nickname);
        tv_nickname.setTextColor(Color.BLUE);
        iv_headimg=(ImageView)view.findViewById(R.id.iv_headimg);

        headimg=SpUtils.getInt(getContext().getApplicationContext(),Constant.LOGIN_HEADIMAGE,1);
        iv_headimg.setImageResource(headimg);

        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntent(getActivity(),DetailsActicity.class);
            }
        });

        //响应listview条目点击事件
        lv_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String friend_account=list.get(position).getAccount();
                String friend_nickname = list.get(position).getNickname();
                int friend_headimg=list.get(position).getHeadimg();
                int friend_sex=list.get(position).getSex();
                String friend_birthday=list.get(position).getBirthday();
                String friend_sign=list.get(position).getSign();
                SpUtils.putString(getContext().getApplicationContext(), Constant.CHAT_NICKNAME, friend_nickname);
                SpUtils.putString(getContext().getApplicationContext(), Constant.CHAT_ACCOUNT, friend_account);
                SpUtils.putInt(getContext().getApplicationContext(), Constant.FRIEND_HEDDIMG, friend_headimg);
                SpUtils.putInt(getContext().getApplicationContext(), Constant.FRIEND_SEX, friend_sex);
                SpUtils.putString(getContext().getApplicationContext(), Constant.FRIEND_BIRTHDAY, friend_birthday);
                SpUtils.putString(getContext().getApplicationContext(), Constant.FRIEND_SIGN, friend_sign);
                MCIntent.sendIntentFromAnimLeft(getActivity(), ChatActivity.class);
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

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //结束广播
//        localBroadcastManager.unregisterReceiver(mReceiver);
//    }

    @Override
    public void onResume() {
        if (mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    /**
     * 获取后台服务ReceiveService发过来的数据
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String receiveMsg = intent.getStringExtra("backMsg");
            Gson gson = new Gson();
            if (list.size()==0&&list!=null){
                list = gson.fromJson(receiveMsg, new TypeToken<List<Account>>(){}.getType());
            }
            mOnlineList = new ArrayList<Account>();
            mUnonlineList = new ArrayList<Account>();
            if (list!=null){
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
                    mAdapter.notifyDataSetChanged();
                    tv_friendscount.setText("好友人数 :" + list.size());
                    int count = mOnlineList.size() + 1;
                    tv_onlinecount.setText("在线人数 ：" + count);
            }


        }
    }


}
