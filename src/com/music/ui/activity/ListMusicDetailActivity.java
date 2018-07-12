package com.music.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
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
import com.music.ui.service.IConstants;
import com.music.ui.view.widget.CircularImage;
import com.music.ui.view.widget.MusicTimeProgressView;
import com.music.utils.ApplicationUtil;
import com.music.utils.DeBug;
import com.music.utils.DialogUtil;
import com.music.utils.LogUtil;
import com.music.utils.MediaUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.music.Constant.MUSIC_CURRENT;
import static com.music.Constant.MUSIC_DURATION;
import static com.music.Constant.MUSIC_PAUSE;
import static com.music.utils.PhotoUtils.INTENT_REQUEST_CODE_CAMERA;
import static com.music.utils.PhotoUtils.INTENT_REQUEST_CODE_CROP;

/**
 *歌单界
 */
@ContentView(value = R.layout.activity_local_music_detail)
public class ListMusicDetailActivity extends BaseMVPActivity<LocalMusicPresenter> implements
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
//	@ViewInject(value = R.id.tv_title)
//	private TextView tv_title;

	@ViewInject(value = R.id.tv_music_title)
	private TextView tv_music_title;

	@ViewInject(value = R.id.tv_mobile)
	private TextView tv_mobile;

	@ViewInject(value = R.id.btn_playing2)
	private ImageButton btn_musicPlaying;


	@ViewInject(value = R.id.iv_music_album)
	private ImageView iv_music_album;


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

//	@ViewInject(value = R.id.tv_local_song_size)
//	private TextView tvLocalSongSize;

	/**
	 * 当前播放的音乐
	 */
	private MusicInfo currentMp3Info;
	private PlayerHelpler mp3Util;

	protected String userHeaderImg;


	@ViewInject(R.id.recyclerView)
	private RecyclerView recyclerView;


	private CommonRecyclerViewAdapter<MusicInfo> adapter;
	private List<MusicInfo> musicListList=new ArrayList<>();
	public static final String KEY="KEY";
	public static void startActivityWithParam(Activity activity, String key){
		Intent intent=new Intent(activity,ListMusicDetailActivity.class);
		intent.putExtra(KEY,key);
		activity.startActivity(intent);
	}
	String title;
	@TargetApi(19)
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mp3Util = PlayerHelpler.getDefault();
		title=getIntent().getStringExtra(KEY);
		initListView();
		registerReceiver();
		initData();

		initWidgetData();
		resetPlayState();
//		adapter.addAll(mPersenter.getMusicList());
		adapter.addAll(mPersenter.getMusicByMusicListName(title));
	}


	CommonRecyclerViewAdapter initAdapter(){
		CommonRecyclerViewAdapter adapter=new CommonRecyclerViewAdapter<MusicInfo>(this,R.layout.item_list_song,musicListList) {
			@Override
			protected void convert(final CommonRecyclerViewHolder holder,final MusicInfo musicList, int position) {
				holder.setText(R.id.tvListTitle,musicList.getTitle());
				holder.setText(R.id.tvListSubTitle,musicList.getArtist());
				final View iv_music_album=holder.getView(R.id.ivListImg);
				iv_music_album.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Bitmap bmp = MediaUtil.getMusicImage(getApplicationContext(),
								musicList,
								iv_music_album.getWidth(),iv_music_album.getHeight());
						holder.setImageBitmap(R.id.ivListImg,bmp);
					}
				});

			}
		};
		return adapter;
	}
	private void initListView() {
		adapter=initAdapter();
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
		adapter.setOnItemClickListener(new MultiItemTypeAdapterForRV.OnItemClickListener() {
			@Override
			public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
				mp3Util.setMusicBaseInfos(musicListList);
				mp3Util.playMusic(position);
			}

			@Override
			public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
				return false;
			}
		});
	}


	private void initData() {
		needPermission(100,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
		tv_title.setText(title+"（"+musicListList.size()+")");
		iv_search.setVisibility(View.GONE);
		iv_more.setVisibility(View.GONE);

	}
	@PermissionSuccess(requestCode = 100)
	public void doSomething(){
		DeBug.d(getClass().getSimpleName(),"doSomething.........");

	}
	@PermissionSuccess(requestCode = 200)
	public void camera(){
		DebugLog.d("拍照");
		userHeaderImg = PhotoUtils
				.takePicture(ListMusicDetailActivity.this, com.music.utils.PhotoUtils.HEADER_PATH,null);

	}
	@PermissionFail(requestCode = 100)
	public void doFailSomething(){
		DialogUtil.showToast(this,"需要权限");
	}
	@PermissionFail(requestCode = 200)
	public void doFailCamera(){
		DialogUtil.showToast(this,"需要权限");
	}
	@Override
	protected void onResume() {
		super.onResume();
		adapter.addAll(mPersenter.getMusicByMusicListName(title));
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
		iv_music_album.post(new Runnable() {
			@Override
			public void run() {
				Bitmap bmp = MediaUtil.getMusicImage(getApplicationContext(),
						currentMp3Info,
						iv_music_album.getWidth(),iv_music_album.getHeight());
				iv_music_album.setImageBitmap(bmp);
			}
		});

		DeBug.d(this,
				"currentMp3Info.getDuration():" + currentMp3Info.getDuration());
		musicTimeProgressView.setMaxProgress(currentMp3Info.getDuration());

	}

	private void initWidgetData() {
		tv_mobile.setText(android.os.Build.MODEL);
	}

	@OnClick({ R.id.iv_back, R.id.btn_next2, R.id.btn_playing2,
			R.id.music_about_layout, R.id.rl_setting, R.id.rl_exit,
			R.id.iv_header, R.id.iv_search,R.id.llLocal })
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.btn_next2:
				PlayerHelpler.getDefault().nextMusic(true);
				break;
		case R.id.btn_playing2:
			PlayerHelpler.getDefault().playMusic();
			break;
		case R.id.music_about_layout:
			startActivity(PlayerActivity.class);
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
							PhotoUtils.selectPhoto(ListMusicDetailActivity.this);
							break;
						default:
							break;
						}

						DialogUtil.closeAlertDialog();

					}
				});
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
//			mHandler.removeCallbacks(progressRunnable);
		}

		@Override
		public void playMusicState() {
			// playPauseDrawable.animatePause();
			btn_musicPlaying
					.setImageResource(R.drawable.img_button_notification_play_pause);
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
	}

}
