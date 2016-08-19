package com.music.model;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.music.utils.LogUtil;

public class BaseHttpModel{
	public static final String BD_MUSIC_API="http://box.zhangmen.baidu.com/x?";
	public static String NET_MUSIC_API="http://s.music.163.com/search/get?src=lofter&type=1&filterDj=true&limit=10&offset=0&s=";
	protected HttpUtils httpUtils=new HttpUtils();
	public <T> void baseHttpRequest(String url, RequestCallBack<T> requestCallBack){
		httpUtils.send(HttpRequest.HttpMethod.GET, url, requestCallBack);
	}
	public void d(String msg){
		LogUtil.d(this, msg);
	}
}
