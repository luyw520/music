package com.music.ui.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.music.ui.broadcastreceiver.MyBroadcastReceiver;
import com.music.ui.broadcastreceiver.State;
import com.music.lu.R;
import com.music.utils.DateUtil;
import com.music.utils.MediaUtil;
import com.music.helpers.PlayerHelpler;
import com.music.ui.view.widget.SliderRelativeLayout;

@SuppressLint("HandlerLeak") 
public class LockScreenActivity extends Activity {
	private final String TAG = "LockScreenActivity";
	private SliderRelativeLayout sliderRelativeLayout;
	public final static int MSG_LOCK_SUCESS = 1;
	
	
	private TextView tv_time, tv_day, tv_songname, tv_current_time, tv_duation;
	private ImageButton ib_player;
	private MyBroadcastReceiver myBroadcastReceiver;
	private final static int TIME=0;
	
	private PlayerHelpler mp3Util;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // title
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); //

		setContentView(R.layout.activity_lock_screen);
		
		mp3Util= PlayerHelpler.getDefault();
		initWedgit();

		registerReceiver();
		getTime();
	}
	
	/**
	 */
	private void registerReceiver() {
		State state = new MyLockState();
		IntentFilter filter = new IntentFilter();
		myBroadcastReceiver = new MyBroadcastReceiver(state, filter);
		registerReceiver(myBroadcastReceiver, filter);

	}
	private void unregisterReceiver() {
		unregisterReceiver(myBroadcastReceiver);
	}

	private void initWedgit() {

		tv_current_time = (TextView) findViewById(R.id.tv_current_time);
		tv_day = (TextView) findViewById(R.id.tv_day);
		tv_duation = (TextView) findViewById(R.id.tv_duation);
		tv_songname = (TextView) findViewById(R.id.tv_songname);
		tv_time = (TextView) findViewById(R.id.tv_time);

		sliderRelativeLayout = (SliderRelativeLayout) findViewById(R.id.sliderLayout);
		sliderRelativeLayout.setMainHandler(handler);
		
		
		ib_player=(ImageButton) findViewById(R.id.ib_player);
		
		if(mp3Util.isPlaying()){
			ib_player.setBackgroundResource(R.drawable.lock_pause);
		}else{
			ib_player.setBackgroundResource(R.drawable.lock_player);
		}
		
		tv_time.setText(DateUtil.getTime());
		tv_day.setText(DateUtil.getDay());
		tv_songname.setText(mp3Util.getCurrentMp3Info().getTitle());
		tv_duation.setText(MediaUtil.formatTime(mp3Util.getCurrentMp3Info().getDuration())
				+ "");
	}
	/**
	 * 每隔分钟发
	 */
	private void getTime(){
		handler.sendEmptyMessageDelayed(TIME, 1000*60);
	}
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOCK_SUCESS :
				finish();
				break;
			case TIME:
				Log.i(TAG, "TIME");
				tv_time.setText(DateUtil.getTime());
				handler.sendEmptyMessageDelayed(TIME, 1000*60);
				break;
			default:
				break;
			}
			
		}
	};

	class MyLockState implements State {

		@Override
		public void playMusicState() {
			tv_songname.setText(mp3Util.getCurrentMp3Info().getTitle());
			tv_duation.setText(MediaUtil.formatTime(mp3Util.getCurrentMp3Info()
					.getDuration()) + "");
//			ib_player
//					.setImageResource(R.drawable.lock_screen_pause_button_default);
			
			ib_player.setBackgroundResource(R.drawable.lock_pause);
		}

		@Override
		public void currentState(Intent intent) {
//			currentTime = intent.getIntExtra("currentTime", -1);
			tv_current_time.setText(MediaUtil.formatTime(intent.getIntExtra("currentTime", -1)));
		}

		@Override
		public void duration(Intent intent) {

		}

		@Override
		public void pauseMusicState() {
			ib_player.setBackgroundResource(R.drawable.lock_player);
		}

	}

	/**
	 * 指定�?有音乐中索引位置的音乐发送播放服�?
	 * 
	 */
	private void nextMusic() {
		
		mp3Util.nextMusic(false);
	};

	public void player(View view) {
		playMusic();
	}

	public void next(View view) {
		nextMusic();
	}
	
	private void playMusic() {
		mp3Util.playMusic();
	}

	/**
	 *
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("onKeyDown");
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				|| event.getKeyCode() == KeyEvent.KEYCODE_HOME
				|| event.getAction() == KeyEvent.KEYCODE_BACK) {

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver();
	}

}
