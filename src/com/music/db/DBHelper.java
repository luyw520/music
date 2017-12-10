package com.music.db;

import com.music.MusicApplication;
import com.music.bean.*;
import com.music.bean.MusicInfoDao;

import java.util.ArrayList;
import java.util.List;

import static com.music.ui.service.IConstants.DB_NAME;

/**
 * Created by lyw on 2017/12/10.
 */

public class DBHelper {
//    com.music.bean.MusicInfoDao musicInfoDao;

    DaoMaster daoMaster;
    DaoSession daoSession;
    private static DBHelper dbHelper=new DBHelper();
    private List<MusicInfo> musicInfoList=new ArrayList<>();
    public static DBHelper getInstance(){
        return dbHelper;
    }
    public void init(){
        DaoMaster.DevOpenHelper openHelper=new DaoMaster.DevOpenHelper(MusicApplication.getInstance(),DB_NAME) ;
        daoMaster=new DaoMaster(openHelper.getWritableDatabase());
        daoSession=daoMaster.newSession();
    }
    public void insertOrReplace(MusicInfo musicInfo){
        MusicInfoDao dao=daoSession.getMusicInfoDao();
        dao.insertOrReplace(musicInfo);
    }
    public List<MusicInfo> queryAllMusic(){
        if (musicInfoList!=null&&!musicInfoList.isEmpty()){
            return musicInfoList;
        }
        musicInfoList.clear();
        MusicInfoDao dao=daoSession.getMusicInfoDao();
        List<MusicInfo> list=dao.queryBuilder().orderAsc(MusicInfoDao.Properties.TitleKey).list();
        musicInfoList.addAll(list);
        return list;
    }
}
