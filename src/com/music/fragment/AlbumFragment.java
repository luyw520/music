package com.music.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.music.LocalMusicActivity;
import com.music.bean.AlbumInfo;
import com.music.lu.R;
import com.music.lu.utils.AsyncTaskUtil;
import com.music.lu.utils.AsyncTaskUtil.IAsyncTaskCallBack;
import com.music.lu.utils.BitmapCacheUtil;
import com.music.lu.utils.LogUtil;
import com.music.lu.utils.MyLog;
import com.music.lu.utils.PhotoUtils;
import com.music.lu.utils.StringUtil;
import com.music.service.IConstants;
import com.music.service.IMediaService;
import com.music.widget.indexablelistview.IndexableListView;

/**
 *
 */
public class AlbumFragment extends Fragment {

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
	private IndexableListView mMusiclist; // 音乐列表

	// private Mp3Util_New mp3Util;
	private List<AlbumInfo> albumInfos;
	private MusicListItemClickListener musicListItemClickListener;
	private List<Map<String, Boolean>> loadList;
	private int[] isLoaded;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		musicListItemClickListener = new MusicListItemClickListener();
		albumInfos = com.music.lu.utils.MusicUtils.getDefault().queryAlbums(
				getActivity());
		isLoaded = new int[albumInfos.size()];
		LogUtil.d(TAG, "onCreate");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment, container,
				false);
		initViewWidget(view);
		LogUtil.d(TAG, "onCreateView");
		return view;
	}

	/**
	 * 初始化view上面的控件
	 */
	private void initViewWidget(View view) {

		mMusiclist = (IndexableListView) view.findViewById(R.id.listview);

		mMusiclist.setFastScrollEnabled(true);
		mMusiclist.setOnItemClickListener(musicListItemClickListener);

		long start = System.currentTimeMillis();

		loadList = new ArrayList<Map<String, Boolean>>();
		long query = System.currentTimeMillis();
		System.out.println("album widget:query " + (query - start) / 1000.0);

		// Collections.sort(albumInfos);
		long sort = System.currentTimeMillis();
		System.out.println("album widget:sort " + (sort - query) / 1000.0);
		listAdapter = new AlbumAdapter(getActivity(), albumInfos);

		LinearLayout listViewFoodView = new LinearLayout(getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		params.bottomMargin = 200;
		params.leftMargin = 100;
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText("共" + albumInfos.size() + "首专辑");

		listViewFoodView.addView(textView, params);

		mMusiclist.addFooterView(listViewFoodView);

		mMusiclist.setAdapter(listAdapter);
		System.out.println("album widget: init"
				+ (System.currentTimeMillis() - sort) / 1000.0);
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
				convertView.setTag(viewHolder); // 表示给View添加一个格外的数据，

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.tv_album_name.setText(album.album_name);
			viewHolder.tv_number_of_songs.setText(album.number_of_songs + "首歌");
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
			// mMusiclist.setShow(true);
			loadImage(viewHolder.albumImage, album.album_path, position);
			return convertView;
		}

		/**
		 * 定义一个内部类 声明相应的控件引用
		 * 
		 * @author wwj
		 * 
		 */
		public class ViewHolder {
			// 所有控件对象引用
			public ImageView albumImage; // 专辑图片
			public TextView tv_album_name; // 音乐标题
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

			// Map<String,Boolean> map=new HashMap<String, Boolean>();
			// map.put(album_path, false);
			// loadList.add(map);
			if (bitmap == null) {

				// loadList.add(new HashM)
				// BitmapCacheUtil.getDefalut().downAndCache(album_path);

				if (isLoaded[postion] == 2) {
					return;
				}

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
								// else{
								// imageView.setBackgroundResource(R.drawable.playing_bar_default_avatar);

								// }
								taskCollection.remove(this);
							}

							@Override
							public Object doInBackground(String... arg0) {
								Bitmap b = null;

								// if(arg0!=null){
								// File file=new File(arg0[0]);
								// if(file!=null&&file.exists()){

//								b = BitmapFactory.decodeFile(arg0[0]);
								b = PhotoUtils.createBitmap(arg0[0], 50,50);
								
								if (b != null) {
									BitmapCacheUtil.getDefalut()
											.addBitmapToMemoryCache(arg0[0], b);
									// Log.d(TAG, "arg0[0]:"+arg0[0]);
									// Map<String,Boolean> map=new
									// HashMap<String, Boolean>();
									// map.put(album_path, true);
									// loadList.add(map);
									
									isLoaded[postion] = 1;
									MyLog.d(TAG, "b.getWidth():"+b.getWidth());
									MyLog.d(TAG, "b.getHeight():"+b.getHeight());
									
								} else {
									isLoaded[postion] = 2;
								}
								// }
								// }

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
