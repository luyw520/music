package com.music.view.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LockScreenService extends Service {
	private final static String TAG = "LockScreenService";
	private Intent lockIntent;
	private KeyguardManager keyguardManager = null;
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
		//重新启动activity
//		startService(new Intent(LockScreenService.this, LockScreenService.class));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}
	
//	/**
//	 * 屏幕变亮的广播，这里要隐藏系统的锁屏界面
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
//				keyguardLock.disableKeyguard(); //这里就是取消系统默认的锁
//				
//				startActivity(lockIntent); //注意这里跳转的意
//			}
//		}
//	};
}
