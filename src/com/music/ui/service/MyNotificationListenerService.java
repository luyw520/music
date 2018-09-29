package com.music.ui.service;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.lu.library.log.DebugLog;

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
    public IBinder onBind(Intent intent) {
        DebugLog.d("服务已经绑定.....");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        DebugLog.d("读取到通知栏消息:"+sbn.toString());
        handlerNotifi(sbn.getNotification());
    }
    void handlerNotifi(Notification notification){
        //通知内容
        String title = null, text = null;
        try {
            if (Build.VERSION.SDK_INT > 18) {
                Bundle extras = notification.extras;
                title = extras.getString(Notification.EXTRA_TITLE);
                text = extras.getString(Notification.EXTRA_TEXT);
                if (TextUtils.isEmpty(text)) {
                    text = notification.tickerText.toString();
                }
                // 特殊消息处理如：[88条]name:hello world
//                if (!TextUtils.isEmpty(text)) {
//                    if (text.contains(":") && type == WECHAT) {
//                        text = text.split(":")[1].trim();
//                    } else if (text.contains("]")) {
//                        text = text.substring(text.indexOf("]") + 1);
//                    }
//                }
            } else {
                title = null;
                text = TextUtils.isEmpty(notification.tickerText) ? null : notification.tickerText.toString();
//                if (!TextUtils.isEmpty(text)) {
//                    if (text.contains(":") && type == WECHAT) {
//                        String[] tmp = text.split(":");
//                        title = tmp[0];
//                        text = tmp[1];
//                        if (tmp[0].contains("]")) {
//                            title = tmp[0].substring(tmp[0].indexOf("]") + 1);
//                        }
//                    }
//                }
            }
        } catch (Exception e) {
            title = null;
            text = null;
        }
        DebugLog.d("title:"+title+",text:"+text);
    }

}
