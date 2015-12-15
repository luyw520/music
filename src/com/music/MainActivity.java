package com.music;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.activity.gesturepassword.UnlockGesturePasswordActivity;
import com.music.adapter.MusicListAdapter;
import com.music.bean.Mp3Info;
import com.music.bean.UserManager;
import com.music.broadcastreceiver.ListenNetStateReceiver;
import com.music.broadcastreceiver.MyBroadcastReceiver;
import com.music.broadcastreceiver.NetState;
import com.music.broadcastreceiver.State;
import com.music.circluarimage.CircularImage;
import com.music.circluarimage.RoundImageView;
import com.music.lockscreen.LockScreenReceiver;
import com.music.lockscreen.LockScreenService;
import com.music.lu.R;
import com.music.lu.utils.ApplicationUtil;
import com.music.lu.utils.AsyncTaskUtil;
import com.music.lu.utils.AsyncTaskUtil.IAsyncTaskCallBack;
import com.music.lu.utils.BitmapCacheUtil;
import com.music.lu.utils.DialogUtil;
import com.music.lu.utils.LogUtil;
import com.music.lu.utils.MediaUtil;
import com.music.lu.utils.Mp3Util_New;
import com.music.lu.utils.PhotoUtils;
import com.music.lu.utils.ScreenShotUtil;
import com.music.popupwindow.PopupWindowUtil;
import com.music.widget.indexablelistview.IndexableListView;
import com.music.widget.sortlistview.SideBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
@SuppressLint("NewApi")
@ContentView(value = R.layout.activity_main)
public class MainActivity extends BaseActivity {

	private static final String TAG = "MainActivity";

	public static final int REQUESTCODE_LOGIN = 11;
	public static final int REQUESTCODE_HEADER = 0;


	@ViewInject(value = R.id.btn_next2)
	private ImageButton btn_musicNext;

	@ViewInject(value = R.id.slidingMenu)
	private com.music.widget.slidingmenu.SlidingMenu slidingMenu;

	@ViewInject(value = R.id.btn_playing2)
	private ImageButton btn_musicPlaying;

	@ViewInject(value = R.id.iv_back)
	private ImageView iv_back;

	@ViewInject(value = R.id.iv_editmode)
	private ImageView iv_editmode;

	/** 圆形头像类 */
	@ViewInject(value = R.id.iv_header)
	private CircularImage iv_header;

	@ViewInject(value = R.id.iv_more)
	private ImageView iv_more;

	@ViewInject(value = R.id.img_state)
	private ImageView img_state;

	@ViewInject(value = R.id.iv_music_album)
	private RoundImageView iv_music_album;

	@ViewInject(value = R.id.iv_search)
	private ImageView iv_search;

	@ViewInject(value = R.id.tv_mobile)
	private TextView tv_mobile;
	


	@ViewInject(value = R.id.sidrbar)
	private SideBar sidrbar;

	@ViewInject(value = R.id.tv_music_Artist)
	private TextView tv_music_Artist;

	@ViewInject(value = R.id.tv_music_time)
	private TextView tv_music_CurrentTime;

	@ViewInject(value = R.id.tv_music_title)
	private TextView tv_music_title;

	@ViewInject(value = R.id.tv_random)
	private TextView tv_random;

	private MusicListAdapter listAdapter;

	private LockScreenReceiver lockScreenReceiver;

	final int Menu_Exit = Menu.FIRST + 1;
	final int Menu_Set = Menu.FIRST;

	@ViewInject(value = R.id.listview)
	private IndexableListView mMusiclist; // 音乐列表

	private Mp3Util_New mp3Util;
	private Mp3Info currentMp3Info;
	private MusicListItemClickListener musicListItemClickListener;
	/** 接收后台服务发来的歌曲播放状态 */
	private MyBroadcastReceiver myBroadcastReceiver;


	private ListenNetStateReceiver listenNetStateReceiver;
	
	private MyNotification myNotification;
	/**
	 * 弹出窗体
	 */

	private PopupWindowUtil popupWindowUtil;

	@ViewInject(value = R.id.tv_username)
	private TextView tv_username;

	@SuppressWarnings("unused")
	private boolean isLogin = false;

	private String userHeaderImg;
	private PushAgent mPushAgent;
	private String mPageName="MainAcitivity";
	
	private PlayPauseDrawable playPauseDrawable;
	private int playColor=0XFFE91E63;
	private int pauseColor=0XFFffffff;
	private int dwableDuaration=300;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		musicListItemClickListener = new MusicListItemClickListener();
		
		mp3Util = Mp3Util_New.getDefault();
//		mp3Util.setPlaying(false);

