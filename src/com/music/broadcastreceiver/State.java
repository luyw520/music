package com.music.broadcastreceiver;

import android.content.Intent;

/**
 * 收到后台服务发来的信息后更新状态
 * @author Administrator
 *
 */
public interface State {
	void playMusicState();
	void currentState(Intent intent);
	void duration(Intent intent);
	void pauseMusicState();
}
