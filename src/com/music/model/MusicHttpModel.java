package com.music.model;

import java.io.File;

import com.lidroid.xutils.http.callback.RequestCallBack;

public class MusicHttpModel extends BaseHttpModel{
	public void searchMusic(String songName,String songer,RequestCallBack<String> callBack){
		String url="http://box.zhangmen.baidu.com/x?op=12&count=1&title="+songName+"$$"+songer+"$$$$";
		baseHttpRequest(url, callBack);
	}
	public void downMusic(String url,String downPath,RequestCallBack<File> callBack){
		httpUtils.download(url, downPath, true, callBack);
	}
}
