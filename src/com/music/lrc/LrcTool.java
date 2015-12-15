package com.music.lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.music.bean.LrcInfo;
import com.music.bean.Mp3Info;



 public class LrcTool {
	private static final String TAG = "LrcTool";

	
	/**
	 * �?有lrc文件的集�?
	 */
	private List<File> fileList=new ArrayList<File>();
	@SuppressLint("SdCardPath")
	private final String DIR="/sdcard/lu/";
	private final String FILE_NAME="lyw";
	/**存储匹配到的歌词的集�?*/
	private File file;
	
	public List<File> getFileList() {
		return fileList;
	}
//	public  void clearFileList() {
//		fileList.clear();
//	}
	public LrcTool(){
		File dir=new File(DIR);
		dir.mkdirs();
		file=new File(dir, FILE_NAME);
	}
	/**
	 * 根据歌曲名匹配歌曲，匹配到了返回�?个List<Map<String, String>>集合。否则返回null
	 * @param song
	 */
	public List<Map<String, String>>  matchSongLrc(String song){
		for(File f:fileList){
			if(f.getAbsolutePath().contains(song)){
				return resolvLrcFile(f);
			}
		}
		return null;
	}
	/**
	 * 根据mp3Infos集合里面的歌曲匹配所有的.lrc文件,并将集合写入文件
	 * @param mp3Infos
	 * @return 匹配到的�?有{@link LrcInfo} 集合
	 */
	public List<LrcInfo> matchAllMp3(List<Mp3Info> mp3Infos){
		
		getAllLrcFile();
		
		
		List<LrcInfo> list=new ArrayList<LrcInfo>();
		
		//寻找�?有可匹配的lrc文件.若找�?,放到lrcInfos�?
		for(int i=0,size=mp3Infos.size();i<size;i++){
			List<Map<String, String>> maps = matchSongLrc(mp3Infos.get(i)
					.getTitle());
			if(maps!=null){
				LrcInfo lrcInfo = new LrcInfo(mp3Infos.get(i).getTitle(), maps);
				if(!list.contains(lrcInfo)){
					list.add(lrcInfo);
				}
			}
		}
		
		if(list.size()>0){
			writeLrcFile(list);
		}
		return list;
		
	}
	/**
	 * 遍历文件中所有的lrc文件并添加到fileList集合�?
	 */
	public void getAllLrcFile(){
		//如果本地查找了一�?,存在fileList集合�?.不再查找
		if(fileList.size()==0){
			File file=Environment.getExternalStorageDirectory();
			addLrcFile(file);
		}
		
	}
	/**
	 * 将file目录下的lrc文件添加到fileList集合�?
	 * @param file 
	 * @param fileList2 
	 */
	public void addLrcFile(File file){
		File[] files=file.listFiles();
		if(files!=null){
			if(files.length>0){
				for(File file2:files){
					if(file2.isDirectory()){
						addLrcFile(file2);
					}else{
						if(isLrcFile(file2)){
							fileList.add(file2);
						}
					}
				}
			}
		}
		
	}
	/**
	 * 将所有匹配到的Lrc集合保存到文�?
	 */
	public void writeLrcFile(List<LrcInfo> lrcInfos){
		
		Log.i(TAG, "lrcInfos.size="+lrcInfos.size());
		ObjectOutput objectOutput=null;
		try {
			//如果文件存在则删�?,重新写入
			if(file.exists()){
				Log.i(TAG, "file.delete()="+file.delete());
			}
			File dir=new File(DIR);
			dir.mkdirs();
			file=new File(dir, FILE_NAME);
			file.createNewFile();
			
			objectOutput=new ObjectOutputStream(new FileOutputStream(file));
			objectOutput.writeObject(lrcInfos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(objectOutput!=null){
				try {
					objectOutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}
	}
	/**从文件中读取匹配到的�?有歌�?,
	 * 没有返回null
	 * */
	@SuppressWarnings("unchecked")
	public List<LrcInfo> readLrcFile(){
		if(!file.exists()){
			return null;
		}
		ObjectInputStream objectIntput=null;
		try {
			objectIntput=new ObjectInputStream(new FileInputStream(file));
			return (List<LrcInfo>)objectIntput.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(objectIntput!=null){
				try {
					objectIntput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}
		return null;
	}
	private boolean isLrcFile(File file){
		return file.getAbsolutePath().endsWith(".lrc");
	}
	/**
	 * 解析�?个lrc文件，将歌词信息放在list集合中并返回
	 * @param file
	 * @return
	 */
	public List<Map<String, String>> resolvLrcFile(File file){
		List<Map<String, String>> maps=new ArrayList<Map<String,String>>();
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			int len=reader.read();
			Map<String,String> map;
			while(len!=-1){
				String line=reader.readLine();
				String[] strings=line.split("]");
				
				
				if(line!=null){
					//�?'[',':','.'去掉.如果全是数字，则表示是歌词部�?
					String string=strings[0].replace("[", "").replace(":", "").replace(".", "").replaceAll("0", "");
					try{
						
						Integer.parseInt(string);
						String str0=strings[0];
						if(str0.contains("[")){
							
							str0=str0.substring(1);
							
						}
						String str1=null;
						if(strings.length==2){
							str1=strings[1];
						}
						map=new HashMap<String, String>();
						if(!str0.equals("")&&str1!=null){
							map.put("time", str0);
							map.put("time2", String.valueOf(format(str0)));
							map.put("lrc", str1);
							maps.add(map);
						}
						
					}catch(NumberFormatException e){
						Log.i(TAG, string);
					}
					

				}
				
			}	
					
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		for(Map s:maps){
//			Log.i(TAG,(s.get("time")+"---->"+s.get("lrc")+"----->"+s.get("time2")));
//		}
		return maps;
	}
	/**
	 * 00:04.40
	 */
	public int format(String string){
		int time=0;
		int minuter=0;
		int secode=0;
		int hao=0;
		String s0=string.substring(0, 2);
		s0.replaceAll("0", "");
		if(s0.equals("")){
			minuter=0;
		}else{
			minuter=Integer.parseInt(s0)*60*1000;
			time+=minuter;
		}
		String s1=string.substring(3,5);
		s1.replaceAll("0", "");
		if(s1.equals("")){
			secode=0;
		}else{
			secode=Integer.parseInt(s1)*1000;
			time+=secode;
		}
		String s2=string.substring(6);
		s2.replaceAll("0", "");
		if(s2.equals("")){
			hao=0;
		}else{
			hao=Integer.parseInt(s2);
			time+=hao;
		}
//		System.out.println(time);
		return time;
	}
	
	

}
