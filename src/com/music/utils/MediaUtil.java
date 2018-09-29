package com.music.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.lu.library.log.DebugLog;
import com.lu.library.util.image.BitmapBuilder;
import com.lu.library.util.image.BitmapTranslater;
import com.lu.library.util.image.BitmapUtil;
import com.music.annotation.ComputeTime;
import com.music.bean.Mp3Info;
import com.music.bean.MusicInfo;
import com.music.lu.R;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressLint("DefaultLocale")
public class MediaUtil {

	private static final Uri albumArtUri = Uri
			.parse("content://media/external/audio/albumart");
	private static String TAG = "MediaUtil";
	private static final int IMG_HEIGHT=200;
	private static final int IMG_WIDTH=200;

	/**
	 *
	 */
	@SuppressLint("DefaultLocale")
	public  List<Mp3Info> getMp3Infos(Context context) {
		long time1 = System.currentTimeMillis();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

		LogUtil.d(this, "MediaStore.Audio.Media.EXTERNAL_CONTENT_URI:"
				+ MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
		LogUtil.d(this, "MediaStore.Audio.Media.DEFAULT_SORT_ORDER:"
				+ MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		LogUtil.d(this,"cursor.getCount()=" + cursor.getCount());
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			Mp3Info mp3Info = new Mp3Info();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID)); //
			String title = cursor.getString((cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE))); //
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST)); //
			String album = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM)); //
			String displayName = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			long albumId = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION)); //
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE)); //
			String url = cursor.getString(cursor
			.getColumnIndex(MediaStore.Audio.Media.DATA)); //


			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); //
//			if (isMusic != 0) { //

				if (isMP3(url)&& isMusic(size)) {
					mp3Info.setId(id);
					mp3Info.setTitle(title);
					mp3Info.setArtist(artist);
					mp3Info.setAlbum(album);
					mp3Info.setDisplayName(displayName);
					mp3Info.setAlbumId(albumId);
					mp3Info.setDuration(duration);
					mp3Info.setSize(size);
					mp3Info.setUrl(url);
					mp3Info.playPath=url;
					mp3Info.setTitlepinyin(toHanyuPinYin(mp3Info.getTitle()));

					mp3Infos.add(mp3Info);
				}
