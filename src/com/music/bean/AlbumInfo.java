package com.music.bean;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.music.helpers.StringHelper;


@SuppressWarnings("rawtypes")
public class AlbumInfo implements Parcelable , Comparable{

	public static final String KEY_ALBUM_NAME = "album_name";
	public static final String KEY_ALBUM_ID = "album_id";
	public static final String KEY_NUMBER_OF_SONGS = "number_of_songs";
	public static final String KEY_ALBUM_ART = "album_art";

	public String album_name;
	public int album_id = -1;
	public int number_of_songs = 0;
	public String album_art;
	public Bitmap bitmap;
	public String album_path;
	public String pinyin;
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return album_name.equals(((AlbumInfo)obj).album_name);
	}

	@Override
	public String toString() {
		return "AlbumInfo{" +
				"album_name='" + album_name + '\'' +
				", album_id=" + album_id +
				", number_of_songs=" + number_of_songs +
				", album_art='" + album_art + '\'' +
				", bitmap=" + bitmap +
				", album_path='" + album_path + '\'' +
				", pinyin='" + pinyin + '\'' +
				'}';
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putString(KEY_ALBUM_NAME, album_name);
		bundle.putString(KEY_ALBUM_ART, album_art);
		bundle.putInt(KEY_NUMBER_OF_SONGS, number_of_songs);
		bundle.putInt(KEY_ALBUM_ID, album_id);
		dest.writeBundle(bundle);
	}

	public static final Parcelable.Creator<AlbumInfo> CREATOR = new Parcelable.Creator<AlbumInfo>() {

		@Override
		public AlbumInfo createFromParcel(Parcel source) {
			AlbumInfo info = new AlbumInfo();
			Bundle bundle = source.readBundle();
			info.album_name = bundle.getString(KEY_ALBUM_NAME);
			info.album_art = bundle.getString(KEY_ALBUM_ART);
			info.number_of_songs = bundle.getInt(KEY_NUMBER_OF_SONGS);
			info.album_id = bundle.getInt(KEY_ALBUM_ID);
			return info;
		}

		@Override
		public AlbumInfo[] newArray(int size) {
			return new AlbumInfo[size];
		}
	};
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		int i=0;
		boolean flag=false;
		AlbumInfo albumInfo=(AlbumInfo) arg0;
		String album_name_first= StringHelper.getPingYin(album_name);

		String album_name2_first=StringHelper.getPingYin(albumInfo.album_name);
		for(;i<album_name_first.length();i++){
			if(i<album_name2_first.length()){
				if(album_name_first.charAt(i)!=album_name2_first.charAt(i)){
					break;
				}else{
					continue;
				}
			}else{
				flag=true;
				break;

			}

		}
		if(i==album_name_first.length()){
			return -1;
		}
		if(flag){
			return 1;
		}
		return album_name_first.charAt(i)-album_name2_first.charAt(i);
//		return 0;
	}

}
