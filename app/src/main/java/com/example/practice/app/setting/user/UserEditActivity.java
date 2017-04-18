package com.example.practice.app.setting.user;

import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.example.practice.utils.TimeUtils;
import com.example.practice.utils.UserEditUtil;
import com.example.practice.view.MCToast;
import com.example.practice.view.MyEditView;
import com.example.practice.view.MyTimePickerDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.ParsePosition;
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
    TextView user_birthday;
    @BindView(R.id.edit_user_sign)
    EditText edit_user_sign;
    private long todayEndTime;
    private long selectTime;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    Date date = new Date();
    int todayTime = Integer.parseInt(sdf.format(date));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useredit);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.perinfo_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        //初始化所有的时间
        todayEndTime = TimeUtils.getDateEndTimeFromDate(new Date()).getTime();

        try {
            Date date = sdf.parse(birthday);
            int  selectTime=Integer.parseInt(sdf.format(date));;
            System.out.println("选择时间："+selectTime);
            int age=todayTime-selectTime;
            user_age.setText(age+"岁");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_birthday:
                final MyTimePickerDialog dialog = new MyTimePickerDialog(this, getResources().getColor(R.color.main_color));
                dialog.show(new Date(), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Date startDate = TimeUtils.getDateFromString(TimeUtils.getBirthDay(dialog.getSelTime()));
                        selectTime = TimeUtils.getDateStartTimeFromDate(startDate).getTime();
                            if (selectTime>todayEndTime){
                                MCToast.show("不能选择将来时间哦", UserEditActivity.this);
                            }
                            else {
                                int  selectTime=Integer.parseInt(sdf.format(date));
                                int age=todayTime-selectTime;
                                dialog.dismiss();
                                user_birthday.setText(TimeUtils.getBirthDay(dialog.getSelTime()));
                                user_age.setText(age+"岁");
                            }
                        }
                });
                break;
            case R.id.user_sex:
                showSexDialog();
                break;
        }
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
            case android.R.id.home:
                exitConfirm();
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

    @Override
    public void onBackPressed() {
        exitConfirm();
    }

    private void exitConfirm(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.common_reminder)
                .setMessage(R.string .not_saved_prompt)
                .setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
    }
}
