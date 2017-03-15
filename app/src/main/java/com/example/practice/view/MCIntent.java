package com.example.practice.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

@Deprecated
public class MCIntent {
	public static final String KEY = "key";

	public static void sendIntent(Context context, Class classes) {
		if (context != null && classes != null) {
			Intent intent = new Intent();
			intent.setClass(context, classes);
			context.startActivity(intent);
		}

	}

	public static void sendIntent(Context context, Class classes, String key, String value) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(context, classes);
		intent.putExtra(key, value);
		context.startActivity(intent);
	}

	public static void sendIntent(Context context, Class classes, String key, int value) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(context, classes);
		intent.putExtra(key, value);
		context.startActivity(intent);
	}

	/**
	 * ecg = (EcgInfo) this.getIntent().getSerializableExtra("ECG");
	 */
	public static void sendIntent(Context context, Class classes, String name, Serializable bean) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(name, bean);
		intent.putExtras(mBundle);
		context.startActivity(intent);
	}

	public static void sendIntent(Context context, Class classes, String name, Serializable bean, String key, boolean value) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(name, bean);
		intent.putExtras(mBundle);
		intent.putExtra(key,value);
		context.startActivity(intent);
	}

	public static void sendIntent(Context context, Class classes, String name, Serializable bean, String name2, Serializable bean2) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(name, bean);
		mBundle.putSerializable(name2, bean2);
		intent.putExtras(mBundle);
		context.startActivity(intent);
	}

	public static void sendIntent(Context context, Class classes, String name, Serializable bean, String name2, int value) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(name, bean);
		intent.putExtras(mBundle);
		intent.putExtra(name2, value);
		context.startActivity(intent);
	}

	public static void sendIntent(Context context, Class classes, String key, String conetnt, String key2, int value) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putString(key, conetnt);
		mBundle.putInt(key2, value);
		intent.putExtras(mBundle);
		context.startActivity(intent);
	}


	public static void sendsetResultIntent(Activity context, String name, Serializable bean) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent();
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(name, bean);
		intent.putExtras(mBundle);
		context.setResult(Activity.RESULT_OK, intent);
	}

	public static void sendsetResultIntent2(Activity context, String name, Serializable bean) {
		Intent intent = new Intent();
		intent.putExtra(name, bean);
		context.setResult(Activity.RESULT_OK, intent);
	}

	public static void sendIntentForResult(Activity context, Class classes, String key, String value, int requestCode) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putString(key, value);
		intent.putExtras(mBundle);
		context.startActivityForResult(intent, requestCode);
	}

	public static void sendIntentForResult(Activity context, Class classes, int requestCode) {
		Intent intent = new Intent(context, classes);
		context.startActivityForResult(intent, requestCode);
	}


	public static void sendIntentForResult(Activity context, Class classes, String key, String value) {
		sendIntentForResult(context, classes, key, value, 0);
	}

	public static void sendIntentForResult(Activity context, Class classes, String key, boolean value, int requestCode) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putBoolean(key, value);
		intent.putExtras(mBundle);
		context.startActivityForResult(intent, requestCode);
	}

	public static void sendIntentForResult(Activity context, Class classes, String key, Serializable bean, int requestCode) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(key, bean);
		intent.putExtras(mBundle);
		context.startActivityForResult(intent, requestCode);
	}


	public static void sendIntentForResult(Activity context, Class classes,
                                           String key, Serializable bean,
                                           String key2, int value,
                                           int requestCode) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(key, bean);
		mBundle.putInt(key2, value);
		intent.putExtras(mBundle);
		context.startActivityForResult(intent, requestCode);
	}

	public static void sendIntentForResult(Fragment fragment, Class classes,
                                           String key, Serializable bean,
                                           String key2, int value,
                                           int requestCode) {
		if (fragment == null || classes == null) {
			return;
		}
		Intent intent = new Intent(fragment.getActivity(), classes);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(key, bean);
		mBundle.putInt(key2, value);
		intent.putExtras(mBundle);
		fragment.startActivityForResult(intent, requestCode);
	}

	public static void sendIntentForResult(Activity context, Class classes, String key, boolean value) {
		sendIntentForResult(context, classes, key, value, 0);
	}

	public static void sendIntentSetResult(Activity context, Class classes, String key, Serializable bean) {
		if (context == null || classes == null) {
			return;
		}

		Intent intent = new Intent(context, classes);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(key, bean);
		intent.putExtras(mBundle);
		context.setResult(Activity.RESULT_OK, intent);
		context.finish();
	}

	public static void sendIntentFromAnimLeft(Activity context, Class classes) {
		if (context == null || classes == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(context, classes);
		context.startActivity(intent);
	}


}
