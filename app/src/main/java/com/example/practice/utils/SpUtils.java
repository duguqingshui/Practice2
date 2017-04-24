package com.example.practice.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AMOBBS on 2016/11/16.
 */

public class SpUtils {

    private static SharedPreferences sp;

    public static void putString(Context context, String key, String defvalue){
        if(sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, defvalue).commit();
    }

    public static String getString(Context context, String key, String defvalue){
        if(sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defvalue);
    }
    public static void putInt(Context context, String key, int defvalue){
        if(sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, defvalue).commit();
    }

    public static int getInt(Context context, String key, int defvalue){
        if(sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defvalue);
    }
    //写
    /**
     *
     * @param ctx 上下文环境
     * @param key 存储节点名称
     * @param value 存储节点的值 boolean
     */
    public static void putBoolean(Context ctx,String key,boolean value){
        //参数  1  存储节点文件名称, 2读写方式
        if(sp==null){
            sp=ctx.getSharedPreferences("comfig",Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }
    /**
     *
     * @param ctx 上下文环境
     * @param key 存储节点名称
     * @param defValue 没有此存储节点的默认值
     * @return  默认值或者此节点读取的结果
     */
    //读
    public static boolean getBoolean(Context ctx,String key,boolean defValue){
        //参数  1  存储节点文件名称, 2读写方式
        if(sp==null){
            sp=ctx.getSharedPreferences("comfig",Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }
}
