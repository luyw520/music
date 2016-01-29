package com.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.music.lu.R;
import com.music.lu.utils.AppConstant;
import com.music.lu.utils.MediaUtil;
import com.music.lu.utils.Mp3Util_New;

public class MyNotification {
	private  Context context;
	private NotificationManager notificationManager;
	private RemoteViews remoteViews;
	private Notification notification;
	private String songTitle;
//	private PlayPauseDrawable playPauseDrawable;
//	private int playColor=0XFFE91E63;
//	private int pauseColor=0XFFffffff;
//	private int dwableDuaration=300;
	private Mp3Util_New mp3Util_New;
	public String getSongTitle() {
		return songTitle;
	}
	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}
	public MyNotification(Context context){
		this.context=context;
//		playPauseDrawable=new PlayPauseDrawable(30, playColor, pauseColor, dwableDuaration);
		
		mp3Util_New=Mp3Util_New.getDefault();
		initNotification();
		registerReceiver();
		
	}
	private void registerReceiver() {
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(AppConstant.NOTIFICATION_PLAY_PAUSE);
		filter2.addAction(AppConstant.NOTIFICATION_NEXT);
		context.registerReceiver(notificationReceiver, filter2);
	}
	/**
	 * 
	 */
	public void unregisterReceiver(){
		context.unregisterReceiver(notificationReceiver);
	};
	@SuppressWarnings("deprecation")
	private void initNotification() {
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		notification = new Notification(R.drawable.playing_bar_default_avatar,
				mp3Util_New.getCurrentMp3Info().getTitle(),
				System.currentTimeMillis());

		remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.notification_layout);
		
		remoteViews.setTextViewText(R.id.tv_title_notification, mp3Util_New.getCurrentMp3Info().getTitle());
		Bitmap bitmap = MediaUtil.getArtwork(context,
				mp3Util_New.getCurrentMp3Info().getId(), mp3Util_New
						.getCurrentMp3Info().getAlbumId(), true, true);
		remoteViews.setImageViewBitmap(R.id.iv_notification, bitmap);
		
		
		Intent intent = new Intent(AppConstant.NOTIFICATION_PLAY_PAUSE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.iv_play_notification,
				pendingIntent);

		Intent intent2 = new Intent(AppConstant.NOTIFICATION_NEXT);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 1,
				intent2, 1);
		remoteViews.setOnClickPendingIntent(R.id.iv_next_notification,
				pendingIntent2);

		Intent intent3 = new Intent(Intent.ACTION_MAIN);
		intent3.setClass(context, LocalMusicActivity.class);
//		intent3.setClass(context, MainActivity.class);
		intent3.addCategory(Intent.CATEGORY_LAUNCHER);
		intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent contentIntent = PendingIntent.getActivity(
				context, 0, intent3,
				PendingIntent.FLAG_UPDATE_CURRENT);

		notification.flags = Notification.FLAG_ONGOING_EVENT;
		// notification.contentIntent = contentIntent;
		notification.setLatestEventInfo(context, "�ƶ�Ӫ��", "",
				contentIntent);
		notification.contentView = remoteViews;
		notificationManager.notify(0, notification);

	}
	public void close(){
		notificationManager.cancel(0);
	}
	/**
	 * ���ò���״̬ͼ��
	 * @param isPlay 
	 */
	public void setPlayImageState(boolean isPlay){
		
		if(isPlay){
			remoteViews.setImageViewResource(R.id.iv_play_notification,
					R.drawable.img_button_notification_play_pause);
		}else{
			remoteViews.setImageViewResource(R.id.iv_play_notification,
					R.drawable.img_button_notification_play_play);
		}
		notificationManager.notify(0, notification);
	}
	/**
	 */
	public void reset(){
		
		
		Bitmap bitmap = MediaUtil.getArtwork(context,
				mp3Util_New.getCurrentMp3Info().getId(),  mp3Util_New
						.getCurrentMp3Info().getAlbumId(), true, true);
		remoteViews.setImageViewResource(R.id.iv_play_notification,
				R.drawable.img_button_notification_play_pause);
		remoteViews.setImageViewBitmap(R.id.iv_notification, bitmap);
		remoteViews.setTextViewText(R.id.tv_title_notification, mp3Util_New.getCurrentMp3Info().getTitle());
		notificationManager.notify(0, notification);
		
	}
	public void cancel(){
		notificationManager.cancel(0);
	} 
	/**
	 * ������Ϣ����������Ϣ
	 */
	private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String acitonString = intent.getAction();
			if (AppConstant.NOTIFICATION_PLAY_PAUSE.equals(acitonString)) {
				mp3Util_New.playMusic();
			} else if (AppConstant.NOTIFICATION_NEXT.equals(acitonString)) {
				mp3Util_New.nextMusic();
			}

		}
	};
}