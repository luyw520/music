package com.music.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.music.bean.LyricSentence;
import com.music.lrc.LyricDownloadManager;
import com.music.lrc.LyricLoadHelper;
import com.music.lrc.LyricLoadHelper.LyricListener;
import com.music.utils.DeBug;
import com.music.utils.FileUtils;

public class LyricModel {
	private Map<String, List<LyricSentence>> lycsList=new HashMap<String,List<LyricSentence>>();
	
	private LyricDownloadManager manager;
	private LyricLoadHelper loadHelper;
	
	public LyricModel(Context context){
		
		manager = new LyricDownloadManager(context);
		loadHelper = new LyricLoadHelper();
		
	}
	public void setLyricListener(LyricListener l){
		loadHelper.setLyricListener(l);
	}
	/**
	 * search lrc from web 
	 * @param songName
	 * @param songer
	 * @return
	 */
	public String searchLyricFromWeb(String songName, String songer){
		return manager.searchLyricFromWeb(songName, songer);
	}
	public void loadLyric(String lrcPath){
		loadHelper.loadLyric(lrcPath);
	}
	public boolean isCache(String title){
		return lycsList.containsKey(title);
	}
	public void putLyric(String title,List<LyricSentence> lyricSentences ){
		if(title==null||lyricSentences==null){
			return;
		}
		if(!lycsList.containsKey(title)){
			DeBug.d(this, "...................cache lrc,title:"+title+",size."+lyricSentences.size());
			List<LyricSentence> tempList=new ArrayList<LyricSentence>();
			tempList.addAll(lyricSentences);
			lycsList.put(title, tempList);
		}
	}
	public List<LyricSentence> getLyricSentences(String title){
		DeBug.d(this, "...................getLyricSentences lrc,title:"+title);
		return lycsList.get(title);
	}
	
	/**
	 * 查找本地是否有歌词
	 * 
	 * @param songer
	 * @param songName
	 * @return
	 */
	public String findLocalLrc(String songer, String songName) {
		File file = new File(FileUtils.lrcPath());
		for (File f : file.listFiles()) {
			if (f.getName().equals(songer + "-" + songName + ".lrc")) {
				DeBug.d(this, "sdcard has lrc");
				return f.getAbsolutePath();
			}
		}
		DeBug.d(this, "sdcard has not lrc ");
		return null;
	}
}
