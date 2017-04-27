package com.example.practice.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.practice.R;

import butterknife.BindView;

/**
 * Created by AMOBBS on 2017/4/27.
 * 详情信息页
 */

public class DetailsActicity extends AppCompatActivity{
    @BindView(R.id.user_account)
    TextView user_account;
    @BindView(R.id.user_sex)
    TextView user_sex;
    @BindView(R.id.user_birthday)
    TextView user_birthday;
    @BindView(R.id.user_sign)
    TextView user_sign;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }
}
