package com.music.utils;

import android.app.Activity;
import android.content.Context;

import com.lu.library.widget.TopNoticeDialog;

public class ToastUtil {



    public static void showCustomToast(final Activity activity, final String msg) {
        TopNoticeDialog.showToast(activity,msg);
    }


    public static void showCustomToast(final Activity activity, final int msg) {
        TopNoticeDialog.showToast(activity,msg);
    }

    /**
     * 显示一个toast
     *
     * @param context
     * @param msg
     */
    public static void showToast(final Context context, final String msg) {
        TopNoticeDialog.showToast(context,msg);
    }

    public static void showToast(Context context, int res) {
        TopNoticeDialog.showToast(context,res);
    }


}