package com.example.practice.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.app.setting.user.UserEditActivity;
import com.example.practice.service.ReceiveService;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMOBBS on 2017/4/28.
 */

public class FriendDetailActivity extends AppCompatActivity{
    @BindView(R.id.friend_img)
    ImageView friend_img;
    @BindView(R.id.friend_name)
    TextView friend_name;
    @BindView(R.id.friend_sex)
    ImageView friend_sex;
    @BindView(R.id.friend_age)
    TextView friend_age;
    @BindView(R.id.friend_account)
    TextView friend_account;
    @BindView(R.id.friend_birthday)
    TextView friend_birthday;
    @BindView(R.id.friend_sign)
    TextView friend_sign;

    private String account,nickname,birthday,sign;
    private int sex,img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frienddetails);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.detail);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
        initView();
    }

    private void initView() {
        account= SpUtils.getString(getApplicationContext(), Constant.CHAT_ACCOUNT, null);
        nickname=SpUtils.getString(getApplicationContext(), Constant.CHAT_NICKNAME, null);
        img=SpUtils.getInt(getApplicationContext(), Constant.FRIEND_HEDDIMG, 0);
        sex= SpUtils.getInt(getApplicationContext(), Constant.FRIEND_SEX, 0);
        birthday=SpUtils.getString(getApplicationContext(), Constant.FRIEND_BIRTHDAY, null);
        sign= SpUtils.getString(getApplicationContext(),  Constant.FRIEND_SIGN, null);

        friend_img.setImageResource(img);
        friend_name.setText(nickname);
        if (sex==0){
            friend_sex.setImageResource(R.drawable.man);
        }
        else {
            friend_sex.setImageResource(R.drawable.woman);
        }
        friend_account.setText(account);
        friend_birthday.setText(birthday);
        friend_sign.setText(sign);
    }
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
}
