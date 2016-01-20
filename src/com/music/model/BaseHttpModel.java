package com.music.model;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class BaseHttpModel{
	public static final String BD_MUSIC_API="http://box.zhangmen.baidu.com/x?";
	protected HttpUtils httpUtils=new HttpUtils();
	public <T> void baseHttpRequest(String url, RequestCallBack<T> requestCallBack){
		httpUtils.send(HttpRequest.HttpMethod.GET, url, requestCallBack);
	}
}
