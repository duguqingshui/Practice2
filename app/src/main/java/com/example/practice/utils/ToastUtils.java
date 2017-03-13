package com.example.practice.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by AMOBBS on 2016/11/21.
 */

public class ToastUtils {

    private Context context;

    public void makeText(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
