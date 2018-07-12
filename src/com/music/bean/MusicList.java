package com.music.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

@Entity(nameInDb = "l_music_list")
public class MusicList implements Serializable{

	@Property(nameInDb = "songId")
	private long songId;

	@Property(nameInDb = "song_list_name")
	private String songListName;

//	@Property(nameInDb = "song_size")
//	private int songSize;
//
	@Property(nameInDb = "pic")
	private String pic;
	public String getSongListName() {
		return this.songListName;
	}

	public void setSongListName(String songListName) {
		this.songListName = songListName;
	}

	public long getSongId() {
		return this.songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}

	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}


	@Generated(hash = 1577597785)
	public MusicList(long songId, String songListName, String pic) {
		this.songId = songId;
		this.songListName = songListName;
		this.pic = pic;
	}

	@Generated(hash = 1135234633)
	public MusicList() {
	}




}