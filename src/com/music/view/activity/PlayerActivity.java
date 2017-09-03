package com.music.view.activity;

import java.util.List;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
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
import com.music.lrc.LyricLoadHelper.LyricListener;
import com.music.lrc.LyricView;
import com.music.lu.R;
import com.music.model.LyricModel;
import com.music.model.ShareModel;
import com.music.utils.AppConstant;
import com.music.utils.AsyncTaskUtil;
import com.music.utils.AsyncTaskUtil.IAsyncTaskCallBack;
import com.music.utils.BitmapUtils;
import com.music.utils.DeBug;
import com.music.utils.DialogUtil;
import com.music.utils.ImageUtil;
import com.music.utils.MediaUtil;
import com.music.utils.Mp3Util_New;
import com.music.view.MusicApplication;
import com.music.view.animator.ActivityAnimator;
import com.music.view.service.MyPlayerNewService;
import com.music.view.widget.RoundImageView;
import com.music.widget.slidingmenu.ScreenUtils;

/**
 *
 * 
 */
@SuppressLint("NewApi")
@ContentView(value = R.layout.activity_play_layout)
public class PlayerActivity extends BaseActivity {
	private final static String TAG = "PlayerActivity";

	@ViewInject(value = R.id.musicTitle)
	private TextView musicTitle;
	@ViewInject(value = R.id.musicArtist)
	private TextView musicArtist;

	@ViewInject(value = R.id.previous_music)
	private ImageButton previousBtn; //

	@ViewInject(value = R.id.repeat_music)
	private ImageButton repeatBtn; //

	@ViewInject(value = R.id.play_music)
	private ImageButton playBtn; //

	@ViewInject(value = R.id.next_music)
	private ImageButton nextBtn; //

	@ViewInject(value = R.id.sb_progress)
	private SeekBar music_progressBar; //

	@ViewInject(value = R.id.ib_back)
	private ImageButton ib_back;

	@ViewInject(value = R.id.img_share)
	private ImageButton img_share;

	@ViewInject(value = R.id.iv_music_album)
	private ImageView iv_music_album;

	@ViewInject(value = R.id.iv_needle)
	private ImageView iv_needle;

	@ViewInject(value = R.id.tv_current_progress)
	private TextView tv_current_progress; //

	@ViewInject(value = R.id.tv_final_progress)
	private TextView tv_finalProgress; //

	@ViewInject(value = R.id.rl_disc)
	private RelativeLayout rl_disc;

	@ViewInject(value = R.id.ll_lrc)
	private LinearLayout ll_lrc;

	private MyBroadcastReceiver playerBroadcastReceiver;

	@ViewInject(value = R.id.lyricView)
	private LyricView lyricView;

	private Mp3Util_New mp3Util;
	private MusicBaseInfo currentMp3Info;
	@ViewInject(value = R.id.ll_bg)
	private LinearLayout ll_bg;

	// private LyricDownloadManager manager;
	// private LyricLoadHelper loadHelper;

	private ObjectAnimator animatorPlay;
	private RotateAnimation animatorNeedlePlay;
	private RotateAnimation animatorNeedlePause;

	private AsyncTaskUtil asyncTaskUtil;
	private LyricModel lyricModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mp3Util = Mp3Util_New.getDefault();

