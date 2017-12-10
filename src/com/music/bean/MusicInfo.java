package com.music.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "l_music")
public class MusicInfo implements Comparable{
	
	@Id
	@Property(nameInDb = "songId")
	private long songId;
	@Property(nameInDb = "albumId")
	private int albumId;
	@Property(nameInDb = "album")
	private String album;
	@Property(nameInDb = "duration")
	private int duration;
	@Property(nameInDb = "title")
	private String  title;
	@Property(nameInDb = "artist")
	private String  artist;
	@Property(nameInDb = "playPath")
	private String  playPath;
	@Property(nameInDb = "folder")
	private String folder;
	@Property(nameInDb = "titleKey")
	private String titleKey;
	@Property(nameInDb = "artistKey")
	private String artistKey;
	@Property(nameInDb = "picUrl")
	public String picUrl;
	@Property(nameInDb = "downUrl")
	public String downUrl;

	@Property(nameInDb = "tag")
	public int tag;



	@Generated(hash = 1106448750)
	public MusicInfo(long songId, int albumId, String album, int duration,
			String title, String artist, String playPath, String folder, String titleKey,
			String artistKey, String picUrl, String downUrl, int tag) {
		this.songId = songId;
		this.albumId = albumId;
		this.album = album;
		this.duration = duration;
		this.title = title;
		this.artist = artist;
		this.playPath = playPath;
		this.folder = folder;
		this.titleKey = titleKey;
		this.artistKey = artistKey;
		this.picUrl = picUrl;
		this.downUrl = downUrl;
		this.tag = tag;
	}

	@Generated(hash = 1735505054)
	public MusicInfo() {
	}


	
	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getPlayPath() {
		return playPath;
	}

	public void setPlayPath(String playPath) {
		this.playPath = playPath;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	public String getArtistKey() {
		return artistKey;
	}

	public void setArtistKey(String artistKey) {
		this.artistKey = artistKey;
	}
	@Override
	public int compareTo(Object another) {
		MusicInfo mp3Info = (MusicInfo) another;
		int i = 0;
		boolean flag = false;
		for (; i < this.titleKey.length(); i++) {
			if (i < mp3Info.getTitleKey().length()) {
				if (titleKey.charAt(i) != mp3Info.getTitleKey().charAt(i)) {
					break;
				} else {
					continue;
				}
			} else {
				flag = true;
				break;

			}

		}
		if (i == titleKey.length()) {
			return -1;
		}
		if (flag) {
			return 1;
		}
		return this.titleKey.charAt(i) - mp3Info.getTitleKey().charAt(i);
	}
	@Override
	public String toString() {
		return "MusicInfo{" +
				"songId=" + songId +
				", albumId=" + albumId +
				", duration=" + duration +
				", title='" + title + '\'' +
				", artist='" + artist + '\'' +
				", playPath='" + playPath + '\'' +
				", folder='" + folder + '\'' +
				", titleKey='" + titleKey + '\'' +
				", artistKey='" + artistKey + '\'' +
				'}';
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getDownUrl() {
		return this.downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getPicUrl() {
		return this.picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getTag() {
		return this.tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
}