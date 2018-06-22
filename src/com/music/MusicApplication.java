package com.music;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.lu.library.monitor.BlockDetectByPrinter;
import com.lu.library.util.CrashHandler;
import com.lu.library.util.SPUtils;
import com.music.db.DBHelper;
import com.music.helpers.FileHelper;
import com.music.helpers.PlayerHelpler;
import com.music.model.ScreenManager;
import com.music.ui.service.MyPlayerNewService;
import com.music.ui.widget.lockpatternview.LockPatternUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMShareAPI;

import java.io.File;

public class MusicApplication extends Application {
	private static MusicApplication musicApplication;
	private LockPatternUtils mLockPatternUtils;
	public static final int DARK_THEME = 0;
	public static final int LIGHT_THEME = 1;
	private static SharedPreferences mSharedPreferences;
	@SuppressWarnings("unused")
	private ScreenManager screenManager;

	public static SharedPreferences getSharedPreferences() {
		return mSharedPreferences;
	}
	public int getCurrentTheme(){
		return DARK_THEME;
	}
//	}
	public long lastPlayTime=0;
	private MyPlayerNewService myPlayerNewService;
	@Override
	public void onCreate() {
		super.onCreate();
		musicApplication=this;
		screenManager=ScreenManager.getScreenManager();
		PlayerHelpler.init(this);
		setmLockPatternUtils(new LockPatternUtils(this));
		initImageLoader(getApplicationContext());
		DBHelper.getInstance().init();
		mSharedPreferences=(getSharedPreferences("lu_music",Context.MODE_PRIVATE));
		BlockDetectByPrinter.start();
		SPUtils.init(this);
		Stetho.initializeWithDefaults(this);
		CrashHandler.getInstance().init(this,"lu");
//		UMAnalyticsConfig.
		UMShareAPI.init(this,"5b2a150ef43e4809b500000f");
		init7_0();

		MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
		UMConfigure.setLogEnabled(true);
		UMConfigure.init(this,"5b2a150ef43e4809b500000f","",UMConfigure.DEVICE_TYPE_PHONE,"");
	}
	private void init7_0() {

		// android 7.0系统解决拍照的问题
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();
	}

	private void initImageLoader(Context applicationContext) {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(applicationContext);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app
		config.diskCache(new UnlimitedDiskCache(new File(FileHelper.imgPathPath())));
		ImageLoader.getInstance().init(config.build());
	}
	public static MusicApplication getInstance() {
		return musicApplication;
	}
	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
	public void setmLockPatternUtils(LockPatternUtils mLockPatternUtils) {
		this.mLockPatternUtils = mLockPatternUtils;
	}

	public MyPlayerNewService getMyPlayerNewService() {
		return myPlayerNewService;
	}

	public void setMyPlayerNewService(MyPlayerNewService myPlayerNewService) {
		this.myPlayerNewService = myPlayerNewService;
	}
}
