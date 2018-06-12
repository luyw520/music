package com.music.bean;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig musicInfoDaoConfig;

    private final MusicInfoDao musicInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        musicInfoDaoConfig = daoConfigMap.get(MusicInfoDao.class).clone();
        musicInfoDaoConfig.initIdentityScope(type);

        musicInfoDao = new MusicInfoDao(musicInfoDaoConfig, this);

        registerDao(MusicInfo.class, musicInfoDao);
    }

    public void clear() {
        musicInfoDaoConfig.clearIdentityScope();
    }

    public MusicInfoDao getMusicInfoDao() {
        return musicInfoDao;
    }

}