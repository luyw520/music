package com.music.db;

import com.lu.library.log.DebugLog;
import com.lu.library.util.ObjectUtil;
import com.music.MusicApplication;
import com.music.bean.DaoMaster;
import com.music.bean.DaoSession;
import com.music.bean.MusicInfo;
import com.music.bean.MusicList;
import com.music.bean.MusicListDao;
import com.music.lu.R;

import java.util.ArrayList;
import java.util.List;

import static com.music.ui.service.IConstants.DB_NAME;

/**
 * Created by lyw on 2017/12/10.
 */

public class DBHelper {

    DaoMaster daoMaster;
    DaoSession daoSession;
    private static DBHelper dbHelper = new DBHelper();
    private List<MusicInfo> musicInfoList = new ArrayList<>();

    public static DBHelper getInstance() {
        return dbHelper;
    }

    public void init() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(MusicApplication.getInstance(), DB_NAME);
        daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }
    public void buildDelete(String musicListName,MusicInfo musicInfo){
        MusicListDao dao=daoSession.getMusicListDao();
        dao.queryBuilder().where(MusicListDao.Properties.SongListName.eq(musicListName),MusicListDao.Properties.SongId.eq(musicInfo.getSongId())).buildDelete();
    }
    public void insertMusucList(String musicListName,MusicInfo musicInfo){
        MusicListDao dao=daoSession.getMusicListDao();
        MusicList musicList=new MusicList();
        musicList.setSongListName(musicListName);
        if (musicInfo!=null){
            List<MusicList> musicListList=dao.queryBuilder().where(MusicListDao.Properties.SongId.eq(musicInfo.getSongId())).list();
            //该歌单已有该歌曲
            if (!ObjectUtil.isCollectionEmpty(musicListList)){
                DebugLog.d("该歌单也有该歌曲");
                return;
            }
            musicList=new MusicList(musicInfo.getSongId(),musicListName,musicInfo.picUrl);
        }


        dao.insert(musicList);
    }
    /**
     * 获取所有的歌单
     * @return
     */
    public List<MusicList> getMusicList(){
        MusicListDao dao=daoSession.getMusicListDao();
        List<MusicList> musicListList=dao.queryBuilder().list();
        if (ObjectUtil.isCollectionEmpty(musicListList)){
            MusicList musicList=new MusicList();
            musicList.setSongListName(MusicApplication.getInstance().getResources().getString(R.string.my_like_song));
            musicListList.add(musicList);
            dao.insert(musicList);
        }
        return musicListList;
    }

//    /**
//     * 获取所有的歌单
//     * @return
//     */
//    public List<MusicListVO> getMusicList(){
//        List<MusicListVO> musicListVOList=new ArrayList<>();
//        MusicListDao dao=daoSession.getMusicListDao();
//        List<MusicList> musicListList=dao.queryBuilder().list();
//        if (ObjecteUtil.isCollectionEmpty(musicListList)){
//            MusicList musicList=new MusicList();
//            musicList.setSongListName(MusicApplication.getInstance().getResources().getString(R.string.my_like_song));
//            musicListList.add(musicList);
//            dao.insert(musicList);
//        }
//        musicListVOList.addAll(musicListToMusicListVO(musicListList));
//        return musicListVOList;
//    }

//    private List<MusicListVO> musicListToMusicListVO(List<MusicList> musicListList){
//        List<MusicListVO> musicListVOList=new ArrayList<>();
//
//        for (MusicList musicList:musicListList){
//            MusicListVO musicListVO=new MusicListVO();
//            musicListVO.musicListName=musicList.getSongListName();
//            boolean isFind=false;
//           for (int i=0,size=musicListVOList.size();i<size;i++){
//               MusicListVO temp= musicListVOList.get(i);
//               if (temp.musicListName.equals(musicListVO.musicListName)){
//                   isFind=true;
//                   temp.songSize++;
//                   break;
//               }
//           }
//           if (!isFind){
//               musicListVOList.add(musicListVO);
//           }
//        }
//        return musicListVOList;
//    }




    public void insertOrReplace(MusicInfo musicInfo) {
        com.music.bean.MusicInfoDao dao = daoSession.getMusicInfoDao();
        dao.insertOrReplace(musicInfo);
    }

    public List<MusicInfo> getMusicOrderAscByTitle() {
        com.music.bean.MusicInfoDao dao = daoSession.getMusicInfoDao();
        List<MusicInfo> list = dao.queryBuilder().orderAsc(com.music.bean.MusicInfoDao.Properties.TitleKey).list();
        return list;
    }





}
