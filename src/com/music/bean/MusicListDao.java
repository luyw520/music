package com.music.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "l_music_list".
*/
public class MusicListDao extends AbstractDao<MusicList, Void> {

    public static final String TABLENAME = "l_music_list";

    /**
     * Properties of entity MusicList.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property SongId = new Property(0, long.class, "songId", false, "songId");
        public final static Property SongListName = new Property(1, String.class, "songListName", false, "song_list_name");
        public final static Property Pic = new Property(2, String.class, "pic", false, "pic");
    }


    public MusicListDao(DaoConfig config) {
        super(config);
    }
    
    public MusicListDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"l_music_list\" (" + //
                "\"songId\" INTEGER NOT NULL ," + // 0: songId
                "\"song_list_name\" TEXT," + // 1: songListName
                "\"pic\" TEXT);"); // 2: pic
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"l_music_list\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MusicList entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getSongId());
 
        String songListName = entity.getSongListName();
        if (songListName != null) {
            stmt.bindString(2, songListName);
        }
 
        String pic = entity.getPic();
        if (pic != null) {
            stmt.bindString(3, pic);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MusicList entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getSongId());
 
        String songListName = entity.getSongListName();
        if (songListName != null) {
            stmt.bindString(2, songListName);
        }
 
        String pic = entity.getPic();
        if (pic != null) {
            stmt.bindString(3, pic);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public MusicList readEntity(Cursor cursor, int offset) {
        MusicList entity = new MusicList( //
            cursor.getLong(offset + 0), // songId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // songListName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // pic
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MusicList entity, int offset) {
        entity.setSongId(cursor.getLong(offset + 0));
        entity.setSongListName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPic(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(MusicList entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(MusicList entity) {
        return null;
    }

    @Override
    public boolean hasKey(MusicList entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}