package com.music.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.lu.library.log.DebugLog;
import com.lu.library.util.PhotoUtil;
import com.music.Constant;
import com.music.annotation.ComputeTime;
import com.music.bean.FolderInfo;
import com.music.bean.MessageEvent;
import com.music.bean.MusicInfo;
import com.music.bean.UserManager;
import com.music.helpers.PlayerHelpler;
import com.music.lu.R;
import com.music.presenter.IPlayState;
import com.music.presenter.LocalMusicPresenter;
import com.music.ui.fragment.LocalMusicFragment;
import com.music.ui.fragment.SongFragment;
import com.music.ui.service.IConstants;
import com.music.ui.view.widget.CircularImage;
import com.music.ui.view.widget.MusicTimeProgressView;
import com.music.utils.ApplicationUtil;
import com.music.utils.DeBug;
import com.music.utils.DialogUtil;
import com.music.utils.LogUtil;
import com.music.utils.MediaUtil;

import java.util.List;

import static com.music.utils.PhotoUtils.INTENT_REQUEST_CODE_CAMERA;
import static com.music.utils.PhotoUtils.INTENT_REQUEST_CODE_CROP;

/**
 *主界面
 *
 */
@ContentView(value = R.layout.activity_localmusic)
public class LocalMusicActivity extends BaseMVPActivity<LocalMusicPresenter> implements
		IConstants,View.OnClickListener {


	public static final String TAG = null;
	public static final int REQUESTCODE_LOGIN = 0;
	private static final int LOCK_PASSWORD = 1;


	private boolean isHome = true;

	@ViewInject(value = R.id.tv_title)
	private TextView tv_title;

	@ViewInject(value = R.id.tv_music_Artist)
	private TextView tv_music_Artist;

	@ViewInject(value = R.id.tv_music_time)
	private TextView tv_music_CurrentTime;

	@ViewInject(value = R.id.tv_music_title)
	private TextView tv_music_title;

	@ViewInject(value = R.id.tv_mobile)
	private TextView tv_mobile;

	@ViewInject(value = R.id.btn_playing2)
	private ImageButton btn_musicPlaying;


	@ViewInject(value = R.id.iv_music_album)
	private ImageView iv_music_album;

//	@ViewInject(value = R.id.slidingMenu)
//	private com.music.ui.widget.slidingmenu.SlidingMenu slidingMenu;

	@ViewInject(value = R.id.iv_back)
	private ImageView iv_back;
	@ViewInject(value = R.id.iv_search)
	private ImageView iv_search;

	@ViewInject(value = R.id.musicTimeProgressView)
	private MusicTimeProgressView musicTimeProgressView;

	@ViewInject(value = R.id.iv_header)
	private CircularImage iv_header;
	@ViewInject(value = R.id.tv_username)
	private TextView tv_username;
	@ViewInject(value = R.id.navigationView)
	private NavigationView navigationView;

	/**
	 * 当前播放的音乐
	 */
	private MusicInfo currentMp3Info;
	private PlayerHelpler mp3Util;

	private SongFragment musicListFragment = null;
	private FragmentTransaction transaction;
	private LocalMusicFragment localMusicFragment;
	/**
	 */
	protected String userHeaderImg;


	@TargetApi(19)
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mp3Util = PlayerHelpler.getDefault();
		initData();
	}



	/**
	 * click ARTIST ,ALBUM,FOLDER jump to musiclistfragment
	 * @param flag
	 * @param object
	 */
	@ComputeTime
	public void changeFragment(int flag, Object object) {
//		if (musicListFragment == null) {
			musicListFragment =  SongFragment.newInstance(SongFragment.TYPE_SONG_FOLDER,object);
//		}

//		musicListFragment.initData(object);
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.id_frame, musicListFragment);
		transaction.addToBackStack(null);
		transaction.commit();

		isHome = false;
		String title = "";
		switch (flag) {
//		case START_FROM_ARTIST:
//			ArtistInfo artistInfo = (ArtistInfo) object;
//			title = artistInfo.artist_name;
//			break;
//		case START_FROM_ALBUM:
//			AlbumInfo albumInfo = (AlbumInfo) object;
//			title = albumInfo.album_name;
//			break;
		case START_FROM_FOLDER:
			FolderInfo folderInfo = (FolderInfo) object;
			title = folderInfo.folder_name;
			break;
		case START_FROM_FAVORITE:
//			title = " fa";
			break;
		}
		tv_title.setText(title);
	}

	private void initData() {
		needPermission(100,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});


	}
	@PermissionSuccess(requestCode = 100)
	public void doSomething(){
		DeBug.d(getClass().getSimpleName(),"doSomething.........");
//		loadDataAsyncTaskUtil.execute("");

		mPersenter.getMusicOrderAscByTitle(new BaseObserver<List<MusicInfo>>(){
			@Override
			public void onComplete() {
				super.onComplete();
				bindData();
			}
		},true);

	}
	@PermissionSuccess(requestCode = 200)
	public void camera(){
		DebugLog.d("拍照");
		userHeaderImg = PhotoUtil
				.takePicture(LocalMusicActivity.this, com.music.utils.PhotoUtils.HEADER_PATH,null);

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
		PlayerHelpler.getDefault().init();
		initWidgetData();
		resetPlayState();
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


	}

	private void initWidgetData() {
		if (localMusicFragment == null) {
			localMusicFragment = new LocalMusicFragment();
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.id_frame, localMusicFragment)
				.commitAllowingStateLoss();
		tv_mobile.setText(android.os.Build.MODEL);
	}

	@OnClick({ R.id.iv_back, R.id.btn_next2, R.id.btn_playing2,
			R.id.music_about_layout, R.id.rl_setting, R.id.rl_exit,
			R.id.iv_header, R.id.iv_search })
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
			break;
		case R.id.iv_search:
			startActivity(SearchMusicActivity.class);
			break;
		default:
			break;
		}

	}


	/**
	 * 返回
	 */
	private void back() {
		if (!isHome) {
			transaction = getSupportFragmentManager().beginTransaction();
			transaction.remove(musicListFragment);
			transaction.commit();
			tv_title.setText(R.string.local_music);
			isHome = true;
			musicListFragment=null;
		} else {
//			slidingMenu.toggle();
//			if (slidingMenu.isOpen()) {
//				iv_back.setImageResource(R.drawable.ic_common_title_bar_forward);
//			} else {
//				iv_back.setImageResource(R.drawable.ic_common_title_bar_back);
//			}
		}
	}


	IPlayState state = new MyMainState();

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
		}

		@Override
		public void playMusicState() {
			// playPauseDrawable.animatePause();
			btn_musicPlaying
					.setImageResource(R.drawable.img_button_notification_play_pause);
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
		mp3Util.unBindService();
		finish();
		System.exit(0);

		// onDestroy();
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
