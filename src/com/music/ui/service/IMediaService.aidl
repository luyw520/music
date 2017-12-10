package com.music.ui.service;
import java.util.List;
import com.music.bean.MusicInfo;
interface IMediaService {
    void play(String playPath);
	void pause();
	void cotinuePlay();
	int duration();
    int position();
    void seekTo(int progress,String playPath);
    boolean getPlayState();
    int getPlayMode();
    void setPlayMode(int mode);
    void sendPlayStateBrocast();
    void exit();
    int getCurMusicId();
}