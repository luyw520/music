package com.music.utils.screen;

import android.app.Activity;


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

}
