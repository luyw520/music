package com.music.service;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.music.lu.utils.AppConstant;
import com.music.lu.utils.ConstantUtil;
import com.music.lu.utils.Mp3Util;

/**
 * 播放音乐服务
 * 
 * @author luyuanwei
 * 
 */
@SuppressLint("HandlerLeak") 
public class MyPlayerService extends Service {
	
	private MediaPlayer mediaPlayer; // 媒体播放器对象
	private String path; // 音乐文件路径
	private int msg; // 播放信息
	/**
	 * 当前播放进度
	 */
	private int currentTime; // 当前播放进度
	private int duration; // 播放长度

	private static String TAG="MyPlayerService";
	
	private Mp3Util mp3Util;
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
		Log.i("MyPlayerService", "MyPlayerService");
		mediaPlayer = new MediaPlayer();
		mp3Util=Mp3Util.getDefault();
		/**
		 * 设置音乐播放完成时的监听器
		 */
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp3Util.nextMusic();
			}
		});
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		/**是否是自定义任务栏里面的控件发来的消息*/
		if(intent==null){
			return 0;
		}
		msg = intent.getIntExtra("MSG", 0);
		Log.i(TAG, "*********************msg=" + msg);
		
			// 获取文件路径
			path = intent.getStringExtra("url");
			Log.i(TAG, "*********************path=" + path);
			switch (msg) {
			case AppConstant.PlayerMsg.PLAY_MSG:
//				play(0);
				play(0);
				break;
			case AppConstant.PlayerMsg.PAUSE_MSG:
				pause();
				break;
			case AppConstant.PlayerMsg.PROGRESS_CHANGE:
				currentTime = intent.getIntExtra("progress", -1);
				play(currentTime);
				break;
			case AppConstant.PlayerMsg.PLAYING_MSG:
				cotinuePlay();
				break;
			default:
				break;
			}
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
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	public void pause() {
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
	private void play(int postion) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.start();
			mp3Util.setPlaying(true);
			
//			sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
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
	public void onDestroy() {
		
//		timer.cancel();
		
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		mp3Util.setCurrentTime(0);
	}
}
