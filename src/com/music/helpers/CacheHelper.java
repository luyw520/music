package com.music.helpers;

import com.music.bean.MusicInfo;
import com.music.vo.MusicListVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music.helpers
 * @description: ${TODO}{ 类注释}
 * @date: 2018/7/12 0012
 * 对象缓存类
 */

public class CacheHelper {

    public List<MusicInfo> allMusic=new ArrayList<>();

    public List<MusicListVO> musicListVOList=new ArrayList<>();


    static CacheHelper cacheHelper=new CacheHelper();

    private CacheHelper(){

    }
    public void clearAndAddAll(List<MusicInfo> datas){
        allMusic.clear();
        allMusic.addAll(datas);
    }
    public static CacheHelper getInstance(){
        return cacheHelper;
    }
}
