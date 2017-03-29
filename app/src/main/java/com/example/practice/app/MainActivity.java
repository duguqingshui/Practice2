package com.example.practice.app;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.adapter.ImageAdapter;
import com.example.practice.app.home.HomeFragment;
import com.example.practice.app.home.MenuFragment;
import com.example.practice.doman.Account;
import com.example.practice.doman.Message;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.swipelistview.Bind;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AMOBBS on 2016/11/4.
 */
public class MainActivity extends AppCompatActivity {
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
    private MenuFragment menuFragment;
    private HomeFragment homeFragment;
    @Bind(R.id.containerMenu)
    FrameLayout containerMenu;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    //布局管理器
    private FragmentManager fManager;

    public Integer[] mThumbIds={//显示的图片数组
            R.mipmap.ig1, R.mipmap.camera, R.mipmap.folder, R.mipmap.ic_launcher, R.mipmap.music, R.mipmap.picture, R.mipmap.video,
            R.mipmap.i3, R.mipmap.i4,R.mipmap.i5,R.mipmap.i6,R.mipmap.i7, R.mipmap.i8
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化 actionbar
        initActionbar();

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
        ll_info = (LinearLayout) findViewById(R.id.ll_info);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_onlinecount = (TextView) findViewById(R.id.tv_onlinecount);
        tv_friendscount = (TextView) findViewById(R.id.tv_friendscount);
        lv_friends = (ListView) findViewById(R.id.lv_friends);

        //获取当前登录的账号和昵称
        account = SpUtils.getString(getApplicationContext(), Constant.LOGIN_ACCOUNT, "");
        nickname = SpUtils.getString(getApplicationContext(), Constant.LOGIN_NICKNAME, "");
        tv_nickname.setText(nickname);
        tv_nickname.setTextColor(Color.BLUE);
        iv_headimg=(ImageView)findViewById(R.id.iv_headimg);

        headimg=SpUtils.getInt(getApplicationContext(),"headimg",1);
        iv_headimg.setImageResource(headimg);
        System.out.println("发送者头像M"+headimg);
        gridview=(GridView)findViewById(R.id.gridview_img);
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
        gridview.setAdapter(new ImageAdapter(this));//调用ImageAdapter.java
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){//监听事件
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getApplicationContext(), ""+position,Toast.LENGTH_SHORT).show();//显示信息;
                iv_headimg.setBackgroundResource(mThumbIds[position]);
               // SpUtils.putInt(getApplicationContext(),"USER_IMG",mThumbIds[position]);
                gridview.setVisibility(View.GONE);
            }
        });

        //绑定服务
        intent = new Intent(this, ReceiveService.class);
        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sendMsg = (ReceiveService.sendBinder) service;
                //初始化数据
                getFriendsData();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, mConnection, BIND_AUTO_CREATE);

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
                SpUtils.putString(getApplicationContext(), Constant.CHAT_NICKNAME, friend_nickname);
                int friend_headimg=list.get(position).getHeadimg();
                SpUtils.putInt(getApplicationContext(), "friend_headimg", friend_headimg);
                startActivity(new Intent(getApplicationContext(), ChatActivity.class));
            }
        });

    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.login_user);
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

    /**
     * 初始化Listview布局，添加好友数据
     */
    private void getFriendsData() {
        Account acc = new Account(account, null, nickname, 0);
        Message msg = new Message(Constant.CMD_GETFRIEND_INFO, acc, null, null, new Date(), Constant.CHAT);
        sendMsg.sendMessage(msg);
    }

    class MyBaseAdapter extends BaseAdapter {

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
                convertView = View.inflate(getApplicationContext(), R.layout.activity_friend, null);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.modify_info_dialog, null);
        dialog.setView(view);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginNickname = SpUtils.getString(getApplicationContext(), Constant.LOGIN_NICKNAME, "");
                EditText et_name = (EditText) view.findViewById(R.id.et_name);
                String modifyNickname = et_name.getText().toString();//修改后的昵称

                Account acc = new Account(null, null, loginNickname, 0);
                Message msg = new Message(Constant.CMD_NOTIFY_NAME, acc, null, modifyNickname, new Date(), Constant.CHAT);
                sendMsg.sendMessage(msg);
                SpUtils.putString(getApplicationContext(), Constant.LOGIN_NICKNAME, modifyNickname);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final AlertDialog dialog=builder.create();
        final View  view=View.inflate(this, R.layout.exit_dialog_layout, null);
        dialog.setView(view);
        dialog.show();

        final Button bt_submit=(Button)view. findViewById(R.id.bt_submit);
        Button bt_cancel=(Button) view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginNickname = SpUtils.getString(getApplicationContext(), Constant.LOGIN_NICKNAME, "");
                Account acc = new Account(null, null, loginNickname, 1);
                Message msg = new Message(Constant.CMD_EXIT, acc, null, loginNickname, new Date(),Constant.CHAT);
                //调用服务的方法登录账号
                sendMsg.sendMessage(msg);
                Intent it=new Intent(getApplicationContext(), LoginActivity.class);
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
    /*
    *点击back事件
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            exitDialog();
        }
        return false;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(mConnection);
        //localBroadcastManager.unregisterReceiver(mReceiver);
    }
}
