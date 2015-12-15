package com.music.lrc;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.music.bean.LrcInfo;
import com.music.lu.utils.AppConstant;
import com.music.lu.utils.DialogUtil;
import com.music.lu.utils.Mp3Util;


public class LrcUtil {
	private static LrcUtil lrcUtil=null;
	private LrcTool lrcTool;
	private Context context;
	private Mp3Util mp3Util;
//	private Handler handler;
	/**
	 * �ֶ�����
	 */
	private boolean isSearch;
	
	/**
	 * ��ǰ���Ÿ����ĸ�ʶ���
	 */
	private LrcInfo currentLrcInfo;
	
	/** ƥ�䵽�ĸ�� */
	private List<LrcInfo> lrcInfos = null; 
	
	public static LrcUtil getInstance(Context context){
		
		if(lrcUtil==null){
			lrcUtil=new LrcUtil(context);
		}
		return lrcUtil;
	}
	private LrcUtil(Context context){
		this.context=context;
		 init();
	}
	
	private void init(){
		lrcTool=new LrcTool();
		lrcInfos=new ArrayList<LrcInfo>();
		mp3Util=Mp3Util.getDefault();
	}
	/**
	 * ƥ�����и��
	 * @author Administrator
	 *
	 */
	@SuppressWarnings("unchecked")
	public void findAndMatchLrc(String songName){
			isSearch=false;
			if(lrcInfos.size()==0){
				//��������.lrc�ļ��������и�������ƥ��
				new MyAsyncTask(songName).execute();
			}else{
				for(LrcInfo lrcInfo:lrcInfos ){
					if(lrcInfo.getSongName().equals(songName)){
						currentLrcInfo=lrcInfo;
						break;
					}else{
						currentLrcInfo=null;
					}
				}
			}
		
			
	}
	/**
	 * ƥ�����и��
	 * @author Administrator
	 *
	 */
	@SuppressWarnings("unchecked")
	public void findAndMatchLrcByHand(String songName){
		isSearch=true;
			//��������.lrc�ļ��������и�������ƥ��
		new MyAsyncTask(songName).execute();
	}
	
	@SuppressWarnings("rawtypes")
	class MyAsyncTask extends AsyncTask{
		
		private String songName;
		public MyAsyncTask(String songName) {
			this.songName=songName;
			
		}

		@Override
		protected void onPreExecute() {
			showDialog();
		}

		@Override
		protected void onPostExecute(Object result) {
			
//			if(lrcInfos.size()>0){
				new Handler().sendEmptyMessage(AppConstant.MATCH_LRC_COMPLETED);
//			}
				DialogUtil.closeAlertDialog();
		}

		@Override
		protected Object doInBackground(Object... params) {
			
			if(!isSearch){
				lrcInfos=lrcTool.readLrcFile();
			}else{
				lrcInfos=null;
			}
			
			if(lrcInfos==null){
				lrcInfos=lrcTool.matchAllMp3(mp3Util.getMp3Infos());
			}
			return null;
		}
		
	}
	public void showDialog() {
		DialogUtil.showWaitDialog(context, "��ʲ���", "������ڼ�����...");
		
	}
	public LrcInfo getCurrentLrcInfo() {
		return currentLrcInfo;
	}
}