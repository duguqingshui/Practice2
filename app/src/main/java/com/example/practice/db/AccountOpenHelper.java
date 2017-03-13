package com.example.practice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by AMOBBS on 2016/11/4.
 */

public class AccountOpenHelper extends SQLiteOpenHelper {

    private Context mContext;

    //创建数据库
    public AccountOpenHelper(Context context) {
        super(context, "Contacts.db", null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建用户表
        db.execSQL("create table accounts(_id integer primary key autoincrement, " +
                "account text, password text, groupID text)");
        //弹出提示框提示表是否创建成功
        Toast.makeText(mContext, "表创建成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
