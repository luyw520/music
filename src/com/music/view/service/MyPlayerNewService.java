package com.music.view.service;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.music.service.IMediaService;
import com.music.utils.ConstantUtil;
import com.music.utils.DeBug;
import com.music.utils.Mp3Util_New;

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
	
	private Mp3Util_New mp3Util;
	private static final int CURRENT_TIME=2;
	private static final int DELAY_TIME=100;
	private Handler handler = new Handler() {
		@SuppressLint("HandlerLeak") @Override
		public void handleMessage(Message msg) {
			if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
				if(msg.what==CURRENT_TIME){
					sendCurrentTimeBroadCast();
					/**
					 */
					handler.sendEmptyMessageDelayed(CURRENT_TIME, DELAY_TIME);
				}
				
				
			}
		}

	};
	private void sendCurrentTimeBroadCast(){
		currentTime = mediaPlayer.getCurrentPosition();
		Intent intent = new Intent();
		intent.setAction(ConstantUtil.MUSIC_CURRENT);
		intent.putExtra("currentTime", currentTime);
		sendBroadcast(intent);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		DeBug.d(this, "MyPlayerService..................onCreate");
		mediaPlayer = new MediaPlayer();
		mp3Util=Mp3Util_New.getDefault();
		/**
		 */
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp3Util.nextMusic(true);
				
			}
		});
	}
	

	private void cotinuePlay() {
//		if(!mp3Util.isPlaying()){
			mediaPlayer.start();
			mp3Util.setPlaying(true);
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
//			handler.sendEmptyMessage(1);
			handler.sendEmptyMessage(CURRENT_TIME);
//		}
		
	}
	
	private IBinder mIBinder=new MediaServiceSub();
	@Override
	public IBinder onBind(Intent intent) {
		DeBug.d(this, "..................onBind");
		return mIBinder;
	}
	class MediaServiceSub extends IMediaService.Stub{

		@Override
		public int duration() throws RemoteException {
			return duration;
		}
		@Override
		public int position() throws RemoteException {
			if(mediaPlayer==null){
				return -1;
			}
			return mediaPlayer.getCurrentPosition();
		}

		@Override
		public boolean getPlayState() throws RemoteException {
			if(mediaPlayer==null){
				return false;
			}
			return mediaPlayer.isPlaying();
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
		if (null != mediaPlayer && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			mp3Util.setPlaying(false);
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PAUSE));
		}

	}

	/**
	 *
	 */
	private void musicPlay(int time) {
		Log.d(TAG, "time:"+time);
		try {
			resetMediaPlay();
			mp3Util.setPlaying(true);
			if (time > 0) {
				mediaPlayer.seekTo(time);
				handler.sendEmptyMessage(CURRENT_TIME);
				sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
				return;
			}
			sendDurationBroadCast();
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
			handler.sendEmptyMessage(CURRENT_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	private void sendDurationBroadCast(){
		Intent intent = new Intent();
		intent.setAction(ConstantUtil.MUSIC_DURATION);//
		duration = mediaPlayer.getDuration();
		mp3Util.setDuration(duration);
		mp3Util.getCurrentMp3Info().setDuration(duration);
		Log.i(TAG, "duration:"+duration);
		intent.putExtra("duration", duration);
		sendBroadcast(intent);
	}
	private void resetMediaPlay() throws IOException{
		mediaPlayer.reset();
		mediaPlayer.setDataSource(playPath);
		mediaPlayer.prepare();
		mediaPlayer.start();
	}
	@Override
	public boolean onUnbind(Intent intent) {
		DeBug.d(this, "...........................onUnbind");
		release();
		return super.onUnbind(intent);
	}
	private void release(){
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		handler.removeMessages(CURRENT_TIME);
		mp3Util.setCurrentTime(0);
	}
	@Override
	public void onDestroy() {
		DeBug.d(this, "...........................onDestroy");
		release();
	}
}
