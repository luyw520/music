package com.music.model;

import android.app.Activity;

import com.lu.library.util.DebugLog;
import com.music.lu.R;
import com.music.utils.DialogUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

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
	/**
	 * umeng share api
	 */
	public void umengShareMusic(final Activity activity) {
//		TopNoticeDialog.showToast(activity, R.string.no_share);
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


		  /*增加自定义按钮的分享面板*/
		ShareAction mShareAction = new ShareAction(activity).setDisplayList(
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
				SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
				.addButton("复制文本", "复制文本", "umeng_socialize_copy", "umeng_socialize_copy")
				.addButton("复制链接", "复制链接", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
				.setShareboardclickCallback(new ShareBoardlistener() {
					@Override
					public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
						if (snsPlatform.mShowWord.equals("复制文本")) {
							DialogUtil.showToast(null, "复制文本按钮");
						} else if (snsPlatform.mShowWord.equals("复制链接")) {
							DialogUtil.showToast(null,"复制链接按钮");

						} else {
							UMWeb web = new UMWeb(url);
							web.setTitle("来自分享面板标题");
							web.setDescription("来自分享面板内容");
							web.setThumb(new UMImage(activity, R.drawable.lmusic));
							new ShareAction(activity).withMedia(web)
									.setPlatform(share_media)
									.setCallback(mShareListener)
									.share();
						}
					}
				});

		mShareAction.open();
	}

}
