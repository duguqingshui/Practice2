package com.example.practice.app;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.doman.Account;
import com.example.practice.doman.Messages;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.utils.TimeUtils;
import com.example.practice.view.MCIntent;
import com.example.practice.view.MCToast;
import com.example.practice.view.MyTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AMOBBS on 2016/11/7.
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.ll_account)
    LinearLayout ll_account;
    @BindView(R.id.ll_info)
    LinearLayout ll_info;
    @BindView(R.id.iv_headimg)
    ImageView iv_headimg;
    @BindView(R.id.et_account)
    EditText et_account;
    @BindView(R.id.et_pass)
    EditText et_pass;
    @BindView(R.id.et_repass)
    EditText et_repass;
    @BindView(R.id.et_nickname)
    EditText et_nickname;
    @BindView(R.id.et_sex)
    TextView et_sex;
    @BindView(R.id.et_birthday)
    TextView et_birthday;
    @BindView(R.id.et_sign)
    EditText et_sign;
    @BindView(R.id.bt_register)
    Button bt_register;

    private GridView gridview;

    private Intent intent;
    private ServiceConnection mConnection;
    private ReceiveService.sendBinder sendMsg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    private  boolean isOrNot=false;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date date = new Date();
    String todayTime = sdf.format(date);
    public Integer[] mThumbIds={//显示的图片数组
            R.mipmap.ig1, R.mipmap.camera, R.mipmap.folder, R.mipmap.ic_launcher, R.mipmap.music, R.mipmap.picture, R.mipmap.video,
            R.mipmap.i3, R.mipmap.i4,R.mipmap.i5,R.mipmap.i6,R.mipmap.i7, R.mipmap.i8
    };
    private String account,password,rePassword,nickname,sex,birthday,sign;
    private int headimg;
    private long todayEndTime;
    private long selectTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.register_user);
        if (getSupportActionBar() != null) {
            // 是否显示应用程序标题，默认为true
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            //显示系统的返回键
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
        //绑定服务
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

    /**
     * 初始化UI
     */
    private void initView() {
        ButterKnife.bind(this);
        //初始化所有的时间
        todayEndTime = TimeUtils.getDateEndTimeFromDate(new Date()).getTime();
    }

    @OnClick(R.id.bt_register)
    public void OnRegisterClick(){
        account = et_account.getText().toString();
        password = et_pass.getText().toString();
        rePassword = et_repass.getText().toString();
        headimg=SpUtils.getInt(getApplicationContext(),"USER_IMG",0);
        nickname = et_nickname.getText().toString();
        sex=et_sex.getText().toString();
        birthday=et_birthday.getText().toString();
        sign=et_sign.getText().toString();
        if (TextUtils.isEmpty(account)){
            MCToast.show(R.string.no_null_account,getApplicationContext());
        }
        else if (TextUtils.isEmpty(password)){
            MCToast.show(R.string.no_null_pass,getApplicationContext());
        }
        else if (TextUtils.isEmpty(rePassword)){
            MCToast.show(R.string.no_null_repass,getApplicationContext());
        }
        if (TextUtils.isEmpty(nickname)){
            MCToast.show(R.string.no_null_name,getApplicationContext());
        }
        else if (headimg==0){
            MCToast.show(R.string.upload_img,getApplicationContext());
        }
        else if (TextUtils.isEmpty(sex)){
            MCToast.show(R.string.no_null_sex,getApplicationContext());
        }
        else if (TextUtils.isEmpty(birthday)){
            MCToast.show(R.string.no_null_birthday,getApplicationContext());
        }
        else if (TextUtils.isEmpty(sign)){
            MCToast.show(R.string.no_null_sign,getApplicationContext());
        }
        else {
            if (account.subSequence(0,1)=="0"){
                et_account.setText("");
                System.out.println("账号首字母："+account.subSequence(0,1));
                MCToast.show(R.string.no_0,getApplicationContext());
            }
            else if (account.length()!=8){
                et_account.setText("");
                MCToast.show(R.string.remind_8,getApplicationContext());
            }
            else if (!password.equals(rePassword)){
                MCToast.show(R.string.no_same_pass,getApplicationContext());
            }
            else {
                Account acc = new Account(account, password, nickname, 0,headimg,getSex(sex),birthday,sign);
                //Log.i("信息", acc.getAccount()+":"+acc.getNickname());
                Messages msg = new Messages(Constant.CMD_REGISTER, acc, null, null, todayTime, Constant.CHAT);
                sendMsg.sendMessage(msg);

                //MCIntent.sendIntentFromAnimLeft(this, LoginActivity.class);
            }
        }
    }
    @OnClick(R.id.iv_headimg)
    public void OnHeadimgClick(){
        MCIntent.sendIntent(this,ImgSelectActivity.class);
    }
    @OnClick(R.id.et_sex)
    public void OnSexClick(){
        showSexDialog();
    }
    @OnClick(R.id.et_birthday)
    public void OnBirthdayClick(){
        final MyTimePickerDialog dialog = new MyTimePickerDialog(this, getResources().getColor(R.color.main_color));
        dialog.show(new Date(), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Date startDate = TimeUtils.getDateFromString(TimeUtils.getBirthDay(dialog.getSelTime()));
                selectTime = TimeUtils.getDateStartTimeFromDate(startDate).getTime();
                if (selectTime>todayEndTime){
                    MCToast.show("不能选择将来时间哦", RegisterActivity.this);
                }
                else {
                    dialog.dismiss();
                    et_birthday.setText(TimeUtils.getBirthDay(dialog.getSelTime()));
                }
            }
        });
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

    /**
     * 点击返回按钮，返回上一个页面
     * @param item
     * @return
     */
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
    /**
     * 获取后台服务ReceiveService发过来的数据
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String receiveMsg = intent.getStringExtra("backMsg");
            Log.i("收到的消息RegisterActivity", receiveMsg);
            if("注册成功".equals(receiveMsg)){
                Toast.makeText(getApplicationContext(), receiveMsg, Toast.LENGTH_SHORT).show();
                MCIntent.sendIntentFromAnimLeft(RegisterActivity.this, LoginActivity.class);
                finish();
            }else if("注册失败,账户已存在".equals(receiveMsg)){
                et_account.setText("");
                Toast.makeText(getApplicationContext(), receiveMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public int getSex(String a){
        int  sex =-1;
        if (a.equals("女")){
            sex=1;
        }
        else if (a.equals("男")){
            sex=0;
        }
        return sex;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(mConnection);
    }
    private void showSexDialog() {
        final String[] sex=getResources().getStringArray(R.array.sex);
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setItems(sex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_sex.setText(sex[which]);
            }
        }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //默认头像
        int  img =SpUtils.getInt(getApplicationContext(),"USER_IMG",0);
        if (img ==0){
            SpUtils.putInt(getApplicationContext(),"USER_IMG",mThumbIds[0]);
        }
        else {
            iv_headimg.setImageResource(img);
        }
    }
}
