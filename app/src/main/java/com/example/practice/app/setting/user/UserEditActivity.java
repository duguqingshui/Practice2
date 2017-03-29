package com.example.practice.app.setting.user;

import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.practice.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AMOBBS on 2017/2/15.
 */

public class UserEditActivity extends AppCompatActivity implements View.OnClickListener {
    TextView user_birthday,user_sex,user_age;
    Date date=new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private String selecttime;//选择的时间
    private String currenttime;//系统时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useredit);
        getSupportActionBar().show();
        setTitle(R.string.perinfo_title);
        initView();

    }

    private void initView() {
        user_sex= (TextView) findViewById(R.id.user_sex);
        user_birthday= (TextView) findViewById(R.id.user_birthday);
        user_age= (TextView) findViewById(R.id.user_age);
        findViewById(R.id.bt_edit_Birthday).setOnClickListener(this);
        findViewById(R.id.bt_edit_sex).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit_Birthday:
                break;
            case R.id.bt_edit_sex:
                showSexDialog();
                break;
        }
    }


    public String getDateToString(long time) {
        Date d = new Date(time);
        selecttime=df.format(d);
        currenttime=df.format(date);
        if (compareTime(selecttime,currenttime)<=0){
            return selecttime;
        }
        else {
            Toast.makeText(getApplicationContext(),"不能选择将来时间", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void showSexDialog() {
        final String[] sex=getResources().getStringArray(R.array.sex);
        Builder builder= new Builder(UserEditActivity.this);
        builder.setItems(sex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user_sex.setText(sex[which]);
            }
        }).show();
    }
    private int  compareTime(String selecttime, String currenttime){
        Calendar c1= Calendar.getInstance();
        Calendar c2= Calendar.getInstance();
        try
        {
            c1.setTime(df.parse(selecttime));
            c2.setTime(df.parse(currenttime));
        }catch(java.text.ParseException e){
            System.err.println("格式不正确");
        }
        int result=c1.compareTo(c2);
        if(result==0)
            System.out.println("c1相等c2");
        else if(result<0)
            System.out.println("c1小于c2");
        else
            System.out.println("c1大于c2");
        return  result;
    }
}
