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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.practice.R;
import com.example.practice.app.home.MainActivity;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.utils.UserEditUtil;
import com.example.practice.view.MyEditView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMOBBS on 2017/2/15.
 */

public class UserEditActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.user_headimage)
    RoundedImageView user_headimage;
    @BindView(R.id.edit_user_name)
    EditText edit_user_name;
    @BindView(R.id.user_account)
    TextView user_account;
    @BindView(R.id.user_sex)
    TextView user_sex;
    @BindView(R.id.user_age)
    TextView user_age;
    @BindView(R.id.user_birthday)
    MyEditView user_birthday;
    @BindView(R.id.edit_user_sign)
    EditText edit_user_sign;
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
        String account=SpUtils.getString(getApplicationContext(), Constant.LOGIN_ACCOUNT, null);
        String nickname=SpUtils.getString(getApplicationContext(), Constant.LOGIN_NICKNAME, null);
        int  img=SpUtils.getInt(getApplicationContext(), Constant.LOGIN_HEADIMAGE, 0);
        int sex= SpUtils.getInt(getApplicationContext(), Constant.LOGIN_SEX, 0);
        String birthday=SpUtils.getString(getApplicationContext(), Constant.LOGIN_BIRTHDAY, null);
        String sign= SpUtils.getString(getApplicationContext(),  Constant.LOGIN_SIGN, null);

        edit_user_name.setText(nickname);
        user_account.setText(account);
        user_headimage.setImageResource(img);
        user_sex.setText(getSex(sex));
        user_birthday.setText(birthday);
        edit_user_sign.setText(sign);
        user_sex.setOnClickListener(this);
        user_birthday.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_birthday:
                UserEditUtil.showDatePicker(this, false, user_birthday);
                break;
            case R.id.user_sex:
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
            case R.id.save :
                Intent intent = new Intent();
                intent.setClass(UserEditActivity.this, MainActivity.class);
                UserEditActivity.this.startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
    public String getSex(int a){
        String sex = null;
        if (a==1){
            sex="女";
        }
        else if (a==0){
            sex="男";
        }
        return sex;
    }
}
