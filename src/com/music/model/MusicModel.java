package com.music.model;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.music.bean.AlbumInfo;
import com.music.bean.ArtistInfo;
import com.music.bean.FolderInfo;
import com.music.bean.MusicInfo;
import com.music.utils.DeBug;
import com.music.utils.LogUtil;
import com.music.utils.MusicUtils;
import com.music.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.music.ui.service.IConstants.START_FROM_ALBUM;
import static com.music.ui.service.IConstants.START_FROM_ARTIST;
import static com.music.ui.service.IConstants.START_FROM_FOLDER;
import static com.music.ui.service.IConstants.START_FROM_LOCAL;


/**
 * Created by lyw on 2017/10/3.
 * 查询音乐信息
 */

public class MusicModel {
    private static String[] proj_music = new String[] {
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION };

    private static String[] proj_album = new String[] { MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART };

    private static String[] proj_artist = new String[] {
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS };

    private static String[] proj_folder = new String[] { MediaStore.Files.FileColumns.DATA };

    public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
    public static final int FILTER_DURATION = 1 * 60 * 1000;// 1 MIN
    private List<FolderInfo> folderInfoList=new ArrayList<>();
    private static MusicModel musicModel=new MusicModel();
    private ArrayList<MusicInfo> musicList = new ArrayList<MusicInfo>();
    List<ArtistInfo> artistInfoList=new ArrayList<>();
//    private MusicInfoDao mMusicInfoDao;
//    private AlbumInfoDao mAlbumInfoDao;
//    private ArtistInfoDao mArtistInfoDao;
//    private FolderInfoDao mFolderInfoDao;
    private MusicModel(){

    }
    public static MusicModel getInstance(){
        return musicModel;
    }
    /**
     *
     * @param context
     * @param selections
     * @param selection
     * @param from
     *            local,artist,album,folder
     * @return
     */
    public List<MusicInfo> queryMusic(Context context, String selections,
                                      String selection, int from) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getContentResolver();

