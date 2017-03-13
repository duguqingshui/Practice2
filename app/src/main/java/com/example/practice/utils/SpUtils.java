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
}
