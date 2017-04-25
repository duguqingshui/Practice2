package com.example.practice.app.home.sessionrecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.practice.R;
import com.example.practice.doman.Messages;
import com.example.practice.utils.Constant;
import com.example.practice.utils.HttpUtils;
import com.example.practice.view.swipelistview.BaseSwipeListViewListener;
import com.example.practice.view.swipelistview.SwipeListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by AMOBBS on 2017/2/7.
 */

public class SessionRecordFragment extends Fragment {
    protected static final String TAG = "Activity";
    private SwipeListView mSwipeListView;
    private DataAdapter mAdapter;
    private List<Messages> mDatas=new ArrayList<Messages>();
    private View view;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    TextView title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_chatrecord, container, false);
        title=(TextView)view.findViewById(R.id.tv_title);
        title.setText(R.string.message);
        mSwipeListView = (SwipeListView)view.findViewById(R.id.id_swipelistview);

        mAdapter = new DataAdapter(getContext(), mDatas , mSwipeListView);
        mSwipeListView.setAdapter(mAdapter);

        mSwipeListView.setSwipeListViewListener(new BaseSwipeListViewListener()
        {
            @Override
            public void onChoiceChanged(int position, boolean selected)
            {
                Log.d(TAG, "onChoiceChanged:" + position + ", " + selected);
            }

            @Override
            public void onChoiceEnded()
            {
                Log.d(TAG, "onChoiceEnded");
            }

            @Override
            public void onChoiceStarted()
            {
                Log.d(TAG, "onChoiceStarted");
            }

            @Override
            public void onClickBackView(int position)
            {
                Log.d(TAG, "onClickBackView:" + position);
            }

            @Override
            public void onClickFrontView(int position)
            {
                Log.d(TAG, "onClickFrontView:" + position);
            }

            @Override
            public void onClosed(int position, boolean fromRight)
            {
                Log.d(TAG, "onClosed:" + position + "," + fromRight);
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions)
            {
                Log.d(TAG, "onDismiss");

            }

            @Override
            public void onFirstListItem()
            {
                Log.d(TAG, "onFirstListItem");
            }

            @Override
            public void onLastListItem()
            {
                Log.d(TAG, "onLastListItem");
            }

            @Override
            public void onListChanged()
            {
                Log.d(TAG, "onListChanged");

                mSwipeListView.closeOpenedItems();

            }

            @Override
            public void onMove(int position, float x)
            {
                Log.d(TAG, "onMove:" + position + "," + x);
            }

            @Override
            public void onOpened(int position, boolean toRight)
            {
                Log.d(TAG, "onOpened:" + position + "," + toRight);
            }

            @Override
            public void onStartClose(int position, boolean right)
            {
                Log.d(TAG, "onStartClose:" + position + "," + right);
            }

            @Override
            public void onStartOpen(int position, int action, boolean right)
            {
                Log.d(TAG, "onStartOpen:" + position + "," + action + ","
                        + right);
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        //注册广播接收器
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
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
    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
    //    private void initDatas()
//    {
//        mDatas = new ArrayList<String>();
//        for (int i = 'A'; i <= 'Z'; i++)
//            mDatas.add((char) i + "");
//    }
    /**
     * 获取后台服务ReceiveService发过来的数据
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String chatInfo = intent.getStringExtra("backMsg");
            Log.i("收到的消息chatActivity1", chatInfo);
            if (!TextUtils.isEmpty(chatInfo)) {
                JsonReader reader = new JsonReader(new StringReader(chatInfo));
                reader.setLenient(true);
                mDatas = new Gson().fromJson(reader, new TypeToken<List<Messages>>(){}.getType());
                for(final Messages message : mDatas){
                    if(message.getType() != Constant.CHAT){
                        //如果消息类型不是语音
                        //eg--http://192.168.0.109:8080/LoadServlet/r1481187782643.amr
                        //eg--http://192.168.0.109:8080/LoadServlet/p1481269307460.jpg
                        try {
                            String name = message.getContent().split("/")[4];
                            File saveFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+name);
                            if(!saveFile.exists() || saveFile == null){
                                HttpUtils.downLodaFile(message.getContent(), saveFile);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mAdapter = new DataAdapter(getActivity(),mDatas,mSwipeListView);
                mSwipeListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mSwipeListView.setSelection(mDatas.size() - 1);
            } else {
                Toast.makeText(getActivity(), "会话记录获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
