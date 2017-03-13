package com.example.practice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.practice.doman.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AMOBBS on 2016/11/4.
 */

public class AccountDao {

    private AccountOpenHelper dbHelper;
    //创建AccountDao的单例模式
    //1、私有化构造方法
    private AccountDao(Context context){
        //创建数据库以及其表结构
        dbHelper = new AccountOpenHelper(context);
    }
    //2、声明一个当前类的对象
    private static AccountDao accountDao = null;
    //3、创建一个静态方法，如果当前类对象是空的，则创建一个新的
    public static AccountDao getInstance(Context context){
        if(accountDao == null){
            accountDao = new AccountDao(context);
        }
        return accountDao;
    }

    //向数据库中添加一个账户
    public void insert(String account, String password, String groupID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account", account);
        values.put("password", password);
        values.put("groupID", groupID);
        db.insert("accounts", null, values);
        //关闭数据库
        db.close();
    }

    //查询数据库中是否包含该用户
    public List<Account> query(String account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<Account> accountList = new ArrayList<Account>();
//        Cursor cursor = db.query("accounts", new String[]{"account", "password"},
//                "account = ?", new String[]{account}, null, null, null);
        Cursor cursor = db.rawQuery("select account,password from accounts", null);
        while (cursor.moveToNext()) {
            Account ab = new Account();
            ab.account = cursor.getString(cursor.getColumnIndex("account"));
            ab.password = cursor.getString(cursor.getColumnIndex("password"));
            accountList.add(ab);
        }
        //关闭数据库
        cursor.close();
        db.close();
        return accountList;
    }

    //删除表中的数据
    public  void delete(String account){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("accounts", "account = ?", new String[]{account});
        db.close();
    }

}
