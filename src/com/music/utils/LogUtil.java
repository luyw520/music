package com.music.utils;
import android.util.Log;

public class LogUtil {
	public static void log(@SuppressWarnings("rawtypes") Class tag, String msg){
		Log.i(tag.getSimpleName(), msg);
	};
	public static void i(@SuppressWarnings("rawtypes") Class tag, String msg){
		Log.i(tag.getSimpleName(), msg);
	};
	public static void i(String tag, String msg){
		Log.i(tag, msg);
	};
	public static void d(String tag, String msg){
		Log.d(tag, msg);
	};
}