//			}
		}
		long time2 = System.currentTimeMillis();
		Log.i(TAG, (time2 - time1) + "ms");
		return mp3Infos;
	}
	/**
	 * @return
	 */
	public static boolean  isMusic(long size){
		return (size>1024*1024);
	}
	public static String toHanyuPinYin(String string) {
		StringBuffer c = new StringBuffer();
		try {
			for (int j = 0, len = string.length(); j < len; j++) {
				String pinyin = PinyinHelper.toHanyuPinyinStringArray(string
						.charAt(j))[0];
				c.append(pinyin);
			}

		} catch (Exception e) {
			c.append(string.toLowerCase(Locale.ENGLISH));

		}
		return c.toString();
	}
	public static Bitmap getMusicImage(Context context,MusicInfo musicInfo,int width,int height){

		return getMusicImage(context,musicInfo.getPlayPath(),musicInfo.getSongId(),musicInfo.getAlbumId(),width,height);
	}
	public static Bitmap getMusicImage(Context context,MusicInfo musicInfo){

		return getMusicImage(context,musicInfo.getPlayPath(),musicInfo.getSongId(),musicInfo.getAlbumId(),IMG_WIDTH,IMG_HEIGHT);
	}
	/**
	 * @Description 获取专辑封面
	 * @param filePath 文件路径，like XXX/XXX/XX.mp3
	 *
	 *
	 *
	 * @return 专辑封面bitmap
	 */
	@ComputeTime
	private static Bitmap getMusicImage(Context context,final String filePath, long song_id,
									   long album_id, int width,int height) {
		Bitmap bitmap = null;
		//能够获取多媒体文件元数据的类
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath); //设置数据源
			byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
			bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if (bitmap!=null){
			DebugLog.d("通过歌曲文件解析图片,大小"+ BitmapUtil.getBitmapSize(bitmap));
			bitmap= BitmapTranslater.compressByScale(bitmap,width,height);
			DebugLog.d("解压后大小"+ BitmapUtil.getBitmapSize(bitmap));
		}else{
			bitmap=getArtwork(context,song_id,album_id,width,height);
		}
		return bitmap;
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
	 *
	 * @param time
	 * @return
	 */
	public static String formatTime(long time) {
		String min = time / (1000 * 60) + "";
		String sec = time % (1000 * 60) + "";
		if (min.length() < 2) {
			min = "0" + time / (1000 * 60) + "";
		} else {
			min = time / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (time % (1000 * 60)) + "";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}

	/**
	 *获取默认的图片
	 * @param context
	 * @return
	 */
	@SuppressLint("ResourceType")
	private static Bitmap getDefaultArtwork(Context context, boolean small) {

		Bitmap bitmap;
		bitmap=BitmapFactory.decodeResource(context.getResources(),small?R.drawable.lmusic_small:R.drawable.lmusic);
		DebugLog.d("获取默认的专辑图片.....small:"+small+",大小:"+ BitmapUtil.getBitmapSize(bitmap));
		return bitmap;
	}


	/**
	 * 根据歌曲ID，专辑ID，获取专辑图片
	 * @param context
	 * @param songid
	 * @param albumid
	 * @param width
	 * @param height
	 * @return 获取到了返回bitmap,获取不到返回默认的图片
	 * @see #getDefaultArtwork(Context, boolean)
	 */
	private static Bitmap getArtworkFromFileOriginal(Context context, long songid,
													  long albumid,int width,int height) {
		Bitmap bm = null;
		DebugLog.d("songid:"+songid+",albumid:"+albumid+",width:"+width+",height:"+height);
		if (albumid < 0 && songid < 0) {
			return getDefaultArtwork(context,true);
		}
		try {
			FileDescriptor fd = null;
			Uri uri=null;
			if (albumid < 0) {
				uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
//				DeBug.d("getArtworkFromFile","通过songid:"+songid+"获取专辑");
			} else {
				String mUriAlbums = "content://media/external/audio/albums";
				uri=Uri.parse(mUriAlbums + "/" + Long.toString(albumid));
				bm= BitmapBuilder.getBitmap(getAlbumImagePath(context,albumid),width,height);
				if (bm!=null){
					DebugLog.d("通过专辑Id获取到专辑图片...大小"+ BitmapUtil.getBitmapSize(bm));
					return bm;
				}
			}
			ParcelFileDescriptor pfd = context.getContentResolver()
					.openFileDescriptor(uri, "r");
			if (pfd != null) {
				fd = pfd.getFileDescriptor();
			}
			bm= BitmapBuilder.getBitmap(fd,width,height);
			if (bm!=null){
				DebugLog.d("通过URI获取专辑:"+uri+",大小"+ BitmapUtil.getBitmapSize(bm));
			}

		} catch (Exception e) {
			 e.printStackTrace();
			 bm=getDefaultArtwork(context,true);
		}
		return bm;
	}

	/**
	 * 通过专辑ID获取专辑图片地址
	 * 某些手机获取为null
	 * @param context
	 * @param album_id
	 * @return
	 */
	private static String getAlbumImagePath(Context context,long album_id) {
		String mUriAlbums = "content://media/external/audio/albums";
		String[] projection = new String[] { "album_art" };
		Cursor cur = context.getContentResolver().query(
				Uri.parse(mUriAlbums + "/" + Long.toString(album_id)),
				projection, null, null, null);
		String album_art = null;
		if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
			cur.moveToNext();
			album_art = cur.getString(0);
		}
		cur.close();

		cur = null;
		DebugLog.d("获取到专辑图片地址:"+album_art);
		return album_art;
	}

	private static Bitmap getArtworkFromFileOriginal(Context context, long songid,
													 long albumid) {
		return getArtworkFromFileOriginal(context,songid,albumid,IMG_WIDTH,IMG_HEIGHT);
	}
	/**
	 * 获取专辑图片
	 * @param context
	 * @param song_id
	 * @param album_id
	 * @param allowdefalut
	 * @return
	 */
	public static Bitmap getArtwork(Context context, long song_id,
			long album_id, boolean allowdefalut, boolean small) {
		return getArtworkFromFileOriginal(context,song_id,album_id);
	}
	/**
	 * 获取专辑图片
	 * @param context
	 * @param song_id
	 * @param album_id
	 * @return
	 */
	public static Bitmap getArtwork(Context context, long song_id,
			long album_id, int width,int height) {
		return getArtworkFromFileOriginal(context,song_id,album_id,width,height);
	}


}
