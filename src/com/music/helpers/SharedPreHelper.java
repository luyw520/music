package com.music.helpers;

import android.content.Context;

import com.lu.library.util.SPUtil;

public class SharedPreHelper {


	public static String getStringValue(Context context,String key,String defaultValue){
		return (String) SPUtil.get(key, defaultValue);
	}
	public static Boolean getBooleanValue(Context context,String key,boolean defaultValue){
		return (Boolean) SPUtil.get(key, defaultValue);
	}
	public static void setBooleanValue(Context context,String key,Boolean value){
		SPUtil.put(key, value);
	}
}
