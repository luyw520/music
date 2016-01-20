package com.music.utils;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.soexample.commons.Constants;

/**
 * 摇一摇截图
 * 
 * @author Administrator
 * 
 */
public class ScreenShotUtil {
	protected static final String TAG = "ScreenShotUtil";
	/**
	 * 摇一摇控制器
	 */
	private static Activity mActivity;
	
	
	private static ScreenShotUtil screenShotUtil=null;
	
	
	
	
	private ScreenShotUtil(Activity activity){
		mActivity=activity;
	}
	public static ScreenShotUtil getInstance(Activity activity){
		
		if(screenShotUtil==null){
			screenShotUtil=new ScreenShotUtil(activity);
		}
		if(mActivity.getClass()!=activity.getClass()){
			Log.i(TAG, "类型不相等");
			mActivity=activity;
		}
		
		return screenShotUtil;
	}
	private UMShakeService mShakeController = UMShakeServiceFactory
			.getShakeService(Constants.DESCRIPTOR);

	public void registerShakeToScrShot() {
		
		mShakeController.registerShakeToScrShot(mActivity, new UMAppAdapter(
				mActivity), mScreenshotListener);
	}

	public void unregisterShakeListener() {
		mShakeController.unregisterShakeListener(mActivity);
	};

	private OnScreenshotListener mScreenshotListener = new OnScreenshotListener() {
		
		@Override
		public void onComplete(Bitmap arg0) {
			Log.i(TAG, arg0.toString());
			
			
			String sctPath=DateUtil.getDate3()+".png";
			sctPath=FileUtils.imgPathPath()+File.separator+sctPath;
			ImageUtil.saveImage(arg0, sctPath, null);
			
			DialogUtil.showToast(mActivity, "截图已完成!图像保存在"+sctPath);
			
		}
		
	};
}
