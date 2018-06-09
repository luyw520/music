package com.music.helpers;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.music.MusicApplication;
import com.music.bean.MusicInfo;
import com.music.model.MusicModel;
import com.music.ui.service.IMediaService;
import com.music.ui.service.MyPlayerNewService;
import com.music.utils.AppConstant;
import com.music.utils.DeBug;
import com.music.utils.DebugLog;
import com.music.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import static com.music.utils.AppConstant.PlayerMsg.PLAYING_QUEUE;
import static com.music.utils.ConstUtils.LIST_POSITION_KEY;
import static com.music.utils.ConstUtils.PLAY_TYPE_KEY;

/**
 * 音乐播放辅助类
 * 绑定了播放服务
 * 和播放服务进行通信
 */
public class PlayerHelpler {
	private static final String TAG = "PlayerHelpler";
	public static final String playService = "com.music.service.myplayerService";
	private static PlayerHelpler mp3Util = null;
	public static PlayerHelpler getDefault() {
		return mp3Util;
	}
	public static PlayerHelpler getInstance() {
		return mp3Util;
	}

	public static void init(Context context) {
		mp3Util = new PlayerHelpler(context);

	}
	private boolean isSortByTime = false;
	/**
	 */

	/**
	 */
	private List<MusicInfo> musicBaseInfos = new ArrayList<>();
	private IMediaService mService;

	private ServiceConnection conn;

	private int index;

	private Context context;

	/**
	 */
	// private Mp3Info currentMp3Info;
	private MusicInfo currentMp3Info=new MusicInfo();

	/**
	 * 当前歌曲时长
	 */
	private int duration;

	/**
	 * 当前播放进度
	 */
	private int currentTime;

	/**
	 *
	 */
	private boolean isPlaying;
	/**
	 * 播放类型
	 */
	private int playType;
	/**
	 * 当前播放索引值
	 */
	private int listPosition;
	/**
	 * 是否显示歌词
	 */
	private boolean isShowLrc;

	private boolean isBindService = false;

	private PlayerHelpler(Context context) {
		this.context = context;
//		init();
	}

	/**
	 *
	 * @author Steven
	 *
	 */
	public final static int PLAY_BY_SONG = 0;
	public final static int PLAY_BY_ARTIST = 1;
	public final static int PLAY_BY_BLUM = 2;
	public final static int PLAY_BY_FOLDER = 3;
	private int mPlayListType;
	@SuppressWarnings("unused")
//	private MusicUtils musicUtils;


	/**
	 *
	 * @param progress
	 */
	public void audioTrackChange(int progress) {
		sendService(AppConstant.PlayerMsg.PROGRESS_CHANGE, progress);

	}

