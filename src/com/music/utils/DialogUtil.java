package com.music.utils;



import com.music.lu.R;
import com.music.widget.dialog.LoadingView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class DialogUtil {
	private static AlertDialog dialog=null;
	private static Toast toast;
	/**
	 * 弹出一个消息框
	 * @param context
	 * @param msg 消息内容
	 */
	public static void showToast(Context context,String msg){
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);

		} else {
			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}
	
	/**
	 * 弹出一个选择对话框
	 * @param context
	 * @param title	对话框的标题
	 * @param items 对话框里面的数据项
	 * @param dialogInterface 点击对话框里面的子项的监听事件
	 */
	public static  void showAlertDialog(Context context,String title,String[] items,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items,dialogInterface);
		dialog=builder.create();
		dialog.show();
	}
	/**
	 * 弹出一个选择对话框
	 * @param context
	 * @param title	对话框的标题
	 * @param items 对话框里面的数据项
	 * @param dialogInterface 点击对话框里面的子项的监听事件
	 */
	public static  void showAlertDialog(Context context,String title,int items,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items,dialogInterface);
		dialog=builder.create();
		dialog.show();
	}
	/**
	 * 关闭弹出框
	 */
	public static void closeAlertDialog(){
		if(dialog!=null && dialog.isShowing()){
			dialog.dismiss();
		}
	}
	/**
	 * show a closeDialog
	 * @param context
	 * @param dialogInterface 确定按钮事件
	 */
	public static void showExitAlertDialog(Context context,OnClickListener dialogInterface){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("退出").setMessage("您确认要推出吗？")
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				closeAlertDialog();
			}
		})
		.setPositiveButton("确定", dialogInterface);
		dialog=builder.create();
		dialog.show();
	}
	
	
	public static void showWaitDialog(Context context){
		dialog=ProgressDialog.show(context, "下载", "正在下载");
		dialog.show();
	}
	public static void showWaitDialog(Context context,String title,String message){
		dialog=ProgressDialog.show(context, title, message);
		dialog.show();
	}
	public static void showCustomWaitDialog(Context context){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		dialog=builder.create();
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(R.layout.dialog_custom_wait, null);
		LoadingView loadingView=(LoadingView) view.findViewById(R.id.loadView);
		loadingView.setLoadingText("数据请求中...");
		dialog.show();
	
		
//		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//		params.width = (int) (300);
//		params.height = (int) (300);
//		dialog.getWindow().setAttributes(params);
		dialog.getWindow().setContentView(view);
		
	}
}
