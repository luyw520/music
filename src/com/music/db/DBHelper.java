package com.music.db;

import com.lu.library.base.BaseObserver;
import com.lu.library.util.DebugLog;
import com.music.MusicApplication;
import com.music.bean.DaoMaster;
import com.music.bean.DaoSession;
import com.music.bean.MusicInfo;
import com.music.bean.MusicInfoDao;
import com.music.model.MusicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    public void sortMp3InfosByTitleByRx(BaseObserver<List<MusicInfo>> receiver){
        if (musicInfoList!=null&&!musicInfoList.isEmpty()){
            receiver.onNext(musicInfoList);
//            return musicInfoList;
        }
        musicInfoList.clear();
        Observable.create(new ObservableOnSubscribe<List<MusicInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MusicInfo>> observableEmitter) throws Exception {
                MusicInfoDao dao=daoSession.getMusicInfoDao();
                DebugLog.d(Thread.currentThread());
                List<MusicInfo> list=dao.queryBuilder().orderAsc(com.music.bean.MusicInfoDao.Properties.TitleKey).list();
                if (list.isEmpty()){
                    DebugLog.d("本地数据没有，去手机多媒体数据库查询");
                    list.addAll(MusicModel.getInstance().sortMp3InfosByTitle(MusicApplication.getInstance()));
                }else{
                    DebugLog.d("从本地数据库获取 ");
                    MusicModel.getInstance().getMusicList().addAll(list);
                }
                musicInfoList.addAll(list);
                observableEmitter.onNext(list);
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(receiver);
    }
     Observable<MusicInfo> sampleObservable() {   //这个玩意跑在子线程
        return Observable.defer(new Callable<ObservableSource<MusicInfo>>() {
            @Override public ObservableSource<MusicInfo> call() throws Exception {
                com.music.bean.MusicInfoDao dao=daoSession.getMusicInfoDao();
                List<MusicInfo> list=dao.queryBuilder().orderAsc(com.music.bean.MusicInfoDao.Properties.TitleKey).list();
                return Observable.fromArray((MusicInfo[])list.toArray());
            }
        });
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
