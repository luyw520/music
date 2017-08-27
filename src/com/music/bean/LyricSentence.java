package com.music.bean;

/**
 * */
public class LyricSentence {

	private long startTime = 0;

	private long duringTime = 0;

	private String contentText = "";

	public LyricSentence(long time, String text) {
		startTime = time;
		contentText = text;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public long getDuringTime() {
		return duringTime;
	}

	public void setDuringTime(long duringTime) {
		this.duringTime = duringTime;
	}
}
