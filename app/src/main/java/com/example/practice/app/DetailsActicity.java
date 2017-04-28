package com.example.practice.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMOBBS on 2017/4/27.
 * 详情信息页
 */

public class DetailsActicity extends AppCompatActivity{
    @BindView(R.id.user_img)
    ImageView user_img;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_sex)
    ImageView user_sex;
    @BindView(R.id.user_age)
    TextView user_age;
    @BindView(R.id.user_account)
    TextView user_account;
    @BindView(R.id.user_birthday)
    TextView user_birthday;
    @BindView(R.id.user_sign)
    TextView user_sign;

    private String account,nickname,birthday,sign;
    private int sex,img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.detail);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
        initView();
    }

    private void initView() {
        account= SpUtils.getString(getApplicationContext(), Constant.LOGIN_ACCOUNT, null);
        nickname=SpUtils.getString(getApplicationContext(), Constant.LOGIN_NICKNAME, null);
        img=SpUtils.getInt(getApplicationContext(), Constant.LOGIN_HEADIMAGE, 0);
        sex= SpUtils.getInt(getApplicationContext(), Constant.LOGIN_SEX, 0);
        birthday=SpUtils.getString(getApplicationContext(), Constant.LOGIN_BIRTHDAY, null);
        sign= SpUtils.getString(getApplicationContext(),  Constant.LOGIN_SIGN, null);

        user_img.setImageResource(img);
        user_name.setText(nickname);
        if (sex==0){
            user_sex.setImageResource(R.drawable.man);
        }
        else {
            user_sex.setImageResource(R.drawable.woman);
        }
        user_account.setText(account);
        user_birthday.setText(birthday);
        user_sign.setText(sign);
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
}
