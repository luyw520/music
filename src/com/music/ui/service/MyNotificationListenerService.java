package com.music.ui.service;


import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.music.utils.DebugLog;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music.ui.service
 * @description: ${TODO}{ 通知栏}
 * @date: 2018/6/12 0012
 */

@SuppressLint("OverrideAbstract")
public class MyNotificationListenerService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        DebugLog.d("读取到通知栏消息:"+sbn.toString());
    }
}
