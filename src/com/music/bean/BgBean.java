package com.music.bean;

import java.io.IOException;
import java.io.InputStream;

import com.music.utils.SharedPreHelper;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BgBean {
	private Bitmap bitmap;
	private boolean isChecked;
	private static Bitmap currentBitmap;
	
	public BgBean(Bitmap bitmap,boolean isChecked){
		
		this.bitmap=bitmap;
		this.isChecked=isChecked;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public static Bitmap getCurrentBitmap() {
		return currentBitmap;
	}
	public static Bitmap getUsedBitmap(Context context) {
		
		int checkedId = 0;
		String checkString = SharedPreHelper.getStringValue(context, "bgindex","");
		if (!checkString.equals("")) {
			checkedId = Integer.parseInt(checkString);
		}

		AssetManager assetManager = context.getAssets();
		InputStream inputStream = null;
		Bitmap bitmap=null;
		try {
			String[] paths = assetManager.list("bgs");
			inputStream = assetManager.open("bgs/" + paths[checkedId]);
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return bitmap;
	}
	public static void setCurrentBitmap(Bitmap currentBitmap) {
		BgBean.currentBitmap = currentBitmap;
	}
	

	
	
}
