package com.example.practice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.practice.utils.Constant;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by AMOBBS on 2016/11/17.
 */
public class ReceiveService extends Service {

    public Socket socket;
    public DataInputStream din;
    public DataOutputStream dout;
    public boolean mConnect = false;
    public LocalBroadcastManager localBroadcastManager;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (mConnect) {
                            String backMsg = din.readUTF();
                            Log.i("要返回的消息", backMsg);
                            //发送广播
                            Intent intent = new Intent("com.example.practice.app.MyBroadcastReceiver");
                            intent.putExtra("backMsg", backMsg);
                            localBroadcastManager.sendBroadcast(intent);
                        }
                    } catch (EOFException e){
                        Log.i("What's fuck!!!!", "又炸了");
                        Toast.makeText(getApplicationContext(), "服务器连接中断", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("后台服务", "onBind()");
        return new sendBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("后台服务", "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.i("后台服务", "onCreate()");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(Constant.SERVER_IP_ADDRESS, 8899);
                    din = new DataInputStream(socket.getInputStream());
                    dout = new DataOutputStream(socket.getOutputStream());
                    mConnect = true;

                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.onCreate();
    }

    public class sendBinder extends Binder {

        public void sendMessage(com.example.practice.doman.Messages message) {
            Gson gson = new Gson();
            String json = gson.toJson(message, com.example.practice.doman.Messages.class);
            Log.i("message==========", message.toString());
            Log.i("json=============", json);
            try {
                dout.writeUTF(json);
                dout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConnect = false;
        stopService(new Intent(getApplicationContext(), ReceiveService.class));
        try {
            din.close();
            dout.close();
            socket.close();
            Log.i("后台服务", "onDestroy()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
