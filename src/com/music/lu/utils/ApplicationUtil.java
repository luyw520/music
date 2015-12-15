package com.music.lu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ApplicationUtil {
	
	public static final String APPINFO="appinfo";
	
	public ApplicationUtil(){
		
	}
 
	public static boolean isFirstRun(Context contex){
		
		final SharedPreferences sharedPreferences=contex.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
		int isFirst=sharedPreferences.getInt("isFirst", 0);
		if(isFirst==0){
			Editor  editor=sharedPreferences.edit();
			editor.putInt("isFirst", 1);
			editor.commit();
			return true;
		}
		return false;
	}
	public static int getAppLockState(Context contex){
		final SharedPreferences sharedPreferences=contex.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
		int  isLock=sharedPreferences.getInt("isLock", 0);
		return isLock;
	}
	/**
	 * 是否开启设备锁 1 开启 0，未开启
	 * @param contex
	 * @param isLock
	 */
	public static void setAppLock(Context contex,int isLock){
		final SharedPreferences sharedPreferences=contex.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putInt("isLock", isLock);
		editor.commit();
		
	}
	public static int getAppToBack(Context contex){
		final SharedPreferences sharedPreferences=contex.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
		int  isLock=sharedPreferences.getInt("moveTaskToBack", 1);
		return isLock;
	}
	public static void setAppToBack(Context contex,int state){
		final SharedPreferences sharedPreferences=contex.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putInt("moveTaskToBack",state);
		editor.commit();
		
	}
	public static int getQieGeIndex(Context contex){
		final SharedPreferences sharedPreferences=contex.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
		int  isLock=sharedPreferences.getInt("qieGeIndex", 0);
		return isLock;
	}
	public static void setQieGeIndex(Context contex,int state){
		final SharedPreferences sharedPreferences=contex.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putInt("qieGeIndex",state);
		editor.commit();
		
	}
	public static boolean getYaoYiYao(Context contex){
		final SharedPreferences sharedPreferences=contex.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
		boolean  isLock=sharedPreferences.getBoolean("yaoyiyao", true);
		return isLock;
	}
	public static void setYaoYiYao(Context contex,boolean state){
		final SharedPreferences sharedPreferences=contex.getSharedPreferences(APPINFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putBoolean("yaoyiyao",state);
		editor.commit();
		
	}
	
}
