package com.music.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import com.lu.library.widget.CommomDialog;
import com.lu.library.widget.TopNoticeDialog;
import com.music.MusicApplication;
import com.music.lu.R;

public class DialogUtil {
	private static AlertDialog dialog=null;
//	private static Toast toast;
	/**
	 */
	public static void showToast(Context context,String msg){
		TopNoticeDialog.showToast(MusicApplication.getInstance(), msg);
	}
	/**
	 */
	public static void showToast(Context context,int res){
		TopNoticeDialog.showToast(MusicApplication.getInstance(), res);
	}
	/**
	 */
	public static void showToast(Context context,CharSequence msg){
		TopNoticeDialog.showToast(MusicApplication.getInstance(), msg);
	}
	/**
	 */
	public static  void showAlertDialog(Context context,String title,String[] items,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items,dialogInterface);
		dialog=builder.create();
		dialog.show();
	}
	/**
	 */
	public static  void showAlertDialog(Context context,String title,int items,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items,dialogInterface);
		dialog=builder.create();
		dialog.show();
	}
	/**
	 */
	public static void closeAlertDialog(){
		if(dialog!=null && dialog.isShowing()){
			dialog.dismiss();
		}
	}
	/**
	 */
	public static void closeAlertDialog(Dialog dialog){
		if(dialog!=null && dialog.isShowing()){
			dialog.dismiss();
		}
	}
	/**
	 * show a closeDialog
	 * @param context
	 */
	public static Dialog showExitAlertDialog(Context context, final View.OnClickListener clickListener){
		CommomDialog commomDialog=new CommomDialog(context,R.style.DialogCommonStyle,"");
		commomDialog.setTitle(context.getString(R.string.dialog_exit_title));
		commomDialog.setMessage(context.getString(R.string.dialog_exit_msg));
		commomDialog.setOnCloseListener(new CommomDialog.OnCloseListener() {
			@Override
			public void onClick(Dialog dialog, boolean confirm) {
				if (confirm){
					clickListener.onClick(null);
				}
			}
		});
		commomDialog.show();

		return commomDialog;
	}


	public static void showWaitDialog(Context context){
		dialog=ProgressDialog.show(context, "wait", "wait mmmm");
		dialog.show();
	}
	public static void showWaitDialog(Context context,String title,String message){
		dialog=ProgressDialog.show(context, title, message);
		dialog.show();
	}

}
