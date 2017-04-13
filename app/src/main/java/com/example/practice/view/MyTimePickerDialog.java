package com.example.practice.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;


import com.example.practice.R;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

public class MyTimePickerDialog {
	
	
	private AlertDialog allMsg;
	private NumberPicker year_pick;
	private NumberPicker month_pick;
	private NumberPicker day_pick;
	private NumberPicker min_pick;
	private NumberPicker hour_pick;
	
	private int color;
	private Context context;
	private Button finishBtn;
	private Button cancelBtn;
	private View allMsgView;

	public MyTimePickerDialog(Context context, int color) {
		this.color=color;
		this.context = context;
		
		initView();
		initListener();
		
	}
	
	public void show(Date date, OnClickListener onOkBtnClick) {
		initValue(date);
		finishBtn.setOnClickListener(onOkBtnClick);		

		final Context context = allMsg.getContext();
		allMsg.show();
		allMsg.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
		allMsg.setContentView(allMsgView);
	}
	
	public void dismiss() {
		allMsg.dismiss();
	}
	
	public Date getSelTime(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year_pick.getValue());
		c.set(Calendar.MONTH, month_pick.getValue()-1);
		c.set(Calendar.DAY_OF_MONTH, day_pick.getValue());
		c.set(Calendar.HOUR_OF_DAY, hour_pick.getValue());
		c.set(Calendar.MINUTE, min_pick.getValue());
		c.set(Calendar.SECOND, 0);
		
	    return  c.getTime();
	}
	
	private void initView(){
		
		allMsgView =LayoutInflater.from(context).inflate(R.layout.my_numberpicker3_dialog, null);  
		allMsg=new AlertDialog.Builder(context).create();
		allMsg.setCanceledOnTouchOutside(false);  
		
		View dialog_colorLin = allMsgView.findViewById(R.id.dialog_color);
		dialog_colorLin.setBackgroundColor(color);
		
		year_pick = (MyNumberPicker) allMsgView.findViewById(R.id.year_pick);  
		month_pick = (MyNumberPicker) allMsgView.findViewById(R.id.month_pick);  
		day_pick = (MyNumberPicker) allMsgView.findViewById(R.id.day_pick);  
		hour_pick = (MyNumberPicker) allMsgView.findViewById(R.id.hour_pick);  
		min_pick = (MyNumberPicker) allMsgView.findViewById(R.id.min_pick);  
		finishBtn = (Button)allMsgView.findViewById(R.id.dialog_finish_btn);
		cancelBtn = (Button)allMsgView.findViewById(R.id.dialog_cancel_btn);
		
		setPickerDividerColor(year_pick);
		setPickerDividerColor(month_pick);
		setPickerDividerColor(day_pick);
		setPickerDividerColor(hour_pick);
		setPickerDividerColor(min_pick);	
	}
	
	private void initValue(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;  
		int day = c.get(Calendar.DAY_OF_MONTH); 
		int hour=c.get(Calendar.HOUR_OF_DAY);
		int min=c.get(Calendar.MINUTE);
		
		year_pick.setMinValue(1900);
		year_pick.setMaxValue(2020);
		year_pick.setValue(year); 
		
		month_pick.setMinValue(1);
		month_pick.setMaxValue(12);
		month_pick.setValue(month); 
		
		day_pick.setMinValue(1);
		day_pick.setMaxValue(31);
		day_pick.setValue(day); 
		
		hour_pick.setMinValue(0);
		hour_pick.setMaxValue(24);
		hour_pick.setValue(hour); 
		
		min_pick.setMinValue(0);
		min_pick.setMaxValue(59);
		min_pick.setValue(min); 
	}
	
	
	
	private void setPickerDividerColor(NumberPicker pick) {
		Field[] pickerFields = NumberPicker.class.getDeclaredFields();
		for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(pick, new ColorDrawable(color));    
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }}
	}

	private void initListener() {
		
		year_pick.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				
				int day=getDaysByYearMonth(arg2,month_pick.getValue());
				day_pick.setMaxValue(day);
			}
		});
		month_pick.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
				
				int day=getDaysByYearMonth(year_pick.getValue(),arg2);
				day_pick.setMaxValue(day);
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				allMsg.dismiss();	
			}
		});
	}
	
	protected int  getDaysByYearMonth(int year, int month) {
		Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);  
        a.roll(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
	}

}
