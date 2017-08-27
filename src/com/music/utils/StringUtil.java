package com.music.utils;

import java.util.Locale;

import com.music.lu.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@SuppressLint("DefaultLocale")
public class StringUtil {
	private final static char KOREAN_UNICODE_START ='a';
	private final static char KOREAN_UNICODE_END  ='a' ;
	private final static char KOREAN_UNIT  ='a' ;
	private final static char[] KOREAN_INITIAL = {};
	
	public static boolean match(String value, String keyword) {
		if (value == null || keyword == null)
			return false;
		if (keyword.length() > value.length())
			return false;
		
		int i = 0, j = 0;
		do {
			if (isKorean(value.charAt(i)) && isInitialSound(keyword.charAt(j))) {
				if (keyword.charAt(j) == getInitialSound(value.charAt(i))) {
					i++;
					j++;
				} else if (j > 0)
					break;
				else
					i++;
			} else {
				if (keyword.charAt(j) == value.charAt(i)) {
					i++;
					j++;
				} else if (j > 0)
					break;
				else
					i++;
			}
		} while (i < value.length() && j < keyword.length());
		
		return (j == keyword.length())? true : false;
	}
	
	private static boolean isKorean(char c) {
		if (c >= KOREAN_UNICODE_START && c <= KOREAN_UNICODE_END)
			return true;
		return false;
	}
	public static String toHanyuPinYin(String string) {
		StringBuffer c = new StringBuffer();
		try {
			for (int j = 0, len = string.length(); j < len; j++) {
				String pinyin = PinyinHelper.toHanyuPinyinStringArray(string
						.charAt(j))[0];
				c.append(pinyin);
			}

		} catch (Exception e) {
			c.append(string.toLowerCase(Locale.ENGLISH));

		}
		return c.toString();
	}
	private static boolean isInitialSound(char c) {
		for (char i : KOREAN_INITIAL) {
			if (c == i)
				return true;
		}
		return false;
	}
	
	private static char getInitialSound(char c) {
		return KOREAN_INITIAL[(c - KOREAN_UNICODE_START) / KOREAN_UNIT];
	}
	/**
	 *
	 * @param c
	 */
	public static char getPinyinFirstLetter(char c) {
		String[] pinyin = null;
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//
		try {
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}

		if (pinyin == null) {
			return 0;
		}

		return pinyin[0].charAt(0);
	}
	
	/**
	 *
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
		if (TextUtils.isEmpty(inputString)) {
			return "";
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);
					if (temp == null || TextUtils.isEmpty(temp[0])) {
						continue;
					}
					output += temp[0].replaceFirst(temp[0].substring(0, 1),
							temp[0].substring(0, 1).toUpperCase());
				} else
					output += java.lang.Character.toString(input[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	public static boolean isDataVaild(String... params){
		return isDataVaild(null, params);
	}
	public static boolean isDataVaild(Context context,String... params){
		for(String s:params){
			if(TextUtils.isEmpty(s)){
				if(context!=null){
					DialogUtil.showToast(context, context.getString(R.string.is_not_empty_tips));
				}
				return false;
			}
		}
		return true;
	}
}
