package com.example.practice.view;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.practice.R;


public class MyNumberPicker extends NumberPicker {

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child){
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index,
            android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        View v=this.getChildAt(1);
        updateView(v);
    }

    public void updateView(View view) {
        if (view instanceof EditText)  {
            ((EditText) view).setTextColor(Color.BLACK);
            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.size22));
            ((EditText) view).setEnabled(false);
            ((EditText) view).setCursorVisible(false);
            ((EditText) view).setInputType(InputType.TYPE_NULL);
            ((EditText) view).setFocusableInTouchMode(false);
        }
    }

}    
