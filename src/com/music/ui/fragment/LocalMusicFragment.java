package com.music.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.music.lu.R;
import com.music.ui.adapter.ViewPagerAdapter;
import com.music.ui.service.IConstants;
import com.music.utils.DeBug;
import com.lu.library.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
public class LocalMusicFragment extends Fragment implements IConstants ,OnClickListener{

	private static final String TAG = "LocalMusicFragment";

	private ViewPagerAdapter viewPagerAapter;

	private List<Fragment> list = new ArrayList<Fragment>();

	private ViewPager viewPager;

	private ImageView iv_tabline;

	private int widthScreen1_4;

	private SongFragment songFragment;
	private ArtistFragment artistFragment;
	private AlbumFragment albumFragment;
	private FolderFragment folderFragment;

	private RelativeLayout rl_folder,rl_album,rl_artist,rl_song;


	/**
	 * 线占屏幕的几分之几
	 */
	private final static int SPLIT_SIZE=2;
//	public static boolean isFirst=true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		songFragment = new SongFragment();
//		artistFragment = new ArtistFragment();
//		albumFragment = new AlbumFragment();
		folderFragment = new FolderFragment();

		list.add(songFragment);
//		list.add(artistFragment);
//		list.add(albumFragment);
		list.add(folderFragment);

		DeBug.d(TAG, "onCreate");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_localmusic, container,
				false);

		initWidget(view);
		initTabLine();
		DeBug.d(TAG, "onCreateView");
		return view;
	}


	private void initWidget(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
		iv_tabline = (ImageView) view.findViewById(R.id.iv_tabline);
		rl_folder=(RelativeLayout) view.findViewById(R.id.rl_folder);
		rl_album=(RelativeLayout) view.findViewById(R.id.rl_album);
		rl_artist=(RelativeLayout) view.findViewById(R.id.rl_artist);
		rl_song=(RelativeLayout) view.findViewById(R.id.rl_song);
		rl_folder.setOnClickListener(this);
		rl_album.setOnClickListener(this);
		rl_artist.setOnClickListener(this);
		rl_song.setOnClickListener(this);


		viewPagerAapter = new ViewPagerAdapter(getChildFragmentManager(), list);

		viewPager.setAdapter(viewPagerAapter);
		// viewPager.fakeDragBy(10);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) iv_tabline
				.getLayoutParams();
		lp.width= ScreenUtils.getScreenWidth(getActivity())/SPLIT_SIZE;
		iv_tabline.setLayoutParams(lp);
	}
	private class MyOnPageChangeListener implements OnPageChangeListener{
		@Override
		public void onPageSelected(int position) {

		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffPx) {
			updateTopLine(position, positionOffset);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}
	public void updateTopLine(int position, float positionOffset) {
		RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) iv_tabline
				.getLayoutParams();
		lp.leftMargin = (int) ((position + positionOffset) * widthScreen1_4);
		iv_tabline.setLayoutParams(lp);
	}

	private void initTabLine() {
		Display display = getActivity().getWindow().getWindowManager()
				.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);

		widthScreen1_4 = displayMetrics.widthPixels /SPLIT_SIZE;

		LayoutParams lp = iv_tabline.getLayoutParams();
		lp.width = widthScreen1_4;
		iv_tabline.setLayoutParams(lp);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_song:
			viewPager.setCurrentItem(0);
			break;
		case R.id.rl_artist:
			viewPager.setCurrentItem(1);
			break;
		case R.id.rl_album:
			viewPager.setCurrentItem(2);
			break;
		case R.id.rl_folder:
			viewPager.setCurrentItem(3);
			break;

		default:
			break;
		}
	}
}
