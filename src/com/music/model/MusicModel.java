package com.music.model;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.lu.library.log.DebugLog;
import com.music.bean.AlbumInfo;
import com.music.bean.ArtistInfo;
import com.music.bean.FolderInfo;
import com.music.bean.MusicInfo;
import com.music.db.DBHelper;
import com.music.helpers.StringHelper;
import com.music.utils.DeBug;
import com.music.utils.LogUtil;

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
    private ArrayList<MusicInfo> musicList = new ArrayList<>();
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

//    /**
//     *
//     *            local,artist,album,folder
//     * @return
//     */
//    public List<MusicInfo> queryMusicByFolder(String folderPath) {
//        DebugLog.d("folderPath:"+folderPath);
//        List<MusicInfo> musicInfos=new ArrayList<>();
//        for (MusicInfo musicInfo:musicList){
//            if (musicInfo.getFolder().equals(folderPath)){
//                musicInfos.add(musicInfo);
//            }
//
//        }
//        return musicInfos;
//    }
    /**
     * 从手机多媒体数据库查询音乐文件
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
                long a = System.currentTimeMillis();
                Cursor cursor = cr.query(uri, proj_music, select.toString(), null,
                        MediaStore.Audio.Media.ARTIST_KEY);
                musicInfos = getMusicList(cursor);
                break;
            case START_FROM_ARTIST:
                break;
            case START_FROM_ALBUM:
                break;
            case START_FROM_FOLDER:
                break;

        }
        return musicInfos;
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

    /**
     * cursor中获取MusicInfo信息
     * @param cursor
     * @return
     */
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
        music.setTitleKey(StringHelper.getPingYin(music.getTitle()));
        music.setArtistKey(StringHelper.getPingYin(music.getArtist()));
        DeBug.d(this,music.toString());
        return music;
    }

    /**
     * 查询MP3文件并且排序
     * @param context
     * @return
     */
    public List<MusicInfo> sortMp3InfosByTitle(Context context){
        if (!musicList.isEmpty()){
            return musicList;
        }
        List<MusicInfo> list = getAllMusicInfos(context);
        Collections.sort(list);
        musicList.addAll(list);
        return list;
    }


    /**
     * 通过 EXTERNAL_CONTENT_URI查找所有音乐文件
     * @param context
     * @return
     */
    private  List<MusicInfo> getAllMusicInfos(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        LogUtil.d(this,"从手机多媒体库中查询 cursor.getCount()=" + cursor.getCount());
        while (cursor.moveToNext()) {
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); //
            if (isMP3(url)&&isMusic(size)){
                MusicInfo musicInfo=resovleMusicInfoFromCursor(cursor);
                musicList.add(musicInfo);
                DBHelper.getInstance().insertOrReplace(musicInfo);
            }
        }
        return musicList;
    }
    /**
     *判断是否.mp3文件
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
    /** 大小是否超过1M
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
     * 有可能获取失败
     * @param cursor
     * @return 专辑集合
     */
    public List<AlbumInfo> getAlbumListFromCursor(Cursor cursor) {
        List<AlbumInfo> list = new ArrayList<AlbumInfo>();
        while (cursor.moveToNext()) {
            AlbumInfo info = new AlbumInfo();
            info.album_name = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            info.pinyin = StringHelper.getPingYin(info.album_name);
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
            DebugLog.d(info.toString());
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

        if (!albumInfoList.isEmpty()){
            return albumInfoList;
        }
		Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();
        Cursor cursor=cr.query(uri, null, null,
                null, null);
        List<AlbumInfo> albumInfos = new ArrayList<>();
//        DebugLog.d("isClosed:"+cursor.isClosed());
//        if(!cursor.isClosed()&&cursor.getCount()==0){
            cursor.close();
//            albumInfos.addAll(getAlbumListFromMp3File());
//        }else{
//            albumInfos.addAll(getAlbumListFromCursor(cursor));
//        }
		Collections.sort(albumInfos);
        albumInfoList.addAll(albumInfos);
		return albumInfos;
//		 }
	}

    /**
     * 所有本地音乐文件
     */
    public ArrayList<MusicInfo> getMusicList() {
        return musicList;
    }

    public void setMusicList(ArrayList<MusicInfo> musicList) {
        this.musicList = musicList;
    }

    /**
     * 利用mp3agic 库解析专辑
     */
//	private List<AlbumInfo> getAlbumListFromMp3File( ){
//        List<AlbumInfo> list = new ArrayList<AlbumInfo>();
//        DebugLog.d(" 利用mp3agic 库解析专辑");
//        Map<String,Integer> map=new HashMap<>();
//            for (MusicInfo mp3Info:musicList){
//                Mp3File mp3file= null;
//                try {
//                    mp3file = new Mp3File(mp3Info.getPlayPath());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (mp3file!=null&&mp3file.hasId3v2Tag()) {
//                    ID3v2 id3v2Tag = mp3file.getId3v2Tag();
//                   String album=  id3v2Tag.getAlbum();
//                   AlbumInfo albumInfo=new AlbumInfo();
//                   albumInfo.album_name=album;
//                   albumInfo.album_art= id3v2Tag.getAlbumArtist();
//                   if (list.contains(albumInfo)){
//                       albumInfo.number_of_songs++;
//                   }else{
//                       albumInfo.number_of_songs=1;
//                       list.add(albumInfo);
//                   }
//                }
//            }
//            return list;
//
//    }
}
