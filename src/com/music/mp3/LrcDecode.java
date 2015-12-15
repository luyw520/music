package com.music.mp3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Hashtable;

/**
 * <b>姝�璇�瑙ｆ��绫�</b>
 * 
 * @author 甯�������
 * @QQ QQ:951868171
 * @version 1.0
 * @email xi_yf_001@126.com
 * */
public class LrcDecode {

	private Hashtable<String, String> lrcTable = null;

	/**
	 * 瑙ｆ��Lrc
	 * */
	public LrcDecode readLrc(InputStream is) {
		lrcTable = new Hashtable<String, String>();
		try {
			BufferedReader bis = new BufferedReader(new InputStreamReader(is, "gbk"));
			String str = null;
			while ((str = bis.readLine()) != null) {
				decodeLine(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			lrcTable = null;
		}
		return this;
	}

	private LrcDecode decodeLine(String str) {

		if (str.startsWith("[ti:")) {
			lrcTable.put("ti", str.substring(4, str.lastIndexOf("]")));

		} else if (str.startsWith("[ar:")) {
			lrcTable.put("ar", str.substring(4, str.lastIndexOf("]")));

		} else if (str.startsWith("[al:")) {
			lrcTable.put("al", str.substring(4, str.lastIndexOf("]")));

		} else if (str.startsWith("[by:")) {
			lrcTable.put("by", str.substring(4, str.lastIndexOf("]")));

		} else if (str.startsWith("[la:")) {
			lrcTable.put("la", str.substring(4, str.lastIndexOf("]")));

		} else {

			int startIndex = -1;
			while ((startIndex = str.indexOf("[", startIndex + 1)) != -1) {
				int endIndex = str.indexOf("]", startIndex + 1);
				lrcTable.put(strToLongToTime(str.substring(startIndex + 1, endIndex)) + "", str.substring(str.lastIndexOf("]") + 1, str.length()));
			}
		}
		return this;
	}

	public Hashtable<String, String> getLrcTable() {
		return lrcTable;
	}

	private String strToLongToTime(String str) {
		int m = Integer.parseInt(str.substring(0, str.indexOf(":")));
		int s = 0;
		int ms = 0;

		if (str.indexOf(".") != -1) {
			s = Integer.parseInt(str.substring(str.indexOf(":") + 1, str.indexOf(".")));
			ms = Integer.parseInt(str.substring(str.indexOf(".") + 1, str.length()));
		} else {
			s = Integer.parseInt(str.substring(str.indexOf(":") + 1, str.length()));
		}
		return timeMode(m * 60000 + s * 1000 + ms * 10);
	}

	public static String timeMode(int time) {
		boolean isNegetive = time < 0;
		if (time < 0)
			time = Math.abs(time);
		time = time / 1000;
		String timeStr = "";
		int min, second;
		min = time / 60;
		second = time - min * 60;
		DecimalFormat mDecimalFormat = new DecimalFormat("00");
		timeStr = mDecimalFormat.format(min) + ":" + mDecimalFormat.format(second);
		return isNegetive ? "-" + timeStr : timeStr;
	}
	
	public static String timeMode2(int time) {
		boolean isNegetive = time < 0;
		if (time < 0)
			time = Math.abs(time);
		time = time / 1000;
		String timeStr = "";
		int hour, min, second;
		hour = time / (60 * 60);
		min = (time - hour * 60*60) / 60;
		second = time - hour * 60*60 - min * 60;
		DecimalFormat mDecimalFormat = new DecimalFormat("00");
		timeStr = mDecimalFormat.format(hour) + ":" + mDecimalFormat.format(min) + ":" + mDecimalFormat.format(second);
		return isNegetive ? "-" + timeStr : timeStr;
	}
}
