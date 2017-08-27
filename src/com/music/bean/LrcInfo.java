package com.music.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LrcInfo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2641056466258188337L;
	public LrcInfo(String songName, List<Map<String, String>> list) {
		super();
		this.songName=(songName);
		this.list=(list);
	}
	public String getSongName() {
		return songName;
	}
	public List<Map<String, String>> getList() {
		return list;
	}
	private String songName;
	private List<Map<String, String>> list;
}
