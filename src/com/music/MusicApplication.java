package com.music;

import android.app.Application;
import android.content.Context;

import com.lu.library.LibContext;
import com.mob.MobSDK;
import com.music.db.DBHelper;
import com.music.helpers.FileHelper;
import com.music.helpers.PlayerHelpler;
import com.music.ui.service.MyPlayerNewService;
import com.music.ui.widget.lockpatternview.LockPatternUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;

public class MusicApplication extends Application {
	private static MusicApplication musicApplication;
	private LockPatternUtils mLockPatternUtils;
	public static final int DARK_THEME = 0;
	public static final int LIGHT_THEME = 1;
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
		LibContext.getInstance().init(this);
		PlayerHelpler.init(this);
		setmLockPatternUtils(new LockPatternUtils(this));
		initImageLoader(getApplicationContext());
		DBHelper.getInstance().init();
		initMobSDK();

	}

	private void initMobSDK() {
		MobSDK.init(this);
	}

	private void initUmeng() {
		UMShareAPI.init(this,"5b2a150ef43e4809b500000f");
		MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
		UMConfigure.setLogEnabled(true);
		UMConfigure.init(this,"5b2a150ef43e4809b500000f","",UMConfigure.DEVICE_TYPE_PHONE,"");
		PlatformConfig.setWeixin("wx4868b35061f87885", "64020361b8ec4c99936c0e3999a9f249");
		//豆瓣RENREN平台目前只能在服务器端配置
		PlatformConfig.setQQZone("1104335219", "J68iUn08AUZwHWrJ");
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
