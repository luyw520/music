package com.music.lu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreHelper {
	public static final String MUSIC_INFO="music_info";
	
	
	public static String getValue(String name,Context context,String key){
		SharedPreferences sharedPreferences=context.getSharedPreferences(name, Context.MODE_PRIVATE);
		
		return sharedPreferences.getString(key, "");
	}
	public static void setValue(Context context,String name,String key,String value){
		SharedPreferences sharedPreferences=context.getSharedPreferences(name, Context.MODE_PRIVATE);
		
		Editor editor=sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static String getStringValue(Context context,String key,String defaultValue){
		SharedPreferences sharedPreferences=context.getSharedPreferences(MUSIC_INFO, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, defaultValue);
	}
	public static void setStringValue(Context context,String key,String value){
		SharedPreferences sharedPreferences=context.getSharedPreferences(MUSIC_INFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static int getIntValue(Context context,String key,int defaultValue){
		SharedPreferences sharedPreferences=context.getSharedPreferences(MUSIC_INFO, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, defaultValue);
	}
	public static void setIntValue(Context context,String key,int value){
		SharedPreferences sharedPreferences=context.getSharedPreferences(MUSIC_INFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	public static Boolean getBooleanValue(Context context,String key,boolean defaultValue){
		SharedPreferences sharedPreferences=context.getSharedPreferences(MUSIC_INFO, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defaultValue);
	}
	public static void setBooleanValue(Context context,String key,Boolean value){
		SharedPreferences sharedPreferences=context.getSharedPreferences(MUSIC_INFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
}
