package com.music.ui.view.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.music.bean.AlbumInfo;
import com.music.lu.R;
import com.music.model.MusicModel;
import com.music.ui.service.IConstants;
import com.music.ui.service.IMediaService;
import com.music.utils.AsyncTaskUtil;
import com.music.utils.AsyncTaskUtil.IAsyncTaskCallBack;
import com.music.utils.image.BitmapCacheUtil;
import com.music.utils.DeBug;
import com.music.utils.LogUtil;
import com.music.utils.PhotoUtils;
import com.music.utils.StringUtil;
import com.music.ui.view.activity.LocalMusicActivity;
import com.music.ui.widget.indexablelistview.IndexableListView;

/**
 *所有专辑
 */
public class AlbumFragment extends BaseFragment {

	protected IMediaService mService;
	private static final String TAG = "AlbumFragment";
	public static final int SONGFRAGMENT = 11;
	public static final int ALBUMFRAGMENT = 0;

	enum fragment {
		SONGFRAGMENT, ALBUMFRAGMENT, ART
	}

	@ViewInject(value = R.id.iv_back)
	private ImageView iv_back;

	@ViewInject(value = R.id.iv_more)
	private ImageView iv_more;

	private AlbumAdapter listAdapter;
	@ViewInject(value = R.id.listview)
	private IndexableListView mMusiclist; //