        StringBuffer select = new StringBuffer(" 1=1 ");
        select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);
        if (!TextUtils.isEmpty(selections)) {
            select.append(selections);
        }
        List<MusicInfo> musicInfos=new ArrayList<>();
        switch (from) {
            case START_FROM_LOCAL:
                // if local database has data
                // if (mMusicInfoDao.hasData()) {
                // musicInfos = mMusicInfoDao.getMusicInfo();
                // } else {
                // query android media database
                long a = System.currentTimeMillis();
                Cursor cursor = cr.query(uri, proj_music, select.toString(), null,
                        MediaStore.Audio.Media.ARTIST_KEY);
                musicInfos = getMusicList(cursor);
                d("query music spend time:" + (System.currentTimeMillis() - a)
                        / 1000.0 + " s");
                d("uri:" + uri.toString());
//                mMusicInfoDao.saveMusicInfo(musicInfos);
                // }
                break;
            case START_FROM_ARTIST:
//                if (mMusicInfoDao.hasData()) {
//                    musicInfos = mMusicInfoDao.getMusicInfoByType(selection,
//                            START_FROM_ARTIST);
//                } else {
//                    // return getMusicList(cr.query(uri, proj_music,
//                    // select.toString(), null,
//                    // MediaStore.Audio.Media.ARTIST_KEY));
//                }
                break;
            case START_FROM_ALBUM:
//                if (mMusicInfoDao.hasData()) {
//                    musicInfos = mMusicInfoDao.getMusicInfoByType(selection,
//                            START_FROM_ALBUM);
//                }
                break;
            case START_FROM_FOLDER:
//                if (mMusicInfoDao.hasData()) {
//                    musicInfos = mMusicInfoDao.getMusicInfoByType(selection,
//                            START_FROM_FOLDER);
//                }
                break;

        }
        return musicInfos;
    }
    private  void d(String msg) {
        DeBug.d(this, msg);
    }
    /**
     * 从游标中获取所有音乐
     * @param cursor
     * @return
     */
    public  ArrayList<MusicInfo> getMusicList(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        DeBug.d(MusicUtils.class,
                "getMusicList,local music,size:" + cursor.getCount());
        while (cursor.moveToNext()) {
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); //
            if (isMP3(url)&&isMusic(size)){
               musicList.add(resovleMusicInfoFromCursor(cursor));
            }
        }
        cursor.close();
        return musicList;
    }
    private MusicInfo resovleMusicInfoFromCursor(Cursor cursor){
        MusicInfo music = new MusicInfo();
        music.setSongId(cursor.getInt(cursor
                .getColumnIndex(MediaStore.Audio.Media._ID)));
        music.setAlbumId(cursor.getInt(cursor
                .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
        music.setDuration(cursor.getInt(cursor
                .getColumnIndex(MediaStore.Audio.Media.DURATION)));
        music.setTitle(cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.TITLE)));
        music.setArtist(cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        String filePath = cursor.getString(cursor
                .getColumnIndex(MediaStore.Audio.Media.DATA));
        String folderPath = filePath.substring(0,
                filePath.lastIndexOf(File.separator));
        music.setFolder(folderPath);
        music.setPlayPath(filePath);
        music.setTitleKey(StringUtil.getPingYin(music.getTitle()));
        music.setArtistKey(StringUtil.getPingYin(music.getArtist()));
        DeBug.d(this,music.toString());
        return music;
    }
    public List<MusicInfo> sortMp3InfosByTitle(Context context){
        if (!musicList.isEmpty()){
            return musicList;
        }
        List<MusicInfo> list = getAllMusicInfos(context);
        Collections.sort(list);
        musicList.addAll(list);
        return list;
    }
    public  List<MusicInfo> getAllMusicInfos(Context context) {
        long time1 = System.currentTimeMillis();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

//        List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
        LogUtil.d(this,"cursor.getCount()=" + cursor.getCount());
        while (cursor.moveToNext()) {
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); //
            if (isMP3(url)&&isMusic(size)){
                musicList.add(resovleMusicInfoFromCursor(cursor));
            }
        }
        long time2 = System.currentTimeMillis();
        return musicList;
    }
    /**
     *
     * @param url
     */
    private static boolean isMP3(String url) {
        /**
         * String file_class = url.substring(url.lastIndexOf("."));
         * file_class=.mp3
         */
        String file_class = url.substring(url.lastIndexOf(".") + 1);
        return file_class.equals("mp3");
    }
    /**
     * @return
     */
    public static boolean  isMusic(long size){
        return (size>1024*1024);
    }
    /**
     *查询所有音乐文件
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public List<FolderInfo> queryFolder(Context context) {
        if (!folderInfoList.isEmpty()){
            return  folderInfoList;
        }
        folderInfoList.clear();
        Uri uri = MediaStore.Files.getContentUri("external");
        ContentResolver cr = context.getContentResolver();
        StringBuilder mSelection = new StringBuilder(MediaStore.Files.FileColumns.MEDIA_TYPE
                + " = " + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO + " and " + "("
                + MediaStore.Files.FileColumns.DATA + " like'%.mp3' or " + MediaStore.Audio.Media.DATA
                + " like'%.wma')");
        // if(sp.getFilterSize()) {
        mSelection.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        // }
        // if(sp.getFilterTime()) {
        mSelection.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);
        // }
        mSelection.append(") group by ( " + MediaStore.Files.FileColumns.PARENT);
        List<FolderInfo> list = resolveFolderInfoFromCursour(cr.query(uri, proj_folder,
                mSelection.toString(), null, null));
        // mFolderInfoDao.saveFolderInfo(list);
        folderInfoList.addAll(list);
        return folderInfoList;
    }
    /**
     * 从游标中获取所有音乐专辑
     * @param cursor
     * @return
     */
    public List<AlbumInfo> getAlbumList(Cursor cursor) {
        List<AlbumInfo> list = new ArrayList<AlbumInfo>();
        while (cursor.moveToNext()) {
            AlbumInfo info = new AlbumInfo();
            info.album_name = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            info.pinyin = StringUtil.getPingYin(info.album_name);
            info.album_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
            info.number_of_songs = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
            info.album_path = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            DeBug.d("getAlbumList:",info.toString());
            list.add(info);
        }
        cursor.close();
        return list;
    }
    /**
     * 从游标中获取所有文件夹
     * @param cursor
     * @return
     */
    public  List<FolderInfo> resolveFolderInfoFromCursour(Cursor cursor) {
        DeBug.d(this,"getFolderList,,,,,,,,,,,,,,,,size:" + cursor.getCount());
        List<FolderInfo> list = new ArrayList<FolderInfo>();
        while (cursor.moveToNext()) {
            FolderInfo info = new FolderInfo();
            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Files.FileColumns.DATA));
            info.folder_path = filePath.substring(0,
                    filePath.lastIndexOf(File.separator));
            info.folder_name = info.folder_path.substring(info.folder_path
                    .lastIndexOf(File.separator) + 1);
            list.add(info);
        }
        cursor.close();
        return list;
    }

    /**
     *查询所有艺术家
     * @param context
     * @return
     */
    public List<ArtistInfo> queryArtist(Context context) {
//        if (mArtistInfoDao==null){
//            mArtistInfoDao=new ArtistInfoDao(context);
//        }
//         if (mArtistInfoDao.hasData()) {
//         return mArtistInfoDao.getArtistInfo();
//         }
        if (!artistInfoList.isEmpty()){
            return artistInfoList;
        }
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor=cr.query(uri, null, null,
                null, MediaStore.Audio.Artists.NUMBER_OF_TRACKS + " desc");
       DeBug.d(this, Arrays.toString(cursor.getColumnNames()));
       DeBug.d(this, cursor.getCount()+"..............");
        List<ArtistInfo> list = getArtistList(cursor);
//
        artistInfoList.addAll(list);
        return list;
    }

    /**
     * 从游标中获取所有音乐歌手
     * @param cursor
     * @return
     */
    public List<ArtistInfo> getArtistList(Cursor cursor) {
        List<ArtistInfo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            ArtistInfo info = new ArtistInfo();
            info.artist_name = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Artists.ARTIST));
            if (info.artist_name.equalsIgnoreCase("<unknown>")){
                continue;
            }
            info.number_of_tracks = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
            DeBug.d(MusicUtils.class,info.toString());
            list.add(info);
        }
        cursor.close();
        return list;
    }
    List<AlbumInfo> albumInfoList=new ArrayList<>();
    /**
     //	 *查询所有专辑
     //	 * @param context
     //	 * @return
     //	 */
