package com.music.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.music.bean.MusicInfo;
import com.music.MusicApplication;
import com.music.ui.service.IMediaService;
import com.music.ui.service.MyPlayerNewService;
import com.music.utils.AppConstant;
import com.music.utils.DeBug;
import com.music.utils.SharedPreHelper;

/**
 * 音乐播放辅助类
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
	 */
	private int duration;

	/**
	 */
	private int currentTime;

	/**
	 */
	private boolean isPlaying;
	/**
	 */
	private int playType;
	/**
	 */
	private int listPosition;
	/**
	 */
	private boolean isShowLrc;

	private boolean isBindService = false;

	private PlayerHelpler(Context context) {
		this.context = context;
		init();
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
			playType = AppConstant.PlayerMsg.PLAYING_QUEUE;
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
		return currentMp3Info;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public int getIndex() {
		return index;
	}


//	public List<Mp3Info> getMp3Infos() {
//		return musicBaseInfos;
//	}

	public int getPlayType() {
		return playType;
	}

	private void init() {
//		mediaUtil = new MediaUtil();

		initCurrentMusicInfo(context);
		isPlaying = false;
		isSortByTime = false;
		isShowLrc = false;
		currentTime = 0;

		mPlayListType = 0;
		bindService();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				musicBaseInfos = MusicModel.getInstance().sortMp3InfosByTitle(context);
//
//				musicBaseInfos = musicBaseInfos;
//				DeBug.d(PlayerHelpler.this, "..........:"
//						+ musicBaseInfos.size()+",listPosition:"+listPosition);
//				if (!musicBaseInfos.isEmpty()) {
//					if (listPosition>=musicBaseInfos.size()){
//						listPosition=0;
//					}
//					currentMp3Info = musicBaseInfos.get(listPosition);
//					DeBug.d(PlayerHelpler.this, "..........listPosition:"
//							+ listPosition);
//				}else{
//					currentMp3Info=new MusicInfo();
//				}
//			}
//		}).start();
	};

	/**
	 */
	public void initCurrentMusicInfo(Context context) {

		listPosition = SharedPreHelper.getIntValue(context, "listPosition", 0);
		playType = SharedPreHelper.getIntValue(context, "playType", 9);
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
		Log.d(TAG, "listpostion:" + listPosition);
		Log.d(TAG, "currentTime:" + currentTime);
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
		case AppConstant.PlayerMsg.PLAYING_QUEUE:
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
		SharedPreHelper.setIntValue(context, "listPosition", listPosition);
		SharedPreHelper.setIntValue(context, "playType", playType);
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

//	public void sortMp3InfosByTime() {
//		mp3Infos.clear();
//		mp3Infos.addAll(mediaUtil.getMp3Infos(context));
//		listPosition = mp3Infos.indexOf(currentMp3Info);
//		setSortByTime(true);
//	}

//	public void sortMp3InfosByTitle() {
//		mp3Infos.clear();
//		mp3Infos.addAll(mediaUtil.sortMp3InfosByTitle(context));
//		setSortByTime(false);
//		listPosition = mp3Infos.indexOf(currentMp3Info);
//	}

	public void unBindService() {
		if (conn != null && isBindService) {
			context.unbindService(conn);
			isBindService = false;
		}
	}

	public List<MusicInfo> getMusicBaseInfos() {
		return musicBaseInfos;
	}

//	/**
//	 *
//	 * @param musicBaseInfos
//	 */
	private void setMusicBaseInfos(List<MusicInfo> musicBaseInfos) {
		this.musicBaseInfos = musicBaseInfos;
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
