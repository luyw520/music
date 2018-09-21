package com.music.utils.screen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.lu.library.util.DateUtil;
import com.lu.library.util.image.ImageUtil;
import com.music.utils.DialogUtil;
import com.music.helpers.FileHelper;
import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;

import java.io.File;


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
//		UMShakeServiceFactory
//		.getShakeService(DESCRIPTOR).registerShakeToScrShot(mActivity, new UMAppAdapter(
//				mActivity), mScreenshotListener);
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
//		UMShakeServiceFactory
//		.getShakeService(DESCRIPTOR).unregisterShakeListener(mActivity);
	};

	private OnScreenshotListener mScreenshotListener = new OnScreenshotListener() {

		@Override
		public void onComplete(Bitmap arg0) {
			Log.i(TAG, arg0.toString());


			String sctPath= DateUtil.getYearAndMouth()+".png";
			sctPath= FileHelper.imgPathPath()+File.separator+sctPath;
			ImageUtil.saveImage(arg0, sctPath, null);

			DialogUtil.showToast(mActivity, " aaa"+sctPath);

		}

	};
}
