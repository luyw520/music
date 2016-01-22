package com.music.view.activity;

import java.util.List;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.bean.LyricSentence;
import com.music.bean.MusicBaseInfo;
import com.music.broadcastreceiver.MyBroadcastReceiver;
import com.music.broadcastreceiver.State;
import com.music.lrc.LyricDownloadManager;
import com.music.lrc.LyricLoadHelper;
import com.music.lrc.LyricLoadHelper.LyricListener;
import com.music.lrc.LyricView;
import com.music.lu.R;
import com.music.model.LyricModel;
import com.music.utils.AppConstant;
import com.music.utils.AsyncTaskUtil;
import com.music.utils.AsyncTaskUtil.IAsyncTaskCallBack;
import com.music.utils.ConstantUtil;
import com.music.utils.DialogUtil;
import com.music.utils.ImageUtil;
import com.music.utils.MediaUtil;
import com.music.utils.Mp3Util_New;
import com.music.view.animator.ActivityAnimator;
import com.music.view.widget.RoundImageView;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

/**
 * 歌曲播放界面类
 * 
 * 
 */
@SuppressLint("NewApi")
@ContentView(value = R.layout.activity_play_layout)
public class PlayerActivity extends BaseActivity implements
		LyricView.LyricViewClickListener {
	private final static String TAG = "PlayerActivity";

	@ViewInject(value = R.id.musicTitle)
	private TextView musicTitle;
	@ViewInject(value = R.id.musicArtist)
	private TextView musicArtist;

	@ViewInject(value = R.id.previous_music)
	private ImageButton previousBtn; // 上一首

	@ViewInject(value = R.id.repeat_music)
	private ImageButton repeatBtn; // 重复（单曲循环、全部循环）

	@ViewInject(value = R.id.play_music)
	private ImageButton playBtn; // 播放（播放、暂停）

	@ViewInject(value = R.id.next_music)
	private ImageButton nextBtn; // 下一首

	@ViewInject(value = R.id.sb_progress)
	private SeekBar music_progressBar; // 歌曲进度

	@ViewInject(value = R.id.ib_back)
	private ImageButton ib_back;

	@ViewInject(value = R.id.img_share)
	private ImageButton img_share;

	@ViewInject(value = R.id.iv_music_album)
	private RoundImageView iv_music_album;
	
	@ViewInject(value = R.id.iv_needle)
	private ImageView iv_needle;

	@ViewInject(value = R.id.tv_current_progress)
	private TextView tv_current_progress; // 当前进度消耗的时间

	@ViewInject(value = R.id.tv_final_progress)
	private TextView tv_finalProgress; // 歌曲时间

	@ViewInject(value = R.id.rl_disc)
	private RelativeLayout rl_disc;

	@ViewInject(value = R.id.ll_lrc)
	private LinearLayout ll_lrc;
	
	private int currentTime; // 当前歌曲播放时间

	private MyBroadcastReceiver playerBroadcastReceiver;


	@ViewInject(value = R.id.lyricView)
	private LyricView lyricView;

	private Mp3Util_New mp3Util;
	private MusicBaseInfo currentMp3Info;
	@ViewInject(value = R.id.ll_bg)
	private LinearLayout ll_bg;

	private LyricDownloadManager manager;
	private LyricLoadHelper loadHelper;
	
	private ObjectAnimator animatorPlay;
	private RotateAnimation animatorNeedlePlay;
	private RotateAnimation animatorNeedlePause;

//	private Map<String, List<LyricSentence>> lycsList=new HashMap<String,List<LyricSentence>>();
	private AsyncTaskUtil asyncTaskUtil;
	private LyricModel lyricModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mp3Util = Mp3Util_New.getDefault();
		
		lyricModel=new LyricModel();
		manager = new LyricDownloadManager(this);
		loadHelper = new LyricLoadHelper();
		loadHelper.setLyricListener(new MyLyricListener());
		asyncTaskUtil = new AsyncTaskUtil();
		
		initViewData();
		setViewOnclickListener();
		setPlayType();
		registerReceiver();
		findMp3Lrc(true);
	}

	private class MyLyricListener implements LyricListener {

		@Override
		public void onLyricLoaded(List<LyricSentence> lyricSentences,
				int indexOfCurSentence) {
			Log.i(TAG, "加载成功");
			lyricModel.putLyric(mp3Util.getCurrentMp3Info().getTitle(), lyricSentences);
			lyricView.setLyricSentences(lyricSentences,true);
			lyricView.setLoadLrc(LyricView.LRC_LOADED);
		}

		@Override
		public void onLyricSentenceChanged(int indexOfCurSentence) {

		}

	}

	/**
	 * 加载歌词
	 */
	private void findMp3Lrc(boolean isInit) {
		String title=mp3Util.getCurrentMp3Info().getTitle();
		if(lyricModel.isCache(title)){
			lyricView.setLyricSentences(lyricModel.getLyricSentences(title),isInit);
			lyricView.invalidate();
			return;
		}
		asyncTaskUtil.setIAsyncTaskCallBack(new IAsyncTaskCallBack() {
			String lrcPath = null;
			@Override
			public void onPostExecute(Object result) {
				// 没有加载到歌词
				if (lrcPath == null) {
					lyricView.setLyricSentences(null,false);
					lyricView.setLoadLrc(LyricView.NO_LRC);
					Log.d(TAG, "网络加载失败..");
				}
				lyricView.invalidate();
			}
			@Override
			public Object doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				currentMp3Info = mp3Util.getCurrentMp3Info();
				String songName = currentMp3Info.getTitle();
				String songer = currentMp3Info.getArtist();
				// 查看本地
				lrcPath = lyricModel.findLocalLrc(songer, songName);
				if (lrcPath == null) {
					// 本地没有，网络加载
					lrcPath = manager.searchLyricFromWeb(songName, songer);
					Log.d(TAG, "网络加载歌词..");
				}
				if (lrcPath != null) {
					// 解析歌词
					loadHelper.loadLyric(lrcPath);
				}

				return null;
			}
		});
		asyncTaskUtil.execute("");

	}


	private void initAnimation() {
		if (animatorPlay == null) {
			animatorPlay = ObjectAnimator.ofFloat(iv_music_album, "rotation", 0,
					360);
			animatorPlay.setDuration(20 * 1000);
			animatorPlay.setRepeatCount(-1);

			if(mp3Util.isPlaying()){
				animatorPlay.start();
			}
		}
		
		if(animatorNeedlePlay==null){
			animatorNeedlePlay=new RotateAnimation(0, -20, 0, 0);
			animatorNeedlePlay.setDuration(3*1000);
			animatorNeedlePlay.setFillAfter(true);
			if(!mp3Util.isPlaying()){
				iv_needle.startAnimation(animatorNeedlePlay);
			}
		}
		
		if(animatorNeedlePause==null){
			animatorNeedlePause=new RotateAnimation(-20, 0, 0, 0);
			animatorNeedlePause.setDuration(3*1000);
			animatorNeedlePause.setFillAfter(true);
		}
		
		
	}

	private void setViewOnclickListener() {
		music_progressBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImpl());
		lyricView.setLyricViewClickListener(this);

	}

	@OnClick({ R.id.play_music, R.id.next_music, R.id.previous_music,
			R.id.repeat_music, R.id.ib_back, R.id.ib_changebg,
			R.id.img_favourite, R.id.img_share, R.id.rl_disc, R.id.ll_lrc,
			R.id.ib_play_list })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.play_music: // 点击播放
			mp3Util.playMusic();
			break;
		case R.id.next_music: // 点击下一首
			next_music();
			break;
		case R.id.previous_music: // 点击上一首
			previous_music();
			break;
		case R.id.repeat_music: // 点击播放顺序
			changePlayType();
			break;
		case R.id.ib_back:
			finishAnimator();
			break;
		case R.id.ib_changebg:
			startActivity(new Intent(this, ChangeBgActivity.class));
			break;
		case R.id.img_favourite:
			break;
		case R.id.img_share:
			umengShareMusic();
			break;
		case R.id.rl_disc:
			mp3Util.setShowLrc(true);
			rl_disc.setVisibility(View.GONE);
			ll_lrc.setVisibility(View.VISIBLE);
			loadLrc(false);
			break;
		case R.id.ll_lrc:
			mp3Util.setShowLrc(false);
			rl_disc.setVisibility(View.VISIBLE);
			ll_lrc.setVisibility(View.GONE);
			break;
		case R.id.ib_play_list:
			finishAnimator();
			break;
		default:
			break;
		}

	}
	/**
	 * umeng share api
	 */
	private void umengShareMusic() {
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
				"1104335219", "J68iUn08AUZwHWrJ");
		qZoneSsoHandler.addToSocialSDK();
		UMQQSsoHandler qHandler = new UMQQSsoHandler(this, "1104335219",
				"J68iUn08AUZwHWrJ");
		qHandler.addToSocialSDK();

		UMSocialService service = UMServiceFactory
				.getUMSocialService("com.umeng.share");

		service.setShareContent("hehehe");
		service.openShare(this, false);
	}

	/**
	 * 根据播放类型 设置背景图
	 * 
	 */
	private void setPlayType() {
		int drawableId = 0;
		String typeString = " ";
		switch (mp3Util.getPlayType()) {
		case AppConstant.PlayerMsg.PLAYING_QUEUE:
			drawableId = R.drawable.play_icn_loop;
			typeString = "顺序播放";
			break;
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
			drawableId = R.drawable.play_icn_one;
			typeString = "单曲循环";
			break;
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			drawableId = R.drawable.play_icn_shuffle;
			typeString = "随机播放";
			break;
		default:
			drawableId = R.drawable.playing_queue;
			break;
		}
		repeatBtn.setBackgroundResource(drawableId);

		DialogUtil.showToast(this, typeString);
	}

	/**
	 * 改变播放类型
	 */
	private void changePlayType() {
		mp3Util.changePlayType();
		setPlayType();

	}

	/**
	 * 上一曲
	 */
	public void previous_music() {
		mp3Util.previous_music();
		// loadLrc();
	}

	/**
	 * 下一曲
	 */
	public void next_music() {
		mp3Util.nextMusic(false);

	}

	/**
	 * 加载歌词
	 */
	private void loadLrc(boolean isInit) {

		if (mp3Util.isShowLrc()) {
			lyricView.setLoadLrc(LyricView.LRC_LOADIGN);
			findMp3Lrc(isInit);
		}

	}

	/**
	 * 初始化控件数据
	 */
	public void initViewData() {
		initAnimation();
		currentMp3Info = mp3Util.getCurrentMp3Info();
		musicTitle.setText(currentMp3Info.getTitle());
		musicArtist.setText(currentMp3Info.getArtist());

		music_progressBar.setMax((int) currentMp3Info.getDuration());
		music_progressBar.setProgress(mp3Util.getCurrentTime());

		tv_current_progress.setText(MediaUtil.formatTime(mp3Util
				.getCurrentTime()));
		tv_finalProgress.setText(MediaUtil.formatTime(mp3Util
				.getCurrentMp3Info().getDuration()));

		if (mp3Util.isPlaying()) {
			playBtn.setBackgroundResource(R.drawable.btn_pause);
			animatorPlay.start();
		} else {
			playBtn.setBackgroundResource(R.drawable.btn_play);
			iv_music_album.clearAnimation();
		}

		Bitmap bmp = MediaUtil
				.getArtwork(getApplicationContext(), currentMp3Info.getSongId(),
						currentMp3Info.getAlbumId(), true, true);
	
		
		Bitmap bmpBg= MediaUtil
				.getArtworkOriginal(getApplicationContext(), currentMp3Info.getSongId(),
						currentMp3Info.getAlbumId(), true, false);
		
		iv_music_album.setImageBitmap(bmp);
//		ll_bg.setBackground(ImageUtil.bitmapToDrawable(ImageUtil.blurBitmap(bmp, this)));
		ll_bg.setBackground(ImageUtil.bitmapToDrawable(ImageUtil.blurBitmap(bmpBg, this)));
		

	}

	/**
	 * 进度条改变事件监听器
	 * 
	 * @author luyuanwei
	 * 
	 */
	private class OnSeekBarChangeListenerImpl implements
			OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			switch (seekBar.getId()) {
			case R.id.sb_progress:
				if (fromUser) {
					sb_progressChange(progress); // 用户控制进度的改变
				}
				break;
			}
		}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	/**
	 * 进度条变化时调用该方法发送服务
	 * 
	 * @param progress
	 */
	public void sb_progressChange(int progress) {
//		mp3Util.sb_progressChange(progress);
		mp3Util.audioTrackChange(progress);
	}

	// 注册广播接收器
	private void registerReceiver() {
		State state = new MyState();

		IntentFilter filter = new IntentFilter();
		playerBroadcastReceiver = new MyBroadcastReceiver(state, filter);
		registerReceiver(playerBroadcastReceiver, filter);

		IntentFilter filterlrc = new IntentFilter();
		filterlrc.addAction(ConstantUtil.LRC_CURRENT);
		registerReceiver(LrcBroadcastReceiver, filterlrc);
	}

	/**
	 * 根据播放器的状态改变界面的状态
	 * 
	 * @author Administrator
	 * 
	 */
	class MyState implements State {
		@Override
		public void playMusicState() {
			playBtn.setBackgroundResource(R.drawable.btn_pause);

			if (animatorPlay.isPaused()) {
				animatorPlay.resume();
			} else {
				animatorPlay.start();
			}
			iv_needle.startAnimation(animatorNeedlePause);
		}

		@Override
		public void currentState(Intent intent) {
			currentTime = intent.getIntExtra("currentTime", -1);
//			DeBug.d(PlayerActivity.this, "currentState currentTime:"+currentTime);
			mp3Util.setCurrentTime(currentTime);

			tv_current_progress.setText(MediaUtil.formatTime(currentTime));
			music_progressBar.setProgress(currentTime);				 
		}

		@Override
		public void duration(Intent intent) {
			
			int duration = intent.getIntExtra("duration", -1);
			Log.i(TAG, "duration:"+duration); 
			music_progressBar.setMax(duration);
			tv_finalProgress.setText(MediaUtil.formatTime(duration));
			musicTitle.setText(mp3Util.getCurrentMp3Info().getTitle());
			musicArtist.setText(mp3Util.getCurrentMp3Info().getArtist());
			initViewData();
			lyricView.clear();
			loadLrc(true);
		}

		@Override
		public void pauseMusicState() {
			playBtn.setBackgroundResource(R.drawable.btn_play);
			animatorPlay.pause();
			
			iv_needle.startAnimation(animatorNeedlePlay);
		}

	}

	@SuppressLint("NewApi")
	private void unregisterReceiver() {
		if (playerBroadcastReceiver != null) {
			unregisterReceiver(playerBroadcastReceiver);
		}
		unregisterReceiver(LrcBroadcastReceiver);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}
	/**
	 * 显示歌词的控件接收服务发来的消息,每0.1s刷新一次
	 */
	private BroadcastReceiver LrcBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mp3Util.isShowLrc()) {
				int currentTime = intent.getIntExtra("currentTime", -1);
//				DeBug.d(PlayerActivity.this, "currentTime:"+currentTime);
				lyricView.updateindex(currentTime);
			}
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if(event.getAction()==KeyEvent.KEYCODE_BACK){
			finishAnimator();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * finish后的动画
	 */
	private void finishAnimator() {
		finish();
		ActivityAnimator animator = new ActivityAnimator();
		try {
			animator.getClass()
					.getMethod(animator.randomAnimator(), Activity.class)
					.invoke(animator, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void lyricViewClick() {
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

}
