package com.music.ui.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.music.MusicApplication;
import com.music.helpers.PlayerHelpler;
import com.lu.library.util.DebugLog;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music.ui.broadcastreceiver
 * @description: ${TODO}{ 耳机按键监听广播}
 * @date: 2018/6/13 0013
 */

public class MediaButtonReceiver extends BroadcastReceiver {
//    private long lastPlayTime=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        DebugLog.d("action:"+action+toString());
        // 获得KeyEvent对象
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

        if (Intent.ACTION_MEDIA_BUTTON.equals(action)) {

            // 获得按键码
            int keycode = event.getKeyCode();

            switch (keycode) {
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    //播放下一首
                    DebugLog.d("播放下一首");
                    PlayerHelpler.getDefault().nextMusic(true);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    //播放上一首
                    DebugLog.d("播放上一首");
                    break;
                case KeyEvent.KEYCODE_HEADSETHOOK:
                    //中间按钮,暂停or播放
                    //可以通过发送一个新的广播通知正在播放的视频页面,暂停或者播放视频

                    long currentTime=System.currentTimeMillis();
                    long delayTime=currentTime- MusicApplication.getInstance().lastPlayTime;
                    DebugLog.d("KEYCODE_HEADSETHOOK ......delayTime:"+delayTime+",currentTime:"+currentTime+",lastPlayTime:"+MusicApplication.getInstance().lastPlayTime);
                    if (delayTime>500){
                        DebugLog.d("nextMusic");
                        MusicApplication.getInstance().lastPlayTime=currentTime;
                        PlayerHelpler.getDefault().nextMusic(true);
                    }

                    break;
                default:
                    break;
            }
        }

    }
}
