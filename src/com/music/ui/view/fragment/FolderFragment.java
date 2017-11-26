package com.music.ui.view.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.music.bean.FolderInfo;
import com.music.lu.R;
import com.music.model.MusicModel;
import com.music.service.IConstants;
import com.music.service.IMediaService;
import com.music.utils.LogUtil;
import com.music.ui.view.activity.LocalMusicActivity;
import com.music.ui.adapter.LuAdapter;
import com.music.ui.widget.indexablelistview.IndexableListView;

/**
 *文件夹
 */
public class FolderFragment extends Fragment {

	protected IMediaService mService;
	private static final String TAG = "FolderFragment";

	private ArtistAdapter listAdapter;
	@ViewInject(value = R.id.listview)
	private IndexableListView mMusiclist;

	private  List<FolderInfo> folderInfos;
	private MusicListItemClickListener musicListItemClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		musicListItemClickListener = new MusicListItemClickListener();
		
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
	 */
	private void initViewWidget(View view) {

		mMusiclist = (IndexableListView) view.findViewById(R.id.listview);

		mMusiclist.setFastScrollEnabled(true);
		mMusiclist.setOnItemClickListener(musicListItemClickListener);
		
		
//		folderInfos = MusicUtils.getDefault().queryFolder(getActivity());
		folderInfos = MusicModel.getInstance().queryFolder(getActivity());
		listAdapter = new ArtistAdapter(getActivity(),folderInfos,R.layout.item_listview_folder);
		
		

		mMusiclist.setShow(false);
		mMusiclist.setAdapter(listAdapter);

	}

	class MusicListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// playMusic(position);
			((LocalMusicActivity)getActivity()).changeFragment(IConstants.START_FROM_FOLDER,folderInfos.get(position));
		}

	}

	class ArtistAdapter extends LuAdapter<FolderInfo>{

		
		public ArtistAdapter(Context context, List<FolderInfo> datas,
				int mItemLayoutId) {
			super(context, datas, mItemLayoutId);
		}

		

		@Override
		public void convert(com.music.ui.adapter.ViewHolder helper, int position) {
			// TODO Auto-generated method stub
			FolderInfo folderInfo=getItem(position);
			
			helper.setString(R.id.tv_folder_name,folderInfo.folder_name);
			helper.setString(R.id.tv_folder_path,folderInfo.folder_path);
			
		}

	}
}
