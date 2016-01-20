package com.music.broadcastreceiver;


public interface NetState {
	public void connected(int type);
	public void disconnected();
}
