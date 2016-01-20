package com.music.utils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import com.music.bean.Mp3Info;

public class Mp3FileHelper {

	private static final String TAG = "Mp3FileHelper";

	@SuppressWarnings("unused")
	private static boolean fileEncode_UTF_8(String path) {/*
														 * File file = new
														 * File(path);
														 * InputStream in; try {
														 * in = new
														 * java.io.FileInputStream
														 * (file); byte[] b =
														 * new byte[3];
														 * in.read(b);
														 * in.close(); if (b[0]
														 * == -17 && b[1] == -69
														 * && b[2] == -65) {
														 * return true; } else {
														 * return false; } }
														 * catch
														 * (FileNotFoundException
														 * e) {
														 * e.printStackTrace();
														 * } catch (IOException
														 * e) {
														 * e.printStackTrace();
														 * }
														 */
		return false;
	}

	/**
	 * get filename whithoutextended
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileNameWhithoutExtended(String filename) {
		if (filename != null && filename.length() > 0) {
			int i = filename.lastIndexOf('.');
			if (i > -1 && i < (filename.length())) {
				return filename.substring(0, i);
			}
		}
		return filename;
	}

	@SuppressLint("NewApi")
	public static Mp3Info getFileId3V2Info(String path) {
		Mp3Info mp3Info = null;
		try {
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		try {
			mmr.setDataSource(path);
			String title = mmr
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			String album = mmr
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
			String artist = mmr
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
			String ALBUMARTIST = mmr
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
			String AUTHOR = mmr
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
			String DURATION = mmr
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

			byte[] bitMap = mmr.getEmbeddedPicture();
			Bitmap bitmap = BitmapFactory.decodeByteArray(bitMap, 0,
					bitMap.length);
			LogUtil.d(TAG, "title:" + title + ",album:" + album + ",artist:"
					+ artist + ",ALBUMARTIST:" + ALBUMARTIST + ",DURATION:"
					+ DURATION + ",bitmap:" + bitmap);
			// if (path.contains("mp3")) {
			if (title != null) {
				String temp = title.replace("?", "");
				try {
					temp = new String(title.getBytes("ISO-8859-1"), "GBK");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				byte tempNum;
				int isIso = 0;
				char[] chars = temp.toCharArray();
				int charsLength = chars.length;
				for (int j = 0; j < charsLength; j++) {
					tempNum = (byte) chars[j];
					if (tempNum == 26 || tempNum == 63) {
						if ((int) chars[j] > 0 && (int) chars[j] < 256) {
							isIso = 1;
							break;
						}
					}
				}
				if (isIso == 0)
					title = temp;
				else
					title = new File(path).getName();
			}
			// }

			if (album != null) {
				String temp = album.replace("?", "");
				try {
					temp = new String(album.getBytes("ISO-8859-1"), "GBK");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				byte tempNum;
				int isIso = 0;
				char[] chars = temp.toCharArray();
				int charsLength = chars.length;
				for (int j = 0; j < charsLength; j++) {
					tempNum = (byte) chars[j];
					if (tempNum == 26 || tempNum == 63) {
						if ((int) chars[j] > 0 && (int) chars[j] < 256) {
							isIso = 1;
							break;
						}
					}
				}
				if (isIso == 0)
					album = temp;
			}

			if (artist != null) {
				String temp = artist.replace("?", "");
				try {
					temp = new String(artist.getBytes("ISO-8859-1"), "GBK");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				byte tempNum;
				int isIso = 0;
				char[] chars = temp.toCharArray();
				int charsLength = chars.length;
				for (int j = 0; j < charsLength; j++) {
					tempNum = (byte) chars[j];
					if (tempNum == 26 || tempNum == 63) {
						if ((int) chars[j] > 0 && (int) chars[j] < 256) {
							isIso = 1;
							break;
						}
					}
				}
				if (isIso == 0)
					artist = temp;
			}
			if (title == null || title.trim().equals(""))
				title = new File(path).getName();
			if (album == null || album.trim().equals(""))
				album = "δ֪";
			if (artist == null || artist.trim().equals(""))
				artist = "δ֪";
			// mId3v2Info = new Mp3Info(title), album, artist, bitMap);
			mp3Info = new Mp3Info();
			mp3Info.setTitle(title);
			mp3Info.setAlbum(album);
			mp3Info.setArtist(artist);
			mp3Info.setBitMap(bitMap);
			LogUtil.d(TAG, "title:" + title + ",album:" + ",artist:" + artist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mp3Info;
	}

	@SuppressLint("NewApi")
	public static void setMp3Info(String path, Mp3Info mp3Info) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();

		mmr.setDataSource(path);
		String title = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		String album = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		String artist = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		String DURATION = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

		byte[] bitMap = mmr.getEmbeddedPicture();
		Bitmap bitmap = BitmapFactory.decodeByteArray(bitMap, 0, bitMap.length);
		LogUtil.d(TAG, "title:" + title + ",album:" + album + ",artist:"
				+ artist + ",DURATION:"
				+ DURATION + ",bitmap:" + bitmap);

	}

	@SuppressLint("NewApi")
	public static Mp3Info getFileId3V2Info_(String path) {/*
														 * Id3v2Info mId3v2Info
														 * = null; String
														 * encoding = "utf-8";
														 * try { encoding =
														 * getFileCharsetName
														 * (path); } catch
														 * (Exception e1) {
														 * e1.printStackTrace();
														 * }
														 * MediaMetadataRetriever
														 * mmr = new
														 * MediaMetadataRetriever
														 * (); try {
														 * mmr.setDataSource
														 * (path); String title
														 * =
														 * mmr.extractMetadata(
														 * MediaMetadataRetriever
														 * .METADATA_KEY_TITLE);
														 * String album =
														 * mmr.extractMetadata
														 * (MediaMetadataRetriever
														 * .METADATA_KEY_ALBUM);
														 * String artist =
														 * mmr.extractMetadata
														 * (MediaMetadataRetriever
														 * .
														 * METADATA_KEY_ARTIST);
														 * byte[] bitMap =
														 * mmr.getEmbeddedPicture
														 * (); if
														 * (path.contains(
														 * "mp3")) { if (title
														 * != null) { String
														 * temp = title; String
														 * luanma =
														 * getChinaStr(temp);
														 * byte[] b0 =
														 * luanma.getBytes
														 * ("iso-8859-1"); if
														 * (b0.length > 0 &&
														 * b0[0] != 63) { temp =
														 * new
														 * String(temp.getBytes
														 * ("ISO-8859-1"),
														 * "gbk"); String
														 * luanma_again =
														 * getChinaStr(temp);
														 * byte[] b0_temp =
														 * luanma_again
														 * .getBytes(
														 * "iso-8859-1"); if
														 * (b0_temp.length > 0
														 * && b0_temp[0] != 63)
														 * { title = new
														 * String(title
														 * .getBytes(
														 * "ISO-8859-1"),
														 * "utf-8"); } else {
														 * title = temp; } } }
														 * if (album != null) {
														 * // album = new
														 * String(
														 * album.getBytes(
														 * "ISO-8859-1"), //
														 * encoding); String
														 * temp = album; String
														 * luanma =
														 * getChinaStr(temp);
														 * byte[] b1 =
														 * luanma.getBytes
														 * ("iso-8859-1"); if
														 * (b1.length > 0 &&
														 * b1[0] != 63) { temp =
														 * new
														 * String(temp.getBytes
														 * ("ISO-8859-1"),
														 * "gbk"); String
														 * luanma_again =
														 * getChinaStr(temp);
														 * byte[] b1_temp =
														 * luanma_again
														 * .getBytes(
														 * "iso-8859-1"); if
														 * (b1_temp.length > 0
														 * && b1_temp[0] != 63)
														 * { album = new
														 * String(album
														 * .getBytes(
														 * "ISO-8859-1"),
														 * "utf-8"); } else {
														 * album = temp; } } }
														 * if (artist != null) {
														 * String temp = artist;
														 * String luanma =
														 * getChinaStr(temp);
														 * byte[] b2 =
														 * luanma.getBytes
														 * ("iso-8859-1"); if
														 * (b2.length > 0 &&
														 * b2[2] != 63) { temp =
														 * new
														 * String(temp.getBytes
														 * ("ISO-8859-1"),
														 * "gbk"); String
														 * luanma_again =
														 * getChinaStr(temp);
														 * byte[] b2_temp =
														 * luanma_again
														 * .getBytes(
														 * "iso-8859-1"); if
														 * (b2_temp.length > 0
														 * && b2_temp[0] != 63)
														 * { artist = new
														 * String(
														 * artist.getBytes
														 * ("ISO-8859-1"),
														 * "utf-8"); } else {
														 * artist = temp; } } //
														 * artist = new
														 * String(artist
														 * .getBytes
														 * ("ISO-8859-1"), //
														 * encoding); } } if
														 * (title == null) title
														 * = new
														 * File(path).getName();
														 * if (album == null)
														 * album =
														 * MusicApplication
														 * .getInstance
														 * ().getResources
														 * ().getString
														 * (R.string.unkown); if
														 * (artist == null)
														 * artist =
														 * MusicApplication
														 * .getInstance
														 * ().getResources
														 * ().getString
														 * (R.string.unkown);
														 * mId3v2Info = new
														 * Id3v2Info
														 * (Mp3FileHelper.
														 * getFileNameWhithoutExtended
														 * (title), album,
														 * artist, bitMap); }
														 * catch (Exception e) {
														 * e.printStackTrace();
														 * } return mId3v2Info;
														 */
		return null;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	// \u4e00-\u9fa5
	public static String getChinaStr(String source) {
		String result = "";
		String PatterChinese = "[A-Za-z0-9|!|,|.|:|;|\\*|%|@|#|\\$|\\^|~|\\&|\\(|\\)"
				+ "|\\{|\\}|\\?|\\+|-|\"|\r|\n|\\s*]";
		Pattern pChinese = Pattern.compile(PatterChinese);
		Matcher m1 = pChinese.matcher(source);
		result = m1.replaceAll("");
		return result;
	}

	public static List<String> getSongList() {
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "luanma" + File.separator);
		List<String> files = new ArrayList<String>();
		if (file.exists()) {
			File[] subfiles = file.listFiles();
			for (File temp : subfiles) {
				files.add(temp.getAbsolutePath());
			}
		}
		return files;
	}

	public static String getFileCharsetName(String path) throws Exception {
		File file = new File(path);
		InputStream in = new java.io.FileInputStream(file);
		byte[] b = new byte[3];
		in.read(b);
		in.close();
		if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
			return "utf-8";
		} else {
			return "gbk";
		}
	}
}
