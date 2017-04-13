package com.example.practice.view.datepicker;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.view.datepicker.adapter.ArrayWheelAdapter;
import com.example.practice.view.datepicker.adapter.NumericWheelAdapter;
import com.example.practice.view.datepicker.widget.OnWheelChangedListener;
import com.example.practice.view.datepicker.widget.WheelView;

/**
 * 日期选择对话框
 *
 * @author pengjian
 *
 */
public class DatePickerDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private Calendar calendar = Calendar.getInstance(); //日历类
    private WheelView year, month, day; //Wheel picker
    private TextView date_title;
    private Button bt_cancel, bt_submit;
    private OnWheelChangedListener onChangeListener; //onChangeListener
    private String selectYear;
    private String selectMonth;
    private String selectDay;
    private String title = "选择日期";
    private OnDatePickListener onDatePickListener;
    public DatePickerDialog(Context context) {
        super(context, R.layout.datepicker_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        this.setContentView(R.layout.datepicker_dialog);
        year = (WheelView) findViewById(R.id.year);
        month = (WheelView) findViewById(R.id.month);
        day = (WheelView) findViewById(R.id.day);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        date_title = (TextView) findViewById(R.id.date_title);
        bt_cancel.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        date_title.setText(title);
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);
            }
        };
        // month
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[] {"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November", "December"};
        month.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);

        // year
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(context, curYear-100, curYear + 50, 0));
        year.setCurrentItem(curYear);
        year.addChangingListener(listener);

        //day
        updateDays(year, month, day);
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        this.setCanceledOnTouchOutside(false);
    }

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_submit:
                if (onDatePickListener != null) {
                    onDatePickListener.onClick(selectYear, selectMonth, selectDay);
                }
                dismiss();
                break;
            case R.id.bt_cancel:
                dismiss();
        }
    }
    public void setDatePickListener(OnDatePickListener onDatePickListener) {
        this.onDatePickListener = onDatePickListener;
    }
    public interface OnDatePickListener {
        public void onClick(String year, String month, String day);
    }
    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);

        }
    }
    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
            // Index of current item
            int currentItem;
            // Index of item to be highlighted
            int currentValue;

            /**
             * Constructor
             */
            public DateArrayAdapter(Context context, String[] items, int current) {
                super(context, items);
                this.currentValue = current;
            }

            @Override
            protected void configureTextView(TextView view) {
                super.configureTextView(view);
                if (currentItem == currentValue) {
                    view.setTextColor(0xFF0000F0);
                }
                view.setTypeface(Typeface.SANS_SERIF);
            }

            @Override
            public View getItem(int index, View cachedView, ViewGroup parent) {
                currentItem = index;
                return super.getItem(index, cachedView, parent);
            }
        }

    @Override
    public void show(int okBtn, int cancelBtn, View.OnClickListener onOkBtnClick, View.OnClickListener onCancelBtnClick) {
        super.show();
    }
}