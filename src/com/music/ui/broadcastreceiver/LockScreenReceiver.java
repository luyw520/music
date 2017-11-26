package com.music.ui.broadcastreceiver;

import com.music.ui.view.activity.LockScreenActivity;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author lihua
 *
 */
public class LockScreenReceiver extends BroadcastReceiver {
//	private final static String TAG = "LockScreenReceiver";
	private KeyguardManager keyguardManager = null;
	@SuppressWarnings("deprecation")
	private KeyguardManager.KeyguardLock keyguardLock = null;
	
	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public void onReceive(Context context, Intent intent) {
			
			if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)
				|| intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
			
			
			Intent lockIntent = new Intent(context, LockScreenActivity.class);
			
		
			keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
			keyguardLock = keyguardManager.newKeyguardLock("");
			keyguardLock.disableKeyguard(); //
//			
//			context.startActivity(lockIntent); //
		}
	}

}
