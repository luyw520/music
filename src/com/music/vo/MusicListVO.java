package com.music.vo;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music.vo
 * @description: ${TODO}{ 类注释}
 * @date: 2018/7/11 0011
 */

public class MusicListVO {
    public String musicListName;
    public int songSize;
    public String picPath;

    @Override
    public boolean equals(Object obj) {
        MusicListVO that= (MusicListVO) obj;
        return that.musicListName.equals(musicListName);
    }
}
