package com.music.model;

import android.app.Activity;

import com.lu.library.widget.TopNoticeDialog;
import com.music.lu.R;

public class ShareModel {
	private static final String QZONE_APP_ID="1104335219";
	private static final String QZONE_APP_KEY="J68iUn08AUZwHWrJ";



	/**
	 * umeng share api
	 */
	public void umengShareMusic(Activity activity) {
		TopNoticeDialog.showToast(activity, R.string.no_share);
//		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
//				QZONE_APP_ID, QZONE_APP_KEY);

//		qZoneSsoHandler.addToSocialSDK();
//		UMQQSsoHandler qHandler = new UMQQSsoHandler(activity, QZONE_APP_ID,
//				QZONE_APP_KEY);
//
//		qHandler.addToSocialSDK();
//
//		UMSocialService service = UMServiceFactory
//				.getUMSocialService("com.umeng.share");
//
//
//		service.setShareContent("hehehe");
//		service.openShare(activity, false);
	}

}
