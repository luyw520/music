package com.music.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo extends MusicBaseInfo implements Parcelable {
	
	
	
	
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
//		bundle.putInt(KEY_ID, _id);
//		bundle.putInt(KEY_SONG_ID, songId);
//		bundle.putInt(KEY_ALBUM_ID, albumId);
//		bundle.putInt(KEY_DURATION, duration);
//		bundle.putString(KEY_MUSIC_NAME, musicName);
		bundle.putString(KEY_ARTIST, artist);
//		bundle.putString(KEY_DATA, data);
		bundle.putString(KEY_FOLDER, folder);
//		bundle.putString(KEY_MUSIC_NAME_KEY, musicNameKey);
//		bundle.putInt(KEY_FAVORITE, favorite);
		dest.writeBundle(bundle);
	}
	
	public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {

		@Override
		public MusicInfo createFromParcel(Parcel source) {
			MusicInfo music = new MusicInfo();
			Bundle bundle = new Bundle();
			bundle = source.readBundle();
//			music._id = bundle.getInt(KEY_ID);
			music.songId = bundle.getInt(KEY_SONG_ID);
			music.albumId = bundle.getInt(KEY_ALBUM_ID);
			music.duration = bundle.getInt(KEY_DURATION);
//			music.musicName = bundle.getString(KEY_MUSIC_NAME);
			music.artist = bundle.getString(KEY_ARTIST);
//			music.data = bundle.getString(KEY_DATA);
			music.folder = bundle.getString(KEY_FOLDER);
//			music.musicNameKey = bundle.getString(KEY_MUSIC_NAME_KEY);
//			music.favorite = bundle.getInt(KEY_FAVORITE);
			return music;
		}

		@Override
		public MusicInfo[] newArray(int size) {
			return new MusicInfo[size];
		}
	};


//	public int getFavorite() {
//		return favorite;
//	}
//	public void setFavorite(int favorite) {
//		this.favorite = favorite;
//	}
	public long getId() {
		// TODO Auto-generated method stub
		return songId;
	}
//	public String getTitle() {
//		// TODO Auto-generated method stub
//		return this.musicName;
//	}
//	public int getAlbumId() {
//		// TODO Auto-generated method stub
//		return this.albumId;
//	}
	public String getArtist() {
		// TODO Auto-generated method stub
		return this.artist;
	}
//	public int getDuration() {
//		// TODO Auto-generated method stub
//		return this.duration;
//	}
//	public String getUrl() {
//		// TODO Auto-generated method stub
//		return this.data;
//	}

}