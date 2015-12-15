package com.music.mp3;

import java.io.File;

public class FileHelper {

	public static Id3v2Info readPlayFileID3(String filepath){
		if(filepath == null || filepath.length() <= 0)
			return null;
		File file = new File(filepath);
		Mp3ReadId3v2 mp3ReadId3v2 = new Mp3ReadId3v2(
				file);
		int length = (int) (file.length() < 1024 * 1024 ? file
				.length() : 1024 * 1024);
		try {
			mp3ReadId3v2.readId3v2(length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mp3ReadId3v2.getInfo();
	}
	
	public static boolean isExistFile(String filepath){
		if(filepath == null || filepath.length() <= 0)
			return false;
		File file = new File(filepath);
		return file.exists();
	}
	
	public static String getParentFilePath(String filepath){
		if(filepath == null || filepath.length() <= 0)
			return null;
		if(!isExistFile(filepath))
			return null;
		File file = new File(filepath);
		return file.getParent();
	}
	
	
	public static String getFileName(String filepath){
		if(filepath == null || filepath.length() <= 0)
			return null;
		if(!isExistFile(filepath))
			return null;
		int lenght = filepath.lastIndexOf("/");
		int filepathlen = filepath.length();
		return filepath.substring(lenght, filepathlen);
	}
	
}
