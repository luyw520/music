package com.music.presenter;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music.presenter
 * @description: ${TODO}{ 类注释}
 * @date: 2018/6/20 0020
 */

public interface IPlayState {

    void playMusicState();
    void currentState(int currentTime);
    void duration(int  duration);
    void pauseMusicState();
}
