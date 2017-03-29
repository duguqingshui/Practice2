package com.example.practice.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import com.example.practice.app.MainActivity;
import com.example.practice.doman.Account;
import com.example.practice.doman.Message;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

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
    private MyBaseAdapter mAdapter;
    private  int  headimg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    private Intent intent;
    private ServiceConnection mConnection;
    private ReceiveService.sendBinder sendMsg;
    private List<Account> list;
    private  boolean isOrNot=false;
    private ArrayList<Account> mOnlineList;//在线集合
    private ArrayList<Account> mUnonlineList;//离线集合
    public Integer[] mThumbIds={//显示的图片数组
            R.mipmap.ig1, R.mipmap.camera, R.mipmap.folder, R.mipmap.ic_launcher, R.mipmap.music, R.mipmap.picture, R.mipmap.video,
            R.mipmap.i3, R.mipmap.i4,R.mipmap.i5,R.mipmap.i6,R.mipmap.i7, R.mipmap.i8
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_addressbook, container, false);
        initView();
        return view;
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



        //点击头像所在布局，弹出对话框，可以修改昵称信息
        ll_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog();
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
        filter.addAction("com.practice.activity.MyBroadcastReceiver");
        localBroadcastManager.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        //结束广播
        localBroadcastManager.unregisterReceiver(mReceiver);
    }



    public class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(getContext().getApplicationContext(), R.layout.activity_friend, null);
                holder = new ViewHolder();
                holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                holder.iv_photo= (ImageView) convertView.findViewById(R.id.iv_photo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String name = list.get(position).getNickname();
            int state = list.get(position).getState();
            int headimg=list.get(position).getHeadimg();
            holder.iv_photo.setImageResource(headimg);

            holder.tv_nickname.setText("昵称： " + name);
            if (state == 1) {
                holder.tv_state.setText("状态： onLine");
                holder.tv_nickname.setTextColor(Color.RED);
                holder.tv_state.setTextColor(Color.RED);
            } else {
                holder.tv_state.setText("状态： offLine");
                holder.tv_nickname.setTextColor(Color.GRAY);
                holder.tv_state.setTextColor(Color.GRAY);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_nickname;
        TextView tv_state;
        ImageView iv_photo;
    }

    /**
     * 修改昵称的对话框
     */
    private void infoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(getContext(), R.layout.modify_info_dialog, null);
        dialog.setView(view);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginNickname = SpUtils.getString(getContext().getApplicationContext(), Constant.LOGIN_NICKNAME, "");
                EditText et_name = (EditText) view.findViewById(R.id.et_name);
                String modifyNickname = et_name.getText().toString();//修改后的昵称

                Account acc = new Account(null, null, loginNickname, 0);
                Message msg = new Message(Constant.CMD_NOTIFY_NAME, acc, null, modifyNickname, new Date(), Constant.CHAT);
                sendMsg.sendMessage(msg);
                SpUtils.putString(getContext().getApplicationContext(), Constant.LOGIN_NICKNAME, modifyNickname);
                tv_nickname.setText(modifyNickname);
                dialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    /**
     * 退出对话框
     * @return
     */
    private void exitDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        final AlertDialog dialog=builder.create();
        final View  view=View.inflate(getContext(), R.layout.exit_dialog_layout, null);
        dialog.setView(view);
        dialog.show();

        final Button bt_submit=(Button)view. findViewById(R.id.bt_submit);
        Button bt_cancel=(Button) view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginNickname = SpUtils.getString(getContext().getApplicationContext(), Constant.LOGIN_NICKNAME, "");
                Account acc = new Account(null, null, loginNickname, 1);
                Message msg = new Message(Constant.CMD_EXIT, acc, null, loginNickname, new Date(),Constant.CHAT);
                //调用服务的方法登录账号
                sendMsg.sendMessage(msg);
                Intent it=new Intent(getContext().getApplicationContext(), LoginActivity.class);
                startActivity(it);
                dialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
            mAdapter = new MyBaseAdapter();
            lv_friends.setAdapter(mAdapter);
            tv_friendscount.setText("好友人数 :" + list.size());
            int count = mOnlineList.size() + 1;
            tv_onlinecount.setText("在线人数 ：" + count);
        }
    }


}