		lyricModel = new LyricModel(this);
		lyricModel.setLyricListener(new MyLyricListener());
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
			lyricModel.putLyric(mp3Util.getCurrentMp3Info().getTitle(),
					lyricSentences);
			lyricView.setLyricSentences(lyricSentences, true);
			lyricView.setLoadLrc(LyricView.LRC_LOADED);
		}
		@Override
		public void onLyricSentenceChanged(int indexOfCurSentence) {
		}

	}

	private void findMp3Lrc(boolean isInit) {
		String title = mp3Util.getCurrentMp3Info().getTitle();
		if (lyricModel.isCache(title)) {
			lyricView.setLyricSentences(lyricModel.getLyricSentences(title),
					isInit);
			lyricView.postInvalidate();
			return;
		}
		asyncTaskUtil.setIAsyncTaskCallBack(new FindLrcCallBack());
		asyncTaskUtil.execute("");
	}
	private class FindLrcCallBack implements IAsyncTaskCallBack{
		String lrcPath = null;
		@Override
		public void onPostExecute(Object result) {
			if (lrcPath == null) {
				lyricView.setLyricSentences(null, false);
				lyricView.setLoadLrc(LyricView.NO_LRC);
			}
			lyricView.invalidate();
		}
		@Override
		public Object doInBackground(String... arg0) {
			currentMp3Info = mp3Util.getCurrentMp3Info();
			String songName = currentMp3Info.getTitle();
			String songer = currentMp3Info.getArtist();
			lrcPath = lyricModel.findLocalLrc(songer, songName);
			if (lrcPath == null) {
				lrcPath = lyricModel.searchLyricFromWeb(songName, songer);
			}
			if (lrcPath != null) {
				lyricModel.loadLyric(lrcPath);
			}

			return null;
		}
	
	}

	/**
	 * 专辑图片的旋转动画
	 */
	private void initAnimatorPlay(){
		if (animatorPlay == null) {
			animatorPlay = ObjectAnimator.ofFloat(iv_music_album, "rotation",
					0, 360);
			animatorPlay.setDuration(20 * 1000);
			animatorPlay.setRepeatCount(-1);
			animatorPlay.setInterpolator(new LinearInterpolator());
			if (mp3Util.isPlaying()) {
				animatorPlay.start();
			}
		}
	}
	private void initAnimation() {
		initAnimatorPlay();
		initAnimatorNeedlePlay();
		initAnimatorNeedlePause();
	}
	private void initAnimatorNeedlePause(){
		if (animatorNeedlePause == null) {
			animatorNeedlePause = new RotateAnimation(-20, 0, 0, 0);
			animatorNeedlePause.setDuration(3 * 1000);
			animatorNeedlePause.setFillAfter(true);
		}
	}
	private void initAnimatorNeedlePlay() {
		if (animatorNeedlePlay == null) {
			animatorNeedlePlay = new RotateAnimation(0, -20, 0, 0);
			animatorNeedlePlay.setDuration(3 * 1000);
			animatorNeedlePlay.setFillAfter(true);
			if (!mp3Util.isPlaying()) {
				iv_needle.startAnimation(animatorNeedlePlay);
			}
		}
		
	}
	private void setViewOnclickListener() {
		music_progressBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImpl());

	}

	@OnClick({ R.id.play_music, R.id.next_music, R.id.previous_music,
			R.id.repeat_music, R.id.ib_back, R.id.ib_changebg,
			R.id.img_favourite, R.id.img_share, R.id.rl_disc, R.id.ll_lrc,
			R.id.ib_play_list })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.play_music: //
			mp3Util.playMusic();
			break;
		case R.id.next_music: //
			next_music();
			break;
		case R.id.previous_music: //
			previous_music();
			break;
		case R.id.repeat_music: //
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
			new ShareModel().umengShareMusic(this);
			break;
		case R.id.rl_disc:
			isShowLrc(true);
			break;
		case R.id.ll_lrc:
			isShowLrc(false);
			break;
		case R.id.ib_play_list:
			finishAnimator();
			break;
		default:
			break;
		}

	}

	/**
	 * 是否显示歌词 界面
	 * @param isShowLrc true 显示歌词界面,false显示默认界面
	 */
	private void isShowLrc(boolean isShowLrc) {
		mp3Util.setShowLrc(isShowLrc);
		if (isShowLrc) {
			rl_disc.setVisibility(View.GONE);
			ll_lrc.setVisibility(View.VISIBLE);
			loadLrc(false);
		} else {
			rl_disc.setVisibility(View.VISIBLE);
			ll_lrc.setVisibility(View.GONE);
		}
	}

	/**
	 *
	 */
	private void setPlayType() {
		int drawableId = 0;
		String typeString = " ";
		switch (mp3Util.getPlayType()) {
		case AppConstant.PlayerMsg.PLAYING_QUEUE:
			drawableId = R.drawable.play_icn_loop;
			typeString = "loop";
			break;
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
			drawableId = R.drawable.play_icn_one;
			typeString = "one";
			break;
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			drawableId = R.drawable.play_icn_shuffle;
			typeString = "suffle";
			break;
		default:
			drawableId = R.drawable.playing_queue;
			break;
		}
		repeatBtn.setBackgroundResource(drawableId);

		DialogUtil.showToast(this, typeString);
	}

	/**
	 */
	private void changePlayType() {
		mp3Util.changePlayType();
		setPlayType();
	}

	/**
	 */
	public void previous_music() {
		mp3Util.previous_music();
	}

	/**
	 */
	public void next_music() {
		mp3Util.nextMusic(false);

	}

	/**
	 * 歌词
	 */
	private void loadLrc(boolean isInit) {
		if (mp3Util.isShowLrc()) {
			lyricView.setLoadLrc(LyricView.LRC_LOADIGN);
			findMp3Lrc(isInit);
		}

	}

	/**
	 */
	public void initViewData() {
		DeBug.d(this, "initViewData............");
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

		Bitmap bmp = MediaUtil.getArtwork(getApplicationContext(),
				currentMp3Info.getSongId(), currentMp3Info.getAlbumId(), true,
				true);
//		Bitmap bmpBg = MediaUtil.getMusicBitmap(getApplicationContext(),
//				currentMp3Info.getSongId(), currentMp3Info.getAlbumId(), ScreenUtils.getScreenWidth(getApplicationContext()),ScreenUtils.getScreenHeight(getApplicationContext()));

		iv_music_album.setImageBitmap(BitmapUtils.toRound(bmp,true));
//		ll_bg.setBackground(ImageUtil.bitmapToDrawable(ImageUtil.blurBitmap(
//				bmpBg, this)));

	}

	/**
	 *
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
					sb_progressChange(progress); //
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
	 *
	 * @param progress
	 */
	public void sb_progressChange(int progress) {
		mp3Util.audioTrackChange(progress);
	}

	private void registerReceiver() {
		State state = new MyState();

		IntentFilter filter = new IntentFilter();
		playerBroadcastReceiver = new MyBroadcastReceiver(state, filter);
		registerReceiver(playerBroadcastReceiver, filter);

		myPlayerNewService=MusicApplication.getInstance().getMyPlayerNewService();
	}
	private Handler mHandler=new Handler();
	private MyPlayerNewService myPlayerNewService;
	private Runnable progressRunnable=new Runnable() {
		@Override
		public void run() {
			if (myPlayerNewService.getMediaPlayer().isPlaying()){
				int currentTime=myPlayerNewService.getMediaPlayer().getCurrentPosition();
				mp3Util.setCurrentTime(currentTime);
				tv_current_progress.setText(MediaUtil.formatTime(currentTime));
				music_progressBar.setProgress(currentTime);

				lyricView.updateindex(currentTime);
				if (mp3Util.isShowLrc()) {
					lyricView.postInvalidate();
				}
			}
			mHandler.postDelayed(progressRunnable,100);
		}
	};
	/**
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
			mHandler.postDelayed(progressRunnable,100);
		}

		@Override
		public void currentState(Intent intent) {
			int currentTime = intent.getIntExtra("currentTime", -1);
			mp3Util.setCurrentTime(currentTime);
			tv_current_progress.setText(MediaUtil.formatTime(currentTime));
			music_progressBar.setProgress(currentTime);

			lyricView.updateindex(currentTime);
			if (mp3Util.isShowLrc()) {
				lyricView.postInvalidate();
			}
		}

		@Override
		public void duration(Intent intent) {
			int duration = intent.getIntExtra("duration", -1);
			Log.i(TAG, "duration:" + duration);
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
			mHandler.removeCallbacks(progressRunnable);
		}

	}

	@SuppressLint("NewApi")
	private void unregisterReceiver() {
		if (playerBroadcastReceiver != null) {
			unregisterReceiver(playerBroadcastReceiver);
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if(event.getAction()==KeyEvent.KEYCODE_BACK){
			finishAnimator();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void finishAnimator() {
		finish();

	}

}
