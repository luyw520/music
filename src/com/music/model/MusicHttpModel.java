package com.music.model;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.music.bean.Mp3Info;
import com.music.bean.MusicInfo;
import com.music.bean.NetMusicGsonBean;
import com.music.bean.NetMusicGsonBean.Artist;
import com.music.bean.NetMusicGsonBean.Song;
import com.music.utils.LogUtil;
import com.music.utils.MediaUtil;

public class MusicHttpModel extends BaseHttpModel {

	public void searchMusic(String songName, String songer,
			final RequestCallBack<String> callBack) {
		String url = "http://box.zhangmen.baidu.com/x?op=12&count=1&title="
				+ songName + "$$" + songer + "$$$$";
		baseHttpRequest(url, callBack);
	}

	public void searchMusicByNetApi(Context context,String key,
									final HttpCallback<List<MusicInfo>> callBack) {
		d(NET_MUSIC_API + key);
		vollyRequset(context,NET_MUSIC_API + key, new HttpCallback<String>() {
			@Override
			public void onFailure(Exception e) {
				if (callBack!=null){
					callBack.onFailure(e);
				}
			}
			public void onSuccess(String result) {
				d(result);
				if (callBack!=null){
					callBack.onSuccess(resolver(result));
				}
			}

		});
	}
	private List<MusicInfo> resolver(String result){
		Gson gson = new Gson();
		NetMusicGsonBean bean = gson.fromJson(result,
				NetMusicGsonBean.class);
		List<MusicInfo> mp3Infos = new ArrayList<>();
		if (bean.code == 200) {
			List<Song> songs = bean.result.songs;
			if (songs != null && !songs.isEmpty()) {

				for (Song song : songs) {
					MusicInfo tempMp3Info = new MusicInfo();
					tempMp3Info.downUrl=(song.audio);
					tempMp3Info.picUrl=(song.album.picUrl);
					tempMp3Info.setPlayPath(tempMp3Info.downUrl);
					String songName=song.name;
					tempMp3Info.setTitle(songName);
					List<Artist> artist = song.artists;
					if (artist != null && !artist.isEmpty()) {
						tempMp3Info.setArtist(artist.get(0).name);
						tempMp3Info.setTitleKey(MediaUtil.toHanyuPinYin(artist.get(0).name));
					}else{
						tempMp3Info.setArtist("songer֪");
					}
					tempMp3Info.picUrl=song.album.picUrl;

					mp3Infos.add(tempMp3Info);
				}
			}
		}
		return mp3Infos;
	}
	public void downMusic(String url, String downPath,
			RequestCallBack<File> callBack) {
		httpUtils.download(url, downPath, true, callBack);
	}
}
