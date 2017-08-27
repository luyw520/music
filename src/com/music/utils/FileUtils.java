package com.music.utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 *
 */
public class FileUtils {
	private static String SDCardRoot=Environment.getExternalStorageDirectory()
	.getAbsolutePath() + File.separator;
	
	
	private static final String TAG="FileUtils";
	/**
	 */
	
	private static String dataPath=SDCardRoot+"lu"+File.separator+"music";
	/**
	 */
	private static String downPath=dataPath+File.separator+"download";
	/**
	 */
	private static String imgPath=dataPath+File.separator+"img";
	
	/**
	 */
	private static String lrcPath=dataPath+File.separator+"lrc";
	
	private static String objPath=dataPath+File.separator+"obj";
	private static String crashPath=dataPath+File.separator+"crash";
	
	public static String objPath(){
		createDirFile(dataPath);
		createDirFile(objPath);
		return objPath;
	}
	/**
	 * @return
	 */
	public static String downPath(){
		createDirFile(dataPath);
		createDirFile(downPath);
		return downPath;
	}
	/**
	 * @return
	 */
	public static String crashPath(){
		createDirFile(dataPath);
		createDirFile(crashPath);
		return crashPath;
	}
	/**
	 * @return
	 */
	public static String lrcPath(){
		createDirFile(dataPath);
		createDirFile(lrcPath);
		return lrcPath;
	}
	
	/**
	 * find local mp3 
	 * @param mp3Path
	 * @return mp3 absolutePath or null
	 */
	public static String findLocalMp3(String mp3Path) {
		File file = new File(FileUtils.downPath());
		for (File f : file.listFiles()) {
			if (f.getParent().equals(mp3Path)) {
				Log.i(TAG, "local mp3 is exist...");
				return f.getAbsolutePath();
			}
		}
		return null;
	}
	/**
	 * @return
	 */
	public static String imgPathPath(){
		createDirFile(dataPath);
		createDirFile(imgPath);
		return imgPath;
	}
	
	/**
	 *
	 * @return
	 */
	@SuppressLint("NewApi") 
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)||Environment.isExternalStorageRemovable()) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param path
	 */
	public static void createDirFile(String path) {
		
		
		
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	@SuppressLint("NewApi") 
	public static File getDiskCacheDir(Context context,String uniqueName){
	
		String cachePath;
		if(isSdcardExist()){
			cachePath=context.getExternalCacheDir().getPath();
		}else{
			cachePath=context.getCacheDir().getPath();
		}
		DeBug.d(TAG, "cachePath:"+cachePath);
		
		return new File(cachePath+File.separator+uniqueName);
	}
	
	
	/**
	 *
	 * @param path
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 *
	 * @param folderPath
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete();
	}

	/**
	 *
	 * @param path
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	/**
	 *
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = " ";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}
	
}
