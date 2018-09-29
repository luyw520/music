package com.music.helpers;

import com.music.bean.MessageEvent;
import com.lu.library.log.DebugLog;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music.helpers
 * @description: ${TODO}{ 类注释}
 * @date: 2018/6/20 0020
 */

public class EventBusHelper {

    public static void post(String type){
        post(type,null);
    }
    public static void post(String type,Object data){
        post(new MessageEvent(type,data));
    }
    public static void post(MessageEvent messageEvent){
        DebugLog.d("发送事件....."+messageEvent.toString());
        EventBus.getDefault().post(messageEvent);
    }
    public static void register(Object subscriber){
        EventBus.getDefault().register(subscriber);
    }
    public static void unregister(Object subscriber){
        EventBus.getDefault().unregister(subscriber);
    }
}
