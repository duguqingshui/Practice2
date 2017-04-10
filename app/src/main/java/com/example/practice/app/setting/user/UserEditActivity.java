package com.example.practice.app.setting.user;

import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.practice.R;
import com.example.practice.app.home.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMOBBS on 2017/2/15.
 */

public class UserEditActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_account)
    TextView user_account;
    @BindView(R.id.user_sex)
    TextView user_sex;
    @BindView(R.id.user_age)
    TextView user_age;
    @BindView(R.id.user_birthday)
    TextView user_birthday;

    @BindView(R.id.edit_name)
    ImageView edit_name;
    @BindView(R.id.edit_sex)
    ImageView edit_sex;
    @BindView(R.id.edit_Birthday)
    ImageView edit_Birthday;
    Date date=new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private String selecttime;//选择的时间
    private String currenttime;//系统时间
    private MenuItem menuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useredit);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.perinfo_title);
        actionBar.show();
        initView();

    }

    private void initView() {
        findViewById(R.id.edit_Birthday).setOnClickListener(this);
        findViewById(R.id.edit_sex).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_Birthday:
                break;
            case R.id.edit_sex:
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit :
                edit_sex.setVisibility(View.VISIBLE);
                edit_name.setVisibility(View.VISIBLE);
                edit_Birthday.setVisibility(View.VISIBLE);
            break;
            case R.id.save :
                edit_sex.setVisibility(View.GONE);
                edit_name.setVisibility(View.GONE);
                edit_Birthday.setVisibility(View.GONE);
                Intent intent = new Intent();
                intent.setClass(UserEditActivity.this, MainActivity.class);
                UserEditActivity.this.startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}
