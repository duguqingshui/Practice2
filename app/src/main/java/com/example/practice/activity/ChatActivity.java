package com.example.practice.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.doman.Account;
import com.example.practice.doman.Message;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.HttpUtils;
import com.example.practice.utils.SpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

/**
 * Created by AMOBBS on 2016/11/17.
 */
public class ChatActivity extends AppCompatActivity {
    /**
     * 上传路径
     */
    private static String requestURL = "http://192.168.0.121:8080/FileTest/servlet/UploadServlet";

    /**
     *下载路径
     */
    private static String downURL = "http://192.168.0.121:8080/FileTest";

    private RadioButton rb_picture;
    private RadioButton rb_record;
    private RadioButton rb_camera;
    private RadioGroup rg_group;

    private ListView lv_message;
    private ImageView iv_add;
    private EditText et_message;
    private Button bt_send,bt_record;
    private String nickname;
    private String loginNickname;
    private MyBroadcastReceiver mReceiver;
    private Intent intent;
    private String chatRecord;
    private MyAdapter mAdapter;
    private ServiceConnection mConnection;
    public ReceiveService.sendBinder sendMsg;
    private LocalBroadcastManager localBroadcastManager;
    private List<Message> msgList;
    private String account;
    private boolean isShowOrNot = false;
    private int headimg,friend_headimg;
    //录音
    private MediaPlayer mPlay = null;
    private MediaRecorder mRecorder = null;
    private String FileRecordName = "";
    private static final String LOG_TAG = "AudioRecordTest";
    private File fileRecordName;
    //拍照
    private String fileDownLoadUrl;
    private String fileName;
    private String picPath;
    private String picName;
    private String picDownLoadUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);}

        //初始化数据
        if (getSupportActionBar() != null) {
            //显示系统的返回键
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        lv_message = (ListView) findViewById(R.id.lv_message);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        et_message = (EditText) findViewById(R.id.et_message);
        bt_send = (Button) findViewById(R.id.bt_send);

        rg_group = (RadioGroup) findViewById(R.id.rg_group);
        rb_record=(RadioButton)findViewById(R.id.rb_record);
        rb_camera=(RadioButton)findViewById(R.id.rb_camera);
        rb_picture=(RadioButton)findViewById(R.id.rb_picture);

        bt_record = (Button) findViewById(R.id.bt_record);//开始录音按钮
        //将标题设置为好友的昵称
        nickname = SpUtils.getString(this, Constant.CHAT_NICKNAME, "");
        setTitle(nickname);

        //获取当前登录用户的昵称
        loginNickname = SpUtils.getString(this, Constant.LOGIN_NICKNAME, "");
        Log.i("当前登录用户的昵称", loginNickname+",好友昵称 ："+nickname);

        //获取当前登录用户的账号
        account = SpUtils.getString(this, Constant.LOGIN_ACCOUNT, "");

        //绑定服务
        intent = new Intent(this, ReceiveService.class);
        mConnection = new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sendMsg = (ReceiveService.sendBinder) service;
                //获取聊天记录
                getChatRecord(loginNickname, nickname);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, mConnection, BIND_AUTO_CREATE);

        //点击发送消息
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //向服务器发送消息
                String msg = et_message.getText().toString();
                if(!TextUtils.isEmpty(msg)){
                    Account loginAcc = new Account(account, null, loginNickname, 0);
                    Account friendAcc = new Account(null, null, nickname, 0);
                    Message message = new Message(Constant.CMD_SENDMSG, loginAcc, friendAcc, msg, new Date(),Message.SEND_TYPE_TXT);
                    sendMsg.sendMessage(message);
                    //更新聊天界面
                    msgList.add(message);
                    mAdapter.notifyDataSetChanged();
                    lv_message.setSelection(msgList.size() - 1);
                }else {
                    Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
                }
                //清空输入框中的内容
                et_message.setText("");
            }
        });
        //点击左侧添加图标
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowOrNot == false) {
                    rg_group.setVisibility(View.VISIBLE); // 设置显示
                    isShowOrNot = true;
                } else {
                    rg_group.setVisibility(View.GONE); // 设置隐藏
                    isShowOrNot = false;
                }
            }
        });
        //点击录音功能
        rb_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowOrNot == false) {
                    rg_group.setVisibility(View.VISIBLE);
                    bt_record.setVisibility(View.GONE);
                    isShowOrNot = true;
                } else {
                    rg_group.setVisibility(View.VISIBLE);
                    bt_record.setVisibility(View.VISIBLE);
                    isShowOrNot = false;
                }
            }
        });
        bt_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
                //设置sdcard路径

                if (action==MotionEvent.ACTION_DOWN){
                    try {
                        FileRecordName = new Date().getTime() + ".3gpp";
                        System.out.println("录音文件1："+FileRecordName);
                        fileRecordName = new File(Environment.getExternalStorageDirectory().getCanonicalFile() +
                                "/" + FileRecordName);
                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                        mRecorder.setOutputFile(fileRecordName.getAbsolutePath());
                        mRecorder.prepare();
                        mRecorder.start();//开始刻录
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                } else if (action == MotionEvent.ACTION_UP) {//松开
                    if(fileRecordName != null && fileRecordName.exists()){
                        mRecorder.stop();//停止刻录
                        mRecorder.release();
                        mRecorder = null;
                        Log.i("录音结束-----------", "end");
                    }


                    //将录音文件上传到服务器
                    //String url = "http://"+Constant.SERVER_IP_ADDRESS+":8080/FileTest/servlet/UploadServlet?name="+ FileRecordName;
                    String   fileDownLoadUrl = "http://"+ Constant.SERVER_IP_ADDRESS+":8080/FileTest/"+ FileRecordName;
                    System.out.println("录音文件2："+FileRecordName);
                    HttpUtils.uploadFile(fileRecordName, requestURL);

                    Account loginAcc = new Account(account, null, loginNickname, 0);
                    Account friendAcc = new Account(null, null, nickname, 0);
                    Message message = new Message(Constant.CMD_SENDMSG, loginAcc, friendAcc, fileDownLoadUrl, new Date(), 1);

                    sendMsg.sendMessage(message);
                    //更新聊天界面
                    msgList.add(message);
                    mAdapter.notifyDataSetChanged();
                    lv_message.setSelection(msgList.size() - 1);
                }
                return false;
            }
        });
        //点击拍照功能
        rb_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取系统公用的图像文件路径
                try {
                    picName = "p" + new Date().getTime() + ".jpg";
                    picPath = Environment.getExternalStorageDirectory().getCanonicalFile() + "/" + picName;
                    Log.i("存放图片的路径", picPath);
                    Uri imgUri = Uri.parse("file://" + picPath);
                    //Uri imgUri = Uri.fromFile(new File(picPath));
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(intent, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //点击图片
        rb_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
    }

    /**
     * 加载聊天记录
     * @param loginNickname 当前登陆的昵称
     * @param nickname      好友的昵称
     */
    private void getChatRecord(String loginNickname, String nickname) {
        Account loginAcc = new Account(account, null, loginNickname, 0);
        Account friendAcc = new Account(null, null, nickname, 0);
        Message message = new Message(Constant.CMD_GETCHATINFO, loginAcc, friendAcc, null, new Date(), Constant.CHAT);
        sendMsg.sendMessage(message);
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

//    @Override
//    protected void onStop() {
//        super.onStop();
//        //结束广播
//        localBroadcastManager.unregisterReceiver(mReceiver);
//    }


    @Override
    protected void onPause() {
        super.onPause();
        //结束广播
        localBroadcastManager.unregisterReceiver(mReceiver);
    }

    /**
     * 加载listview，显示聊天记录
     */
    class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return msgList.size();
        }

        @Override
        public Object getItem(int position) {
            return msgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Message message= (Message) getItem(position);
            headimg=SpUtils.getInt(getApplicationContext(),"headimg",0);//发送者头像
            friend_headimg=SpUtils.getInt(getApplicationContext(),"friend_headimg",0);//接受者头像
            final ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(getApplicationContext(), R.layout.chat_content_item, null);
                holder = new ViewHolder();
                holder.ll_left = (LinearLayout) convertView.findViewById(R.id.ll_left);
                holder.ll_right = (LinearLayout) convertView.findViewById(R.id.ll_right);
                holder.tv_left = (TextView) convertView.findViewById(R.id.tv_left);
                holder.tv_right = (TextView) convertView.findViewById(R.id.tv_right);
                holder.iv_left_img= (ImageView) convertView.findViewById(R.id.iv_left_img);
                holder.iv_left_record= (ImageView) convertView.findViewById(R.id.iv_left_record);
                holder.iv_right_img= (ImageView) convertView.findViewById(R.id.iv_right_img);
                holder.iv_right_record= (ImageView) convertView.findViewById(R.id.iv_right_record);
                holder.iv_left= (ImageView) convertView.findViewById(R.id.iv_left);
                holder.iv_right= (ImageView) convertView.findViewById(R.id.iv_right);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            //设置数据
            holder.iv_left.setImageResource(friend_headimg);
            holder.iv_right.setImageResource(headimg);
            //接受者
            if(msgList.get(position).getReceicer().getNickname().equals(loginNickname)){
                holder.ll_left.setVisibility(View.VISIBLE);
                holder.ll_right.setVisibility(View.GONE);
                switch (message.getType()){
                    //文本消息
                    case 0:
                        holder.tv_left.setVisibility(View.VISIBLE);
                        holder.iv_left_img.setVisibility(View.GONE);
                        holder.iv_left_record.setVisibility(View.GONE);
                        holder.tv_left.setText(msgList.get(position).getContent());
                        break;
                    //表示接受录音
                    case 1:
                        holder.tv_left.setVisibility(View.GONE);
                        holder.iv_left_img.setVisibility(View.GONE);
                        holder.iv_left_record.setVisibility(View.VISIBLE);

                        holder.iv_left_record.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPlay = new MediaPlayer();
                                //播放动画
                                AnimationDrawable aDrawable;
                                holder.iv_left_record.setBackgroundResource(R.drawable.receive_horn);
                                aDrawable=(AnimationDrawable)holder.iv_left_record.getBackground();
                                if (isShowOrNot==false){
                                    aDrawable.start();
                                    isShowOrNot=true;
                                }
                                else {
                                    aDrawable.stop();
                                    isShowOrNot=false;
                                }
                                try{
                                    String filename = msgList.get(position).getContent().split("/")[4];
                                    String path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+filename;
                                    File file = new File(path);
                                    mPlay.setDataSource(filename);
                                    mPlay.prepare();
                                    mPlay.start();
                                } catch (IOException e) {
                                    Toast.makeText(getApplicationContext(),"播放失败",Toast.LENGTH_LONG);
                                }
                            }
                        });
                        break;
                    //表示接受图片
                    case 2:
                        holder.tv_left.setVisibility(View.GONE);
                        holder.iv_left_img.setVisibility(View.VISIBLE);
                        holder.iv_left_record.setVisibility(View.GONE);


                        try {
                            String filename = msgList.get(position).getContent().split("/")[4];
                            String path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+filename;
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            holder.iv_left_img.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            //发送者
            else {
                holder.ll_left.setVisibility(View.GONE);
                holder.ll_right.setVisibility(View.VISIBLE);
                switch (message.getType()){
                    case 0:
                        holder.tv_right.setVisibility(View.VISIBLE);
                        holder.iv_right_img.setVisibility(View.GONE);
                        holder.iv_right_record.setVisibility(View.GONE);
                        holder.tv_right.setText(msgList.get(position).getContent());
                        break;
                    //表示发送录音
                    case 1:
                        holder.tv_right.setVisibility(View.GONE);
                        holder.iv_right_img.setVisibility(View.GONE);
                        holder.iv_right_record.setVisibility(View.VISIBLE);

                        holder.iv_right_record.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPlay = new MediaPlayer();
                                //播放动画
                                AnimationDrawable aDrawable;
                                holder.iv_right_record.setBackgroundResource(R.drawable.anim);
                                aDrawable=(AnimationDrawable)holder.iv_right_record.getBackground();
                                aDrawable.start();

                                try{
                                    String filename = msgList.get(position).getContent().split("/")[4];
                                    String path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+filename;
                                    mPlay.setDataSource(path);
                                    mPlay.prepare();
                                    mPlay.start();
                                } catch (IOException e) {
                                    Toast.makeText(getApplicationContext(),"播放失败",Toast.LENGTH_LONG);
                                }
                            }
                        });
                        break;
                    //表示发送图片
                    case 2:
                        holder.tv_right.setVisibility(View.GONE);
                        holder.iv_right_img.setVisibility(View.VISIBLE);
                        holder.iv_right_record.setVisibility(View.GONE);
                        try {
                            String filename = msgList.get(position).getContent().split("/")[4];
                            String path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+filename;
                            // String  path1=message.getContent();
                            // System.out.println("发送图片路径："+path1);
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            holder.iv_right_img.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            //将listview定位到最后一行
            lv_message.setSelection(msgList.size());
            return convertView;
        }
    }

    class ViewHolder{
        LinearLayout ll_left;
        LinearLayout ll_right;
        TextView tv_left;
        TextView tv_right;
        ImageView iv_left_img;
        ImageView iv_left_record;
        ImageView iv_right_img;
        ImageView iv_right_record;
        ImageView iv_right;
        ImageView iv_left;
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
                //结束当前页面
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

        @Override
        public void onReceive(Context context, Intent intent) {

            String chatInfo = intent.getStringExtra("backMsg");
            Log.i("收到的消息chatActivity", chatInfo);
            if (!TextUtils.isEmpty(chatInfo)) {
                JsonReader reader = new JsonReader(new StringReader(chatInfo));
                reader.setLenient(true);
                msgList = new Gson().fromJson(reader, new TypeToken<List<Message>>(){}.getType());
                for(final Message message : msgList){
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
                mAdapter = new MyAdapter();
                lv_message.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                lv_message.setSelection(msgList.size() - 1);
            } else {
                Toast.makeText(getApplicationContext(), "赶快和他/她聊天吧", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //上传图片到Tomcat
                    String url = "http://"+Constant.SERVER_IP_ADDRESS+":8080/LoadServlet/UploadServlet?name="+picName;
                    picDownLoadUrl = "http://"+ Constant.SERVER_IP_ADDRESS+":8080/LoadServlet/" + picName;
                    HttpUtils.uploadFile(new File(picPath), url);

                    //向服务器发送消息
                    Account loginAcc = new Account(account, null, loginNickname, 0);
                    Account friendAcc = new Account(null, null, nickname, 0);
                    Message message = new Message(Constant.CMD_SENDMSG, loginAcc, friendAcc, picDownLoadUrl, new Date(), 2);
                    sendMsg.sendMessage(message);
                    //更新聊天界面
                    msgList.add(message);
                    mAdapter.notifyDataSetChanged();
                    lv_message.setSelection(msgList.size() - 1);
                    break;
                case 2:
                    Uri uri = data.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, columns, null, null, null);
                    if (cursor.moveToFirst()) {
                        String imagePath1 = cursor.getString(0);
                        SpUtils.putString(getApplicationContext(),"picPath",imagePath1);
                        System.out.println("文件：" + imagePath1);
                        String picname=getFileName(imagePath1);
                        SpUtils.putString(getApplicationContext(), Constant.PIC_PATH, picname);
                        cursor.close();
                    }
                    String picPath=SpUtils.getString(getApplicationContext(),"picPath",null);
                    String imagePath2 = SpUtils.getString(getApplicationContext(), Constant.PIC_PATH, null);

                    String url1 = "http://"+Constant.SERVER_IP_ADDRESS+":8080/FileTest/servlet/UploadServlet?name="+imagePath2;
                    String picDownLoadUrl = "http://"+ Constant.SERVER_IP_ADDRESS+":8080/FileTest/" + imagePath2;
                    HttpUtils.uploadFile(new File(picPath), url1);

                    Account loginAcc1 = new Account(account, null, loginNickname, 0);
                    Account friendAcc1 = new Account(null, null, nickname, 0);
                    Message message1 = new Message(Constant.CMD_SENDMSG, loginAcc1, friendAcc1, picDownLoadUrl, new Date(), 2);
                    sendMsg.sendMessage(message1);
                    //更新聊天界面
                    msgList.add(message1);
                    mAdapter.notifyDataSetChanged();
                    lv_message.setSelection(msgList.size() - 1);
                    break;
            }
        }
    }
    //分割路径 获取文件名
    public String getFileName(String path){
        File tempFile =new File(path.trim());

        String fileName = tempFile.getName();//文件
        String SuffixName = fileName.substring(fileName.lastIndexOf(".")+1);//后缀名
        return fileName;
    }
}
