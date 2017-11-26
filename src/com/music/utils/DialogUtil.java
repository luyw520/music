package com.music.utils;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.music.lu.R;
import com.music.ui.widget.CommomDialog;
import com.music.utils.screen.ScreenUtils;

public class DialogUtil {
	private static AlertDialog dialog=null;
//	private static Toast toast;
	/**
	 */
	public static void showToast(Context context,String msg){
		TopNoticeDialog.showToast(context, msg);
	}
	/**
	 */
	public static void showToast(Context context,int res){
		TopNoticeDialog.showToast(context, res);
	}
	/**
	 */
	public static void showToast(Context context,CharSequence msg){
		TopNoticeDialog.showToast(context, msg);
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
//		AlertDialog.Builder builder=new AlertDialog.Builder(context);
//		View view= LayoutInflater.from(context).inflate(R.layout.dialog_exit,null);
//		final Dialog mdialog=builder.create();
//		view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				closeAlertDialog(mdialog);
//			}
//		});
//		view.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				closeAlertDialog(mdialog);
//				clickListener.onClick(v);
//			}
//		});
//		builder.setView(view);
//
//		Window window=mdialog.getWindow();
//		WindowManager.LayoutParams params = window.getAttributes();
//
//		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//		mdialog.show();
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
