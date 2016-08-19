package com.music.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.music.bean.Mp3Info;
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

	public void searchMusicByNetApi(String key,
			final RequestCallBack<Object> callBack) {
		baseHttpRequest(NET_MUSIC_API + key, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (callBack != null) {
					callBack.onFailure(arg0, arg1);
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				d("onSuccess:"+arg0.result);
				if (callBack == null) {
					return;
				}
				Gson gson = new Gson();
				NetMusicGsonBean bean = gson.fromJson(arg0.result,
						NetMusicGsonBean.class);
				if (bean.code == 200) {
					List<Song> songs = bean.result.songs;
					if (songs != null && !songs.isEmpty()) {
						List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
						for (Song song : songs) {
							Mp3Info tempMp3Info = new Mp3Info();
							tempMp3Info.setDownUrl(song.audio);
							tempMp3Info.setUrl(song.album.picUrl);
							tempMp3Info.playPath = tempMp3Info.getDownUrl();
							
							
							
//							mp3.setTitle(songName);
//							mp3.setAlbum(songer);
//							mp3.setTitlepinyin(MediaUtil.toHanyuPinYin(songName));
//							
							String songName=song.name;
							tempMp3Info.setTitle(songName);
							tempMp3Info.setDisplayName(songName);
							List<Artist> artist = song.artists;
							if (artist != null && !artist.isEmpty()) {
								tempMp3Info.setSonger(artist.get(0).name);
								tempMp3Info.setTitlepinyin(MediaUtil.toHanyuPinYin(artist.get(0).name));
							}else{
								tempMp3Info.setSonger("δ֪");
							} 
								
							mp3Infos.add(tempMp3Info);
						}
						ResponseInfo<Object> response = new ResponseInfo<Object>(
								null, userTag, false);
						response.result = mp3Infos;
						callBack.onSuccess(response);
						return;
					}
				}
				callBack.onFailure(new HttpException("no song"), "no song");

			}

		});
	}

	public void downMusic(String url, String downPath,
			RequestCallBack<File> callBack) {
		httpUtils.download(url, downPath, true, callBack);
	}
}
