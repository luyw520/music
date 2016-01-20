package com.music.broadcastreceiver;


import java.util.ArrayList;
import java.util.List;

import com.music.utils.ConstantUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG="MyBroadcastReceiver";
	
	private List<State> states=new ArrayList<State>();
	State state;
	public MyBroadcastReceiver(State state ,IntentFilter filter)
	{
		this.state=state;
		 addAction(filter);
	}
	public void addState(State state){
		states.add(state);
	}
	public void addAction(IntentFilter filter){
		
		// Ö¸¶¨BroadcastReceiver¼àÌýµÄAction
		filter.addAction(ConstantUtil.MUSIC_PAUSE);
		filter.addAction(ConstantUtil.MUSIC_PLAYER);
		filter.addAction(ConstantUtil.MUSIC_PRE);
		filter.addAction(ConstantUtil.MUSIC_CURRENT);
		filter.addAction(ConstantUtil.MUSIC_DURATION);
		filter.addAction(ConstantUtil.MUSIC_NEXT_PLAYER);
		
		filter.addAction(ConstantUtil.CHANGED_BG);
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
		Log.i(TAG, "action="+action);
		if (action.equals(ConstantUtil.MUSIC_CURRENT)) {
			state.currentState(intent);
		} else if (action.equals(ConstantUtil.MUSIC_DURATION)) {
			state.duration(intent);
		}else if(ConstantUtil.MUSIC_PAUSE.equals(action)){
			state.pauseMusicState();
		}else if(ConstantUtil.MUSIC_PLAYER.equals(action)){
			state.playMusicState();
		}else if(ConstantUtil.MUSIC_PAUSE.equals(action)){
			state.pauseMusicState();
		}else if(ConstantUtil.CHANGED_BG.equals(action)){
//			state.changedBg();
		}
	}	

}
