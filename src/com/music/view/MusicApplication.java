package com.music.view;

import java.io.File;

import android.app.Application;
import android.content.Context;

import com.music.model.ScreenManager;
import com.music.utils.BitmapCacheUtil;
import com.music.utils.DeBug;
import com.music.utils.FileUtils;
import com.music.utils.Mp3Util_New;
import com.music.utils.MusicUtils;
import com.music.widget.lockpatternview.LockPatternUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MusicApplication extends Application {
	private static final String TAG = "MusicApplication";
	private static MusicApplication musicApplication;
	private LockPatternUtils mLockPatternUtils;
	@SuppressWarnings("unused")
	private ScreenManager screenManager;
	@Override
	public void onCreate() {
		super.onCreate();
		musicApplication=this;
		screenManager=ScreenManager.getScreenManager();
		DeBug.d(TAG, "MusicApplication ............onCreate:");
		long start=System.currentTimeMillis();
		Mp3Util_New.init(this);
		long mp3=System.currentTimeMillis();
		DeBug.d(TAG, "Mp3Util_New.init(this):"+(mp3-start)/1000.0);
		BitmapCacheUtil.init(this);
		long bit=System.currentTimeMillis();
		DeBug.d(TAG, "BitmapCacheUtil.init(this):"+(bit-mp3)/1000.0);
		MusicUtils.init(this);
		DeBug.d(TAG, "MusicUtils.init(this):"+(System.currentTimeMillis()-bit)/1000.0);
		
		setmLockPatternUtils(new LockPatternUtils(this));
		setMusicApplication(this);
		
		initImageLoader(getApplicationContext());
		
		
//		FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/a.ttf");
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/a.ttf");
//        FontsOverride.setDefaultFont(this, "SERIF", "fonts/a.ttf");
//        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/a.ttf");
	}
	
	private void initImageLoader(Context applicationContext) {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(applicationContext);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app
		config.diskCache(new UnlimitedDiskCache(new File(FileUtils.imgPathPath())));
		ImageLoader.getInstance().init(config.build());
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
