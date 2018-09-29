package com.music.ui.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.lu.library.log.DebugLog;
import com.music.Constant;
import com.music.MusicApplication;
import com.music.bean.MessageEvent;
import com.music.helpers.EventBusHelper;
import com.music.helpers.PlayerHelpler;
import com.music.utils.DeBug;

import java.io.IOException;

/**
 *
 * @author luyuanwei
 *
 */
@SuppressLint("HandlerLeak")
public class MyPlayerNewService extends Service {

	private MediaPlayer mediaPlayer; //
	/**
	 */
	private String playPath; //
	/**
	 */
	private int currentTime; //
	private int duration; //

	private static String TAG="MyPlayerService";

	private PlayerHelpler mp3Util;
	private static final int CURRENT_TIME=2;
	private static final int DELAY_TIME=100;
	private static final int CURRENT_TIME_LOOP=1000;
//	private Handler handler = new Handler() {
//		@SuppressLint("HandlerLeak") @Override
//		public void handleMessage(Message msg) {
//			if(getMediaPlayer() !=null&& getMediaPlayer().isPlaying()){
//				if(msg.what==CURRENT_TIME){
//					sendCurrentTimeBroadCast();
//					/**
//					 */
//					handler.sendEmptyMessageDelayed(CURRENT_TIME, DELAY_TIME);
//				}
//
//
//			}
//		}
//
//	};
	private Handler handler = new Handler();

	Runnable loopTask=new Runnable() {
		@Override
		public void run() {
			sendCurrentTimeBroadCast();
			if (getMediaPlayer().isPlaying()){
				handler.postDelayed(this,CURRENT_TIME_LOOP);
			}
		}
	};
	void sendLoopTask(){
		handler.removeCallbacks(loopTask);
		handler.postDelayed(loopTask,CURRENT_TIME_LOOP);
	}
	private void sendCurrentTimeBroadCast(){
		currentTime = getMediaPlayer().getCurrentPosition();
		DebugLog.d("发送事件："+ Constant.MUSIC_CURRENT);
		EventBusHelper.post(new MessageEvent(Constant.MUSIC_CURRENT,currentTime));
	}
	@Override
	public void onCreate() {
		super.onCreate();
		DeBug.d(this, "MyPlayerService..................onCreate");
		setMediaPlayer(new MediaPlayer());
		mp3Util= PlayerHelpler.getDefault();
		/**
		 */
		getMediaPlayer().setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp3Util.nextMusic(true);

			}
		});
		MusicApplication.getInstance().setMyPlayerNewService(this);
	}


	private void cotinuePlay() {
			getMediaPlayer().start();
			mp3Util.setPlaying(true);
		sendLoopTask();
		EventBusHelper.post((Constant.MUSIC_PLAYER));
	}

	private IBinder mIBinder=new MediaServiceSub();
	@Override
	public IBinder onBind(Intent intent) {
		DeBug.d(this, "..................onBind");
		return mIBinder;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	class MediaServiceSub extends IMediaService.Stub{

		@Override
		public int duration() throws RemoteException {
			return duration;
		}
		@Override
		public int position() throws RemoteException {
			if(getMediaPlayer() ==null){
				return -1;
			}
			return getMediaPlayer().getCurrentPosition();
		}

		@Override
		public boolean getPlayState() throws RemoteException {
			if(getMediaPlayer() ==null){
				return false;
			}
			return getMediaPlayer().isPlaying();
		}

		@Override
		public int getPlayMode() throws RemoteException {
			return 0;
		}

		@Override
		public void setPlayMode(int mode) throws RemoteException {

		}

		@Override
		public void sendPlayStateBrocast() throws RemoteException {

		}

		@Override
		public void exit() throws RemoteException {
//			System.exit(0);
			release();
//			stopSelf();

		}

		@Override
		public int getCurMusicId() throws RemoteException {
			return 0;
		}
		@Override
		public void play(String playPath) throws RemoteException {
			MyPlayerNewService.this.playPath=playPath;
			musicPlay(0);
		}
		@Override
		public void pause() throws RemoteException {
			musicPause();
		}
		@Override
		public void seekTo(int progress,String playPath) throws RemoteException {
			MyPlayerNewService.this.playPath=playPath;
			musicPlay(progress);
		}
		@Override
		public void cotinuePlay() throws RemoteException {
			MyPlayerNewService.this.cotinuePlay();
		}
	}
	public void musicPause() {
		if (null != getMediaPlayer() && getMediaPlayer().isPlaying()) {
			getMediaPlayer().pause();
			mp3Util.setPlaying(false);
			handler.removeCallbacks(loopTask);
			EventBusHelper.post(Constant.MUSIC_PAUSE);
//			sendBroadcast(new Intent(Constant.MUSIC_PAUSE));
		}

	}

	/**
	 *
	 */
	private void musicPlay(int time) {
		DebugLog.d(TAG, "time:"+time);
		try {
			resetMediaPlay();
			mp3Util.setPlaying(true);
			if (time > 0) {
				getMediaPlayer().seekTo(time);
//				handler.sendEmptyMessage(CURRENT_TIME);
//				sendBroadcast(new Intent(Constant.MUSIC_PLAYER));
				EventBusHelper.post((Constant.MUSIC_PLAYER));
				return;
			}
			sendDurationBroadCast();
			sendLoopTask();
//			sendBroadcast(new Intent(Constant.MUSIC_PLAYER));
			EventBusHelper.post(new MessageEvent(Constant.MUSIC_PLAYER));
//			handler.sendEmptyMessage(CURRENT_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	private void sendDurationBroadCast(){
		duration = getMediaPlayer().getDuration();
		mp3Util.setDuration(duration);
		mp3Util.getCurrentMp3Info().setDuration(duration);
		EventBusHelper.post(Constant.MUSIC_DURATION,duration);
	}
	private void resetMediaPlay() throws IOException{
		getMediaPlayer().reset();
		getMediaPlayer().setDataSource(playPath);
		getMediaPlayer().prepare();
		getMediaPlayer().start();
	}
	@Override
	public boolean onUnbind(Intent intent) {
		DeBug.d(this, "...........................onUnbind");
		release();
		return super.onUnbind(intent);
	}
	private void release(){
		if (getMediaPlayer() != null) {
			getMediaPlayer().stop();
			getMediaPlayer().release();
			setMediaPlayer(null);
		}
		handler.removeMessages(CURRENT_TIME);
		mp3Util.setCurrentTime(0);
	}
	@Override
	public void onDestroy() {
		DeBug.d(this, "...........................onDestroy");
		handler.removeCallbacks(loopTask);
		release();
	}
}
