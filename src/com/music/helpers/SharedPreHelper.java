package com.music.helpers;

import android.content.Context;

import com.lu.library.util.SPUtils;

public class SharedPreHelper {


	public static String getStringValue(Context context,String key,String defaultValue){
		return (String) SPUtils.get(key, defaultValue);
	}
	public static Boolean getBooleanValue(Context context,String key,boolean defaultValue){
		return (Boolean) SPUtils.get(key, defaultValue);
	}
	public static void setBooleanValue(Context context,String key,Boolean value){
		SPUtils.put(key, value);
	}
}
