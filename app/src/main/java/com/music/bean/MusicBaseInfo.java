package com.music.bean;

public interface  MusicBaseInfo {
	/**
	 * 歌曲ID
	 * @return
	 */
	public long getId();
	/**
	 * 歌曲标题
	 * @return
	 */
	public String getTitle();
	/**
	 * 
	 * @return 歌曲专辑ID
	 */
	public long getAlbumId();
	/**
	 * 
	 * @return 歌曲的歌手
	 */
	public String getArtist();
	
	/**
	 * 
	 * @return 歌曲时长
	 */
	public long getDuration();
	
	/**
	 * 
	 * @return 播放路径
	 */
	public String getUrl();

}
