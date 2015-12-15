package com.music.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.music.adapter.LuAdapter;
import com.music.bean.AlbumInfo;
import com.music.bean.ArtistInfo;
import com.music.bean.FolderInfo;
import com.music.bean.MusicInfo;
import com.music.lu.R;
import com.music.lu.utils.LogUtil;
import com.music.lu.utils.Mp3Util_New;
import com.music.lu.utils.MusicUtils;
import com.music.service.IConstants;
import com.music.service.IMediaService;
import com.music.widget.indexablelistview.IndexableListView;

/**
 *
 */
public class MusicListFragment extends Fragment implements IConstants{

	protected IMediaService mService;
	private static final String TAG = "MusicListFragment";

	private ArtistAdapter listAdapter;
	@ViewInject(value = R.id.listview)
	private IndexableListView mMusiclist; // 音乐列表

	private  List<MusicInfo> musicInfos;
	private MusicListItemClickListener musicListItemClickListener;

	
	private Mp3Util_New mp3Util;
	
	private int mPlayListType=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		musicListItemClickListener = new MusicListItemClickListener();
		mp3Util=Mp3Util_New.getDefault();
		LogUtil.i(TAG, "onCreate");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment, container,
				false);
		initViewWidget(view);
		
		LogUtil.i(TAG, "onCreateView");
		return view;
	}

	/**
	 * 初始化view上面的控件
	 */
	private void initViewWidget(View view) {

		mMusiclist = (IndexableListView) view.findViewById(R.id.listview);

		mMusiclist.setFastScrollEnabled(true);
		mMusiclist.setOnItemClickListener(musicListItemClickListener);
		
		
//		folderInfos = com.music.lu.utils.MusicUtils.queryFolder(getActivity());
		listAdapter = new ArtistAdapter(getActivity(),musicInfos,R.layout.item_listview__layout);
		mMusiclist.setShow(false);
		
		LinearLayout listViewFoodView = new LinearLayout(
				getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		params.bottomMargin = 250;
		params.leftMargin = 100;
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText("共" + musicInfos.size() + "首歌曲");

		listViewFoodView.addView(textView, params);

		mMusiclist.addFooterView(listViewFoodView);
		
		
		mMusiclist.setAdapter(listAdapter);
		
		
		

	}
	public void initData(int flag,Object object,Context context){
		StringBuffer select = new StringBuffer();
		switch (flag) {
		case START_FROM_ARTIST:
			ArtistInfo artistInfo = (ArtistInfo) object;
			musicInfos=(MusicUtils.queryMusic(context,
					select.toString(), artistInfo.artist_name,
					START_FROM_ARTIST));
			mPlayListType=1;
			break;
		case START_FROM_ALBUM:
			AlbumInfo albumInfo = (AlbumInfo) object;
			musicInfos=(MusicUtils.queryMusic(context,
					select.toString(), albumInfo.album_id + "",
					START_FROM_ALBUM));
			mPlayListType=2;
			break;
		case START_FROM_FOLDER:
			FolderInfo folderInfo = (FolderInfo) object;
			musicInfos=(MusicUtils.queryMusic(context,
					select.toString(), folderInfo.folder_path,
					START_FROM_FOLDER));
			mPlayListType=3;
			break;
		case START_FROM_FAVORITE:
			musicInfos=(MusicUtils.queryFavorite(context));
			break;
		}
	}
	class MusicListItemClickListener implements OnItemClickListener {

		

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			if(position<mp3Util.getCurrentPlayListSize()){
				mp3Util.setMusicBaseInfos(musicInfos, mPlayListType);
				mp3Util.playMusic(position);
			}
			
		}

	}

	class ArtistAdapter extends LuAdapter<MusicInfo>{

		
		public ArtistAdapter(Context context, List<MusicInfo> datas,
				int mItemLayoutId) {
			super(context, datas, mItemLayoutId);
		}

		

		@Override
		public void convert(com.music.adapter.ViewHolder helper, int position) {
			// TODO Auto-generated method stub
			MusicInfo musicInfo=getItem(position);
			
//			helper.setString(R.id.albumImage,folderInfo.folder_name);
			helper.setString(R.id.music_album,musicInfo.artist);
			helper.setString(R.id.music_title,musicInfo.musicName);
			helper.getView(R.id.catalog).setVisibility(View.GONE);
		}

	}
}
