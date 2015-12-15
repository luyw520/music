package com.music;

import com.lidroid.xutils.view.annotation.ContentView;
import com.music.lu.R;
@ContentView(value=R.layout.activity_main)
public class Main_ViewPagerActivity extends BaseActivity{/*
	*//**
	 * 底部播放布局的所有控件
	 * 
	 *//*
	class ButtomUI {
		ImageButton btn_musicNext;
		ImageButton btn_musicPlaying;
		ImageView iv_music_album;
		RelativeLayout layout_music_about;
		RelativeLayout rl_btn_next;
		RelativeLayout rl_btn_playing;
		TextView tv_music_Artist;
		TextView tv_music_CurrentTime;

		TextView tv_music_Time;
		TextView tv_music_title;
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
	 * @author Administrator
	 * 
	 *//*
	class MyMainState implements State {

		@Override
		public void currentState(Intent intent) {
			// Log.i(TAG, "currentState()");
			int currentTime = intent.getIntExtra("currentTime", -1);

			mp3Util.setCurrentTime(currentTime);
			buttomUI.tv_music_Time.setText(MediaUtil.formatTime(currentTime));
		}

		@Override
		public void duration(Intent intent) {
			Log.i(TAG, "duration()");
			buttomUI.tv_music_Artist.setText(mp3Util.getCurrentMp3Info()
					.getArtist());
			buttomUI.tv_music_title.setText(mp3Util.getCurrentMp3Info()
					.getTitle());
		}

		@Override
		public void nextMusic() {
			Main_ViewPagerActivity.this.nextMusic();

		}


		@Override
		public void pauseMusicState() {
			// Log.i(TAG, "pauseMusicState()");
			buttomUI.btn_musicPlaying
					.setBackgroundResource(R.drawable.img_button_notification_play_play);

			mp3Util.setPlaying(false);
			remoteViews.setImageViewResource(R.id.iv_play_notification,
					R.drawable.img_button_notification_play_play);
			notificationManager.notify(0, notification);
		}

		@Override
		public void playMusicState() {
			// Log.i(TAG, "playMusicState()");
			buttomUI.btn_musicPlaying
					.setBackgroundResource(R.drawable.img_button_notification_play_pause);

			Bitmap bitmap = MediaUtil.getArtwork(getApplicationContext(),
					mp3Util.getCurrentMp3Info().getId(), mp3Util
							.getCurrentMp3Info().getAlbumId(), true, true);

			buttomUI.iv_music_album.setImageBitmap(bitmap);
			buttomUI.tv_music_Artist.setText(mp3Util.getCurrentMp3Info()
					.getArtist());
			buttomUI.tv_music_title.setText(mp3Util.getCurrentMp3Info()
					.getTitle());

			remoteViews.setImageViewResource(R.id.iv_play_notification,
					R.drawable.img_button_notification_play_pause);

			remoteViews.setImageViewBitmap(R.id.iv_notification, bitmap);
			remoteViews.setTextViewText(R.id.tv_title_notification, mp3Util
					.getCurrentMp3Info().getTitle());
			notificationManager.notify(0, notification);

		}

		@Override
		public void changedBg() {
			// TODO Auto-generated method stub
			
		}

	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_nextPage:
				setCurrentPage(viewPager.getCurrentItem() + 1);
				break;
			case R.id.rl_more:
				setCurrentPage(viewPager.getCurrentItem() - 1);
				break;
			case R.id.rl_listmusic:
				setCurrentPage(viewPager.getCurrentItem() + 1);
				break;
			case R.id.music_about_layout:

				Intent intent = new Intent(Main_ViewPagerActivity.this,
						PlayerActivity.class);
				intent.putExtra("disapperTopLeftAnimation", "disappearTopLeft");
				startActivity(intent);

				break;
			case R.id.iv_more:
				break;
			case R.id.btn_next:
				nextMusic();
				break;
			case R.id.btn_playing:
				playMusic();
				break;
			case R.id.tv_random:
				mp3Util.randomPlay();
				break;
			default:
				break;
			}

		}

	}

	class PopupWindowUI {
		ImageView iv_on_music;
		ImageView iv_on_time;
		LinearLayout ll_menu_folder;
		LinearLayout ll_menu_match;
		LinearLayout ll_menu_on_music;
		LinearLayout ll_menu_on_time;

		LinearLayout ll_menu_scan;
		LinearLayout ll_menu_singer;
	}

	private static final String TAG = "MainActivity";
	private Button btn_exit;
	private ButtomUI buttomUI;

	private GridView gridView;
	private GridView gridView_middle;
	private GridView gridViewMyLike;
	private ImageView img_nextPage;
	private ImageView iv_back;
	*//** 圆形头像类 *//*
	private CircularImage iv_header;
	private ImageView iv_more;
	private ImageView iv_nextPage;
	private MusicListAdapter listAdapter;
	private LockScreenReceiver lockScreenReceiver;
	final int Menu_Exit = Menu.FIRST + 1;
	final int Menu_Set = Menu.FIRST;
	private IndexableListView mMusiclist; // 音乐列表
	private Mp3Util mp3Util;

	private MusicListItemClickListener musicListItemClickListener;
	*//** 接收后台服务发来的歌曲播放状态 *//*
	private MyBroadcastReceiver myBroadcastReceiver;
	private MyGridViewAdapter myGridViewAdapter;

	private MyGridViewAdapter myGridViewAdapterMiddle;

	private MyGridViewAdapter myGridViewAdapterMyLike;
	private MyOnClickListener myOnClickListener;
	private Notification notification;
	private NotificationManager notificationManager;

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

	*//**
	 * 弹出窗体
	 *//*
	private PopupWindow popupWindow;

	PopupWindowUI popupWindowUI;

	private OnClickListener popupWindowUIOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_menu_singer:
				toast("按歌手查看");
				break;
			case R.id.ll_menu_folder:
				toast("按文件夹查看");
				break;
			case R.id.ll_menu_scan:
				toast("扫描本地歌曲");
				break;
			case R.id.ll_menu_match:
				toast("一键匹配词图");
				break;
			case R.id.ll_menu_on_time:
				toast("按添加时间排序");
				popupWindowUI.iv_on_time.setVisibility(View.VISIBLE);
				popupWindowUI.iv_on_music.setVisibility(View.INVISIBLE);

				mp3Util.sortMp3InfosByTime();
				listAdapter.notifyDataSetChanged();
				break;
			case R.id.ll_menu_on_music:
				toast("按歌曲名称排序");
				popupWindowUI.iv_on_music.setVisibility(View.VISIBLE);
				popupWindowUI.iv_on_time.setVisibility(View.INVISIBLE);
				mp3Util.sortMp3InfosByTitle();
				listAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	};

	private RemoteViews remoteViews;
	private RelativeLayout rl_more, rl_listmusic;

	private TextView tv_random;

	private TextView tv_songcount;

	private View view1, view2, view3;

	private JazzyViewPager viewPager;

	private List<View> views;
	private void bindService() {
		Intent intent = new Intent(this, LockScreenService.class);
		startService(intent);
	}

	*//**
	 * 应用程序退出
	 *//*
	public void exit() {
		finish();
		Intent intent = new Intent(Main_ViewPagerActivity.this, MyPlayerService.class);
		unregisterReceiver();
		stopService(intent);
	};

	@Override
	public void finish() {
		notificationManager.cancel(0);
		super.finish();
	}

	public ViewPager getViewPager() {
		return viewPager;
	}

	*//**
	 * 初始化通知栏
	 *//*
	@SuppressWarnings("deprecation")
	private void initNotification() {
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notification = new Notification(R.drawable.playing_bar_default_avatar,
				mp3Util.getCurrentMp3Info().getTitle(),
				System.currentTimeMillis());

		remoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_layout);
		remoteViews.setTextViewText(R.id.tv_title_notification, mp3Util
				.getCurrentMp3Info().getTitle());
		Intent intent = new Intent(ConstantUtil.NOTIFICATION_PLAY_PAUSE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.iv_play_notification,
				pendingIntent);

		Intent intent2 = new Intent(ConstantUtil.NOTIFICATION_NEXT);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 1,
				intent2, 1);
		remoteViews.setOnClickPendingIntent(R.id.iv_next_notification,
				pendingIntent2);

		Intent intent3 = new Intent(this, Main_ViewPagerActivity.class);
		intent3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent3, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.contentIntent = contentIntent;
		notification.contentView = remoteViews;
		notificationManager.notify(0, notification);

	}

	private void initOnListener() {

		buttomUI.btn_musicNext.setOnClickListener(myOnClickListener);
		buttomUI.btn_musicPlaying.setOnClickListener(myOnClickListener);

	}

	*//**
	 * 初始化弹出窗体的控件
	 * 
	 * @param view
	 *//*
	private void initPopupWindowUI(View view) {
		popupWindowUI = new PopupWindowUI();
		popupWindowUI.ll_menu_singer = (LinearLayout) view
				.findViewById(R.id.ll_menu_singer);

		popupWindowUI.ll_menu_folder = (LinearLayout) view
				.findViewById(R.id.ll_menu_folder);
		popupWindowUI.ll_menu_scan = (LinearLayout) view
				.findViewById(R.id.ll_menu_scan);
		popupWindowUI.ll_menu_match = (LinearLayout) view
				.findViewById(R.id.ll_menu_match);
		popupWindowUI.ll_menu_on_time = (LinearLayout) view
				.findViewById(R.id.ll_menu_on_time);
		popupWindowUI.ll_menu_on_music = (LinearLayout) view
				.findViewById(R.id.ll_menu_on_music);
		popupWindowUI.iv_on_time = (ImageView) view
				.findViewById(R.id.iv_on_time);
		popupWindowUI.iv_on_music = (ImageView) view
				.findViewById(R.id.iv_on_music);

		popupWindowUI.ll_menu_singer
				.setOnClickListener(popupWindowUIOnClickListener);
		popupWindowUI.ll_menu_folder
				.setOnClickListener(popupWindowUIOnClickListener);
		popupWindowUI.ll_menu_scan
				.setOnClickListener(popupWindowUIOnClickListener);
		popupWindowUI.ll_menu_match
				.setOnClickListener(popupWindowUIOnClickListener);
		popupWindowUI.ll_menu_on_time
				.setOnClickListener(popupWindowUIOnClickListener);
		popupWindowUI.ll_menu_on_music
				.setOnClickListener(popupWindowUIOnClickListener);
	}

	*//**
	 * 初始化view1上面的控件
	 *//*
	private void initView1Widget() {
		img_nextPage = (ImageView) view1.findViewById(R.id.img_nextPage);
		img_nextPage.setOnClickListener(myOnClickListener);

		buttomUI = new ButtomUI();
		btn_exit = (Button) view1.findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);

		buttomUI.tv_music_title = (TextView) findViewById(R.id.tv_music_title);

		buttomUI.tv_music_Time = (TextView) findViewById(R.id.tv_music_time);
		buttomUI.tv_music_CurrentTime = (TextView) findViewById(R.id.tv_music_currenttime);
		buttomUI.tv_music_Artist = (TextView) findViewById(R.id.tv_music_Artist);
		buttomUI.btn_musicPlaying = (ImageButton) findViewById(R.id.btn_playing);
		buttomUI.btn_musicNext = (ImageButton) findViewById(R.id.btn_next);
		buttomUI.iv_music_album = (ImageView) findViewById(R.id.iv_music_album);
		buttomUI.layout_music_about = (RelativeLayout) findViewById(R.id.music_about_layout);
		buttomUI.rl_btn_next = (RelativeLayout) findViewById(R.id.rl_btn_next);
		buttomUI.rl_btn_playing = (RelativeLayout) findViewById(R.id.rl_btn_playing);

		buttomUI.layout_music_about.setOnClickListener(myOnClickListener);

		buttomUI.tv_music_title.setText(mp3Util.getCurrentMp3Info().getTitle());
		buttomUI.tv_music_Artist.setText(mp3Util.getCurrentMp3Info()
				.getArtist());

		int[] imgs = { R.drawable.ic_navigation_more_monthlytraffic,
				R.drawable.ic_navigation_more_musichunter,
				R.drawable.ic_navigation_more_scantransfer,
				R.drawable.ic_navigation_more_musicalarm,
				R.drawable.ic_navigation_more_equalizer,
				R.drawable.ic_navigation_more_settings, };
		final String[] textString = { "包流量畅听", "听歌识曲", "扫描和传歌", "定时退出",
				"音效(普通)", "设置", };

		myGridViewAdapter = new MyGridViewAdapter(this, imgs, textString,
				R.layout.item_grideview_layout);
		gridView = (GridView) view1.findViewById(R.id.gridview);
		gridView.setAdapter(myGridViewAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toast(textString[position]);
			}
		});
		int[] imgs2 = { R.drawable.ic_navigation_more_crbt,
				R.drawable.ic_navigation_more_kg_ringtone,
				R.drawable.ic_navigation_more_kg_fm,
				R.drawable.ic_navigation_more_kg_fx, };
		final String[] textString2 = { "彩铃", "铃声", "收音机", "K歌女神" };
		myGridViewAdapterMiddle = new MyGridViewAdapter(this, imgs2,
				textString2, R.layout.gridviewmiddle_item_layout);
		gridView_middle = (GridView) view1.findViewById(R.id.gridview_middle);
		gridView_middle.setAdapter(myGridViewAdapterMiddle);
		gridView_middle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toast(textString2[position]);
			}
		});

	}

	*//**
	 * 初始化view2的控件
	 *//*
	private void initView2Widget() {
		iv_nextPage = (ImageView) view2.findViewById(R.id.iv_nextPage);
		iv_nextPage.setOnClickListener(this);

		iv_header = (CircularImage) view2.findViewById(R.id.iv_header);
		iv_header.setImageResource(R.drawable.header);

		rl_more = (RelativeLayout) view2.findViewById(R.id.rl_more);
		rl_more.setOnClickListener(myOnClickListener);
		rl_listmusic = (RelativeLayout) view2.findViewById(R.id.rl_listmusic);
		rl_listmusic.setOnClickListener(myOnClickListener);

		tv_songcount = (TextView) view2.findViewById(R.id.tv_songcount);
		tv_songcount.setText("共" + mp3Util.getAllMp3Size() + "首歌");

		int[] mylike = {
				R.drawable.btn_navigation_localentry_fav_cloud_default,
				R.drawable.btn_navigation_localentry_playlist_cloud_default,
				R.drawable.btn_navigation_localentry_download_default,
				R.drawable.btn_navigation_localentry_history_default, };
		final String[] mylikeStrings = { "我喜欢", "我的歌单", "下载管理", "最近播放" };
		myGridViewAdapterMyLike = new MyGridViewAdapter(this, mylike,
				mylikeStrings, R.layout.item_grideview_layout);
		gridViewMyLike = (GridView) view2.findViewById(R.id.gridview_mylike);
		gridViewMyLike.setAdapter(myGridViewAdapterMyLike);

		gridViewMyLike.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toast(mylikeStrings[position]);
			}
		});

	}

	*//**
	 * 初始化view3的控件
	 *//*
	private void initView3Widget() {
		iv_back = (ImageView) view3.findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_random = (TextView) view3.findViewById(R.id.tv_random);
		tv_random.setOnClickListener(myOnClickListener);

		mMusiclist = (IndexableListView) view3.findViewById(R.id.listview);
		mMusiclist.setFastScrollEnabled(true);
//		sidrbar = (SideBar) view3.findViewById(R.id.sidrbar);
//		
//		dialog = (TextView) findViewById(R.id.dialog);
//		sidrbar.setTextView(dialog);
		
		mMusiclist.setOnItemClickListener(musicListItemClickListener);

		listAdapter = new MusicListAdapter(this,
				R.layout.item_listview__layout, mp3Util.getMp3Infos(),mMusiclist);

//		// 设置右侧触摸监听
//		sidrbar.setOnTouchingLetterChangedListener(new com.music.widget.sortlistview.SideBar.OnTouchingLetterChangedListener() {
//
//			@Override
//			public void onTouchingLetterChanged(String s) {
//				// 该字母首次出现的位置
//				int position = listAdapter.getPositionForSection(s.charAt(0));
//				if (position != -1) {
//					mMusiclist.setSelection(position);
//				}
//
//			}
//		});

		iv_more = (ImageView) view3.findViewById(R.id.iv_more);
		iv_more.setOnClickListener(this);
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
	}

	*//**
	 * 初始化viewPager
	 *//*
	private void initViewPager() {
		viewPager = (JazzyViewPager) findViewById(R.id.viewpager);
		viewPager.setTransitionEffect(TransitionEffect.Tablet);
		views = new ArrayList<View>();
		LayoutInflater layoutInflater = getLayoutInflater();
		view1 = layoutInflater.inflate(R.layout.activity_main_pag1, null);
		view2 = layoutInflater.inflate(R.layout.activity_main_pag2, null);
		view3 = layoutInflater.inflate(R.layout.activity_listmusicactivity,
				null);

		views.add(view1);
		views.add(view2);
		views.add(view3);
		PagerAdapter pagerAdapter = new MyPagerAdapter(views, viewPager);
		viewPager.setAdapter(pagerAdapter);

	}

	*//**
	 * 根据播放类型发送下一首歌曲播放服务
	 *//*
	private void nextMusic() {
		mp3Util.nextMusic();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_more:
			showWindow(v);
			break;
		case R.id.btn_exit:
			exit();
			break;
		case R.id.iv_back:
			setCurrentPage(viewPager.getCurrentItem() - 1);
			break;
		case R.id.iv_nextPage:
			setCurrentPage(viewPager.getCurrentItem() + 1);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main_viewpager);
		musicListItemClickListener = new MusicListItemClickListener();
		myOnClickListener = new MyOnClickListener();

		mp3Util = Mp3Util.getInstance(this);
		mp3Util.setPlaying(false);

		initViewPager();
		initView1Widget();
		initView2Widget();
		initView3Widget();

		initOnListener();
		registerReceiver();
		initNotification();
		bindService();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			showExitDialog();

		}
		return super.onKeyDown(keyCode, event);
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
	}

	*//**
	 * 注册广播接收器
	 *//*
	private void registerReceiver() {
		State state = new MyMainState();
		IntentFilter filter = new IntentFilter();
		myBroadcastReceiver = new MyBroadcastReceiver(state, filter);
		// 注册BroadcastReceiver
		registerReceiver(myBroadcastReceiver, filter);

		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(ConstantUtil.NOTIFICATION_PLAY_PAUSE);
		filter2.addAction(ConstantUtil.NOTIFICATION_NEXT);
		registerReceiver(notificationReceiver, filter2);

		// 注册广播
		IntentFilter mScreenOffFilter = new IntentFilter(
				Intent.ACTION_SCREEN_OFF);
		lockScreenReceiver = new LockScreenReceiver();
		registerReceiver(lockScreenReceiver, mScreenOffFilter);
	}

	*//**
	 * 设置viewPager的当前项
	 * 
	 * @param currentPage
	 *//*
	public void setCurrentPage(int currentPage) {

		if (currentPage > views.size()) {
			currentPage = views.size();
		}
		if (currentPage < 0) {
			currentPage = 0;
		}
		viewPager.setCurrentItem(currentPage);
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

	*//**
	 * 显示弹出窗体
	 * 
	 * @param parent
	 *            在该控件下显示弹出窗体
	 *//*
	@SuppressWarnings("deprecation")
	private void showWindow(View parent) {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = getLayoutInflater();
			View view = layoutInflater.inflate(R.layout.popupwindow, null);
			initPopupWindowUI(view);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击空白的地方关闭PopupWindow
		popupWindow.showAsDropDown(parent);

	}

	private void toast(String message) {
		Util.showToast(this, message);
	}

	public void unregisterReceiver() {
		unregisterReceiver(myBroadcastReceiver);
		unregisterReceiver(notificationReceiver);
		unregisterReceiver(lockScreenReceiver);
	}
*/}
