package com.music.utils;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.music.bean.Mp3Info;
import com.music.service.MyPlayerService;

public class Mp3Util {
	private static final String TAG = "Mp3Util";

	private static Mp3Util mp3Util = null;

	private MediaUtil mediaUtil;
	
	private boolean isSortByTime=false;
	/**
	 * 所有的歌曲
	 */
	private List<Mp3Info> mp3Infos = null;

	public List<Mp3Info> getMp3Infos() {
		return mp3Infos;
	}
	@SuppressWarnings("unchecked")
	public void addMp3(Mp3Info mp3Info){
		
		if(!mp3Infos.contains(mp3Info)){
			mp3Infos.add(mp3Info);
			Collections.sort(mp3Infos);
		}
		
	}
	private int index;
//	public static Mp3Util getInstance(Context context) {
//		if (mp3Util == null) {
//			mp3Util = new Mp3Util(context);
//		}
//		return mp3Util;
//	}
	public static Mp3Util getInstance() {
		return mp3Util;
	}
	public static Mp3Util getDefault() {
		return mp3Util;
	}
	public static void init(Context context){
		mp3Util = new Mp3Util(context);
	}
	
	private Context context;
	/**
	 * 当前正在播放的歌曲类
	 */
	private Mp3Info currentMp3Info;
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

	private Mp3Util(Context context) {
		this.context = context;
		init();
	}
	
	
	private void init() {
		mediaUtil = new MediaUtil();
		initCurrentMusicInfo(context);
		isPlaying=false;
		isSortByTime=false;
		isShowLrc=false;
		currentTime=0;
		mp3Infos=mediaUtil.sortMp3InfosByTitle(context);
		currentMp3Info = mp3Infos.get(listPosition);
		Log.d("Mp3Util",currentMp3Info.getDisplayName());
		Log.d("Mp3Util","listPosition:"+listPosition);
	}
	/**
	 * 应用程序退出时保存当前播放的音乐信息
	 */
	public void saveCurrentMusicInfo(Context context){
		Log.i("Mp3Util", "保存值:listPosition="+listPosition);
		SharedPreHelper.setIntValue(context, "listPosition", listPosition);
		SharedPreHelper.setIntValue(context, "playType", playType);
	}
	/**
	 * 应用程序退出时保存当前播放的音乐信息
	 */
	public void initCurrentMusicInfo(Context context){
		
		listPosition=SharedPreHelper.getIntValue(context, "listPosition", 0);
		playType=SharedPreHelper.getIntValue(context, "playType", 9);
		Log.i("Mp3Util", "获取值:listPosition="+listPosition);
//		playType=9;

	}
	public void sortMp3InfosByTitle(){
		mp3Infos.clear();
		mp3Infos.addAll(mediaUtil.sortMp3InfosByTitle(context));
		setSortByTime(false);
		listPosition=mp3Infos.indexOf(currentMp3Info);
	}
	public void sortMp3InfosByTime(){
		mp3Infos.clear();
		mp3Infos.addAll(mediaUtil.getMp3Infos(context));
		listPosition=mp3Infos.indexOf(currentMp3Info);
		setSortByTime(true);
	}
	
	public Mp3Info getCurrentMp3Info() {
		return currentMp3Info;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * 指定所有音乐中索引位置的音乐发送播放服务
	 * 
	 * @param listPosition
	 *            要播放音乐的索引位置
	 */
	public void playMusic(int listPosition) {
		this.isPlaying = true;
		this.listPosition = listPosition;
		this.currentMp3Info = mp3Infos.get(listPosition);
		sendService(AppConstant.PlayerMsg.PLAY_MSG,0);
	};
	private void sendService(int msg,int progress){
		
		Log.i("Mp3Util", "listPosition="+listPosition);
		Log.i("Mp3Util", "currentMp3Info="+currentMp3Info.getDisplayName());
		Intent intent = new Intent(context, MyPlayerService.class);
		intent.putExtra("MSG", msg);
		intent.putExtra("url", currentMp3Info.getUrl());
		intent.putExtra("progress", progress);
		context.startService(intent);
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
		sendService(AppConstant.PlayerMsg.PLAY_MSG,0);
	}

	/**
	 * 进度条变化时调用该方法发送服务
	 * 
	 * @param progress
	 */
	public void audioTrackChange(int progress) {
		sendService(AppConstant.PlayerMsg.PROGRESS_CHANGE,progress);
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
			
			if(currentTime>0){
				MSG = AppConstant.PlayerMsg.PLAYING_MSG;
			}else{
				MSG = AppConstant.PlayerMsg.PLAY_MSG;
			}
			
			isPlaying = true;
		}
		Log.d(TAG, "listpostion:"+listPosition);
		Log.d(TAG, "currentTime:"+currentTime);
		sendService(MSG,0);
	}
	/**
	 * 发送播放或暂停音乐服务 如是当前是播放状态,则暂停.反之亦然
	 * 
	 * 
	 */
	public void playMusic(Mp3Info mp3Info) {
		currentMp3Info=mp3Info;
		sendService(AppConstant.PlayerMsg.PLAY_MSG,0);
	}

	/**
	 * 根据播放类型发送下一首歌曲播放服务
	 */
	public void nextMusic() {
		Log.i(TAG, "nextMusic()");
		switch (playType) {
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			listPosition = randomNum();
			break;
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
		case AppConstant.PlayerMsg.PLAYING_QUEUE:
			listPosition = (listPosition + 1 >= mp3Infos.size() ? 0
					: listPosition + 1);
			break;

		}
		playMusic(listPosition);
	}
	/**
	 * 随机一首
	 */
	public void randomPlay(){
		listPosition = randomNum();
		playMusic(listPosition);
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

	/**
	 * 在所有歌曲当中随机产生一个随机索引
	 * 
	 * @return
	 */
	private int randomNum() {
		return (int) (mp3Infos.size() * Math.random());
	}

	public void setCurrentMp3Info(Mp3Info currentMp3Info) {
		this.currentMp3Info = currentMp3Info;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public int getPlayType() {
		return playType;
	}

	public void setPlayType(int playType) {
		this.playType = playType;
	}

	public int getListPosition() {
		return listPosition;
	}

	public void setListPosition(int listPosition) {
		this.listPosition = listPosition;
	}
	public int getAllMp3Size(){
		return this.mp3Infos.size();
	}

	public boolean isSortByTime() {
		return isSortByTime;
	}

	public void setSortByTime(boolean isSortByTime) {
		this.isSortByTime = isSortByTime;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isShowLrc() {
		return isShowLrc;
	}
	public void setShowLrc(boolean isShowLrc) {
		this.isShowLrc = isShowLrc;
	}
}
