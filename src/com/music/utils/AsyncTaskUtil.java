package com.music.utils;


import android.R.integer;
import android.os.AsyncTask;

public class AsyncTaskUtil {

	private IAsyncTaskCallBack iAsyncTaskCallBack;

	public void setIAsyncTaskCallBack(IAsyncTaskCallBack iAsyncTaskCallBack) {
		this.iAsyncTaskCallBack = iAsyncTaskCallBack;
	}
	public AsyncTaskUtil(IAsyncTaskCallBack iAsyncTaskCallBack){
		this.iAsyncTaskCallBack=iAsyncTaskCallBack;
	}
	public AsyncTaskUtil( ){
		
	}
	public void execute(String... params){
		new MyTask().execute(params);
	}
	class MyTask extends AsyncTask<String, integer, Object> {

		@Override
		protected Object doInBackground(String... arg0) {
			return iAsyncTaskCallBack.doInBackground(arg0);
		}
		@Override
		protected void onPostExecute(Object result) {
			iAsyncTaskCallBack.onPostExecute(result);
		}
		
	}
	public interface IAsyncTaskCallBack{
		public Object doInBackground(String... arg0);

		public void onPostExecute(Object result);
	}
}
