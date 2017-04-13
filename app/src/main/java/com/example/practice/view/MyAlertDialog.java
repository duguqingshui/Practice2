package com.example.practice.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import com.example.practice.R;


public class MyAlertDialog {

	public interface OnMyAlertDialogClickListener {
		void onAlertDialogAffirmed(String clickId);
	}

	public static void showDialog(Context context, String tip, String scope, String unit, final OnMyAlertDialogClickListener onclick) {
		Builder builder = new Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
		builder.setIcon(android.R.drawable.stat_notify_error);
		builder.setTitle(R.string.common_message);
		builder.setMessage(context.getResources().getString(
				R.string.mc_dialog_tip1) + tip + context.getResources().getString(R.string.mc_dialog_tip2) + scope + " " + unit);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onclick.onAlertDialogAffirmed("");
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void showDialog(Context context, int tip, final OnMyAlertDialogClickListener onclick) {
		Builder builder = new Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
		builder.setTitle(R.string.common_message).setIcon(android.R.drawable.stat_notify_error);
		builder.setMessage(context.getResources().getString(tip));
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onclick.onAlertDialogAffirmed("");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void showListDialog(Context context, final OnMyAlertDialogClickListener onclick) {
		Builder builder = new Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
		builder.setIcon(R.drawable.icon_model);
		builder.setTitle("模板管理");
		// 指定下拉列表的显示数据
		final String[] cities = {"\t\t用当前界面创建模板", "\t\t新建模板",};
		// 设置一个下拉的列表选择项
		builder.setItems(cities, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onclick.onAlertDialogAffirmed(which + "");
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
