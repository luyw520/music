package com.music.presenter;

import com.lu.library.base.BaseObserver;
import com.lu.library.base.BasePresenter;
import com.lu.library.log.DebugLog;
import com.lu.library.util.ObjectUtil;
import com.music.MusicApplication;
import com.music.bean.MusicInfo;
import com.music.bean.MusicList;
import com.music.db.DBHelper;
import com.music.helpers.CacheHelper;
import com.music.lu.R;
import com.music.model.MusicModel;
import com.music.vo.MusicListVO;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lyw on 2017/12/10.
 */

public class LocalMusicPresenter extends BasePresenter {



    private List<MusicListVO> musicListToMusicListVO(List<MusicList> musicListList){
        List<MusicListVO> musicListVOList=new ArrayList<>();

        for (MusicList musicList:musicListList){
            MusicListVO musicListVO=new MusicListVO();
            musicListVO.musicListName=musicList.getSongListName();
            boolean isFind=false;
            for (int i=0,size=musicListVOList.size();i<size;i++){
                MusicListVO temp= musicListVOList.get(i);
                if (temp.musicListName.equals(musicListVO.musicListName)){
                    isFind=true;
                    temp.songSize++;
                    break;
                }
            }
            if (!isFind){
                musicListVOList.add(musicListVO);
            }
        }
        return musicListVOList;
    }
    public List<MusicInfo> getCacheMusic(){
        return CacheHelper.getInstance().allMusic;
    }
    public List<MusicListVO> getCacheMusicList(){
        return CacheHelper.getInstance().musicListVOList;
    }
    public List<MusicInfo> getMusicByMusicListName(String musicListName){
        List<MusicInfo> list=new ArrayList<>();
        List<MusicList> musicLists= DBHelper.getInstance().getMusicList();
        if (ObjectUtil.isCollectionEmpty(musicLists)){
            return list;
        }
        //找到所有歌单
        for (MusicList musicList:musicLists){

            //找到对应的歌单
            if (musicList.getSongListName().equals(musicListName)){
                //找到对应的歌曲
                for (MusicInfo musicInfo:getCacheMusic()){
                    if (musicInfo.getSongId()==musicList.getSongId()){
                        list.add(musicInfo);
                    }
                }


            }
        }
        return list;
    }
    public void getMusicOrderAscByTitle(BaseObserver<List<MusicInfo>> receiver,boolean isCache) {

        if (isCache&&!ObjectUtil.isCollectionEmpty(CacheHelper.getInstance().allMusic)) {
            receiver.onNext(CacheHelper.getInstance().allMusic);
            receiver.onComplete();
            return;
        }
//        musicInfoList.clear();
        Observable.create(new ObservableOnSubscribe<List<MusicInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MusicInfo>> observableEmitter) throws Exception {
                DebugLog.d("subscribe:" + Thread.currentThread().getName());
                List<MusicInfo> list = DBHelper.getInstance().getMusicOrderAscByTitle();
                if (list.isEmpty()) {
                    DebugLog.d("本地数据没有，去手机多媒体数据库查询");
                    list.addAll(MusicModel.getInstance().sortMp3InfosByTitle(MusicApplication.getInstance()));
                } else {
                    DebugLog.d("从本地数据库获取 ");
                    MusicModel.getInstance().getMusicList().addAll(list);
                }
                CacheHelper.getInstance().clearAndAddAll(list);
                observableEmitter.onNext(list);
                observableEmitter.onComplete();
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(receiver);
    }
    /**
     * 获取所有的歌单
     * @return
     */
    public List<MusicListVO> getMusicList(){
        List<MusicListVO> musicListVOList=new ArrayList<>();
        List<MusicList> musicListList= DBHelper.getInstance().getMusicList();
        if (ObjectUtil.isCollectionEmpty(musicListList)){
            MusicList musicList=new MusicList();
            musicList.setSongListName(MusicApplication.getInstance().getResources().getString(R.string.my_like_song));
            musicListList.add(musicList);
        }
        musicListVOList.addAll(musicListToMusicListVO(musicListList));
        return musicListVOList;
    }


    public void setMusicLove(MusicInfo musicInfo) {
        if (musicInfo.getTag() == 0) {
            musicInfo.setTag(1);
            DBHelper.getInstance().insertMusucList(MusicApplication.getInstance().getResources().getString(R.string.my_like_song),musicInfo);
        } else {
            DBHelper.getInstance().buildDelete(MusicApplication.getInstance().getResources().getString(R.string.my_like_song),musicInfo);
            musicInfo.setTag(0);
        }
        DBHelper.getInstance().insertOrReplace(musicInfo);
    }

    public List<MusicInfo> queryMusicByFolder(String folderPath) {
        DebugLog.d("folderPath:"+folderPath);
        List<MusicInfo> musicInfos=new ArrayList<>();
        for (MusicInfo musicInfo:getCacheMusic()){
            if (musicInfo.getFolder().equals(folderPath)){
                musicInfos.add(musicInfo);
            }

        }
        return musicInfos;
    }
}
