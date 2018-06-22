package com.music.helpers;

import android.content.Context;
import android.text.TextUtils;

import com.music.lu.R;
import com.music.utils.DialogUtil;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Locale;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music.helpers
 * @description: ${TODO}{ 类注释}
 * @date: 2018/6/22 0022
 */

public class StringHelper {

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
    public static boolean isDataVaild(Context context, String... params){
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
}
