package com.music.broadcastreceiver;

import com.music.lu.utils.ConnectionUtil;
import com.music.lu.utils.LogUtil;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ListenNetStateReceiver extends BroadcastReceiver {
	
	private NetState netState;
	public ListenNetStateReceiver(IntentFilter intentFilter,NetState netState){
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		
		this.netState=netState;
	}
	
	
	@SuppressLint("InlinedApi") @Override
	public void onReceive(Context context, Intent intent) {
		boolean isConnected=ConnectionUtil.isNetWorkConnected(context);
		
		LogUtil.i(this.getClass(), "isConnected="+isConnected);
		
		if(isConnected){
			int type=ConnectionUtil.getNetWorkType(context);
			netState.connected(type);
			LogUtil.i(this.getClass(), "type="+type);
		}else{
			netState.disconnected();
		}
	}

}
