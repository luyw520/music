package com.music.utils.screen;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.music.utils.DateUtil;
import com.music.utils.DialogUtil;
import com.music.utils.FileUtils;
import com.music.utils.image.ImageUtil;
import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;

import static com.music.utils.ConstUtils.DESCRIPTOR;

/**
 *
 * @author Administrator
 * 
 */
public class ScreenShotUtil {
	protected static final String TAG = "ScreenShotUtil";
	/**
	 */
	private  Activity mActivity;
	
	
	private static ScreenShotUtil screenShotUtil=null;
	
	
	
	
	private ScreenShotUtil(){
		
	}
	public void setShakeActivity(Activity activity){
		mActivity=activity;
	}
	public static ScreenShotUtil getInstance(){
		if(screenShotUtil==null){
			screenShotUtil=new ScreenShotUtil();
		}
		
		return screenShotUtil;
	}
	
//	private UMShakeService mShakeController = UMShakeServiceFactory
//			.getShakeService(Constants.DESCRIPTOR);

	public void registerShakeToScrShot() {
		UMShakeServiceFactory
		.getShakeService(DESCRIPTOR).registerShakeToScrShot(mActivity, new UMAppAdapter(
				mActivity), mScreenshotListener);
	}
	public void registerShakeToScrShot(Activity activity) {
		
//		if(mActivity!=activity){
//			mActivity=activity;
//			DeBug.d(this, "mActivity:"+mActivity);
//			UMShakeServiceFactory
//			.getShakeService(Constants.DESCRIPTOR).registerShakeToScrShot(mActivity, new UMAppAdapter(
//					mActivity), mScreenshotListener);
//		}
		
	}
	public void unregisterShakeListener() {
		UMShakeServiceFactory
		.getShakeService(DESCRIPTOR).unregisterShakeListener(mActivity);
	};

	private OnScreenshotListener mScreenshotListener = new OnScreenshotListener() {
		
		@Override
		public void onComplete(Bitmap arg0) {
			Log.i(TAG, arg0.toString());
			
			
			String sctPath= DateUtil.getDate3()+".png";
			sctPath= FileUtils.imgPathPath()+File.separator+sctPath;
			ImageUtil.saveImage(arg0, sctPath, null);
			
			DialogUtil.showToast(mActivity, " aaa"+sctPath);
			
		}
		
	};
}
