package com.music;

import android.app.Application;

import com.music.lu.utils.BitmapCacheUtil;
import com.music.lu.utils.Mp3Util_New;
import com.music.lu.utils.MusicUtils;
import com.music.lu.utils.MyLog;
import com.music.widget.lockpatternview.LockPatternUtils;

public class MusicApplication extends Application {
	private static final String TAG = "MusicApplication";
	private static MusicApplication musicApplication;
	private LockPatternUtils mLockPatternUtils;
	@Override
	public void onCreate() {
		super.onCreate();
//		startService(new Intent(this,MyPlayerService.class));
//		Mp3Util.init(this);
		long start=System.currentTimeMillis();
		Mp3Util_New.init(this);
		long mp3=System.currentTimeMillis();
		MyLog.d(TAG, "Mp3Util_New.init(this):"+(mp3-start)/1000.0);
		BitmapCacheUtil.init(this);
		long bit=System.currentTimeMillis();
		MyLog.d(TAG, "BitmapCacheUtil.init(this):"+(bit-mp3)/1000.0);
		MusicUtils.init(this);
		MyLog.d(TAG, "MusicUtils.init(this):"+(System.currentTimeMillis()-bit)/1000.0);
		
		setmLockPatternUtils(new LockPatternUtils(this));
		setMusicApplication(this);
	}
	public static MusicApplication getInstance() {
		return musicApplication;
	}
	public static void setMusicApplication(MusicApplication musicApplication) {
		MusicApplication.musicApplication = musicApplication;
	}
	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
	public void setmLockPatternUtils(LockPatternUtils mLockPatternUtils) {
		this.mLockPatternUtils = mLockPatternUtils;
	}
	
}
