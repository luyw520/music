package com.music.utils;

/**
 * @author wwj
 *
 */
public interface AppConstant {
	/**
	 */
	 int MATCH_LRC_COMPLETED=0;



	 interface PlayerMsg {
		/**
		 */
		 int PLAY_MSG = 1;		//
		/**
		 */
		 int PAUSE_MSG = 2;		//
		/**
		 * ֹͣ
		 */
		 int STOP_MSG = 3;		//ֹͣ
		/**
		 */
		 int CONTINUE_MSG = 4;	//
		/**
		 */
		 int PRIVIOUS_MSG = 5;	//
		/**
		 */
		 int NEXT_MSG = 6;		//
		/**
		 */
		 int PROGRESS_CHANGE = 7;//
		/**
		 */
		 int PLAYING_MSG = 8;	//
		 /**
		  * 顺序播放
		  */
		 int PLAYING_QUEUE=9;		//
		 /**
		  * 随机播放
		  */
		 int PLAYING_SHUFFLE=10;		//
		 /**
		  * 重复播放
		  */
		 int PLAYING_REPEAT=11;		//

	}
	/**
	 */
	 String NOTIFICATION_PLAY_PAUSE="com.action.lu.play_pause";
	/**
	 */
	 String NOTIFICATION_NEXT="com.action.lu.next";
	/**
	 *
	 */
	 String MUSIC_NEXT = "com.wwj.action.MUSIC_NEXT";
}
