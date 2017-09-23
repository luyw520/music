package com.music.ui.widget.lockpatternview;


import android.app.Application;

public class App extends Application {
	private static App mInstance;
	private LockPatternUtils mLockPatternUtils;

	public static App getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mLockPatternUtils = new LockPatternUtils(this);
		System.out.println("app oncreate");
	}

	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
}
