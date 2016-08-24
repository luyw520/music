package com.music.bean;

import java.util.List;

public class NetMusicGsonBean {
	public int code;
	public Result result;
	
	public class Result{
		public int songCount;
		public List<Song> songs;
		
	}
	public class Song{
		public int id;
		public String name;
		public List<Artist> artists;
		public Album album;
		public	String audio;
		public	String page;
	}
	public class Artist{
		public int id;
		public String name;
		public String picUrl;
	}
	public class Album{
		public String picUrl;
		public String name;
		public int id;
	}
}
