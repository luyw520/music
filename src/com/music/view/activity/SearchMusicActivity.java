package com.music.view.activity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.bean.Mp3Info;
import com.music.lu.R;
import com.music.model.MusicHttpModel;
import com.music.model.XmlParseModel;
import com.music.utils.DialogUtil;
import com.music.utils.FileUtils;
import com.music.utils.LogUtil;
import com.music.utils.MediaUtil;
import com.music.utils.Mp3Util_New;
import com.music.widget.dialog.LoadingView;

@ContentView(value = R.layout.activity_searchmusic)
public class SearchMusicActivity extends BaseActivity {

	private static final String TAG = "SearchMusicActivity";
	@ViewInject(value = R.id.tv_title)
	TextView tv_title;
	@ViewInject(value = R.id.tv_no_song)
	TextView tv_no_song;
	@ViewInject(value = R.id.tv_singer)
	TextView tv_singer;
	@ViewInject(value = R.id.iv_search)
	ImageView iv_search;
	@ViewInject(value = R.id.iv_more)
	ImageView iv_more;
	@ViewInject(value = R.id.iv_searchMusic)
	ImageView iv_searchMusic;

	@ViewInject(value = R.id.auto_tv_key)
	AutoCompleteTextView auto_tv_key;
	@ViewInject (value = R.id.listView)
	ListView listView;
	
	@ViewInject(value=R.id.loadView)
	private LoadingView loadView;
	
	private Mp3Info mp3=new Mp3Info();
	private List<Mp3Info> datas=new ArrayList<Mp3Info>();
	
	private MyAdapter myAdapter=new MyAdapter();
	private String songName,songer;
	private String path="";
	HttpUtils httpUtils;
	String encode="";
	String decode="";
	private MusicHttpModel musicHttpModel;
	private XmlParseModel xmlParseModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initWidget();
		musicHttpModel=new MusicHttpModel();
		xmlParseModel=new XmlParseModel();
	}

	@OnClick({ R.id.iv_back, R.id.iv_searchMusic})
	public void onClickListener(View v) {

		LogUtil.i(this.getClass(), "click");
		switch (v.getId()) {
		case R.id.iv_searchMusic:
			loadView.setLoadingText("音乐搜索中..");
			loadView.setVisibility(View.VISIBLE);
			closeSoftInput();
			handler.sendEmptyMessageDelayed(0, 3000);
			break;
		case R.id.iv_back:
			finish();
		}
	}
	@SuppressLint("HandlerLeak") 
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			searchMusic();
		};
	};
	private void closeSoftInput(){
		  View view = getWindow().peekDecorView();
	        if (view != null) {
	            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
	        }
	}
	private void searchMusic() {
		songName=auto_tv_key.getText().toString();
		songer=tv_singer.getText().toString();
		mp3.setArtist(songer);
		mp3.setTitle(songName);
		mp3.setAlbum(songer);
		mp3.setTitlepinyin(MediaUtil.toHanyuPinYin(songName));
		mp3.setDisplayName(songer+"-"+songName+".mp3");
		datas.clear();
		musicHttpModel.searchMusic(songName, songer, new RequestCallBack<String>(){
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				checkHasData();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Mp3Info tempMp3Info=xmlParseModel.parseMp3FromString(arg0.result);
				if(tempMp3Info!=null){
					mp3.setDownUrl(tempMp3Info.getDownUrl());
					mp3.setUrl(tempMp3Info.getDownUrl());
					mp3.setSize(tempMp3Info.getSize());
					datas.add(mp3);
				}
				checkHasData();
			}
			
		});
	}
	private void checkHasData(){
		loadView.setVisibility(View.GONE);
		if(datas.isEmpty()){
			tv_no_song.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}else{
			tv_no_song.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			myAdapter.notifyDataSetChanged();
		}
	}
	private void initWidget() {
		tv_title.setText("搜索音乐");
		iv_more.setVisibility(View.GONE);
		iv_search.setVisibility(View.GONE);
		listView.setAdapter(myAdapter);
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder viewHolder=null;
			if(convertView==null){	
				
				convertView=LayoutInflater.from(SearchMusicActivity.this).inflate(R.layout.item_search_music, null);
				viewHolder=new ViewHolder();
				viewHolder.tv_songName=(TextView)convertView.findViewById(R.id.tv_songName);
				viewHolder.btn_down=(Button) convertView.findViewById(R.id.btn_down);
				viewHolder.tv_downing=(TextView) convertView.findViewById(R.id.tv_down);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
				
			}
			viewHolder.tv_songName.setText(datas.get(position).getDisplayName());
			viewHolder.btn_down.setOnClickListener(new DownOnClick(viewHolder.tv_downing,position));
			return convertView;
		}
		class ViewHolder {
			TextView tv_songName;
			Button btn_down;
			TextView tv_downing;
		} 
		class DownOnClick implements OnClickListener{
			private TextView textView;
			private int position;
			public DownOnClick(TextView textView,int position){
				this.textView=textView;
				this.position=position;
			}
			@Override
			public void onClick(View v) {
				final Button button=(Button) v;
				String flag=button.getText().toString();
				
				if("下载".equals(flag)){
					down(button);
				}else if("点击播放".equals(flag)){
					
					play();
					down(button);
				}
				
				
				
			}
			private void down(final Button button){
				HttpUtils http=new HttpUtils();
				
				path=findLocalMp3(datas.get(position).getDisplayName());
				if(path!=null){
					return;
				}
				path=FileUtils.downPath()+File.separator+datas.get(position).getDisplayName();
				http.download(datas.get(position).getDownUrl(), path, true, new RequestCallBack<File>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						DialogUtil.showToast(getApplicationContext(), mp3.getTitle()+"下载成功");
					}
					
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						System.out.println("下载出错");
						button.setEnabled(true);
						button.setText("下载");
						DialogUtil.showToast(SearchMusicActivity.this, "下载出错");
						textView.setVisibility(View.INVISIBLE);
					}
				});
			}
		}
		
	}
	private String findLocalMp3(String mp3Path){
		File file=new File(FileUtils.downPath());
		for(File f:file.listFiles()){
			if(f.getParent().equals(mp3Path)){
				Log.i(TAG, "本地歌曲存在...");
				return f.getAbsolutePath();
			}
		}
		return null;
	}
	private void play(){
		Mp3Util_New.getDefault().playMusic(mp3);
		startActivity(new Intent(this,PlayerActivity.class));
	}
}
