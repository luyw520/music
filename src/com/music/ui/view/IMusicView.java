package com.music.ui.view;

import com.music.bean.MusicInfo;

import java.util.List;

/**
 * Created by lyw on 2017/12/10.
 */

public interface IMusicView {
    void load(List<MusicInfo> musicInfoList);
}
