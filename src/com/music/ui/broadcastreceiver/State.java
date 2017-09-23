package com.music.ui.broadcastreceiver;

import android.content.Intent;

/**
 * @author Administrator
 *
 */
public interface State {
	void playMusicState();
	void currentState(Intent intent);
	void duration(Intent intent);
	void pauseMusicState();
}
