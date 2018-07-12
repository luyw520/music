package com.music.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lu.library.base.BaseObserver;
import com.lu.library.permissiongen.PermissionFail;
import com.lu.library.permissiongen.PermissionSuccess;
import com.lu.library.recyclerview.CommonRecyclerViewAdapter;
import com.lu.library.recyclerview.MultiItemTypeAdapterForRV;
import com.lu.library.recyclerview.base.CommonRecyclerViewHolder;
import com.lu.library.util.DebugLog;
import com.lu.library.util.PhotoUtils;
import com.music.Constant;
import com.music.bean.MessageEvent;
import com.music.bean.MusicInfo;
import com.music.bean.UserManager;
import com.music.helpers.PlayerHelpler;
import com.music.lu.R;
import com.music.presenter.IPlayState;
import com.music.presenter.LocalMusicPresenter;
import com.music.ui.broadcastreceiver.MediaButtonReceiver;
import com.music.ui.service.IConstants;
import com.music.ui.view.MyNotification;
import com.music.ui.view.SplashScreen;
import com.music.ui.view.widget.CircularImage;
import com.music.ui.view.widget.MusicTimeProgressView;
import com.music.utils.ApplicationUtil;
import com.music.utils.DeBug;
import com.music.utils.DialogUtil;
import com.music.utils.LogUtil;
import com.music.utils.MediaUtil;
import com.music.vo.MusicListVO;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.music.Constant.MUSIC_CURRENT;
import static com.music.Constant.MUSIC_DURATION;
import static com.music.Constant.MUSIC_PAUSE;
import static com.music.utils.PhotoUtils.INTENT_REQUEST_CODE_CAMERA;
import static com.music.utils.PhotoUtils.INTENT_REQUEST_CODE_CROP;

/**
 *主界面
 *歌单界面
 */
