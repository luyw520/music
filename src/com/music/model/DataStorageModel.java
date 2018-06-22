package com.music.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.music.helpers.FileHelper;

public class DataStorageModel {
	private DataStorageModel(){
	}
	public static DataStorageModel getDefault(){
		return DataStorageModelInner.d;
	}
	public static class DataStorageModelInner{
		public static DataStorageModel d=new DataStorageModel();
	}
	public void saveObjectToFile(Object object, String fileName) {
		File f = new File(FileHelper.objPath(), fileName);
		ObjectOutputStream o = null;
		try {
			o = new ObjectOutputStream(new FileOutputStream(f));
			o.writeObject(object);
			o.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (o != null) {
				try {
					o.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public Object getObjectFromFile(String fileName) {
		Object object=null;
		File f = new File(FileHelper.objPath(), fileName);
		ObjectInputStream i = null;
		try {
			i = new ObjectInputStream(new FileInputStream(f));
			object=i.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (i != null) {
				try {
					i.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return object;
	}
}
