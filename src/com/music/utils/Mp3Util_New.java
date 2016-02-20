package com.music.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.music.bean.Mp3Info;
import com.music.bean.MusicBaseInfo;
import com.music.service.IMediaService;

public class Mp3Util_New {
	private static final String TAG = "Mp3Util_New";
	public static final String playService = "com.music.service.myplayerService";
	private static Mp3Util_New mp3Util = null;

	public static Mp3Util_New getDefault() {
		return mp3Util;
	}

	public static Mp3Util_New getInstance() {
		return mp3Util;
	}

	public static void init(Context context) {
		mp3Util = new Mp3Util_New(context);

	}

	private MediaUtil mediaUtil;;
	private boolean isSortByTime = false;
	/**
	 * 所有的歌曲
	 */
	private List<Mp3Info> mp3Infos = null;

	/**
	 * 当前播放列表
	 */
	private List<? extends MusicBaseInfo> musicBaseInfos = new ArrayList<MusicBaseInfo>();
	private IMediaService mService;

	private ServiceConnection conn;

	private int index;

	private Context context;

	/**
	 * 当前正在播放的歌曲类
	 */
	// private Mp3Info currentMp3Info;
	private MusicBaseInfo currentMp3Info;

	/**
	 * 当前播放歌曲的时长
	 */
	private int duration;

	/**
	 * 当期已播放的时长
	 */
	private int currentTime;

	/**
	 * 当前播放状态
	 */
	private boolean isPlaying;
	/**
	 * 播放类型
	 */
	private int playType;
	/**
	 * 当前播放歌曲在所有歌曲中的索引
	 */
	private int listPosition;
	/**
	 * 是否显示歌词
	 */
	private boolean isShowLrc;

	private boolean isBindService=false;
	private Mp3Util_New(Context context) {
		this.context = context;
		init();
	}

	/**
	 * 当前播放列表类型,所有歌曲,
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
	private MusicUtils musicUtils;

	@SuppressWarnings("unchecked")
	public void addMp3(Mp3Info mp3Info) {

		if (!mp3Infos.contains(mp3Info)) {
			mp3Infos.add(mp3Info);
			Collections.sort(mp3Infos);
		}

	}

	/**
	 * 进度条变化时调用该方法发送服务
	 * 
	 * @param progress
	 */
	public void audioTrackChange(int progress) {
		sendService(AppConstant.PlayerMsg.PROGRESS_CHANGE, progress);

	}

