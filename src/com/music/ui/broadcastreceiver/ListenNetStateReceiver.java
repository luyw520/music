package com.music.ui.broadcastreceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.lu.library.util.NetUtil;
import com.music.utils.LogUtil;

public class ListenNetStateReceiver extends BroadcastReceiver {

	private NetState netState;
	public ListenNetStateReceiver(IntentFilter intentFilter,NetState netState){
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

		this.netState=netState;
	}


	@SuppressLint("InlinedApi") @Override
	public void onReceive(Context context, Intent intent) {
		boolean isConnected= NetUtil.isConnected(context);

		LogUtil.i(this.getClass(), "isConnected="+isConnected);

		if(isConnected){
			int type= 0;
					NetUtil.getNetworkType();
			netState.connected(type);
			LogUtil.i(this.getClass(), "type="+type);
		}else{
			netState.disconnected();
		}
	}

}
