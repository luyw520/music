package com.music.model;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
		if (msg==null){
			LogUtil.d(this, "msg is null");
			return;
		}
		LogUtil.d(this, msg);
	}


	public void vollyRequset(Context context,String url,final HttpCallback callback){
		StringRequest stringRequest = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						d(response);
						if (callback!=null){
							callback.onSuccess(response);
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				d(error.getMessage());
				error.printStackTrace();
				if (callback!=null){
					callback.onFailure(error);
				}
			}
		});
		RequestQueue mQueue = Volley.newRequestQueue(context);
		mQueue.add(stringRequest);
	}

}
