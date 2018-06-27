package com.music.model;

import android.app.Activity;
import android.graphics.Bitmap;

import com.lu.library.util.AsyncTaskUtil;
import com.lu.library.util.DebugLog;
import com.lu.library.util.ScreenUtils;
import com.lu.library.util.image.ImageUtil;
import com.music.helpers.FileHelper;
import com.music.utils.DialogUtil;
import com.music.utils.ShareListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;

public class ShareModel {
	private static final String QZONE_APP_ID="1104335219";
	private static final String QZONE_APP_KEY="J68iUn08AUZwHWrJ";

	private UMShareListener mShareListener;

	private static class CustomShareListener implements UMShareListener {


		private CustomShareListener() {

		}

		@Override
		public void onStart(SHARE_MEDIA platform) {
			DebugLog.d("onStart");
		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
			DebugLog.d("onResult");
			if (platform.name().equals("WEIXIN_FAVORITE")) {
				DialogUtil.showToast(null," 收藏成功啦");
			} else {
				if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
						&& platform != SHARE_MEDIA.EMAIL
						&& platform != SHARE_MEDIA.FLICKR
						&& platform != SHARE_MEDIA.FOURSQUARE
						&& platform != SHARE_MEDIA.TUMBLR
						&& platform != SHARE_MEDIA.POCKET
						&& platform != SHARE_MEDIA.PINTEREST

						&& platform != SHARE_MEDIA.INSTAGRAM
						&& platform != SHARE_MEDIA.GOOGLEPLUS
						&& platform != SHARE_MEDIA.YNOTE
						&& platform != SHARE_MEDIA.EVERNOTE) {
					DialogUtil.showToast(null," 分享成功啦");
				}

			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			t.printStackTrace();
			DebugLog.d(t.toString());
			if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
					&& platform != SHARE_MEDIA.EMAIL
					&& platform != SHARE_MEDIA.FLICKR
					&& platform != SHARE_MEDIA.FOURSQUARE
					&& platform != SHARE_MEDIA.TUMBLR
					&& platform != SHARE_MEDIA.POCKET
					&& platform != SHARE_MEDIA.PINTEREST

					&& platform != SHARE_MEDIA.INSTAGRAM
					&& platform != SHARE_MEDIA.GOOGLEPLUS
					&& platform != SHARE_MEDIA.YNOTE
					&& platform != SHARE_MEDIA.EVERNOTE) {
				DialogUtil.showToast(null," 分享失败啦");

			}

		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {

			DialogUtil.showToast(null, " 分享取消了");
		}
	};
	public static String url ="http://mobile.umeng.com/social";
	static String  path ;
	/**
	 * umeng share api
	 */
	public void umengShareMusic(final Activity activity) {
		path = FileHelper.imgPathPath() + File.separator + "s_ido_" + System.currentTimeMillis() / 1000 + ".png";
		ShareListener shareListener=new ShareListener(activity,true);
		shareListener.path=(path);
		final Bitmap bitmap= ScreenUtils.captureWithoutStatusBar(activity);
		new AsyncTaskUtil(new AsyncTaskUtil.IAsyncTaskCallBack() {
			@Override
			public Object doInBackground(String... arg0) {
				ImageUtil.saveImage(bitmap,path,null);
				return null;
			}
			@Override
			public void onPostExecute(Object result) {
			}
		}).execute("");

		com.music.ui.view.DialogUtil.showShareActivityDialog(activity,shareListener);
	}

}
