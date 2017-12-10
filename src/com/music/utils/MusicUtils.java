package com.music.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Files.FileColumns;
import android.text.TextUtils;

import com.music.bean.AlbumInfo;
import com.music.bean.ArtistInfo;
import com.music.bean.FolderInfo;
import com.music.bean.MusicInfo;
import com.music.db.AlbumInfoDao;
import com.music.db.ArtistInfoDao;
import com.music.db.DBHelper;
import com.music.db.FavoriteInfoDao;
import com.music.db.FolderInfoDao;
import com.music.db.MusicInfoDao;
import com.music.ui.service.IConstants;

/**
 *
 * @author longdw(longdawei1988@gmail.com)
 * 
 */
public class MusicUtils implements IConstants {



	private static String[] proj_music = new String[] {
			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID,
			MediaStore.Audio.Media.DURATION };

	private static String[] proj_album = new String[] { Albums.ALBUM,
			Albums.NUMBER_OF_SONGS, Albums._ID, Albums.ALBUM_ART };

	private static String[] proj_artist = new String[] {
			MediaStore.Audio.Artists.ARTIST,
			MediaStore.Audio.Artists.NUMBER_OF_TRACKS };

	private static String[] proj_folder = new String[] { FileColumns.DATA };

	public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
	public static final int FILTER_DURATION = 1 * 60 * 1000;// 1 MIN

	static {
	}

	private MusicInfoDao mMusicInfoDao;
	private AlbumInfoDao mAlbumInfoDao;
	private ArtistInfoDao mArtistInfoDao;
	private FolderInfoDao mFolderInfoDao;
	private FavoriteInfoDao mFavoriteDao;

	private List<AlbumInfo> albumInfos = new ArrayList<AlbumInfo>();
	private List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
	private static MusicUtils musicUtils = null;
	private Context mContext;

	private MusicUtils(Context context) {
		mContext = context;
		// initData();
	}

	public static void init(Context context) {
		musicUtils = new MusicUtils(context);

	}

	public void initData() {

//		queryAlbums(mContext);
	}

	public static MusicUtils getDefault() {
		return musicUtils;
	}

	public List<MusicInfo> queryFavorite(Context context) {
		if (mFavoriteDao == null) {
			mFavoriteDao = new FavoriteInfoDao(context);
		}
		return mFavoriteDao.getMusicInfo();
	}

//	/**
//	 *查询所有音乐文件
//	 * @param context
//	 * @return
//	 */
//	@SuppressLint("NewApi")
	public List<FolderInfo> queryFolder(Context context) {
		if (mFolderInfoDao == null) {
			mFolderInfoDao = new FolderInfoDao(context);
		}
		// if (mFolderInfoDao.hasData()) {
		// return mFolderInfoDao.getFolderInfo();
		// }
		Uri uri = MediaStore.Files.getContentUri("external");
		ContentResolver cr = context.getContentResolver();
		StringBuilder mSelection = new StringBuilder(FileColumns.MEDIA_TYPE
				+ " = " + FileColumns.MEDIA_TYPE_AUDIO + " and " + "("
				+ FileColumns.DATA + " like'%.mp3' or " + Media.DATA
				+ " like'%.wma')");
		// if(sp.getFilterSize()) {
		mSelection.append(" and " + Media.SIZE + " > " + FILTER_SIZE);
		// }
		// if(sp.getFilterTime()) {
		mSelection.append(" and " + Media.DURATION + " > " + FILTER_DURATION);
		// }
		mSelection.append(") group by ( " + FileColumns.PARENT);
		List<FolderInfo> list = getFolderList(cr.query(uri, proj_folder,
				mSelection.toString(), null, null));
		// mFolderInfoDao.saveFolderInfo(list);
		return list;
	}
//
//	/**
//	 *查询所有艺术家
//	 * @param context
//	 * @return
//	 */
	public List<ArtistInfo> queryArtist(Context context) {
		if (mArtistInfoDao == null) {
			mArtistInfoDao = new ArtistInfoDao(context);
		}
		// if (mArtistInfoDao.hasData()) {
		// return mArtistInfoDao.getArtistInfo();
		// }
		Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();
		d("queryArtist.....................uri:"+uri.toString());
		List<ArtistInfo> list = getArtistList(cr.query(uri, proj_artist, null,
				null, MediaStore.Audio.Artists.NUMBER_OF_TRACKS + " desc"));
//		mArtistInfoDao.saveArtistInfo(list);
		d(list.toString());
		return list;
	}

//	/**
//	 *查询所有专辑
//	 * @param context
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
	public List<AlbumInfo> queryAlbums(Context context) {
		if (mAlbumInfoDao == null) {
			mAlbumInfoDao = new AlbumInfoDao(context);
		}

		if (albumInfos.size() > 0) {
			return albumInfos;
		}
		// SPStorage sp = new SPStorage(context);

		Uri uri = Albums.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();
		StringBuilder where = new StringBuilder(Albums._ID
				+ " in (select distinct " + Media.ALBUM_ID
				+ " from audio_meta where (1=1 ");

		// if(sp.getFilterSize()) {
		where.append(" and " + Media.SIZE + " > " + FILTER_SIZE);
		// }
		// if(sp.getFilterTime()) {
		where.append(" and " + Media.DURATION + " > " + FILTER_DURATION);
		// }
		where.append("))");

		// if (mAlbumInfoDao.hasData()) {
		// return mAlbumInfoDao.getAlbumInfo();
		// } else {
		albumInfos = getAlbumList(cr.query(uri, proj_album, where.toString(),
				null, Media.ALBUM_KEY));
		// mAlbumInfoDao.saveAlbumInfo(list);
		Collections.sort(albumInfos);
		d(albumInfos.toString());
		return albumInfos;
		// }
	}