	private void bindService() {
		Intent service = new Intent(MusicApplication.getInstance(),MyPlayerNewService.class);

		conn = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				mService = IMediaService.Stub.asInterface(service);
				isBindService = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				isBindService = false;
			}
		};
		context.bindService(service, conn, Context.BIND_AUTO_CREATE);

	}

	/**
	 */
	public void changePlayType() {
		if ((playType + 1) > AppConstant.PlayerMsg.PLAYING_REPEAT) {
			playType = PLAYING_QUEUE;
		} else {
			playType++;
		}
	}

	public int getAllMp3Size() {
		return this.musicBaseInfos.size();
	}

	public int getCurrentPlayListSize() {
		return this.musicBaseInfos.size();
	}

	public MusicInfo getCurrentMp3Info() {

		if (TextUtils.isEmpty(currentMp3Info.getTitle())){
			DebugLog.d("musicBaseInfos.isEmpty()："+musicBaseInfos.isEmpty());
			if (!musicBaseInfos.isEmpty()){
				currentMp3Info=musicBaseInfos.get(0);
			}
		}
		DebugLog.d(currentMp3Info);
		return currentMp3Info;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public int getIndex() {
		return index;
	}

	public int getPlayType() {
		return playType;
	}

	public void init() {
//		mediaUtil = new MediaUtil();

		initCurrentMusicInfo(context);
		setMusicBaseInfos(MusicModel.getInstance().getMusicList());
		isPlaying = false;
		isSortByTime = false;
		isShowLrc = false;
		currentTime = 0;

		mPlayListType = 0;
		if (listPosition<musicBaseInfos.size()){
			currentMp3Info = musicBaseInfos.get(listPosition);
		}else{
			if (!musicBaseInfos.isEmpty()){
				currentMp3Info = musicBaseInfos.get(0);
			}

		}

		bindService();
	};

	/**
	 */
	public void initCurrentMusicInfo(Context context) {

		listPosition = (int) SPUtils.get(LIST_POSITION_KEY, 0);
		playType = (int) SPUtils.get(context, PLAY_TYPE_KEY, PLAYING_QUEUE);
		// playType=9;

	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public boolean isShowLrc() {
		return isShowLrc;
	}

	public boolean isSortByTime() {
		return isSortByTime;
	}


	/**
	 */
	public void nextMusic(boolean isComplete) {
		switch (playType) {
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			listPosition = randomNum();
			break;
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
			if (isComplete) {
				break;
			}
		case PLAYING_QUEUE:
			listPosition = (listPosition + 1 >= musicBaseInfos.size() ? 0
					: listPosition + 1);
			break;

		}
		SPUtils.put(LIST_POSITION_KEY,listPosition);
		playMusic(listPosition);
	}

	/**
	 *
	 *
	 */
	public void playMusic() {
		int MSG = 0;
		if (isPlaying()) { //
			MSG = AppConstant.PlayerMsg.PAUSE_MSG;
			isPlaying = false;
		} else {

			if (currentTime > 0) {
				MSG = AppConstant.PlayerMsg.PLAYING_MSG;
			} else {
				MSG = AppConstant.PlayerMsg.PLAY_MSG;
			}

			isPlaying = true;
		}
//		Log.d(TAG, "listpostion:" + listPosition);
//		Log.d(TAG, "currentTime:" + currentTime);
		sendService(MSG, 0);
	}

	/**
	 *
	 * @param listPosition
	 */
	public void playMusic(int listPosition) {

		if (listPosition < musicBaseInfos.size()) {

			this.isPlaying = true;
			this.listPosition = listPosition;
			this.currentMp3Info = musicBaseInfos.get(listPosition);

			Log.d(TAG, "listpostion:" + listPosition);
			Log.d(TAG, "getTitle:" + currentMp3Info.getTitle());
			sendService(AppConstant.PlayerMsg.PLAY_MSG, 0);
		}
	}

	/**
	 *
	 *
	 */
	public void playMusic(MusicInfo mp3Info) {
		currentMp3Info = mp3Info;
		if (!musicBaseInfos.contains(currentMp3Info)) {
			// musicBaseInfos.add(currentMp3Info);
			musicBaseInfos.add(mp3Info);
		}
		sendService(AppConstant.PlayerMsg.PLAY_MSG, 0);
	}

	/**
	 */
	public void previous_music() {
		switch (playType) {
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
		case PLAYING_QUEUE:
			listPosition = (listPosition > 0 ? listPosition - 1 : musicBaseInfos
					.size() - 1);
			break;
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			listPosition = randomNum();
			break;
		}
		currentMp3Info = musicBaseInfos.get(listPosition);
		sendService(AppConstant.PlayerMsg.PLAY_MSG, 0);
	}

	/**
	 *
	 * @return
	 */
	private int randomNum() {
		return (int) (musicBaseInfos.size() * Math.random());
	}

	/**
	 */
	public void randomPlay() {
		listPosition = randomNum();
		playMusic(listPosition);
	}

	/**
	 */
	public void saveCurrentMusicInfo(Context context) {
		SPUtils.put( LIST_POSITION_KEY, listPosition);
		SPUtils.put(PLAY_TYPE_KEY, playType);
	}

	private void sendService(int msg, int progress) {
		try {
			switch (msg) {
			case AppConstant.PlayerMsg.PLAY_MSG:
				mService.play(currentMp3Info.getPlayPath());
				break;
			case AppConstant.PlayerMsg.PLAYING_MSG:
				mService.cotinuePlay();
				break;
			case AppConstant.PlayerMsg.PAUSE_MSG:
				mService.pause();
				break;
			case AppConstant.PlayerMsg.PROGRESS_CHANGE:
				mService.seekTo(progress, currentMp3Info.getPlayPath());
				break;
			default:
				break;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public void setCurrentMp3Info(MusicInfo currentMp3Info) {
		this.currentMp3Info = currentMp3Info;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setListPosition(int listPosition) {
		this.listPosition = listPosition;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public void setPlayType(int playType) {
		this.playType = playType;
	}

	public void setShowLrc(boolean isShowLrc) {
		this.isShowLrc = isShowLrc;
	}

	public void setSortByTime(boolean isSortByTime) {
		this.isSortByTime = isSortByTime;
	}


	public void unBindService() {
		if (conn != null && isBindService) {
			context.unbindService(conn);
			isBindService = false;
		}
	}

	public List<MusicInfo> getMusicBaseInfos() {
		return MusicModel.getInstance().getMusicList();
	}

//	/**
//	 *
//	 * @param musicBaseInfos
//	 */
	public void setMusicBaseInfos(List<MusicInfo> musicBaseInfos) {
		this.musicBaseInfos.clear();
		this.musicBaseInfos.addAll(musicBaseInfos);
	}

	/**
	 *
	 */
	public void setMusicBaseInfos(List<MusicInfo> musicBaseInfos,
			int playListType) {
		// mPlayListType;
//		if (mPlayListType != playListType) {
			DeBug.d(TAG, "playListType changed----------------------");
			mPlayListType = playListType;
			setMusicBaseInfos(musicBaseInfos);
//		}

	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
