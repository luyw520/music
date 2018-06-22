package com.music.db;

import com.lu.library.util.DebugLog;
import com.music.MusicApplication;
import com.music.annotation.ComputeTime;
import com.music.bean.DaoMaster;
import com.music.bean.DaoSession;
import com.music.bean.MusicInfo;
import com.music.model.MusicModel;

import java.util.ArrayList;
import java.util.List;

import static com.music.ui.service.IConstants.DB_NAME;

/**
 * Created by lyw on 2017/12/10.
 */

public class DBHelper {

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
        com.music.bean.MusicInfoDao dao=daoSession.getMusicInfoDao();
        dao.insertOrReplace(musicInfo);
    }
    @ComputeTime
    public List<MusicInfo> sortMp3InfosByTitle(){
        if (musicInfoList!=null&&!musicInfoList.isEmpty()){
            return musicInfoList;
        }
        musicInfoList.clear();
        com.music.bean.MusicInfoDao dao=daoSession.getMusicInfoDao();
        List<MusicInfo> list=dao.queryBuilder().orderAsc(com.music.bean.MusicInfoDao.Properties.TitleKey).list();
        if (list.isEmpty()){
            DebugLog.d("本地数据没有，去手机多媒体数据库查询");
            list.addAll(MusicModel.getInstance().sortMp3InfosByTitle(MusicApplication.getInstance()));
        }else{
            DebugLog.d("从本地数据库获取 ");
            MusicModel.getInstance().getMusicList().addAll(list);
        }
        musicInfoList.addAll(list);
        return null;
    }
    public void setMusicLove(MusicInfo musicInfo){
       if (musicInfo.getTag()==0){
           musicInfo.setTag(1);
       }else{
           musicInfo.setTag(0);
       }
        insertOrReplace(musicInfo);
    }
}
