package com.music.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lu.library.overscroll.VerticalOverScrollBounceEffectDecorator;
import com.lu.library.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;
import com.music.bean.MusicInfo;
import com.music.db.DBHelper;
import com.music.helpers.PlayerHelpler;
import com.music.lu.R;
import com.music.model.MusicModel;
import com.music.ui.adapter.MusicListAdapter;
import com.music.ui.recyclerview.CommonRecyclerViewAdapter;
import com.music.ui.recyclerview.MultiItemTypeAdapterForRV;
import com.music.ui.recyclerview.base.CommonRecyclerViewHolder;
import com.music.ui.service.IConstants;
import com.music.ui.widget.indexablelistview.IndexableListView;
import com.music.utils.DeBug;

import java.util.List;

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
	private PlayerHelpler mp3Util;
	private List<MusicInfo> mp3Infos;


	RecyclerView recyclerView;

	CommonRecyclerViewAdapter<MusicInfo> adapter;
	TextView stickyHeaderView;
	public static final int FIRST_STICKY_VIEW = 1;
	public static final int HAS_STICKY_VIEW = 2;
	public static final int NONE_STICKY_VIEW = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mp3Util= PlayerHelpler.getDefault();
		DeBug.d(this, "onCreate................");

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DeBug.d(this, "onCreateView................");
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_song, container, false);
		initViewWidget(view);
		return view;
	}





	/**
	 */
	private void initViewWidget(View view) {

		mMusiclist= view.findViewById(R.id.listview);


		stickyHeaderView=view.findViewById(R.id.catalog);
		mMusiclist.setFastScrollEnabled(false);


//		mp3Infos= new MediaUtil().getMp3Infos(getContext());
		mp3Infos= MusicModel.getInstance().sortMp3InfosByTitle(getContext());


		listAdapter = new MusicListAdapter(getActivity(),
				R.layout.item_listview__layout, mp3Infos,
				mMusiclist);
		mMusiclist.addFooterView(getFoodView(getString(R.string.music_size,mp3Util.getAllMp3Size())));
		mMusiclist.setAdapter(listAdapter);
		mMusiclist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mp3Util.setMusicBaseInfos(mp3Infos, PlayerHelpler.PLAY_BY_SONG);
				mp3Util.playMusic(position);
			}
		});
		initRecycleView(view);
	}


	void initRecycleView(View root){
		recyclerView=root.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter=new CommonRecyclerViewAdapter<MusicInfo>(getContext(),R.layout.item_listview__layout,mp3Infos) {
			@Override
			protected void convert(CommonRecyclerViewHolder holder, final MusicInfo mp3Info, int position) {
				holder.setText(R.id.music_title,mp3Info.getTitle()); //
				holder.setText(R.id.music_album,mp3Info.getArtist());
				holder.setText(R.id.catalog,mp3Info.getTitleKey().substring(0, 1));
				holder.itemView.setContentDescription(mp3Info.getTitleKey().substring(0, 1));
				if (position==0){
					holder.setVisible(R.id.catalog, true);
					holder.itemView.setTag(FIRST_STICKY_VIEW);
				}else{
					String preFirstPin=mp3Infos.get(position - 1).getTitleKey().substring(0,1);
					if (!TextUtils.equals(preFirstPin,mp3Info.getTitleKey().substring(0, 1))){
						holder.setVisible(R.id.catalog, true);
						holder.itemView.setTag(HAS_STICKY_VIEW);
					}else{
						holder.setVisible(R.id.catalog, false);
						holder.itemView.setTag(NONE_STICKY_VIEW);
					}
				}
				if (mp3Info.getTag()==0){
					holder.setImageResource(R.id.albumImage,R.drawable.note_btn_love_white);
				}else{
					holder.setImageResource(R.id.albumImage,R.drawable.note_btn_loved);
				}
				holder.getView(R.id.albumImage).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						DBHelper.getInstance().setMusicLove(mp3Info);
						adapter.notifyDataSetChanged();
					}
				});
			}

		};
		recyclerView.setAdapter(adapter);

		recyclerView.addOnScrollListener(onScrollListener);
		adapter.setOnItemClickListener(new MultiItemTypeAdapterForRV.OnItemClickListener() {
			@Override
			public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
				mp3Util.setMusicBaseInfos(mp3Infos, PlayerHelpler.PLAY_BY_SONG);
				mp3Util.playMusic(position);
			}

			@Override
			public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
				return false;
			}
		});

		new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView));
	}
	RecyclerView.OnScrollListener onScrollListener=new  RecyclerView.OnScrollListener() {
		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
			//判断是当前layoutManager是否为LinearLayoutManager
			// 只有LinearLayoutManager才有查找第一个可见view位置的方法
			if (layoutManager instanceof LinearLayoutManager) {
				LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
				//获取第一个可见view的位置
				int firstItemPosition = linearManager.findFirstVisibleItemPosition();
				if (firstItemPosition != 0) {
					stickyHeaderView.setVisibility(View.VISIBLE);
					View stickyInfoView = recyclerView.findChildViewUnder(
							stickyHeaderView.getMeasuredWidth() / 2, 5);

					if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
						stickyHeaderView.setText(stickyInfoView.getContentDescription());
					}

					View transInfoView = recyclerView.findChildViewUnder(
							stickyHeaderView.getMeasuredWidth() / 2, stickyHeaderView.getMeasuredHeight() + 1);

					if (transInfoView != null && transInfoView.getTag() != null) {

						int transViewStatus = (int) transInfoView.getTag();
						int dealtY = transInfoView.getTop() - stickyHeaderView.getMeasuredHeight();

						if (transViewStatus == HAS_STICKY_VIEW) {
							if (transInfoView.getTop() > 0) {
								stickyHeaderView.setTranslationY(dealtY);
							} else {
								stickyHeaderView.setTranslationY(0);
							}
						} else if (transViewStatus == NONE_STICKY_VIEW) {
							stickyHeaderView.setTranslationY(0);
						}
					}
				} else {
					stickyHeaderView.setVisibility(View.GONE);
				}
			}
		}
	};


}
