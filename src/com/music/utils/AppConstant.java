package com.music.utils;

/**
     * @author wwj
     *
     */
    public  interface AppConstant {
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

    /**
     */
    String MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT"; //
    /**
     */
    String MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION"; //
    /**
     */
    String REPEAT_ACTION = "com.wwj.action.REPEAT_ACTION"; //




    /**
     *
     */
    String MUSIC_NEXT_PLAYER = "com.action.MUSIC_NEXT";
    /**
     *
     */
    String MUSIC_PRE = "com.wwj.action.MUSIC_PRE";
    /**
     *
     */
    String MUSIC_PLAYER = "com.wwj.action.MUSIC_PLAYER";

    String SHUFFLE_ACTION = "com.wwj.action.SHUFFLE_ACTION"; //
    /**
     */
    String MUSIC_PAUSE="com.wwj.action.MUSIC_PAUSE";	//

    /**
     */
    String LRC_CURRENT="com.lu.lrc.current";
    String CHANGED_BG = "com.lu.changedgb";


    /**
     *
     */
    String AUTOMATIC_DOWN_LRC="AUTOMATIC_DOWN_LRC";
    String LISTENER_DOWN="LISTENER_DOWN";
    String SCREEN_SHOT="SCREEN_SHOT";


    String LIST_POSITION_KEY="LIST_POSITION_KEY";
    String PLAY_TYPE_KEY="PLAY_TYPE_KEY";
    String BG_INDEX_KEY="BG_INDEX_KEY";
    }