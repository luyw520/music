package com.music.ui.activity;

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
import com.music.bean.Mp3Info;
import com.music.bean.MusicInfo;
import com.music.helpers.PlayerHelpler;
import com.music.helpers.StringHelper;
import com.music.lu.R;
import com.music.model.DataStorageModel;
import com.music.model.HttpCallback;
import com.music.model.MusicHttpModel;
import com.lu.library.adapter.LuAdapter;
import com.lu.library.adapter.ViewHolder;
import com.music.ui.widget.dialog.LoadingView;
import com.music.utils.DeBug;
import com.music.utils.DialogUtil;
import com.music.helpers.FileHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//	@ViewInject(value = R.id.iv_searchMusic)
//	ImageView iv_searchMusic;

//	@ViewInject(value = R.id.atvSongName)
//	AutoCompleteTextView atvSongName;
	@ViewInject(value = R.id.listView)
	ListView listView;

	@ViewInject(value = R.id.loadView)
	private LoadingView loadView;

	private List<MusicInfo> datas = new ArrayList<>();

	private MyAdater myAdapter;
	private String songName, songer;
	private String path = "";
	HttpUtils httpUtils;
	String encode = "";
	String decode = "";
	private MusicHttpModel musicHttpModel;
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
	private String key;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		clearHistory = getString(R.string.clear_search_history);
		initWidget();
		musicHttpModel = new MusicHttpModel();
	}

	@OnClick({ R.id.iv_back, R.id.iv_searchMusic })
	public void viewClick(View v) {
		switch (v.getId()) {
		case R.id.iv_searchMusic:
			key = atvSonger.getText().toString();
			if(!StringHelper.isDataVaild(this,new String[]{key})){
				DialogUtil.showToast(this, R.string.empty_key_tips);
				return;
			};

			loadView.setLoadingText(getString(R.string.searching_music));
			loadView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			datas.clear();
			closeSoftInput();
			handler.postDelayed(new mRun(), 2000);
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
				 */
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					filterResults.values = objects; //
					filterResults.count = objects.size(); //
					DeBug.d(SearchMusicActivity.this,
							"performFiltering------------historys.size():"
									+ objects.size());
					return filterResults;
				}

				/**
				 */
				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						DeBug.d(SearchMusicActivity.this,
								"publishResults------------objects.size():"
										+ objects.size());
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}

			};
			return filter;
		}

	}

	private void searchMusic() {
//		atvSongName.clearFocus();
		atvSonger.clearFocus();
		saveHistorySearch();
		musicHttpModel.searchMusicByNetApi(getApplicationContext(),key, new SearchMusicCallBack());
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
		history.put(HISTORY_SONGER, historySonger);
		history.put(HISTORY_SONGNAME, historySongName);
		DataStorageModel.getDefault().saveObjectToFile(history, HISTORY_SEARCH);
	}

	private class SearchMusicCallBack implements HttpCallback<List<MusicInfo>> {
		public void onFailure(Exception arg0) {
			checkHasData();
		}
		public void onSuccess(List<MusicInfo> result) {
			List<MusicInfo> temMp3Infos=(List<MusicInfo>) result;
			datas.addAll(temMp3Infos);
			checkHasData();

		}

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

//		atvSongName.setThreshold(1);
//		atvSongName.setAdapter(historyNamesAdapter);

//		atvSonger.setThreshold(1);
//		atvSonger.setAdapter(historySongerAdapter);

		atvSonger.setOnItemClickListener(new ClearHistoryItemClick(0));
//		atvSongName.setOnItemClickListener(new ClearHistoryItemClick(1));
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
//				atvSongName.setText("");
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

	class MyAdater extends LuAdapter<MusicInfo> {
		public MyAdater(Context context, List<MusicInfo> datas, int mItemLayoutId) {
			super(context, datas, mItemLayoutId);
		}

		@Override
		public void convert(ViewHolder viewHolder,
				int position) {
			viewHolder.setString(R.id.tv_songName,
					(datas.get(position).getTitle()));
			viewHolder.getView(R.id.btn_down).setOnClickListener(
					new DownOnClick(position));

			ImageLoader.getInstance().displayImage(datas.get(position).picUrl,(ImageView) viewHolder.getView(R.id.img));
		}

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
			play(datas.get(position));
			down(button);

		}

		private void down(final Button button) {
			path = FileHelper.findLocalMp3(datas.get(position).getTitle());
			if (path != null) {
				return;
			}
			path = FileHelper.downPath() + File.separator
					+ datas.get(position).getTitle();
			musicHttpModel.downMusic(datas.get(position).downUrl, path,
					new DownMusicCallBack());
		}

		private class DownMusicCallBack extends RequestCallBack<File> {
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				DialogUtil.showToast(getApplicationContext(), datas.get(position).getTitle()
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

	private void play(MusicInfo mp3Info) {
		PlayerHelpler.getDefault().playMusic(mp3Info);
		startActivity(new Intent(this, PlayerActivity.class));
	}
}
