package com.music.ui.broadcastreceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.music.Constant;

import java.util.ArrayList;
import java.util.List;


public class MyBroadcastReceiver extends BroadcastReceiver {
	@SuppressWarnings("unused")
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

		filter.addAction(Constant.MUSIC_PAUSE);
		filter.addAction(Constant.MUSIC_PLAYER);
		filter.addAction(Constant.MUSIC_PRE);
		filter.addAction(Constant.MUSIC_CURRENT);
		filter.addAction(Constant.MUSIC_DURATION);
		filter.addAction(Constant.MUSIC_NEXT_PLAYER);

		filter.addAction(Constant.CHANGED_BG);
	}
	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if (action.equals(Constant.MUSIC_CURRENT)) {
			state.currentState(intent);
		} else if (action.equals(Constant.MUSIC_DURATION)) {
			state.duration(intent);
		}else if(Constant.MUSIC_PAUSE.equals(action)){
			state.pauseMusicState();
		}else if(Constant.MUSIC_PLAYER.equals(action)){
			state.playMusicState();
		}else if(Constant.MUSIC_PAUSE.equals(action)){
			state.pauseMusicState();
		}else if(Constant.CHANGED_BG.equals(action)){
//			state.changedBg();
		}
	}

}
