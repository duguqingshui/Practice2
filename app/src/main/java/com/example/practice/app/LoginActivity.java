package com.example.practice.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.app.home.MainActivity;
import com.example.practice.doman.Account;
import com.example.practice.doman.Messages;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCIntent;
import com.example.practice.view.MCToast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AMOBBS on 2017/4/25.
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.bt_login)
    Button bt_login;
    @BindView(R.id.et_account)
    EditText usernameEdit;
    @BindView(R.id.et_password)
    EditText passwordEdit;
    @BindView(R.id.bt_user_clear)
    Button usernameClearBtn;
    @BindView(R.id.bt_psw_clear)
    Button passwordClearBtn;
    @BindView(R.id.tv_register)
    TextView tv_register;
    private Intent intent;
    private ServiceConnection mConnection;
    private ReceiveService.sendBinder sendMsg;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver mReceiver;
    private String account;
    private String password;
    private Account act=new Account();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date date = new Date();
    String todayTime = sdf.format(date);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //解决 Honeycomb SDK（3.0）以上的版本 数据发送问题
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        setTitle(R.string.login_user);
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

    private void initView() {
        InputWatcher inputWatcher = new InputWatcher(usernameClearBtn, usernameEdit);
        usernameEdit.addTextChangedListener(inputWatcher);
        inputWatcher =new InputWatcher(passwordClearBtn,passwordEdit);
        passwordEdit.addTextChangedListener(inputWatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播接收器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.practice.app.MyBroadcastReceiver");
        localBroadcastManager.registerReceiver(mReceiver, filter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //结束广播
        localBroadcastManager.unregisterReceiver(mReceiver);
    }
    @OnClick(R.id.bt_login)
    public void OnloginClick(){
        if (checkInput()){
            Account acc = new Account(account, password, null, 0);

            Messages msg = new Messages(Constant.CMD_LOGIN, acc, null, null, todayTime, Constant.CHAT);
            //调用服务的方法登录账号
            sendMsg.sendMessage(msg);
        }
    }
    @OnClick(R.id.tv_register)
    public  void OnResgisterClick(){
        MCIntent.sendIntentFromAnimLeft(this, RegisterActivity.class);
    }
    private boolean checkInput() {
        account = usernameEdit.getText().toString();
        if (TextUtils.isEmpty(account)) {
            MCToast.show("请输入账户", this);
            return false;
        }

        password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            MCToast.show("请输入密码", this);
            return false;
        }
        return true;
    }

    /**
     * Edit 内容清空
     */
    public class InputWatcher implements TextWatcher {
        private static final String TAG = "InputWatcher" ;
        private Button mBtnClear;
        private EditText mEtContainer ;

        /**
         *
         * @param btnClear 清空按钮 可以是button的子类
         * @param etContainer edittext
         */
        public InputWatcher(Button btnClear, EditText etContainer) {
            if (btnClear == null || etContainer == null) {
                throw new IllegalArgumentException("请确保btnClear和etContainer不为空");
            }
            this.mBtnClear = btnClear;
            this.mEtContainer = etContainer;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                if (mBtnClear != null) {
                    mBtnClear.setVisibility(View.VISIBLE);
                    mBtnClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mEtContainer != null) {
                                mEtContainer.getText().clear();
                            }
                        }
                    });
                }
            } else {
                if (mBtnClear != null) {
                    mBtnClear.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    /**
     * 获取后台服务ReceiveService发过来的数据
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String receiveMsg = intent.getStringExtra("backMsg");
            Log.i("收到的消息LoginActivity123", receiveMsg);
            String nickname = receiveMsg.split(",")[0];
            if (receiveMsg.split(",").length>5){
                Toast.makeText(getApplicationContext(), receiveMsg.split(",")[5], Toast.LENGTH_SHORT).show();
                if("登录成功".equals(receiveMsg.split(",")[5])){
                    String headinmg = receiveMsg.split(",")[1];
                    int img = Integer.parseInt(headinmg);
                    String loginsex= receiveMsg.split(",")[2];
                    int sex= Integer.parseInt(loginsex);
                    String birthday= receiveMsg.split(",")[3];
                    String sign= receiveMsg.split(",")[4];
                    //记录用户个人信息
                    SpUtils.putString(getApplicationContext(), Constant.LOGIN_ACCOUNT, account);
                    SpUtils.putString(getApplicationContext(), Constant.LOGIN_PASSWORD, password);
                    SpUtils.putString(getApplicationContext(), Constant.LOGIN_NICKNAME, nickname);
                    SpUtils.putInt(getApplicationContext(),  Constant.LOGIN_HEADIMAGE, img);
                    SpUtils.putInt(getApplicationContext(), Constant.LOGIN_SEX, sex);
                    SpUtils.putString(getApplicationContext(), Constant.LOGIN_BIRTHDAY, birthday);
                    SpUtils.putString(getApplicationContext(),  Constant.LOGIN_SIGN, sign);
                    //登陆成功，进入主页面
                    MCIntent.sendIntentFromAnimLeft(LoginActivity.this, MainActivity.class);
                    finish();
                }
            }
            else if (receiveMsg.split(",").length==2){
                Toast.makeText(getApplicationContext(), receiveMsg.split(",")[1], Toast.LENGTH_SHORT).show();
                if("密码错误".equals(receiveMsg.split(",")[1])){
                    passwordEdit.setText("");
                }else if("你还没有账号请注册...".equals(receiveMsg.split(",")[1])){
                    usernameEdit.setText("");
                    passwordEdit.setText("");
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(mConnection);
    }
}
