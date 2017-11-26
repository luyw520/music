package com.music.ui.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LockScreenService extends Service {
	private final static String TAG = "LockScreenService";
	@SuppressWarnings("unused")
	private Intent lockIntent;
	@SuppressWarnings("unused")
	private KeyguardManager keyguardManager = null;
	@SuppressWarnings({ "unused", "deprecation" })
	private KeyguardManager.KeyguardLock keyguardLock = null;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

//		lockIntent = new Intent(LockScreenService.this, LockScreenActivity.class);
//		lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//		//注册广播
//		IntentFilter mScreenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//		LockScreenService.this.registerReceiver(mScreenOffReceiver, mScreenOffFilter);
		
		Log.i(TAG, "LockScreenService");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		LockScreenService.this.unregisterReceiver(mScreenOffReceiver);
//		startService(new Intent(LockScreenService.this, LockScreenService.class));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}
	
//	/**
//	 */
//	private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Log.i(TAG, intent.getAction());
//			if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)
//					|| intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
//				
//				keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//				keyguardLock = keyguardManager.newKeyguardLock("");
//				keyguardLock.disableKeyguard(); //
//				
//				startActivity(lockIntent); //
//			}
//		}
//	};
}
