package com.lu.library.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final String DATE_FORMAT_1="yyyy_MM_dd";
	private static final String DATE_FORMAT_2="hh:mm:ss";
	private static final String DATE_FORMAT_3=DATE_FORMAT_1+" "+DATE_FORMAT_2;
	/**
	 * 获取当前日期数据
	 * @see #DATE_FORMAT_3
	 * @see #DATE_FORMAT_2
	 * @see #DATE_FORMAT_1
	 *@return DATE_FORMAT_3
	 */
	public static String getCurrentDate(){
		return format(DATE_FORMAT_3,Calendar.getInstance().getTime());
	}

	/**
	 * 获取当前年月日数据
	 * @see #DATE_FORMAT_2
	 *@return DATE_FORMAT_2
	 */
	public static String getCurrentHMS(){
		return format(DATE_FORMAT_2,Calendar.getInstance().getTime());
	}


	/**
	 * 获取当前年月日数据
	 * @see #DATE_FORMAT_1
	 *@return DATE_FORMAT_1
	 */
	public static String getCurrentYMD(){
		return format(DATE_FORMAT_1,Calendar.getInstance().getTime());
	}
	private static String format(String formatStr,Date date){
		SimpleDateFormat format=new SimpleDateFormat(formatStr);
		return format.format(date);
	}

}