	/**
	 * 查询所有音乐
	 * @param context
	 * @param from
	 * @return
	 */
	public List<MusicInfo> queryMusic(Context context, int from) {
		return queryMusic(context, null, null, from);
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

		if (mMusicInfoDao == null) {
			mMusicInfoDao = new MusicInfoDao(context);
		}
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();

		StringBuffer select = new StringBuffer(" 1=1 ");
		select.append(" and " + Media.SIZE + " > " + FILTER_SIZE);
		select.append(" and " + Media.DURATION + " > " + FILTER_DURATION);
		if (!TextUtils.isEmpty(selections)) {
			select.append(selections);
		}
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
//			mMusicInfoDao.saveMusicInfo(musicInfos);
			// }
			break;
		case START_FROM_ARTIST:
			if (mMusicInfoDao.hasData()) {
				musicInfos = mMusicInfoDao.getMusicInfoByType(selection,
						START_FROM_ARTIST);
			} else {
				// return getMusicList(cr.query(uri, proj_music,
				// select.toString(), null,
				// MediaStore.Audio.Media.ARTIST_KEY));
			}
			break;
		case START_FROM_ALBUM:
			if (mMusicInfoDao.hasData()) {
				musicInfos = mMusicInfoDao.getMusicInfoByType(selection,
						START_FROM_ALBUM);
			}
			break;
		case START_FROM_FOLDER:
			if (mMusicInfoDao.hasData()) {
				musicInfos = mMusicInfoDao.getMusicInfoByType(selection,
						START_FROM_FOLDER);
			}
			break;

		}
		return musicInfos;
	}

	private  static void d(String msg) {
		DeBug.d(MusicUtils.class, msg);
	}

//	/**
//	 * 从游标中获取所有音乐
//	 * @param cursor
//	 * @return
//	 */
	public static ArrayList<MusicInfo> getMusicList(Cursor cursor) {
		if (cursor == null) {
			return null;
		}
		ArrayList<MusicInfo> musicList = new ArrayList<MusicInfo>();
		DeBug.d(MusicUtils.class,
				"getMusicList,local music,size:" + cursor.getCount());
		while (cursor.moveToNext()) {
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

			music.setTitleKey(StringUtil.getPingYin(music.getTitle()));
			music.setArtistKey(StringUtil.getPingYin(music.getArtist()));
			musicList.add(music);

			DBHelper.getInstance().insertOrReplace(music);
			DeBug.d(MusicUtils.class,music.toString());
		}
		cursor.close();
		return musicList;
	}
//	/**
//	 * 从游标中获取所有音乐专辑
//	 * @param cursor
//	 * @return
//	 */
	public List<AlbumInfo> getAlbumList(Cursor cursor) {
		List<AlbumInfo> list = new ArrayList<AlbumInfo>();
		d("album,size:" + cursor.getCount());
		while (cursor.moveToNext()) {
			AlbumInfo info = new AlbumInfo();
			info.album_name = cursor.getString(cursor
					.getColumnIndex(Albums.ALBUM));
			info.pinyin = StringUtil.getPingYin(info.album_name);
			info.album_id = cursor.getInt(cursor.getColumnIndex(Albums._ID));
			info.number_of_songs = cursor.getInt(cursor
					.getColumnIndex(Albums.NUMBER_OF_SONGS));
			info.album_path = cursor.getString(cursor
					.getColumnIndex(Albums.ALBUM_ART));
			DeBug.d("getAlbumList:",info.toString());
			list.add(info);
		}
		cursor.close();
		return list;
	}
//	/**
//	 * 从游标中获取所有音乐歌手
//	 * @param cursor
//	 * @return
//	 */
	public List<ArtistInfo> getArtistList(Cursor cursor) {
		d("getArtistList:...........................size:"+cursor.getCount());
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

//	/**
//	 * 从游标中获取所有文件夹
//	 * @param cursor
//	 * @return
//	 */
	public static List<FolderInfo> getFolderList(Cursor cursor) {
		d("getFolderList,,,,,,,,,,,,,,,,size:" + cursor.getCount());
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



}
