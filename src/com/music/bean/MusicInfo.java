package com.music.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Comparable{
	
	
	
	private int songId;
	private int albumId;
	private String album;
	private int duration;
	private String  title;
	private String  artist;
	private String  playPath;
	private String folder;

	private String titleKey;
	private String artistKey;

	public String picUrl;
	public String downUrl;
	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
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
}