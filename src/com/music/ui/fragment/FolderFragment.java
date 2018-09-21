package com.music.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lu.library.adapter.ViewHolder;
import com.lu.library.widget.overscroll.VerticalOverScrollBounceEffectDecorator;
import com.lu.library.widget.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;
import com.lu.library.widget.recyclerview.CommonRecyclerViewAdapter;
import com.lu.library.widget.recyclerview.MultiItemTypeAdapterForRV;
import com.lu.library.widget.recyclerview.base.CommonRecyclerViewHolder;
import com.music.bean.FolderInfo;
import com.music.lu.R;
import com.music.model.MusicModel;
import com.music.ui.activity.LocalMusicActivity;
import com.lu.library.adapter.LuAdapter;
import com.music.ui.service.IConstants;
import com.music.ui.service.IMediaService;
import com.music.ui.widget.indexablelistview.IndexableListView;
import com.lu.library.util.DebugLog;
import com.music.utils.LogUtil;

import java.util.List;

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


	RecyclerView recyclerView;

	CommonRecyclerViewAdapter<FolderInfo> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		musicListItemClickListener = new MusicListItemClickListener();

		LogUtil.i(TAG, "onCreate");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_song, container,
				false);
		initViewWidget(view);

		LogUtil.i(TAG, "onCreateView");
		return view;
	}

	/**
	 */
	private void initViewWidget(View view) {

//		mMusiclist = (IndexableListView) view.findViewById(R.id.listview);
//
//		mMusiclist.setFastScrollEnabled(true);
//		mMusiclist.setOnItemClickListener(musicListItemClickListener);


//		folderInfos = MusicUtils.getDefault().queryFolder(getActivity());
		folderInfos = MusicModel.getInstance().queryFolder(getActivity());
//		listAdapter = new ArtistAdapter(getActivity(),folderInfos,R.layout.item_listview_folder);
//
//
//
//		mMusiclist.setShow(false);
//		mMusiclist.setAdapter(listAdapter);
		view.findViewById(R.id.catalog).setVisibility(View.GONE);
		initRecycleView(view);

	}

	private void initRecycleView(View root) {

		recyclerView=root.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		DebugLog.d("size:"+folderInfos.size());
		adapter=new CommonRecyclerViewAdapter<FolderInfo>(getContext(),R.layout.item_listview_folder,folderInfos) {
			@Override
			protected void convert(CommonRecyclerViewHolder holder, FolderInfo folderInfo, int position){
				DebugLog.d("position:"+position+","+folderInfo.toString());
				holder.setText(R.id.tv_folder_name,folderInfo.folder_name);
				holder.setText(R.id.tv_folder_path,folderInfo.folder_path);
			}};
		adapter.setOnItemClickListener(new MultiItemTypeAdapterForRV.OnItemClickListener() {
			@Override
			public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
				((LocalMusicActivity)getActivity()).changeFragment(IConstants.START_FROM_FOLDER,folderInfos.get(position));
			}

			@Override
			public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
				return false;
			}
		});
		recyclerView.setAdapter(adapter);
		new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView));
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
		public void convert(ViewHolder helper, int position) {
			// TODO Auto-generated method stub
			FolderInfo folderInfo=getItem(position);

			helper.setString(R.id.tv_folder_name,folderInfo.folder_name);
			helper.setString(R.id.tv_folder_path,folderInfo.folder_path);

		}

	}
}
