package com.music.bean;

import com.music.utils.AsyncTaskUtil;
import com.music.utils.image.BitmapCacheUtil;
import com.music.utils.PhotoUtils;
import com.music.utils.AsyncTaskUtil.IAsyncTaskCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class UserManager {
	private static UserManager userManager=null;
	public static final String USERINFO="userInfo";
	private static UserBean userBean=null;
	public  UserBean getUserBean() {
		return userBean;
	}
	public  void setUserBean(UserBean userBean2) {
		userBean = userBean2;
	}
	private UserManager(){
		
	}
	/**
	 */
	public static boolean isLogin(){
		return userBean!=null;
	}
	public  static UserManager getInstance(){
		if(userManager==null){
			userManager=new UserManager();
		}
		return userManager;
	}
	/**
	 * @param context
	 * @return
	 */
	public static UserBean getUserBean(Context context){
		SharedPreferences sharedPreferences=context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
		String username=sharedPreferences.getString("username", "");
		
		if(!"".equals(username)){
			String password=sharedPreferences.getString("password", "");
			userBean=new UserBean(username, password);
			return userBean;
		}
		
		
		
		return null;
	}
	/**
	 * @param context
	 * @return
	 */
	public void saveUserName(Context context,String name){
		SharedPreferences sharedPreferences=context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putString("username", name);
		editor.commit();
	}
	/**
	 * @param context
	 * @return
	 */
	public void saveUserBean(Context context,UserBean userBean){
		SharedPreferences sharedPreferences=context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putString("username", userBean.getUsername());
		editor.putString("password", userBean.getPasswrod());
		editor.commit();
	}
	/**
	 * @param context
	 * @return
	 */
	public void saveUserPassword(Context context,String password){
		SharedPreferences sharedPreferences=context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putString("password", password);
		editor.commit();
	}
	/**
	 * @param url
	 */
	public void downUserHeader(final String url,final ImageView iv_header){
		

		AsyncTaskUtil asyncTaskUtil=new AsyncTaskUtil(new IAsyncTaskCallBack() {
			
			@Override
			public void onPostExecute(Object result) {
				Bitmap bitmap=(Bitmap) result;
				if(bitmap!=null){
					iv_header.setImageBitmap(bitmap);
				}
			}
			
			@Override
			public Object doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				Bitmap bitmap=BitmapCacheUtil.getDefalut().getCacheBitmap(url);
				return bitmap;
			}
		});
		asyncTaskUtil.execute(url);
	}

	@SuppressWarnings("deprecation")
	public void chooseHeaderImg(int requestCode, int resultCode, Intent data,Activity context,ImageView iv_header, String userHeaderImg) {
		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_ALBUM) {
			if (data == null)
				return;

			if (resultCode == Activity.RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
			}
			Uri uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.managedQuery(uri, proj, null, null, null);
			if (cursor != null) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
					String path = cursor.getString(column_index);
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (PhotoUtils.bitmapIsLarge(bitmap)) {
						PhotoUtils.cropPhoto(context, context, path);
					} else {
						iv_header.setImageBitmap(bitmap);
					}
				}
			}
		} else if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CAMERA) {

			if (resultCode ==Activity.RESULT_OK) {

				String path = userHeaderImg;
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				if (PhotoUtils.bitmapIsLarge(bitmap)) {
					PhotoUtils.cropPhoto(context, context, path);
				} else {
					iv_header.setImageBitmap(bitmap);
				}
			}

		} else if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CROP) {

			if (resultCode == Activity.RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (bitmap != null) {
						iv_header.setImageBitmap(bitmap);
					}
				}
			}

		}

	}
}