		popupWindowUtil = new PopupWindowUtil(this);
		popupWindowUtil
				.setPopupWindowUIOnClickListener(popupWindowUIOnClickListener);
		
		playPauseDrawable=new PlayPauseDrawable(30, playColor, pauseColor, dwableDuaration);
		initViewWidget();
		registerReceiver();
		myNotification=new MyNotification(this);
		
	
		
		initUmeng();
		
		handler.sendEmptyMessageDelayed(0, 500);
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			playPauseDrawable.toggle();
		};
	};

	private void initUmeng() {
		// TODO Auto-generated method stub
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		
		MobclickAgent.setDebugMode(true);
//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.setAutoLocation(true);
//		MobclickAgent.setSessionContinueMillis(1000);
		
		MobclickAgent.updateOnlineConfig(this);
	}

	private PopupWindowUtil.PopupWindowUIOnClickListener popupWindowUIOnClickListener = new PopupWindowUtil.PopupWindowUIOnClickListener() {
		@Override
		public void sortByTime() {
			mp3Util.sortMp3InfosByTime();
			listAdapter.notifyDataSetChanged();

		}

		@Override
		public void sortByName() {
			mp3Util.sortMp3InfosByTitle();
			listAdapter.notifyDataSetChanged();

		}
	};

	
	@SuppressWarnings("unused")
	private void bindService() {
		Intent intent = new Intent(this, LockScreenService.class);
		startService(intent);
	}

	/**
	 * 应用程序退出
	 */
	public void exit() {

//		Intent intent = new Intent(MainActivity.this, MyPlayerService.class);
		unregisterReceiver();
//		stopService(intent);
		ApplicationUtil.setAppToBack(this, 1);
		mp3Util.saveCurrentMusicInfo(this);
		myNotification.cancel();
//		mp3Util.serviceDestory();
		mp3Util.unBindService();
		finish();
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		
		myNotification.setPlayImageState(mp3Util.isPlaying());
		
//		playPauseDrawable.toggle();
//		if(mp3Util.isPlaying()){
//			playPauseDrawable.animatePause();
//		}else{
//			playPauseDrawable.animatePlay();
//		}
		initPlayState();
		if(ApplicationUtil.getYaoYiYao(this)){
			ScreenShotUtil.getInstance(this).registerShakeToScrShot();
		}
		
		
		// 如果开启手势密码锁
		
		Log.i(TAG, "ApplicationUtil.getAppLockState(this)="+ApplicationUtil.getAppLockState(this));
		Log.i(TAG, "ApplicationUtil.getAppToBack(this)="+ApplicationUtil.getAppToBack(this));
		
		if(ApplicationUtil.getAppLockState(this) == 1){
			if (ApplicationUtil.getAppToBack(this)==1) {
				Log.i(TAG, "Intent..............");
				Intent intent=new Intent(this, UnlockGesturePasswordActivity.class);
//				intent.putExtra("flag", 1);
				startActivity(intent);
			}
			
		}
		
		listAdapter.notifyDataSetChanged();
		
		
		MobclickAgent.onPageStart( mPageName );
		MobclickAgent.onResume(this);
	}

	/**
	 * 初始化view上面的控件
	 */
	private void initViewWidget() {
		tv_mobile.setText(android.os.Build.MODEL);
		mMusiclist.setFastScrollEnabled(true);
		mMusiclist.setOnItemClickListener(musicListItemClickListener);
		
		listAdapter = new MusicListAdapter(this,
				R.layout.item_listview__layout, mp3Util.getMp3Infos(),
				mMusiclist);

		LinearLayout listViewFoodView = new LinearLayout(
				getApplicationContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.topMargin = 15;
		params.bottomMargin = 200;
		params.leftMargin = 100;
		TextView textView = new TextView(getApplicationContext());
		textView.setGravity(Gravity.CENTER);
		textView.setText("共" + mp3Util.getAllMp3Size() + "首歌曲");

		listViewFoodView.addView(textView, params);

		mMusiclist.addFooterView(listViewFoodView);

		mMusiclist.setAdapter(listAdapter);
		
		btn_musicPlaying.setImageDrawable(playPauseDrawable);
//		
	}
	private void initPlayState(){
		
		
//		currentMp3Info=mp3Util.getCurrentMp3Info();
		tv_music_title.setText(currentMp3Info.getTitle());
		tv_music_Artist.setText(currentMp3Info.getArtist());
		
		Bitmap bmp=MediaUtil.getArtwork(getApplicationContext(),
				currentMp3Info.getId(), currentMp3Info.getAlbumId(), true, true);
		iv_music_album.setImageBitmap(bmp);
		
		
//		if (mp3Util.isPlaying()) {
//			btn_musicPlaying
//					.setBackgroundResource(R.drawable.img_button_notification_play_pause);
//		} else {
//			btn_musicPlaying
//					.setBackgroundResource(R.drawable.img_button_notification_play_play);
//
//		}
	}
	/**
	 * 根据播放类型发送下一首歌曲播放服务
	 */
	private void nextMusic() {
		mp3Util.nextMusic();
	}

	@OnClick({ R.id.iv_more, R.id.music_about_layout, R.id.tv_random,
			R.id.iv_back, R.id.iv_header, R.id.rl_setting, R.id.rl_exit,
			R.id.iv_search,R.id.iv_editmode})
	public void onClickListener(View v) {
		LogUtil.i(this.getClass(), "click");
		switch (v.getId()) {
		case R.id.iv_header:
			if(!UserManager.isLogin()){
				DialogUtil.showToast(getApplicationContext(), "您还未登陆,请先登陆");
				Intent intent2 = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivityForResult(intent2, REQUESTCODE_LOGIN);
			}else{
				chooseHeaderImgDialog();
			}
			break;
		case R.id.rl_setting:
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.rl_exit:
			showExitDialog();
			break;
		case R.id.iv_search:
			searchMusic();
			break;
		case R.id.iv_back:
			slidingMenu.toggle();
			if (slidingMenu.isOpen()) {
				iv_back.setImageResource(R.drawable.ic_common_title_bar_forward);
			} else {
				iv_back.setImageResource(R.drawable.ic_common_title_bar_back);
			}
			break;
		case R.id.iv_more:
			popupWindowUtil.showWindow(v);
			break;
		case R.id.music_about_layout:
			Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
			intent.putExtra("disapperTopLeftAnimation", "disappearTopLeft");
			startActivity(intent);
			break;
		case R.id.tv_random:
			mp3Util.randomPlay();
			break;
		case R.id.iv_editmode:
			startActivity(new Intent(this,SearchMusicActivity.class));
			break;
		default:
			break;
		}

	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( mPageName );
		MobclickAgent.onPause(this);
	}

	/**
	 * 搜索音乐
	 */
	private void searchMusic() {

		startActivity(new Intent(this, SearchMusicActivity.class));
	}

	/**
	 * 选择头像
	 */
	private void chooseHeaderImgDialog() {
		DialogUtil.showAlertDialog(this, "头像选择", new String[] { "拍照", "本地" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							userHeaderImg = PhotoUtils
									.takePicture(MainActivity.this);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.i(getClass(), "resultCode=" + resultCode);

		switch (requestCode) {
		case REQUESTCODE_LOGIN:
			loginResult(data);
			break;

		default:
			chooseHeaderImg(requestCode, resultCode, data);
			break;
		}

	}
	
	private void loginResult(Intent data) {

		if (data == null) {
			DialogUtil.showToast(getApplicationContext(), "登陆失败");
			return;
		}
		DialogUtil.showToast(getApplicationContext(), "登录成功");
		String username = UserManager.getInstance().getUserBean().getUsername();
		tv_username.setText(username);
		String userHeader=UserManager.getInstance().getUserBean().getHeadPath();
		
		downUserHeader(userHeader);
	}

	@SuppressWarnings("deprecation")
	private void chooseHeaderImg(int requestCode, int resultCode, Intent data) {
		if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_ALBUM) {
			if (data == null)
				return;

			if (resultCode == RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
			}
			Uri uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			if (cursor != null) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
					String path = cursor.getString(column_index);
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (PhotoUtils.bitmapIsLarge(bitmap)) {
						PhotoUtils.cropPhoto(this, this, path);
					} else {
						iv_header.setImageBitmap(bitmap);
					}
				}
			}
			//拍照
		} else if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CAMERA) {

			if (resultCode == RESULT_OK) {

				String path = userHeaderImg;
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				if (PhotoUtils.bitmapIsLarge(bitmap)) {
					PhotoUtils.cropPhoto(this, this, path);
				} else {
					iv_header.setImageBitmap(bitmap);
				}
			}

		} else if (requestCode == PhotoUtils.INTENT_REQUEST_CODE_CROP) {

			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (bitmap != null) {
						iv_header.setImageBitmap(bitmap);
					}
				}
			}

		}

	}
	/**
	 * 下载用户头像
	 * @param url
	 */
	private void downUserHeader(final String url){
		

		AsyncTaskUtil asyncTaskUtil=new AsyncTaskUtil(new IAsyncTaskCallBack() {
			
			@Override
			public void onPostExecute(Object result) {
				Bitmap bitmap=(Bitmap) result;
				if(bitmap!=null){
					iv_header.setImageBitmap(bitmap);
				}
			}
			
			@Override
			public Object doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				Bitmap bitmap=BitmapCacheUtil.getDefalut().getCacheBitmap(url);
				return bitmap;
			}
		});
		asyncTaskUtil.execute(url);
	}
	public void nextMusic(View view) {
		nextMusic();
	}

	public void playMusic(View view) {
		playMusic();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.action_exit:
			showExitDialog();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void playMusic() {
		mp3Util.playMusic();
	}

	/**
	 * 指定所有音乐中索引位置的音乐发送播放服务
	 * 
	 * @param listPosition
	 *            要播放音乐的索引位置
	 */
	private void playMusic(int listPosition) {
		mp3Util.playMusic(listPosition);
	}

	class MusicListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			playMusic(position);
		}

	}

	/**
	 * 当前播放状体类
	 * 
	 * 
	 */
	class MyMainState implements State {

		@Override
		public void currentState(Intent intent) {
			int currentTime = intent.getIntExtra("currentTime", -1);
			mp3Util.setCurrentTime(currentTime);
			tv_music_CurrentTime.setText(MediaUtil.formatTime(currentTime));
		}

		@Override
		public void duration(Intent intent) {
			Log.i(TAG, "duration()");
			initPlayState();
			myNotification.reset();
		}


		@Override
		public void pauseMusicState() {
			playPauseDrawable.animatePlay();
			myNotification.setPlayImageState(false);
		}

		@Override
		public void playMusicState() {
			playPauseDrawable.animatePause();
			myNotification.setPlayImageState(true);

		}
	}

	

	/**
	 * 注册广播接收器
	 */
	private void registerReceiver() {

		regisgerPlayStateReceiver();
		registenerScreenStateReceiver();
		registenerListeNetStateReceiver();
		
		
	}

	public void unregisterReceiver() {
		unregisterReceiver(myBroadcastReceiver);
		unregisterReceiver(lockScreenReceiver);
		unregisterReceiver(listenNetStateReceiver);
		myNotification.unregisterReceiver();
	}

	/**
	 * 注册屏幕状态接收器
	 */
	private void registenerScreenStateReceiver() {
		IntentFilter mScreenOffFilter = new IntentFilter(
				Intent.ACTION_SCREEN_OFF);
		lockScreenReceiver = new LockScreenReceiver();
		registerReceiver(lockScreenReceiver, mScreenOffFilter);
	}

	/**
	 * 注册歌曲播放状态接收器
	 */
	private void regisgerPlayStateReceiver() {
		State state = new MyMainState();
		IntentFilter filter = new IntentFilter();
		myBroadcastReceiver = new MyBroadcastReceiver(state, filter);
		registerReceiver(myBroadcastReceiver, filter);
	}

	/**
	 * 注册网络状态监听广播接收器
	 */
	private void registenerListeNetStateReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		listenNetStateReceiver = new ListenNetStateReceiver(intentFilter,
				new NetState() {
					@Override
					public void disconnected() {
						img_state
								.setImageResource(R.drawable.ic_playing_bar_state_local);
					}
					@Override
					public void connected(int type) {
						switch (type) {
						case ConnectivityManager.TYPE_WIFI:
							img_state
									.setImageResource(R.drawable.ic_playing_bar_state_wifi);
							break;
						case 2:
							img_state
									.setImageResource(R.drawable.ic_playing_bar_state_2g);
							break;
						case 3:
							img_state
									.setImageResource(R.drawable.ic_playing_bar_state_3g);
							break;
						default:
							break;
						}
					}
				});
		registerReceiver(listenNetStateReceiver, intentFilter);
	}

	private void showExitDialog() {
		DialogUtil.showExitAlertDialog(this,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						exit();
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			ApplicationUtil.setAppToBack(this,1);
			moveTaskToBack(false);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			LogUtil.i(this.getClass(),
					"slidingMenu.isOpen()=" + slidingMenu.isOpen());
//			if (!slidingMenu.isOpen()) {
//				iv_back.setBackgroundResource(R.drawable.ic_common_title_bar_forward);
//			} else {
//				iv_back.setBackgroundResource(R.drawable.ic_common_title_bar_back);
//			}
			if (!slidingMenu.isOpen()) {
				iv_back.setImageResource(R.drawable.ic_common_title_bar_forward);
			} else {
				iv_back.setImageResource(R.drawable.ic_common_title_bar_back);
			}
		}
		return super.onTouchEvent(event);
	}

}
