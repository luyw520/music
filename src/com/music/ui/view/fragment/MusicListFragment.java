package com.music.ui.view.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.music.bean.AlbumInfo;
import com.music.bean.ArtistInfo;
import com.music.bean.FolderInfo;
import com.music.bean.MusicInfo;
import com.music.lu.R;
import com.music.model.MusicModel;
import com.music.ui.service.IConstants;
import com.music.utils.LogUtil;
import com.music.helpers.PlayerHelpler;
import com.music.ui.adapter.LuAdapter;
import com.music.ui.widget.indexablelistview.IndexableListView;

/**
 *歌手或者专辑或者文件夹所有歌曲列表
 */
public class MusicListFragment extends BaseFragment implements IConstants{

//	protected IMediaService mService;
	private static final String TAG = "MusicListFragment";

	private ArtistAdapter listAdapter;
	@ViewInject(value = R.id.listview)
	private IndexableListView mMusiclist; //

	private  List<MusicInfo> musicInfos;
	private MusicListItemClickListener musicListItemClickListener;

	
	private PlayerHelpler mp3Util;
	
	private int mPlayListType=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		musicListItemClickListener = new MusicListItemClickListener();
		mp3Util= PlayerHelpler.getDefault();
		LogUtil.i(TAG, "onCreate");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment, container,
				false);
		initViewWidget(view);
		
		LogUtil.i(TAG, "onCreateView");
		return view;
	}

	/**
	 */
	private void initViewWidget(View view) {

		mMusiclist = (IndexableListView) view.findViewById(R.id.listview);

		mMusiclist.setFastScrollEnabled(true);
		mMusiclist.setOnItemClickListener(musicListItemClickListener);
		
		
		listAdapter = new ArtistAdapter(getActivity(),musicInfos,R.layout.item_listview__layout);
		mMusiclist.setShow(false);
		mMusiclist.addFooterView(getFoodView(getString(R.string.music_size,musicInfos.size())));
		mMusiclist.setAdapter(listAdapter);
		
		
		

	}
	public void initData(int flag,Object object,Context context){
		StringBuffer select = new StringBuffer();
		switch (flag) {
		case START_FROM_ARTIST:
			ArtistInfo artistInfo = (ArtistInfo) object;
			musicInfos=(MusicModel.getInstance().queryMusic(context,
					select.toString(), artistInfo.artist_name,
					START_FROM_ARTIST));
			mPlayListType=1;
			break;
		case START_FROM_ALBUM:
			AlbumInfo albumInfo = (AlbumInfo) object;
			musicInfos=(MusicModel.getInstance().queryMusic(context,
					select.toString(), albumInfo.album_id + "",
					START_FROM_ALBUM));
			mPlayListType=2;
			break;
		case START_FROM_FOLDER:
			FolderInfo folderInfo = (FolderInfo) object;
			musicInfos=(MusicModel.getInstance().queryMusic(context,
					select.toString(), folderInfo.folder_path,
					START_FROM_FOLDER));
			mPlayListType=3;
			break;
//		case START_FROM_FAVORITE:
////			musicInfos=(MusicModel.getInstance().queryFavorite(context));
//			break;
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
		public void convert(com.music.ui.adapter.ViewHolder helper, int position) {
			// TODO Auto-generated method stub
			MusicInfo musicInfo=getItem(position);
			
//			helper.setString(R.id.albumImage,folderInfo.folder_name);
			helper.setString(R.id.music_album,musicInfo.getAlbum());
			helper.setString(R.id.music_title,musicInfo.getTitle());
			helper.getView(R.id.catalog).setVisibility(View.GONE);
		}

	}
}