@ContentView(value = R.layout.activity_list_music)
public class MainActivity extends BaseMVPActivity<LocalMusicPresenter> implements
		IConstants,View.OnClickListener {


	public static final String TAG = null;
	public static final int REQUESTCODE_LOGIN = 0;
	private static final int LOCK_PASSWORD = 1;

	private SplashScreen mSplashScreen;

	private boolean isHome = true;

	@ViewInject(value = R.id.tv_title)
	private TextView tv_title;

	@ViewInject(value = R.id.tv_music_Artist)
	private TextView tv_music_Artist;

	@ViewInject(value = R.id.tv_music_time)
	private TextView tv_music_CurrentTime;
//	@ViewInject(value = R.id.tv_title)
//	private TextView tv_title;

	@ViewInject(value = R.id.tv_music_title)
	private TextView tv_music_title;

	@ViewInject(value = R.id.tv_mobile)
	private TextView tv_mobile;

	@ViewInject(value = R.id.btn_playing2)
	private ImageButton btn_musicPlaying;

	private MyNotification myNotification;

	@ViewInject(value = R.id.iv_music_album)
	private ImageView iv_music_album;

//	@ViewInject(value = R.id.slidingMenu)
//	private com.music.ui.widget.slidingmenu.SlidingMenu slidingMenu;
	@ViewInject(value = R.id.slidingMenu)
	private DrawerLayout drawerLayout;

	@ViewInject(value = R.id.iv_back)
	private ImageView iv_back;
	@ViewInject(value = R.id.iv_search)
	private ImageView iv_search;
	@ViewInject(value = R.id.iv_more)
	private ImageView iv_more;

	@ViewInject(value = R.id.musicTimeProgressView)
	private MusicTimeProgressView musicTimeProgressView;

	@ViewInject(value = R.id.iv_header)
	private CircularImage iv_header;
	@ViewInject(value = R.id.tv_username)
	private TextView tv_username;

	@ViewInject(value = R.id.tv_local_song_size)
	private TextView tvLocalSongSize;
	@ViewInject(value = R.id.navigationView)
	private NavigationView navigationView;

	@SuppressWarnings("unused")
////	private PlayPauseDrawable playPauseDrawable;
//	private int playColor = 0XFFE91E63;
//	private int pauseColor = 0XFFffffff;
//	private int dwableDuaration = 200;
	/**
	 * 当前播放的音乐
	 */
	private MusicInfo currentMp3Info;
	private PlayerHelpler mp3Util;

	/**
	 */
	protected String userHeaderImg;

//	private boolean isFirst = true;

	@ViewInject(R.id.recyclerView)
	private RecyclerView recyclerView;


	private CommonRecyclerViewAdapter<MusicListVO> adapter;
	private List<MusicListVO> musicListList=new ArrayList<>();

	private AudioManager mAudioManager;
	private ComponentName mComponentName;
	@TargetApi(19)
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		showSplash();
		mp3Util = PlayerHelpler.getDefault();
		initSlideMenu();
		initListView();

		registerReceiver();
		registerMediaButtonEventReceiver();
		initData();

	}

	private void showSplash() {
		mSplashScreen = new SplashScreen(this);
		mSplashScreen.show(R.drawable.image_splash_background_new,
				SplashScreen.SLIDE_UP);
	}

	private void registerMediaButtonEventReceiver(){
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// AudioManager注册一个MediaButton对象
		mComponentName = new ComponentName(getPackageName(), MediaButtonReceiver.class.getName());
		mAudioManager.registerMediaButtonEventReceiver(mComponentName);
	}
	private void initSlideMenu() {
		View navigaHeadder=navigationView.inflateHeaderView(R.layout.layout_menu);
		iv_header=navigaHeadder.findViewById(R.id.iv_header);
		tv_username=navigaHeadder.findViewById(R.id.tv_username);
		iv_header.setOnClickListener(this);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				switch (item.getItemId()){
					case R.id.photo:
						startActivity(SettingActivity.class);
						break;
					case R.id.wallet:
						showExitDialog();
						break;
				}
				return false;
			}
		});
	}

	private void initListView() {
		adapter=new CommonRecyclerViewAdapter<MusicListVO>(this,R.layout.item_list_song,musicListList) {
			@Override
			protected void convert(CommonRecyclerViewHolder holder, MusicListVO musicList, int position) {
				holder.setText(R.id.tvListTitle,musicList.musicListName);
				holder.setText(R.id.tvListSubTitle,String.format(getString(R.string.song_size),String.valueOf(musicList.songSize)));
			}
		};
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
		adapter.setOnItemClickListener(new MultiItemTypeAdapterForRV.OnItemClickListener() {
			@Override
			public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
				ListMusicDetailActivity.startActivityWithParam(MainActivity.this,musicListList.get(position).musicListName);
			}

			@Override
			public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
				return false;
			}
		});
	}


	private void initData() {
		needPermission(100,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
		iv_header.setImageBitmap(UserManager.getInstance().getHeader(this));

		tv_title.setText("");
		iv_back.setImageResource(R.drawable.ic_common_title_bar_menu);
		iv_search.setVisibility(View.GONE);
		iv_more.setVisibility(View.GONE);
	}
	@PermissionSuccess(requestCode = 100)
	public void doSomething(){
		DeBug.d(getClass().getSimpleName(),"doSomething.........");
		mPersenter.getMusicOrderAscByTitle(new BaseObserver<List<MusicInfo>>(){
			@Override
			public void onComplete() {

				bindData();
			}
		},true);

	}
	@PermissionSuccess(requestCode = 200)
	public void camera(){
		DebugLog.d("拍照");
		userHeaderImg = PhotoUtils
				.takePicture(MainActivity.this, com.music.utils.PhotoUtils.HEADER_PATH,null);

	}
	@PermissionFail(requestCode = 100)
	public void doFailSomething(){
		DialogUtil.showToast(this,"需要权限");
	}
	@PermissionFail(requestCode = 200)
	public void doFailCamera(){
		DialogUtil.showToast(this,"需要权限");
	}
	void bindData(){
		mSplashScreen.removeSplashScreen();
		PlayerHelpler.getDefault().init();
		tvLocalSongSize.setText(getString(R.string.local_music)+"("+ mPersenter.getCacheMusic().size()+")");
		initWidgetData();
		resetPlayState();
		adapter.addAll(mPersenter.getMusicList());
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.addAll(mPersenter.getMusicList());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.i(getClass(), "onActivityResult..............resultCode="
				+ resultCode);
		switch (requestCode) {
		case REQUESTCODE_LOGIN:
//			loginResult(data);
//			break;

		case INTENT_REQUEST_CODE_CAMERA:
			case INTENT_REQUEST_CODE_CROP:
			UserManager.getInstance().chooseHeaderImg(requestCode, resultCode,
					data, this, iv_header, userHeaderImg);
			break;
		default:

			break;
		}

	}

	/**
	 * 登录后
	 * @param data
	 */
	private void loginResult(Intent data) {
		if (data == null) {
			DialogUtil.showToast(getApplicationContext(), R.string.title_login_faild);
			return;
		}
		DialogUtil.showToast(getApplicationContext(), R.string.title_login_success);
		String username = UserManager.getInstance().getUserBean().getUsername();
		tv_username.setText(username);
		String userHeaderUrl = UserManager.getInstance().getUserBean()
				.getHeadPath();
		ImageLoader.getInstance().displayImage(userHeaderUrl, iv_header);
	}

	/**
	 * 重新设置播放状态
	 */
	private void resetPlayState() {

		currentMp3Info = mp3Util.getCurrentMp3Info();
		tv_music_title.setText(currentMp3Info.getTitle());
		tv_music_Artist.setText(currentMp3Info.getArtist());
		Bitmap bmp = MediaUtil.getMusicImage(getApplicationContext(),
				currentMp3Info,
				iv_music_album.getWidth(),iv_music_album.getHeight());
		iv_music_album.setImageBitmap(bmp);
		DeBug.d(this,
				"currentMp3Info.getDuration():" + currentMp3Info.getDuration());
		musicTimeProgressView.setMaxProgress(currentMp3Info.getDuration());
		myNotification.reset();


	}

	private void initWidgetData() {
		tv_mobile.setText(android.os.Build.MODEL);
		myNotification = new MyNotification(this);
	}

	@OnClick({ R.id.iv_back, R.id.btn_next2, R.id.btn_playing2,
			R.id.music_about_layout, R.id.rl_setting, R.id.rl_exit,
			R.id.iv_header, R.id.iv_search,R.id.llLocal })
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.iv_back:
			back();
			break;
		case R.id.btn_playing2:
			PlayerHelpler.getDefault().playMusic();
			break;
		case R.id.btn_next2:
			PlayerHelpler.getDefault().nextMusic(false);
			break;
		case R.id.rl_setting:
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.music_about_layout:
//			startActivity(PlayerActivity.class,iv_music_album,"share");
			startActivity(PlayerActivity.class);
			break;
		case R.id.rl_exit:
			showExitDialog();
			break;
		case R.id.iv_header:
			chooseHeader();
			break;
			case R.id.llLocal:
				startActivity(LocalMusicActivity.class);
				break;
		case R.id.iv_search:
			startActivity(SearchMusicActivity.class);
			break;
		default:
			break;
		}

	}

	/**
	 * 选择头像
	 */
	private void chooseHeader() {
//		if (!UserManager.isLogin()) {
//			DialogUtil.showToast(getApplicationContext(), R.string.no_login_tips);
//			Intent intent2 = new Intent(this, LoginActivity.class);
//			startActivityForResult(intent2, REQUESTCODE_LOGIN);
//		} else {
			chooseHeaderImgDialog();
//		}
	}

	/**
	 * 返回
	 */
	private void back() {
		if (!isHome) {
		} else {
			drawerLayout.openDrawer(Gravity.LEFT);
		}
	}

	/**
	 * 选择头像对话框
	 */
	private void chooseHeaderImgDialog() {
		DialogUtil.showAlertDialog(this, getString(R.string.select_dialog_title), new String[] { getString(R.string.select_dialog_capture), getString(R.string.select_dialog_pick) },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							needPermission(200,new String[]{Manifest.permission.CAMERA});
							break;
						case 1:
							PhotoUtils.selectPhoto(MainActivity.this);
							break;
						default:
							break;
						}

						DialogUtil.closeAlertDialog();

					}
				});
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isHome) {
				tv_title.setText(R.string.local_music);
				isHome = true;
			} else {
				DeBug.d(TAG, "moveTaskToBack.........................");
				ApplicationUtil.setAppToBack(this, 1);
				moveTaskToBack(false);
				return true;
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 */
	private void registerReceiver() {
		regisgerPlayStateReceiver();

	}
	IPlayState state = new MyMainState();
	/**
	 */
	private void regisgerPlayStateReceiver() {

	}

	public void handleMessage(MessageEvent event){
		DebugLog.d("接收事件:"+event.toString());
		switch (event.type){
			case Constant.MUSIC_PLAYER:
				state.playMusicState();
				break;
			case MUSIC_CURRENT:
				state.currentState((int)event.data);
				break;
			case MUSIC_DURATION:
				state.duration((Integer) event.data);
				break;
			case MUSIC_PAUSE:
				state.pauseMusicState();
				break;
		}
	}
	/**
	 *
	 *
	 */
	class MyMainState implements IPlayState {

		@Override
		public void currentState(int currentTime) {
			mp3Util.setCurrentTime(currentTime);
			tv_music_CurrentTime.setText(MediaUtil.formatTime(currentTime));
			musicTimeProgressView.setCurrentProgress(currentTime);
		}

		@Override
		public void duration(int duration) {
			Log.i(TAG, "duration()");
			resetPlayState();

		}

		@Override
		public void pauseMusicState() {
			btn_musicPlaying
					.setImageResource(R.drawable.img_button_notification_play_play);
			myNotification.setPlayImageState(false);
//			mHandler.removeCallbacks(progressRunnable);
		}

		@Override
		public void playMusicState() {
			// playPauseDrawable.animatePause();
			btn_musicPlaying
					.setImageResource(R.drawable.img_button_notification_play_pause);
			myNotification.setPlayImageState(true);
//			mHandler.postDelayed(progressRunnable,100);
		}

	}

	private void showExitDialog() {
		DialogUtil.showExitAlertDialog(this,
				new View.OnClickListener() {
					public void onClick(View view) {
						exit();
					}
				});
	}

	/**
	 */
	public void exit() {

		ApplicationUtil.setAppToBack(this, 1);
		mp3Util.saveCurrentMusicInfo(this);
		myNotification.cancel();
		mp3Util.unBindService();
		finish();
		System.exit(0);

		// onDestroy();
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
	}

	public void unregisterReceiver() {
		myNotification.unregisterReceiver();
	}

}
