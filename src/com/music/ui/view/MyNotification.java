package com.music.ui.view;

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
import com.music.utils.AppConstant;
import com.music.utils.MediaUtil;
import com.music.helpers.PlayerHelpler;
import com.music.ui.activity.LocalMusicActivity;

import static android.content.Intent.FILL_IN_ACTION;

/**
 * 通知栏
 */
public class MyNotification {
	private  Context context;
	private NotificationManager notificationManager;
	private RemoteViews remoteViews;
	private Notification notification;
	private PlayerHelpler playerHelpler;
	private final static int id=0;
	public MyNotification(Context context){
		this.context=context;
		playerHelpler = PlayerHelpler.getDefault();
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

	/**
	 * 初始化自定义通知栏控件
	 * @return
	 */
	private RemoteViews initRemoteViews(){
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.notification_layout);



		Intent intent = new Intent(AppConstant.NOTIFICATION_PLAY_PAUSE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.iv_play_notification,
				pendingIntent);

		Intent intent2 = new Intent(AppConstant.NOTIFICATION_NEXT);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 1,
				intent2, FILL_IN_ACTION);
		remoteViews.setOnClickPendingIntent(R.id.iv_next_notification,
				pendingIntent2);
		return remoteViews;
	}
	@SuppressWarnings("deprecation")
	private void initNotification() {
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//		Notification.Builder builder=new Notification.Builder(context);
		notification = new Notification(R.drawable.playing_bar_default_avatar,
				playerHelpler.getCurrentMp3Info().getTitle(),
				System.currentTimeMillis());
		remoteViews=initRemoteViews();
		bindViewData();
		Intent intent3 = new Intent(Intent.ACTION_MAIN);
		intent3.setClass(context, LocalMusicActivity.class);
		intent3.addCategory(Intent.CATEGORY_LAUNCHER);
		intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent contentIntent = PendingIntent.getActivity(
				context, 0, intent3,
				PendingIntent.FLAG_UPDATE_CURRENT);

		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.contentIntent = contentIntent;
		notification.contentView = remoteViews;
		notificationManager.notify(id, notification);
	}
	public void notifyNotification(){
		notificationManager.notify(id, notification);
	}
	public void close(){
		notificationManager.cancel(id);
	}
	/**
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
		notificationManager.notify(id, notification);
	}
	void bindViewData(){
		Bitmap bitmap = MediaUtil.getArtwork(context,
				playerHelpler.getCurrentMp3Info().getSongId(),  playerHelpler
						.getCurrentMp3Info().getAlbumId(), true, true);
		remoteViews.setImageViewResource(R.id.iv_play_notification,
				R.drawable.img_button_notification_play_pause);
		remoteViews.setImageViewBitmap(R.id.iv_notification, bitmap);
		remoteViews.setTextViewText(R.id.tv_title_notification, playerHelpler.getCurrentMp3Info().getTitle());
		remoteViews.setTextViewText(R.id.tv_name,playerHelpler.getCurrentMp3Info().getArtist());
	}
	/**
	 */
	public void reset(){
		bindViewData();
		notificationManager.notify(id, notification);

	}
	public void cancel(){
		notificationManager.cancel(id);
	}
	/**
	 */
	private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String acitonString = intent.getAction();
			if (AppConstant.NOTIFICATION_PLAY_PAUSE.equals(acitonString)) {
				playerHelpler.playMusic();
			} else if (AppConstant.NOTIFICATION_NEXT.equals(acitonString)) {
				playerHelpler.nextMusic(false);
			}

		}
	};
}
