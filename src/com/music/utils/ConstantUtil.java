package com.music.utils;

/**
 *  常量工具类
 *
 */
public interface  ConstantUtil {
	/**
	 * 更新动作
	 */
	public static final String UPDATE_ACTION = "com.wwj.action.UPDATE_ACTION"; // 更新动作
	
	/**
	 * 当前音乐改变动作
	 */
	public static final String MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT"; // 当前音乐改变动作
	/**
	 * 音乐时长改变动作
	 */
	public static final String MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION"; // 音乐时长改变动作
	/**
	 * 音乐重复改变动作
	 */
	public static final String REPEAT_ACTION = "com.wwj.action.REPEAT_ACTION"; // 音乐重复改变动作
	
	
	
	
	/**
	 * 
	 * 播放下一首
	 */
	public static final String MUSIC_NEXT_PLAYER = "com.action.MUSIC_NEXT";
	/**
	 * 
	 * 上一首
	 */
	public static final String MUSIC_PRE = "com.wwj.action.MUSIC_PRE";
	/**
	 * 
	 * 播放
	 */
	public static final String MUSIC_PLAYER = "com.wwj.action.MUSIC_PLAYER";
	
	public static final String SHUFFLE_ACTION = "com.wwj.action.SHUFFLE_ACTION"; // 音乐随机播放动作
	/**
	 * 暂停
	 */
	public static final String MUSIC_PAUSE="com.wwj.action.MUSIC_PAUSE";	//音乐暂停
	
	/**
	 * 歌词更改的动作
	 */
	public static final String LRC_CURRENT="com.lu.lrc.current";
	public static final String CHANGED_BG = "com.lu.changedgb";
	
	
	/**
	 * 
	 */
	public static final String AUTOMATIC_DOWN_LRC="AUTOMATIC_DOWN_LRC";
	public static final String LISTENER_DOWN="LISTENER_DOWN";
	public static final String SCREEN_SHOT="SCREEN_SHOT";
//	/**
//	 * 获取屏幕大小
//	 * @param context
//	 * @return
//	 */
//	public static int[] getScreen(Context context) {
//		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		Display display = windowManager.getDefaultDisplay();
//		DisplayMetrics outMetrics = new DisplayMetrics();
//		display.getMetrics(outMetrics);
//		return new int[] {(int) (outMetrics.density * outMetrics.widthPixels),
//				(int)(outMetrics.density * outMetrics.heightPixels)
//		};
//	}
}
