package com.music.lu.utils;

/**
 * 应用常量类
 * @author wwj
 *
 */
public class AppConstant {
	/**
	 * 匹配歌词
	 */
	public final static int MATCH_LRC_COMPLETED=0;
	
	
//	public enum PlayType{
//		PLAYING_QUEUE,PLAYING_SHUFFLE,PLAYING_REPEAT
//	}
	public class PlayerMsg {
		/**
		 * 开始播放
		 */
		public static final int PLAY_MSG = 1;		//
		/**
		 * 暂停
		 */
		public static final int PAUSE_MSG = 2;		//暂停
		/**
		 * 停止
		 */
		public static final int STOP_MSG = 3;		//停止
		/**
		 * 继续
		 */
		public static final int CONTINUE_MSG = 4;	//继续
		/**
		 * 上一首
		 */
		public static final int PRIVIOUS_MSG = 5;	//上一首
		/**
		 * 下一首
		 */
		public static final int NEXT_MSG = 6;		//下一首
		/**
		 * 进度改变
		 */
		public static final int PROGRESS_CHANGE = 7;//进度改变
		/**
		 * 继续播放
		 */
		public static final int PLAYING_MSG = 8;	//继续播放
		
		/**顺序播放*/
		public static final int PLAYING_QUEUE=9;		//顺序播放
		/**随机播放*/
		public static final int PLAYING_SHUFFLE=10;		//
		/**单曲播放*/
		public static final int PLAYING_REPEAT=11;		//
		
	}
	/**
	 * //通知栏发来的消息
	 */
	public static final String NOTIFICATION_PLAY_PAUSE="com.action.lu.play_pause";
	/**
	 * //通知栏发来的播放下一首消息
	 */
	public static final String NOTIFICATION_NEXT="com.action.lu.next";
	/**
	 * 
	 * 下一首
	 */
	public static final String MUSIC_NEXT = "com.wwj.action.MUSIC_NEXT";
}
