package com.music.lu.utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

/**
 *
 */
public class FileUtils {
	private static String SDCardRoot=Environment.getExternalStorageDirectory()
	.getAbsolutePath() + File.separator;
	
	
	private static final String TAG="FileUtils";
	/**
	 * 应用程序路径
	 */
	
	private static String dataPath=SDCardRoot+"lu";
	/**
	 * 下载路径
	 */
	private static String downPath=dataPath+File.separator+"download";
	/**
	 * 图片路径
	 */
	private static String imgPath=dataPath+File.separator+"img";
	
	/**
	 * 图片路径
	 */
	private static String lrcPath=dataPath+File.separator+"lrc";
	
	private static String crashPath=dataPath+File.separator+"crash";
	/**
	 * 下载地址
	 * @return
	 */
	public static String downPath(){
		createDirFile(dataPath);
		createDirFile(downPath);
		return downPath;
	}
	/**
	 * 程序异常地址
	 * @return
	 */
	public static String crashPath(){
		createDirFile(dataPath);
		createDirFile(crashPath);
		return crashPath;
	}
	/**
	 * 下载地址
	 * @return
	 */
	public static String lrcPath(){
		createDirFile(dataPath);
		createDirFile(lrcPath);
		return lrcPath;
	}
	/**
	 *  图片地址
	 * @return
	 */
	public static String imgPathPath(){
		createDirFile(dataPath);
		createDirFile(imgPath);
		return imgPath;
	}
	
	/**
	 * 判断SD是否可以
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
	 * 创建目录
	 * 
	 * @param path
	 *            目录路径
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
		MyLog.d(TAG, "cachePath:"+cachePath);
		
		return new File(cachePath+File.separator+uniqueName);
	}
	
	
	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 创建的文件
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
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹的路径
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件的路径
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
	 * 获取文件的Uri
	 * 
	 * @param path
	 *            文件的路径
	 * @return
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	/**
	 * 换算文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "未知大小";
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
