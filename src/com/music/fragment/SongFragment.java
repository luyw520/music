package com.music.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.music.adapter.MusicListAdapter;
import com.music.bean.Mp3Info;
import com.music.lu.R;
import com.music.lu.utils.Mp3Util_New;
import com.music.service.IConstants;
import com.music.service.IMediaService;
import com.music.widget.indexablelistview.IndexableListView;

/**
 *所有歌曲
 */
public class SongFragment extends Fragment implements IConstants{

	protected IMediaService mService;
	private static final String TAG = "SongFragment";

	
	private MusicListAdapter listAdapter;
	@ViewInject(value = R.id.listview)
	private IndexableListView mMusiclist; // 音乐列表

	private Mp3Util_New mp3Util;
	private List<Mp3Info> mp3Infos;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mp3Util=Mp3Util_New.getDefault();
		System.out.println(TAG+":onCreate");
		
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment, container, false);
		initViewWidget(view);
		return view;
	}





	/**
	 * 初始化view上面的控件
	 */
	private void initViewWidget(View view) {
		
		
		mMusiclist=(IndexableListView) view.findViewById(R.id.listview);
		
		
		mMusiclist.setFastScrollEnabled(true);
		
		
		mp3Infos=mp3Util.getMp3Infos();
//		mp3Util.setMusicBaseInfos(mp3Infos);
		
		
		listAdapter = new MusicListAdapter(getActivity(),
				R.layout.item_listview__layout, mp3Infos,
				mMusiclist);

		LinearLayout listViewFoodView = new LinearLayout(
				getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		params.bottomMargin = 200;
		params.leftMargin = 100;
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText("共" + mp3Util.getAllMp3Size() + "首歌曲");

		listViewFoodView.addView(textView, params);

		mMusiclist.addFooterView(listViewFoodView);

		mMusiclist.setAdapter(listAdapter);
		
		
		mMusiclist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				position<mp3Util.getCurrentPlayListSize()
				mp3Util.setMusicBaseInfos(mp3Infos, Mp3Util_New.PLAY_BY_SONG);
				
				mp3Util.playMusic(position);
			}
		});
	}
	

	


	
}