//	@SuppressWarnings("unchecked")
	public List<AlbumInfo> queryAlbums(Context context) {
//		if (mAlbumInfoDao == null) {
//			mAlbumInfoDao = new AlbumInfoDao(context);
//		}
        if (!albumInfoList.isEmpty()){
            return albumInfoList;
        }
//		if (albumInfos.size() > 0) {
//			return albumInfos;
//		}
//		 SPStorage sp = new SPStorage(context);

		Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();
//		StringBuilder where = new StringBuilder(MediaStore.Audio.Albums._ID
//				+ " in (select distinct " + MediaStore.Audio.Media.ALBUM_ID
//				+ " from audio_meta where (1=1 ");
//
//		// if(sp.getFilterSize()) {
//		where.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
//		// }
//		// if(sp.getFilterTime()) {
//		where.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);
//		// }
//		where.append("))");

//		 if (mAlbumInfoDao.hasData()) {
//		 return mAlbumInfoDao.getAlbumInfo();
//		 } else {
        Cursor cursor=cr.query(uri, null, null,
                null, null);
        DeBug.d(this, Arrays.toString(cursor.getColumnNames()));
        DeBug.d(this, cursor.getCount()+"..............");
        List<AlbumInfo> albumInfos = getAlbumList(cursor);
		// mAlbumInfoDao.saveAlbumInfo(list);
		Collections.sort(albumInfos);
        albumInfoList.addAll(albumInfos);
		return albumInfos;
//		 }
	}
}