	private List<AlbumInfo> albumInfos=new ArrayList<>();
	private MusicListItemClickListener musicListItemClickListener;
	@SuppressWarnings("unused")
	private List<Map<String, Boolean>> loadList;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment, container,
				false);
		initViewWidget(view);
		initData();
		return view;
	}

	private void initData() {
		new AsyncTaskUtil(new IAsyncTaskCallBack() {
			@Override
			public Object doInBackground(String... arg0) {

				albumInfos.addAll(MusicModel.getInstance().queryAlbums(getActivity()));
				DeBug.d("aaa","加载完成 albumInfos.size():"+albumInfos.size());
				return null;
			}

			@Override
			public void onPostExecute(Object result) {
				listAdapter.notifyDataSetChanged();
			}
		}).execute("");
	}

	/**
	 */
	private void initViewWidget(View view) {

		mMusiclist = (IndexableListView) view.findViewById(R.id.listview);

		mMusiclist.setFastScrollEnabled(true);
		mMusiclist.setOnItemClickListener(musicListItemClickListener);

		long start = System.currentTimeMillis();

		loadList = new ArrayList<Map<String, Boolean>>();

		// Collections.sort(albumInfos);
		listAdapter = new AlbumAdapter(getActivity(), albumInfos);


		mMusiclist.addFooterView(getFoodView(getString(R.string.album_size,albumInfos.size())));

		mMusiclist.setAdapter(listAdapter);
	}

	class MusicListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// playMusic(position);
			((LocalMusicActivity)getActivity()).changeFragment(IConstants.START_FROM_ALBUM,albumInfos.get(position));
		}

	}

	class AlbumAdapter extends BaseAdapter implements SectionIndexer {
		private Set<AsyncTaskUtil> taskCollection;

		public AlbumAdapter(Context context, List<AlbumInfo> objects) {
			// super(context, resource, objects);
			this.context = context;
			this.albumInfos = objects;
			taskCollection = new HashSet<AsyncTaskUtil>();
		}

		private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		private Context context;
		private List<AlbumInfo> albumInfos;
		@SuppressWarnings("unused")
		private int pos = -1;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return albumInfos.size();
		}

		@Override
		public AlbumInfo getItem(int position) {
			// TODO Auto-generated method stub
			return albumInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			AlbumInfo album = getItem(position);
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_listview_album, null);
				viewHolder.albumImage = (ImageView) convertView
						.findViewById(R.id.iv_album);
				viewHolder.tv_album_name = (TextView) convertView
						.findViewById(R.id.tv_album_name);
				viewHolder.tv_number_of_songs = (TextView) convertView
						.findViewById(R.id.tv_number_of_songs);
				viewHolder.catalog = (TextView) convertView
						.findViewById(R.id.catalog);
				viewHolder.tv_artist = (TextView) convertView
						.findViewById(R.id.tv_artist);
				convertView.setTag(viewHolder); //

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.tv_album_name.setText(album.album_name);
			viewHolder.tv_number_of_songs.setText(album.number_of_songs + "首歌曲");
			viewHolder.tv_artist.setText(album.album_art);

			viewHolder.catalog.setVisibility(View.VISIBLE);


			viewHolder.albumImage.setTag(album.album_path);
			// viewHolder.albumImage.setBackgroundResource(R.drawable.playing_bar_default_avatar);

			viewHolder.albumImage
					.setImageResource(R.drawable.playing_bar_default_avatar);
			// Bitmap bitmap = BitmapCacheUtil.getDefalut()
			
			String firstPinYin = album.pinyin.substring(0, 1);
			viewHolder.catalog.setText(firstPinYin);
			if (position > 0) {
				String nextFirstPinYin = albumInfos.get(position - 1).pinyin
						.substring(0, 1);
				if (firstPinYin.equals(nextFirstPinYin)) {
					viewHolder.catalog.setVisibility(View.GONE);
				} else {
					viewHolder.catalog.setVisibility(View.VISIBLE);
				}
			}
			viewHolder.albumImage.setTag(album.album_path);
			return convertView;
		}

		/**
		 *
		 * @author wwj
		 * 
		 */
		public class ViewHolder {
			public ImageView albumImage; //
			public TextView tv_album_name; //
			public TextView tv_number_of_songs;
			public TextView tv_artist;
			public TextView catalog;
		}

		@Override
		public Object[] getSections() {
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));
			return sections;
		}

		@Override
		public int getPositionForSection(int section) {

			int result = -1;
			// boolean isFind=false;
			String s = mSections.substring(section, section + 1);
			for (int i = 0; i < albumInfos.size(); i++) {
				String string = StringUtil.getPingYin(
						albumInfos.get(i).album_name).substring(0, 1);
				// String string = albumInfos.get(i).getTitlepingyin()
				// .substring(0, 1);

				if (string.equalsIgnoreCase(s)) {
					result = i;
					// isFind=true;
					break;
				}
			}
			// if(!isFind){
			// result=section;
			// }
			return result;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		public void loadImage(ImageView albumImage, final String album_path,
				final int postion) {

			Bitmap bitmap = BitmapCacheUtil.getDefalut()
					.getBitmapFromMemoryCache(album_path);

			if (bitmap == null) {

//				if (isLoaded[postion] == 2) {
//					return;
//				}

				AsyncTaskUtil arg0 = new AsyncTaskUtil(
						new IAsyncTaskCallBack() {

							@Override
							public void onPostExecute(Object result) {
								ImageView imageView = (ImageView) mMusiclist
										.findViewWithTag(album_path);
								Bitmap b = (Bitmap) result;
								if (imageView != null && b != null) {
									imageView.setImageBitmap(b);
								}
								taskCollection.remove(this);
							}

							@Override
							public Object doInBackground(String... arg0) {
								Bitmap b = null;

								b = PhotoUtils.createBitmap(arg0[0], 50,50);
								
								if (b != null) {
									BitmapCacheUtil.getDefalut()
											.addBitmapToMemoryCache(arg0[0], b);
									
//									isLoaded[postion] = 1;
									DeBug.d(TAG, "b.getWidth():"+b.getWidth());
									DeBug.d(TAG, "b.getHeight():"+b.getHeight());
									
								} else {
//									isLoaded[postion] = 2;
								}

								return b;
							}
						});

				taskCollection.add(arg0);
				arg0.execute(album_path);

			} else {

				if (albumImage != null && bitmap != null) {
					albumImage.setImageBitmap(bitmap);
				}
			}
		}

	}
	
	
}
