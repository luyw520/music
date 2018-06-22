package com.music.utils;

import java.util.Calendar;

public class DateUtil {
	private static Calendar calendar=null;
	/**
	 *
	 */
	public static String getDate3(){
		return getDay3()+"_"+getTime3();
	}
	/**
	 *
	 */
	public static String getTime(){
		calendar=Calendar.getInstance();
		StringBuffer buffer=new StringBuffer();
		final int hour=calendar.get(Calendar.HOUR_OF_DAY);
		if(hour<10){
			buffer.append("0"+hour);
		}else{
			buffer.append(String.valueOf(hour));
		}
		buffer.append(":");
		final int minute=calendar.get(Calendar.MINUTE);
		if(minute<10){
			buffer.append("0"+minute);
		}else{
			buffer.append(String.valueOf(minute));
		}
		buffer.append(":");
		final int second=calendar.get(Calendar.SECOND);
		if(second<10){
			buffer.append("0"+second);
		}else{
			buffer.append(String.valueOf(second));
		}

		return new String(buffer);
	}
	/**
	 *
	 */
	public static String getTime3(){
		calendar=Calendar.getInstance();
		StringBuffer buffer=new StringBuffer();
		final int hour=calendar.get(Calendar.HOUR_OF_DAY);
		if(hour<10){
			buffer.append("0"+hour);
		}else{
			buffer.append(String.valueOf(hour));
		}
		buffer.append("_");
		final int minute=calendar.get(Calendar.MINUTE);
		if(minute<10){
			buffer.append("0"+minute);
		}else{
			buffer.append(String.valueOf(minute));
		}
		buffer.append("_");
		final int second=calendar.get(Calendar.SECOND);
		if(second<10){
			buffer.append("0"+second);
		}else{
			buffer.append(String.valueOf(second));
		}

		return new String(buffer);
	}


	/**
	 *
	 */
	public static String getDay(){
		calendar=Calendar.getInstance();
		final StringBuffer buffer=new StringBuffer();

		final int year=calendar.get(Calendar.YEAR);
		if(year<10){
			buffer.append("0"+year);
		}else{
			buffer.append(String.valueOf(year));
		}
		buffer.append("-");
		final int mouth=(calendar.get(Calendar.MONTH)+1);
		if(mouth<10){
			buffer.append("0"+mouth);
		}else{
			buffer.append(String.valueOf(mouth));
		}
		buffer.append("-");
		final int day=calendar.get(Calendar.DAY_OF_MONTH);
		if(day<10){
			buffer.append("0"+day);
		}else{
			buffer.append(String.valueOf(day));
		}

		return new String(buffer);
	}
	/**
	 *
	 */
	public static String getDay3(){
		calendar=Calendar.getInstance();
		final StringBuffer buffer=new StringBuffer();

		final int year=calendar.get(Calendar.YEAR);
		if(year<10){
			buffer.append("0"+year);
		}else{
			buffer.append(String.valueOf(year));
		}
		buffer.append("_");
		final int mouth=(calendar.get(Calendar.MONTH)+1);
		if(mouth<10){
			buffer.append("0"+mouth);
		}else{
			buffer.append(String.valueOf(mouth));
		}
		buffer.append("_");
		final int day=calendar.get(Calendar.DAY_OF_MONTH);
		if(day<10){
			buffer.append("0"+day);
		}else{
			buffer.append(String.valueOf(day));
		}

		return new String(buffer);
	}
	/**
	 *
	 */
	public static String getDay2(){
		calendar=Calendar.getInstance();
		final StringBuffer buffer=new StringBuffer();

		final int year=calendar.get(Calendar.YEAR);
		if(year<10){
			buffer.append("0"+year);
		}else{
			buffer.append(String.valueOf(year));
		}
		buffer.append("/");
		final int mouth=(calendar.get(Calendar.MONTH)+1);
		if(mouth<10){
			buffer.append("0"+mouth);
		}else{
			buffer.append(String.valueOf(mouth));
		}
		buffer.append("/");
		final int day=calendar.get(Calendar.DAY_OF_MONTH);
		if(day<10){
			buffer.append("0"+day);
		}else{
			buffer.append(String.valueOf(day));
		}
		return new String(buffer);
	}


}
