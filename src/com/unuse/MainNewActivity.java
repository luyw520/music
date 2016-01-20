package com.unuse;

import android.annotation.SuppressLint;

import com.lidroid.xutils.view.annotation.ContentView;
import com.music.lu.R;
import com.music.view.activity.BaseActivity;
@SuppressLint("NewApi")
@ContentView(value = R.layout.activity_main)
public class MainNewActivity extends BaseActivity {/*

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

	*//** 圆形头像类 *//*
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

//	private RemoteViews remoteViews;

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

	private Mp3Util mp3Util;
	private MusicListItemClickListener musicListItemClickListener;
	*//** 接收后台服务发来的歌曲播放状态 *//*
	private MyBroadcastReceiver myBroadcastReceiver;


	private ListenNetStateReceiver listenNetStateReceiver;

	*//**
	 * 弹出窗体
	 *//*

	private PopupWindowUtil popupWindowUtil;

	@ViewInject(value = R.id.tv_username)
	private TextView tv_username;

	@SuppressWarnings("unused")
	private boolean isLogin = false;

	private String userHeaderImg;
	
	private String headerImgPath="/sdcard/lu/img/head.jpg";
	private MyNotification myNotification;
	@SuppressLint("HandlerLeak") private Handler handler=new Handler(){
		
		@SuppressWarnings("static-access")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DownloadUtil.DOWNLOAD_SUCCESS:
//				tv_username.setText(username);
				PhotoUtils photoUtils=new PhotoUtils();
				Bitmap bitmap=photoUtils.getBitmapFromFile(headerImgPath);
				Log.i(TAG, "bitmap="+bitmap);
				iv_header.setImageBitmap(bitmap);
				break;
			case DownloadUtil.DOWNLOAD_FAIL:
				Log.i(TAG, "下载头像失败");
				break;
			default:
				break;
			}
			
		};
	};
	*//**
	 * 接收消息栏发来的消息
	 *//*
	private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String acitonString = intent.getAction();
			if (ConstantUtil.NOTIFICATION_PLAY_PAUSE.equals(acitonString)) {
				playMusic();
			} else if (ConstantUtil.NOTIFICATION_NEXT.equals(acitonString)) {
				nextMusic();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		Context context=getApplicationContext();
		XGPushManager.registerPush(context);
		Intent intent=new Intent(context,XGPushService.class);
		context.startService(intent);
		XGPushManager.registerPush(this);
		
		musicListItemClickListener = new MusicListItemClickListener();

		mp3Util = Mp3Util.getInstance(this);
		mp3Util.setPlaying(false);

		popupWindowUtil = new PopupWindowUtil(this);
		popupWindowUtil
				.setPopupWindowUIOnClickListener(popupWindowUIOnClickListener);
		initViewWidget();
		registerReceiver();
		EventBus.getDefault().register(this);
		myNotification=new MyNotification(this, mp3Util.getCurrentMp3Info().getTitle());
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

	*//**
	 * 应用程序退出
	 *//*
	public void exit() {

		Intent intent = new Intent(MainNewActivity.this, MyPlayerService.class);
		unregisterReceiver();
		stopService(intent);
		
		ApplicationUtil.setAppToBack(this, 1);
		finish();
	};

	public void finish() {
		super.finish();
		myNotification.close();
	}

	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (mp3Util.isPlaying()) {
			btn_musicPlaying
					.setBackgroundResource(R.drawable.img_button_notification_play_pause);
		} else {
			btn_musicPlaying
					.setBackgroundResource(R.drawable.img_button_notification_play_play);
		}
		myNotification.setPlayImageState(mp3Util.isPlaying());
		
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
		

	}

	*//**
	 * 初始化view上面的控件
	 *//*
	private void initViewWidget() {

		tv_mobile.setText(android.os.Build.MODEL);

		tv_music_title.setText(mp3Util.getCurrentMp3Info().getTitle());
		tv_music_Artist.setText(mp3Util.getCurrentMp3Info().getArtist());

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

//		slidingMenu
//				.setBackground(new BitmapDrawable(BgBean.getUsedBitmap(this)));

	}

	*//**
	 * 根据播放类型发送下一首歌曲播放服务
	 *//*
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
//			if (!isLogin) {
			
			
			if(!UserManager.isLogin()){
				DialogUtil.showToast(getApplicationContext(), "您还未登陆,请先登陆");
				Intent intent2 = new Intent(MainNewActivity.this,
						LoginActivity.class);
				startActivityForResult(intent2, REQUESTCODE_LOGIN);
			}else{
				chooseHeaderImgDialog();
			}
				
				
//			tencentLogin();
//			shareSDKLogin();
//			
			break;
		case R.id.rl_setting:
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.rl_exit:
			exit();
			break;
		case R.id.iv_search:
			searchMusic();
			break;
		case R.id.iv_back:
			slidingMenu.toggle();
			if (slidingMenu.isOpen()) {
				iv_back.setBackgroundResource(R.drawable.ic_common_title_bar_forward);
			} else {
				iv_back.setBackgroundResource(R.drawable.ic_common_title_bar_back);
			}
			break;
		case R.id.iv_more:
			popupWindowUtil.showWindow(v);
			break;
		case R.id.music_about_layout:
			Intent intent = new Intent(MainNewActivity.this, PlayerActivity.class);
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
	*//**
	 * 利用shareSDKAPI登陆
	 *//*
	@SuppressWarnings("unused")
	private void shareSDKLogin(){
		ShareSDK.initSDK(this);
		Platform platform=ShareSDK.getPlatform(QZone.NAME);
		platform.SSOSetting(false);
		platform.setPlatformActionListener(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				Log.i(TAG, "onError");
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				Platform platform=ShareSDK.getPlatform(MainNewActivity.this,QZone.NAME);
				
				String username=platform.getDb().getUserName();
				String id=platform.getDb().getUserId();
				String img=platform.getDb().getUserIcon();  
				
				
				DownloadUtil downloadUtil=new DownloadUtil(img, headerImgPath, handler);
				downloadUtil.download();
				Log.i(TAG, "id="+id+",name="+username+",img="+img);
				Log.i(TAG, "onComplete");
				
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				Log.i(TAG, "onCancel");
				
			}
		});
		platform.showUser(null);
	}
	*//**
	 * 利用腾讯API登陆
	 *//*
	@SuppressWarnings("unused")
	private void tencentLogin() {
		String appId="1104335219";
		final Tencent tencent=Tencent.createInstance(appId, getApplicationContext());
		final String Scope="get_simple_userinfo";
//				final String Scope="get_user_info";
//				if(!tencent.isSessionValid()){
			
			
			tencent.login(this, Scope, new IUiListener(){

				@Override
				public void onCancel() {
					
				}

				@Override
				public void onComplete(Object arg0) {
					
					LogUtil.i(MainNewActivity.this.getClass(),arg0.toString());
					UserInfo userInfo=new UserInfo(MainNewActivity.this, tencent.getQQToken());
////							LogUtil.i(MainActivity.this.getClass(),"userInfo="+userInfo.toString());
					userInfo.getUserInfo(new IUiListener() {
						@Override
						public void onError(UiError arg0) {
							// TODO Auto-generated method stub
							
						}
						@Override
						public void onComplete(Object arg0) {
							LogUtil.i(MainNewActivity.this.getClass(),arg0.toString());
							Message msg=Message.obtain();
							msg.obj=arg0;
							msg.what=0;
							handler.sendMessage(msg);
							
						}
						
						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							
						}
					});
					
				}

				@Override
				public void onError(UiError arg0) {
					
				}
				
			})	;
//				}
//			}
	}

	*//**
	 * 搜索音乐
	 *//*
	private void searchMusic() {

		startActivity(new Intent(this, SearchMusicActivity.class));
	}

	*//**
	 * 选择头像
	 *//*
	private void chooseHeaderImgDialog() {
		DialogUtil.showAlertDialog(this, "头像选择", new String[] { "拍照", "本地" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							userHeaderImg = PhotoUtils
									.takePicture(MainNewActivity.this);
							break;
						case 1:
							PhotoUtils.selectPhoto(MainNewActivity.this);
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
		String username = UserManager.getInstance().getUserBean().getUsername();
		String img=UserManager.getInstance().getUserBean().getHeadPath();
		
		tv_username.setText(username);
		*//**
		 * 是否本地登陆,true,本地登陆.false，第三方登陆
		 *//*
		
		DownloadUtil downloadUtil=new DownloadUtil(img, headerImgPath, handler);
		downloadUtil.download();
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

	*//**
	 * 指定所有音乐中索引位置的音乐发送播放服务
	 * 
	 * @param listPosition
	 *            要播放音乐的索引位置
	 *//*
	private void playMusic(int listPosition) {
		mp3Util.playMusic(listPosition);
//		System.out.println("发送事件....listPosition"+listPosition);
//		EventBus.getDefault().post(listPosition, TagUtil.TAG_PLAY_BY_INDEX);
	}

	class MusicListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			playMusic(position);
		}

	}

	*//**
	 * 当前播放状体类
	 * 
	 * 
	 *//*
	class MyMainState implements State {

		@Override
		public void currentState(Intent intent) {
			// Log.i(TAG, "currentState()");
			int currentTime = intent.getIntExtra("currentTime", -1);

			mp3Util.setCurrentTime(currentTime);
			tv_music_CurrentTime.setText(MediaUtil.formatTime(currentTime));
		}

		@Override
		public void duration(Intent intent) {
			Log.i(TAG, "duration()");
			tv_music_Artist.setText(mp3Util.getCurrentMp3Info().getArtist());
			tv_music_title.setText(mp3Util.getCurrentMp3Info().getTitle());
		}

		@Override
		public void nextMusic() {
			MainNewActivity.this.nextMusic();

		}


		@Override
		public void pauseMusicState() {
			// Log.i(TAG, "pauseMusicState()");
			btn_musicPlaying
					.setBackgroundResource(R.drawable.img_button_notification_play_play);
			mp3Util.setPlaying(false);
			myNotification.setPlayImageState(false);
		}

		@Override
		public void playMusicState() {
			btn_musicPlaying
					.setBackgroundResource(R.drawable.img_button_notification_play_pause);

			Bitmap bitmap = MediaUtil.getArtwork(getApplicationContext(),
					mp3Util.getCurrentMp3Info().getId(), mp3Util
							.getCurrentMp3Info().getAlbumId(), true, true);
			iv_music_album.setImageBitmap(bitmap);

			// playAnimation();

			tv_music_Artist.setText(mp3Util.getCurrentMp3Info().getArtist());
			tv_music_title.setText(mp3Util.getCurrentMp3Info().getTitle());

			myNotification.reset(bitmap);

		}

		@SuppressWarnings("deprecation")
		@Override
		public void changedBg() {
			slidingMenu.setBackground(new BitmapDrawable(BgBean
					.getCurrentBitmap()));

		}

	}

	@SuppressWarnings("unused")
	private void playAnimation() {
		Animation operatingAnim = AnimationUtils
				.loadAnimation(this, R.anim.tip);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			iv_music_album.startAnimation(operatingAnim);
		}
	}

	*//**
	 * 注册广播接收器
	 *//*
	private void registerReceiver() {

		regisgerPlayStateReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(ConstantUtil.NOTIFICATION_PLAY_PAUSE);
		filter2.addAction(ConstantUtil.NOTIFICATION_NEXT);
		registerReceiver(notificationReceiver, filter2);

		// 注册广播

		registenerScreenStateReceiver();
		registenerListeNetStateReceiver();
		
		
	}

	public void unregisterReceiver() {
		unregisterReceiver(myBroadcastReceiver);
		unregisterReceiver(notificationReceiver);
		unregisterReceiver(lockScreenReceiver);
		unregisterReceiver(listenNetStateReceiver);
	}

	*//**
	 * 注册屏幕状态接收器
	 *//*
	private void registenerScreenStateReceiver() {
		IntentFilter mScreenOffFilter = new IntentFilter(
				Intent.ACTION_SCREEN_OFF);
		lockScreenReceiver = new LockScreenReceiver();
		registerReceiver(lockScreenReceiver, mScreenOffFilter);
	}

	*//**
	 * 注册歌曲播放状态接收器
	 *//*
	private void regisgerPlayStateReceiver() {
		State state = new MyMainState();
		IntentFilter filter = new IntentFilter();
		myBroadcastReceiver = new MyBroadcastReceiver(state, filter);
		registerReceiver(myBroadcastReceiver, filter);
	}

	*//**
	 * 注册网络状态监听广播接收器
	 *//*
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
		com.wwj.sb.utils.Util.showExitAlertDialog(this,
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
			if (!slidingMenu.isOpen()) {
				iv_back.setBackgroundResource(R.drawable.ic_common_title_bar_forward);
			} else {
				iv_back.setBackgroundResource(R.drawable.ic_common_title_bar_back);
			}

		}
		return super.onTouchEvent(event);
	}

*/}
