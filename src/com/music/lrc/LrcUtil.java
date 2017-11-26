package com.music.lrc;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.music.bean.LrcInfo;
import com.music.utils.AppConstant;
import com.music.utils.DialogUtil;
import com.music.utils.Mp3Util_New;


public class LrcUtil {
	private static LrcUtil lrcUtil=null;
	private LrcTool lrcTool;
	private Context context;
	private Mp3Util_New mp3Util;
//	private Handler handler;
	/**
	 */
	private boolean isSearch;
	
	/**
	 */
	private LrcInfo currentLrcInfo;
	
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
		mp3Util=Mp3Util_New.getDefault();
	}
	/**
	 * @author Administrator
	 *
	 */
	@SuppressWarnings("unchecked")
	public void findAndMatchLrc(String songName){
			isSearch=false;
			if(lrcInfos.size()==0){
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
	 * @author Administrator
	 *
	 */
	@SuppressWarnings("unchecked")
	public void findAndMatchLrcByHand(String songName){
		isSearch=true;
		new MyAsyncTask(songName).execute();
	}
	
	@SuppressWarnings("rawtypes")
	class MyAsyncTask extends AsyncTask{
		
		@SuppressWarnings({ "unused" })
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
				lrcInfos=lrcTool.matchAllMp3(mp3Util.getMusicBaseInfos());
			}
			return null;
		}
		
	}
	public void showDialog() {
		DialogUtil.showWaitDialog(context, "wait", "wait..");
		
	}
	public LrcInfo getCurrentLrcInfo() {
		return currentLrcInfo;
	}
}
