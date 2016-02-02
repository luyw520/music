package com.music.view.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
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
import com.lu.demo.adapter.LuAdapter;
import com.lu.demo.adapter.ViewHolder;
import com.music.bean.Mp3Info;
import com.music.lu.R;
import com.music.model.DataStorageModel;
import com.music.model.MusicHttpModel;
import com.music.model.XmlParseModel;
import com.music.utils.DeBug;
import com.music.utils.DialogUtil;
import com.music.utils.FileUtils;
import com.music.utils.MediaUtil;
import com.music.utils.Mp3Util_New;
import com.music.utils.StringUtil;
import com.music.widget.dialog.LoadingView;

/**
 * 
 * @author search online music
 * 
 */
@ContentView(value = R.layout.activity_searchmusic)
public class SearchMusicActivity extends BaseActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "SearchMusicActivity";
	@ViewInject(value = R.id.tv_title)
	TextView tv_title;
	@ViewInject(value = R.id.tv_no_song)
	TextView tv_no_song;

	@ViewInject(value = R.id.atvSonger)
	AutoCompleteTextView atvSonger;

	@ViewInject(value = R.id.iv_search)
	ImageView iv_search;
	@ViewInject(value = R.id.iv_more)
	ImageView iv_more;
	@ViewInject(value = R.id.iv_searchMusic)
	ImageView iv_searchMusic;

	@ViewInject(value = R.id.atvSongName)
	AutoCompleteTextView atvSongName;
	@ViewInject(value = R.id.listView)
	ListView listView;

	@ViewInject(value = R.id.loadView)
	private LoadingView loadView;

	private Mp3Info mp3 = new Mp3Info();
	private List<Mp3Info> datas = new ArrayList<Mp3Info>();

	private MyAdater myAdapter;
	private String songName, songer;
	private String path = "";
	HttpUtils httpUtils;
	String encode = "";
	String decode = "";
	private MusicHttpModel musicHttpModel;
	private XmlParseModel xmlParseModel;
	private List<String> historySongName = new ArrayList<String>();
	private List<String> historySonger = new ArrayList<String>();
	private Map<String, List<String>> history = new HashMap<String, List<String>>();
	private HistoryAdater historyNamesAdapter;
	private HistoryAdater historySongerAdapter;

	private static final String HISTORY_SONGNAME = "HISTORYSONGNAME";
	private static final String HISTORY_SONGER = "HISTORYSONGER";
	private static final String HISTORY_SEARCH = "history_search.lu";
	private String clearHistory;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		clearHistory = getString(R.string.clear_search_history);
		initWidget();
		musicHttpModel = new MusicHttpModel();
		xmlParseModel = new XmlParseModel();
	}

	@OnClick({ R.id.iv_back, R.id.iv_searchMusic })
	public void viewClick(View v) {
		switch (v.getId()) {
		case R.id.iv_searchMusic:
			loadView.setLoadingText(getString(R.string.searching_music));
			loadView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			datas.clear();
			closeSoftInput();
			handler.postDelayed(new mRun(), 3000);
			break;
		case R.id.iv_back:
			finish();
		}
	}

	class mRun implements Runnable {
		public void run() {
			searchMusic();
		}
	};

	private void closeSoftInput() {
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	private class HistoryAdater extends ArrayAdapter<String> {
		private List<String> objects;

		public HistoryAdater(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
			this.objects = objects;
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				/**
				 * 本方法在后台线程执行，定义过滤算法
				 */
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					filterResults.values = objects; // results是上面的过滤结果
					filterResults.count = objects.size(); // 结果数量
					DeBug.d(SearchMusicActivity.this,
							"performFiltering------------historys.size():"
									+ objects.size());
					return filterResults;
				}

				/**
				 * 本方法在UI线程执行，用于更新自动完成列表
				 */
				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						// 有过滤结果，显示自动完成列表
						DeBug.d(SearchMusicActivity.this,
								"publishResults------------objects.size():"
										+ objects.size());
						notifyDataSetChanged();
					} else {
						// 无过滤结果，关闭列表
						notifyDataSetInvalidated();
					}
				}

			};
			return filter;
		}

	}

	private void searchMusic() {
		
		
		songName = atvSongName.getText().toString();
		songer = atvSonger.getText().toString();
		if(!StringUtil.isDataVaild(new String[]{songName,songer})){
			return;
		};
		atvSongName.clearFocus();
		atvSonger.clearFocus();
		saveHistorySearch();
		mp3.setArtist(songer);
		mp3.setTitle(songName);
		mp3.setAlbum(songer);
		mp3.setTitlepinyin(MediaUtil.toHanyuPinYin(songName));
		mp3.setDisplayName(songer + "-" + songName + ".mp3");

		musicHttpModel.searchMusic(songName, songer, new SearchMusicCallBack());

	}
	private void saveHistorySearch() {
		if (historySongName.size() == 0) {
			historySongName.add(clearHistory);
		}
		if (historySonger.size() == 0) {
			historySonger.add(clearHistory);
		}
		if (!historySongName.contains(songName)) {
			historySongName.add(0,songName);
			
		}
		if (!historySonger.contains(songer)) {
			historySonger.add(0,songer);
		}
//		if(historySongName.contains(clearHistory)){
//			historySongName.
//		}
		history.put(HISTORY_SONGER, historySonger);
		history.put(HISTORY_SONGNAME, historySongName);
		DataStorageModel.getDefault().saveObjectToFile(history, HISTORY_SEARCH);
	}

	private class SearchMusicCallBack extends RequestCallBack<String> {
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			checkHasData();
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			Mp3Info tempMp3Info = xmlParseModel.parseMp3FromString(arg0.result);
			if (tempMp3Info != null) {
				mp3.setDownUrl(tempMp3Info.getDownUrl());
				mp3.setUrl(tempMp3Info.getDownUrl());
				mp3.playPath = tempMp3Info.getDownUrl();
				mp3.setSize(tempMp3Info.getSize());
				datas.add(mp3);
			}
			checkHasData();

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DeBug.d(this, "onDestroy..................................");
	}

	private void checkHasData() {
		loadView.setVisibility(View.GONE);
		if (datas.isEmpty()) {
			tv_no_song.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			tv_no_song.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			myAdapter.notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unchecked")
	private void initWidget() {
		tv_title.setText(getString(R.string.search_music));
		iv_more.setVisibility(View.GONE);
		iv_search.setVisibility(View.GONE);

		myAdapter = new MyAdater(this, datas, R.layout.item_search_music);
		listView.setAdapter(myAdapter);

		Object o = DataStorageModel.getDefault().getObjectFromFile(
				HISTORY_SEARCH);
		if (o != null) {
			history = (Map<String, List<String>>) o;
		}
		List<String> sn = history.get(HISTORY_SONGNAME);
		if (sn != null) {
			historySongName.addAll(sn);
		}
		List<String> s = history.get(HISTORY_SONGER);
		if (s != null) {
			historySonger.addAll(s);
		}

		historyNamesAdapter = new HistoryAdater(this,
				android.R.layout.simple_dropdown_item_1line, historySongName);

		historySongerAdapter = new HistoryAdater(this,
				android.R.layout.simple_dropdown_item_1line, historySonger);

		atvSongName.setThreshold(1);
		atvSongName.setAdapter(historyNamesAdapter);

		atvSonger.setThreshold(1);
		atvSonger.setAdapter(historySongerAdapter);

		atvSonger.setOnItemClickListener(new ClearHistoryItemClick(0));
		atvSongName.setOnItemClickListener(new ClearHistoryItemClick(1));
	}

	class ClearHistoryItemClick implements OnItemClickListener {
		private int type = 0;

		public ClearHistoryItemClick(int type) {
			this.type = type;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (type) {
			case 0:
				clearSongerHistory(position);
				break;
			case 1:
				clearSongNameHistory(position);
				break;
			}
			
		}

		private void clearSongNameHistory(int position) {
			if (position == (historySongName.size() - 1)) {
				historySongName.clear();
				historyNamesAdapter.notifyDataSetChanged();
				history.put(HISTORY_SONGNAME, historySongName);
				atvSongName.setText("");
				DataStorageModel.getDefault().saveObjectToFile(history, HISTORY_SEARCH);
			}

		}

		private void clearSongerHistory(int position) {
			if (position == (historySonger.size() - 1)) {
				historySonger.clear();
				historySongerAdapter.notifyDataSetChanged();
				history.put(HISTORY_SONGER, historySonger);
				atvSonger.setText("");
				DataStorageModel.getDefault().saveObjectToFile(history, HISTORY_SEARCH);
			}
		}
	}

	class MyAdater extends LuAdapter<Mp3Info> {
		public MyAdater(Context context, List<Mp3Info> datas, int mItemLayoutId) {
			super(context, datas, mItemLayoutId);
		}

		@Override
		public void convert(com.lu.demo.adapter.ViewHolder viewHolder,
				int position) {
			viewHolder.setString(R.id.tv_songName,
					(datas.get(position).getDisplayName()));
			viewHolder.getView(R.id.btn_down).setOnClickListener(
					new DownOnClick(position));

		}

		@Override
		public void convert(ViewHolder arg0, Mp3Info arg1) {

		}
	}

	class DownOnClick implements OnClickListener {
		private int position;

		public DownOnClick(TextView textView, int position) {
			this.position = position;
		}

		public DownOnClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			final Button button = (Button) v;
			play();
			down(button);

		}

		private void down(final Button button) {
			path = FileUtils.findLocalMp3(datas.get(position).getDisplayName());
			if (path != null) {
				return;
			}
			path = FileUtils.downPath() + File.separator
					+ datas.get(position).getDisplayName();
			musicHttpModel.downMusic(datas.get(position).getDownUrl(), path,
					new DownMusicCallBack());
		}

		private class DownMusicCallBack extends RequestCallBack<File> {
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				DialogUtil.showToast(getApplicationContext(), mp3.getTitle()
						+ getString(R.string.down_success));
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				arg0.printStackTrace();
				DeBug.d(SearchMusicActivity.this, "onFailure ");
				DialogUtil.showToast(SearchMusicActivity.this,
						getString(R.string.down_faild));
			}
		}
	}

	private void play() {
		Mp3Util_New.getDefault().playMusic(mp3);
		startActivity(new Intent(this, PlayerActivity.class));
	}
}
