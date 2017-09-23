package com.music.ui.view.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.music.bean.Mp3Info;
import com.music.lu.R;
import com.music.service.IConstants;
import com.music.utils.DeBug;
import com.music.utils.Mp3Util_New;
import com.music.ui.view.adapter.MusicListAdapter;
import com.music.ui.widget.indexablelistview.IndexableListView;

/**
 * 所有歌曲
 */
public class SongFragment extends BaseFragment implements IConstants{

//	protected IMediaService mService;
	@SuppressWarnings("unused")
	private static final String TAG = "SongFragment";

	
	private MusicListAdapter listAdapter;
	@ViewInject(value = R.id.listview)
	private IndexableListView mMusiclist; //
	private Mp3Util_New mp3Util;
	private List<Mp3Info> mp3Infos;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mp3Util=Mp3Util_New.getDefault();
		DeBug.d(this, "onCreate................");
		
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DeBug.d(this, "onCreateView................");
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment, container, false);
		initViewWidget(view);
		return view;
	}





	/**
	 */
	private void initViewWidget(View view) {
		
		
		mMusiclist=(IndexableListView) view.findViewById(R.id.listview);
		
		
		mMusiclist.setFastScrollEnabled(false);
		
		
		mp3Infos=mp3Util.getMp3Infos();

		
		listAdapter = new MusicListAdapter(getActivity(),
				R.layout.item_listview__layout, mp3Infos,
				mMusiclist);
		mMusiclist.addFooterView(getFoodView(getString(R.string.music_size,mp3Util.getAllMp3Size())));
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
