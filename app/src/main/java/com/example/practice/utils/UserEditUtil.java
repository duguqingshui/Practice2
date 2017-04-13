package com.example.practice.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;


import com.example.practice.R;
import com.example.practice.view.MCToast;
import com.example.practice.view.MyEditView;
import com.example.practice.view.MyTimePickerDialog;
import com.example.practice.utils.TimeUtils;
import java.util.Date;


/**
 * 该类已经过期，请使用com.microcardio.utir.TimePickerUtil
 *
 * @author malei TimePickerUtil.showDatePicker(this, view_recordTime_ed.getTextView());
 */
@Deprecated
public class UserEditUtil {


	/**
	 * 设置日期
	 *
	 * @param editText
	 */
	public void setDateClickListener(final Context mContext, final EditText editText) {
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View View, boolean focused) {
				if (focused) {
					showDatePicker(mContext, editText);
				}
			}
		});
		editText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePicker(mContext, editText);
			}
		});
	}

	/**
	 * 设置时间
	 */
	public void setDateClickListener(final Context mContext, final EditText editText, final Date date, final int color) {
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View View, boolean focused) {
				if (focused) {
					showDatePicker(mContext, editText, date, color);
				}
			}
		});
		editText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePicker(mContext, editText, date, color);
			}
		});
	}

	/**
	 * 设置时间
	 *
	 * @param futureTime 为true 不可以选择将来时间
	 */
	public static void showDatePicker(final Context context, boolean futureTime, final MyEditView ed) {
		showDatePicker(context, ed, futureTime, new Date(), null);
	}

	public static void showDatePicker(final Context context, final MyEditView ed, final boolean futureTime, Date curDate, final MyEditView other) {
		final MyTimePickerDialog dialog = new MyTimePickerDialog(context, context.getResources().getColor(R.color.main_color));
		dialog.show(curDate != null ? curDate : new Date(), new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (futureTime) {
					ed.setText(TimeUtils.getBirthDay(dialog.getSelTime()));
					if (other != null) {
						other.setText(TimeUtils.getAge(dialog.getSelTime()) + "");
					}
					dialog.dismiss();
				}
				else {
					if (dialog.getSelTime().getTime() > System.currentTimeMillis()) {
						MCToast.show(R.string.common_future_time, context);
					}
					else {
						ed.setText(TimeUtils.getBirthDay(dialog.getSelTime()));
						if (other != null) {
							other.setText(TimeUtils.getAge(dialog.getSelTime()) + "");
						}
						dialog.dismiss();
					}
				}
			}
		});
	}

	private void showDatePicker(Context mContext, final EditText editText) {
		showDatePicker(mContext, editText, new Date(), R.color.main_color);

	}

	private static void showDatePicker(final Context mContext, final EditText editText, Date date, int color) {
		if (date == null) {
			date = new Date();
		}
		final MyTimePickerDialog dialog = new MyTimePickerDialog(mContext, color);
		dialog.show(date, new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (dialog.getSelTime().getTime() > System.currentTimeMillis()) {
					MCToast.show(R.string.common_future_time, mContext);
				}
				else {
					editText.setText(TimeUtils.getBirthDay(dialog.getSelTime()));
					dialog.dismiss();
				}
			}
		});
	}



	public static void showDatePicker(final Context mContext, final TextView tv) {
		showDatePicker(mContext, tv, "");
	}

	public static void showDatePicker(final Context mContext, final TextView tv, final String tip) {
		final MyTimePickerDialog dialog = new MyTimePickerDialog(mContext, mContext.getResources().getColor(R.color.main_color));
		dialog.show(new Date(), new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (dialog.getSelTime().getTime() > System.currentTimeMillis()) {
					MCToast.show(R.string.common_future_time, mContext);
				}
				else {
					MCToast.show(tip, mContext);
					tv.setText(TimeUtils.getBirthDay(dialog.getSelTime()));
					dialog.dismiss();
				}
			}
		});
	}

}
