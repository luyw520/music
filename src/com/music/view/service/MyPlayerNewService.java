package com.music.view.service;
import java.util.TimerTask;

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
 * 播放音乐服务
 * 
 * @author luyuanwei
 * 
 */
@SuppressLint("HandlerLeak") 
public class MyPlayerNewService extends Service {
	
	private MediaPlayer mediaPlayer; // 媒体播放器对象
	/**
	 * 文件播放路径
	 */
	private String playPath; // 音乐文件路径
	@SuppressWarnings("unused")
	private int msg; // 播放信息
	/**
	 * 当前播放进度
	 */
	private int currentTime; // 当前播放进度
	private int duration; // 播放长度

	private static String TAG="MyPlayerService";
	
	private Mp3Util_New mp3Util;
	private Handler handler = new Handler() {

		@SuppressLint("HandlerLeak") @Override
		public void handleMessage(Message msg) {
			/**
			 * 歌曲进度
			 */
			if (msg.what == 1) {
				if (mediaPlayer != null) {
					// 得到当前播放的进度,发送广播出去
					if(mediaPlayer.isPlaying()){
						currentTime = mediaPlayer.getCurrentPosition();
						Intent intent = new Intent();
						intent.setAction(ConstantUtil.MUSIC_CURRENT);
						intent.putExtra("currentTime", currentTime);
						sendBroadcast(intent);
						/**
						 * 每隔一秒钟把当前播放的音乐播放时间和当前播放
						 */
						handler.sendEmptyMessageDelayed(1, 1000);
					}
					
				}
			}
			/**
			 * 歌词进度
			 */
			if (msg.what == 2) {
				if (mediaPlayer != null) {
					// 得到当前播放的进度,发送广播出去
					if(mediaPlayer.isPlaying()){
						currentTime = mediaPlayer.getCurrentPosition();
						Intent intent = new Intent();
						intent.setAction(ConstantUtil.LRC_CURRENT);
						intent.putExtra("currentTime", currentTime);
						sendBroadcast(intent);
						/**
						 * 每隔豪秒钟把当前播放的音乐播放时间和当前播放
						 */
						handler.sendEmptyMessageDelayed(2, 100);
					}
					
				}
			}
			
		}

	};
	@Override
	public void onCreate() {
		super.onCreate();
		DeBug.d(this, "MyPlayerService..................onCreate");
		mediaPlayer = new MediaPlayer();
		mp3Util=Mp3Util_New.getDefault();
		/**
		 * 设置音乐播放完成时的监听器
		 */
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp3Util.nextMusic(true);
				
			}
		});
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		DeBug.d(this, "MyPlayerService..................onStartCommand");
		/**是否是自定义任务栏里面的控件发来的消息*/
		if(intent==null){
			return 0;
		}
//		msg = intent.getIntExtra("MSG", 0);
//		Log.i(TAG, "*********************msg=" + msg);
//		
//			// 获取文件路径
//			playPath = intent.getStringExtra("url");
//			Log.i(TAG, "*********************path=" + playPath);
//			switch (msg) {
//			case AppConstant.PlayerMsg.PLAY_MSG:
////				play(0);
//				musicPlay(0);
//				break;
//			case AppConstant.PlayerMsg.PAUSE_MSG:
//				musicPause();
//				break;
//			case AppConstant.PlayerMsg.PROGRESS_CHANGE:
//				currentTime = intent.getIntExtra("progress", -1);
//				musicPlay(currentTime);
//				break;
//			case AppConstant.PlayerMsg.PLAYING_MSG:
//				cotinuePlay();
//				break;
//			default:
//				break;
//			}
//		}
		
		return 1;
	}

	private void cotinuePlay() {
		
//		if(!mp3Util.isPlaying()){
		
		
			
			mediaPlayer.start();
			mp3Util.setPlaying(true);
			//发送播放广播
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
			handler.sendEmptyMessage(1);
			handler.sendEmptyMessage(2);
//		}
		
	}
	
	private IBinder mIBinder=new MediaServiceSub();
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
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
			return 0;
		}

		@Override
		public int getPlayState() throws RemoteException {
			return 0;
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
			
//			System.exit(0);
//			Intent service = new Intent(Mp3Util_New.playService);
//			MyPlayerNewService.this.onUnbind(service);
			
		}

		@Override
		public int getCurMusicId() throws RemoteException {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void play(String playPath) throws RemoteException {
			// TODO Auto-generated method stub
			MyPlayerNewService.this.playPath=playPath;
			musicPlay(0);
		}
		@Override
		public void pause() throws RemoteException {
			// TODO Auto-generated method stub
			musicPause();
		}
		@Override
		public void seekTo(int progress) throws RemoteException {
			// TODO Auto-generated method stub
			musicPlay(progress);
		}
		@Override
		public void cotinuePlay() throws RemoteException {
			// TODO Auto-generated method stub
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
	 * 从当前时间currentTime处开始播放
	 * 
	 * @param currentTime
	 */
	private void musicPlay(int postion) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(playPath);
			mediaPlayer.prepare();
			mediaPlayer.start();
			mp3Util.setPlaying(true);
			
			Log.d(TAG, "postion:"+postion);
			if (postion > 0) {
				mediaPlayer.seekTo(postion);
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				return;
			}
			Intent intent = new Intent();
			intent.setAction(ConstantUtil.MUSIC_DURATION);// 新音乐长度更新动作
			duration = mediaPlayer.getDuration();
			
			
			mp3Util.setDuration(duration);
			mp3Util.getCurrentMp3Info().setDuration(duration);
			Log.i(TAG, "duration:"+duration);
			intent.putExtra("duration", duration);
			sendBroadcast(intent);
			
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
			
			handler.sendEmptyMessage(1);
			handler.sendEmptyMessage(2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	class MyTask extends TimerTask{

		@Override
		public void run() {
			if (mediaPlayer != null) {
				// 得到当前播放的进度,发送广播出去
				if(mp3Util.isPlaying()){
					currentTime = mediaPlayer.getCurrentPosition();
					Log.d(TAG, "MyTask currentTime:"+currentTime);
					/**
					 * 歌词跟新广播
					 */
					if(mp3Util.isShowLrc()){
						Intent intentLrc = new Intent();
						intentLrc.setAction(ConstantUtil.LRC_CURRENT);
						intentLrc.putExtra("currentTime", currentTime);
						sendBroadcast(intentLrc);
					}
					
					/**
					 * 歌曲进度广播
					 */
					Intent intent = new Intent();
					intent.setAction(ConstantUtil.MUSIC_CURRENT);
					intent.putExtra("currentTime", currentTime);
					mp3Util.setCurrentTime(currentTime);
					sendBroadcast(intent);
				}
				
			}
		}
		
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
		handler.removeMessages(1);
		handler.removeMessages(2);
		mp3Util.setCurrentTime(0);
	}
	@Override
	public void onDestroy() {
		DeBug.d(this, "...........................onDestroy");
		release();
//		timer.cancel();
		
		
	}
}