	private void bindService() {
		Intent service = new Intent(playService);

		conn = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				mService = IMediaService.Stub.asInterface(service);
				isBindService=true;
				Log.d(TAG, "服务绑定成功....");
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				isBindService=false;
			}
		};
		context.bindService(service, conn, Context.BIND_AUTO_CREATE);

	}

	/**
	 * 改变播放类型
	 */
	public void changePlayType() {
		if ((playType + 1) > AppConstant.PlayerMsg.PLAYING_REPEAT) {
			playType = AppConstant.PlayerMsg.PLAYING_QUEUE;
		} else {
			playType++;
		}
	}

	public int getAllMp3Size() {
		return this.musicBaseInfos.size();
	}

	public int getCurrentPlayListSize() {
		return this.mp3Infos.size();
	}

	public MusicBaseInfo getCurrentMp3Info() {
		return currentMp3Info;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public int getIndex() {
		return index;
	}

	public int getListPosition() {
		return listPosition;
	}

	public List<Mp3Info> getMp3Infos() {
		return mp3Infos;
	}

	public int getPlayType() {
		return playType;
	}

	private void init() {
		mediaUtil = new MediaUtil();

		musicUtils = MusicUtils.getDefault();
		initCurrentMusicInfo(context);
		isPlaying = false;
		isSortByTime = false;
		isShowLrc = false;
		currentTime = 0;

		mPlayListType = 0;
		bindService();

		new Thread(new Runnable() {

			@Override
			public void run() {
				mp3Infos = mediaUtil.sortMp3InfosByTitle(context);
				// mp3Infos = musicUtils.getMusicInfos();
				musicBaseInfos = mp3Infos;
				currentMp3Info = musicBaseInfos.get(listPosition);
				DeBug.d(Mp3Util_New.this, "..........listPosition:"
						+ listPosition);
			}
		}).start();
	};

	/**
	 * 应用程序退出时保存当前播放的音乐信息
	 */
	public void initCurrentMusicInfo(Context context) {

		listPosition = SharedPreHelper.getIntValue(context, "listPosition", 0);
		playType = SharedPreHelper.getIntValue(context, "playType", 9);
		Log.i("Mp3Util", "获取值:listPosition=" + listPosition);
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

	public void completeNextMusic() {
		Log.i(TAG, "completeNextMusic()");
	}

	/**
	 * 根据播放类型发送下一首歌曲播放服务
	 */
	public void nextMusic(boolean isComplete) {
		Log.i(TAG, "nextMusic()");
		switch (playType) {
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			listPosition = randomNum();
			break;
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
			if (isComplete) {
				break;
			}
		case AppConstant.PlayerMsg.PLAYING_QUEUE:
			listPosition = (listPosition + 1 >= musicBaseInfos.size() ? 0
					: listPosition + 1);
			break;

		}
		Log.d(TAG, "listpostion:" + listPosition);
		playMusic(listPosition);
	}

	/**
	 * 发送播放或暂停音乐服务 如是当前是播放状态,则暂停.反之亦然
	 * 
	 * 
	 */
	public void playMusic() {
		int MSG = 0;
		if (isPlaying()) { // 如果正在播放，发送暂停信息
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
		Log.d(TAG, "listpostion:" + listPosition);
		Log.d(TAG, "currentTime:" + currentTime);
		sendService(MSG, 0);
	}

	/**
	 * 指定所有音乐中索引位置的音乐发送播放服务
	 * 
	 * @param listPosition
	 *            要播放音乐的索引位置
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
	 * 发送播放或暂停音乐服务 如是当前是播放状态,则暂停.反之亦然
	 * 
	 * 
	 */
	public void playMusic(Mp3Info mp3Info) {
		currentMp3Info = mp3Info;
		if (!mp3Infos.contains(currentMp3Info)) {
			// musicBaseInfos.add(currentMp3Info);
			mp3Infos.add(mp3Info);
			musicBaseInfos = mp3Infos;
		}
		sendService(AppConstant.PlayerMsg.PLAY_MSG, 0);
	}

	/**
	 * 上一曲
	 */
	public void previous_music() {
		switch (playType) {
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
		case AppConstant.PlayerMsg.PLAYING_QUEUE:
			listPosition = (listPosition > 0 ? listPosition - 1 : mp3Infos
					.size() - 1);
			break;
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			listPosition = randomNum();
			break;
		}
		currentMp3Info = mp3Infos.get(listPosition);
		sendService(AppConstant.PlayerMsg.PLAY_MSG, 0);
	}

	/**
	 * 在所有歌曲当中随机产生一个随机索引
	 * 
	 * @return
	 */
	private int randomNum() {
		return (int) (mp3Infos.size() * Math.random());
	}

	/**
	 * 随机一首
	 */
	public void randomPlay() {
		listPosition = randomNum();
		playMusic(listPosition);
	}

	/**
	 * 应用程序退出时保存当前播放的音乐信息
	 */
	public void saveCurrentMusicInfo(Context context) {
		Log.i("Mp3Util", "保存值:listPosition=" + listPosition);
		SharedPreHelper.setIntValue(context, "listPosition", listPosition);
		SharedPreHelper.setIntValue(context, "playType", playType);
	}

	private void sendService(int msg, int progress) {
		try {
			switch (msg) {
			case AppConstant.PlayerMsg.PLAY_MSG:
				mService.play(currentMp3Info.playPath);
				break;
			case AppConstant.PlayerMsg.PLAYING_MSG:
				mService.cotinuePlay();
				break;
			case AppConstant.PlayerMsg.PAUSE_MSG:
				mService.pause();
				break;
			case AppConstant.PlayerMsg.PROGRESS_CHANGE:
				mService.seekTo(progress,currentMp3Info.playPath);
				break;
			default:
				break;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public void setCurrentMp3Info(Mp3Info currentMp3Info) {
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

	public void sortMp3InfosByTime() {
		mp3Infos.clear();
		mp3Infos.addAll(mediaUtil.getMp3Infos(context));
		listPosition = mp3Infos.indexOf(currentMp3Info);
		setSortByTime(true);
	}

	public void sortMp3InfosByTitle() {
		mp3Infos.clear();
		mp3Infos.addAll(mediaUtil.sortMp3InfosByTitle(context));
		setSortByTime(false);
		listPosition = mp3Infos.indexOf(currentMp3Info);
	}

	public void unBindService() {
		if (conn != null&&isBindService) {
			context.unbindService(conn);
			isBindService=false;
		}
	}

	public List<? extends MusicBaseInfo> getMusicBaseInfos() {
		return musicBaseInfos;
	}

	/**
	 * 设置播放列表
	 * 
	 * @param musicBaseInfos
	 */
	private void setMusicBaseInfos(List<? extends MusicBaseInfo> musicBaseInfos) {

		this.musicBaseInfos = musicBaseInfos;

	}

	/**
	 * 设置播放列表
	 * 
	 * @param musicBaseInfos
	 */
	public void setMusicBaseInfos(List<? extends MusicBaseInfo> musicBaseInfos,
			int playListType) {
		// mPlayListType;
		if (mPlayListType != playListType) {
			DeBug.d(TAG, "playListType changed----------------------");
			mPlayListType = playListType;
			setMusicBaseInfos(musicBaseInfos);
		}

	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
