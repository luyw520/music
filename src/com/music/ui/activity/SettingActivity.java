package com.music.ui.activity;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lu.library.util.DebugLog;
import com.music.helpers.PlayerHelpler;
import com.music.lu.R;
import com.music.presenter.ChangeSkinContract;
import com.music.ui.service.MyNotificationListenerService;
import com.music.ui.view.gesturepressword.GresturePasswordSetActivity;
import com.music.ui.view.gesturepressword.GuideGesturePasswordActivity;
import com.music.ui.view.popwindow.PopupWindowQieGe;
import com.music.ui.view.popwindow.PopupWindowQieGe.PopupWindowUIOnClickListener;
import com.music.utils.ApplicationUtil;
import com.music.utils.DialogUtil;
import com.music.utils.LogUtil;
import com.music.utils.SensorManagerUtil;
import com.music.utils.SensorManagerUtil.SensorChangedListener;
import com.music.utils.SharedPreHelper;
import com.music.utils.screen.ScreenShotUtil;

import static com.music.utils.AppConstant.AUTOMATIC_DOWN_LRC;
import static com.music.utils.AppConstant.LISTENER_DOWN;
import static com.music.utils.AppConstant.SCREEN_SHOT;

@ContentView(value = R.layout.activity_setting)
public class SettingActivity extends BaseActivity{

	protected static final String TAG = "SettingActivity";
	@ViewInject(value = R.id.iv_search)



	private ImageView iv_search;

	@ViewInject(value = R.id.iv_more)
	private ImageView iv_more;
	@ViewInject(value = R.id.iv_back)
	private ImageView iv_back;

	@ViewInject(value = R.id.tv_title)
	private TextView tv_title;

	@ViewInject(value = R.id.tv_lockpasswordstate)
	private TextView tv_lockpasswordstate;


	@ViewInject(value = R.id.tv_qiege)
	private TextView tv_yaoyiyao;

	@ViewInject(value = R.id.tv_cache)
	private TextView tv_cache;


	@ViewInject(value = R.id.tv_skin)
	private TextView tv_skin;

	@ViewInject(value = R.id.cb_automatic_down_lrc)
	private CheckBox cb_automatic_down_lrc;
	@ViewInject(value = R.id.cb_dlna)
	private CheckBox cb_dlna;

	@ViewInject(value = R.id.cbScreenShot)
	private CheckBox cbScreenShot;
	@ViewInject(value = R.id.cb_listener_down)
	private CheckBox cb_listener_down;

	private PopupWindowQieGe popupWindowQieGe;

	@ViewInject(value = R.id.ll_parent)
	private View ll_parent;

	private SensorManagerUtil sensorManagerUtil;
	private Handler handle=new Handler();

	private ChangeSkinContract.Presenter mChangeSkinPresenter;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_setting);


		popupWindowQieGe = new PopupWindowQieGe(this);
		popupWindowQieGe.setPopupWindowUIOnClickListener(popupWindowUIOnClickListener);
		sensorManagerUtil=SensorManagerUtil.getInstance(this);
		sensorManagerUtil.setS(sensorChangedListener);
		initWidget();

		if (!isNotificationEnabled()){
			startNotification();
		}else{
			toggleNotificationListenerService();
		}

	};
	private void toggleNotificationListenerService() {
		PackageManager pm = getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName(this, MyNotificationListenerService.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

		pm.setComponentEnabledSetting(new ComponentName(this, MyNotificationListenerService.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

	}
	public void startNotification() {
		startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 1);
	}
	private void initWidget() {
		iv_more.setVisibility(View.GONE);
		iv_search.setVisibility(View.GONE);
		tv_title.setText(R.string.title_setting);

//		tv_cache.setText(BitmapCacheUtil.getDefalut().formatFileSize());

		cbScreenShot.setChecked(ApplicationUtil.getYaoYiYao(this));

		if(ApplicationUtil.getYaoYiYao(this)){
			ScreenShotUtil.getInstance().registerShakeToScrShot(this);
		}


		cb_automatic_down_lrc.setChecked(SharedPreHelper.getBooleanValue(this, AUTOMATIC_DOWN_LRC, false));
		cb_listener_down.setChecked(SharedPreHelper.getBooleanValue(this, LISTENER_DOWN, false));
		cbScreenShot.setChecked(SharedPreHelper.getBooleanValue(this, SCREEN_SHOT, false));
	}
	public void onCancel(View view){
		popupWindowQieGe.dismiss();

	}
	@Override
	protected void onResume() {
		super.onResume();
		switch (ApplicationUtil.getAppLockState(this)) {
		case 0:
			tv_lockpasswordstate.setText("wait");
			break;
		case 1:
			tv_lockpasswordstate.setText("wait");
			break;
		case 2:
			tv_lockpasswordstate.setText("tv_lockpasswordstate");
			break;


		default:
			break;
		}

//		setQieGeState(ApplicationUtil.getQieGeIndex(this));
	}
	/**
	 * 是否打开了通知权限
	 *
	 * @return
	 */
	private boolean isNotificationEnabled() {
		ContentResolver contentResolver = getContentResolver();
		String enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
		DebugLog.d("enabledListeners:" + enabledListeners);
		if (!TextUtils.isEmpty(enabledListeners)) {
			return enabledListeners.contains(MyNotificationListenerService.class.getName());
		} else {
			return false;
		}
	}
	private void setQieGeState(int index) {
		LogUtil.d(this.getClass(), "index="+index);
		sensorManagerUtil.registerListener();
		switch (index) {
		case 0:
			tv_yaoyiyao.setText("wait");
			sensorManagerUtil.unRegisterListener();
			break;
		case 1:
			tv_yaoyiyao.setText("wait˦");
			sensorManagerUtil.setValue(14);
			break;
		case 2:
			tv_yaoyiyao.setText("wait");
			sensorManagerUtil.setValue(17);
			break;
		case 3:
			tv_yaoyiyao.setText("wait");
			sensorManagerUtil.setValue(20);
			break;

		default:
			break;
		}
	}


	@OnClick({ R.id.iv_back, R.id.rl_down, R.id.rl_clear_memory,
			R.id.rl_refresh, R.id.rl_about_software, R.id.rl_trylistener,
			R.id.rl_automatic_down_lrc, R.id.rl_listener_down,
			R.id.rl_yaoyiyao, R.id.rlScreenShot, R.id.rl_lockpasswordstate,
			R.id.rl_dlna,R.id.rl_scan,R.id.rl_change_skin })
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.rl_down:
			break;
		case R.id.rl_clear_memory:
			DialogUtil.showWaitDialog(this,"wait","wait..");
			handle.postDelayed(new Runnable() {
				@Override
				public void run() {
					DialogUtil.closeAlertDialog();
					tv_cache.setText("0M");
				}
			}, 1000);
			break;
		case R.id.rl_refresh:
			break;
		case R.id.rl_about_software:
			break;
		case R.id.rl_trylistener:
			break;
		case R.id.rl_automatic_down_lrc:
			cb_automatic_down_lrc
					.setChecked(!cb_automatic_down_lrc.isChecked());
			SharedPreHelper.setBooleanValue(this, AUTOMATIC_DOWN_LRC, cb_automatic_down_lrc.isChecked());
			DialogUtil.showToast(this, "lll");
			break;
		case R.id.rl_listener_down:
			cb_listener_down.setChecked(!cb_listener_down.isChecked());

			SharedPreHelper.setBooleanValue(this, LISTENER_DOWN, cb_listener_down.isChecked());
			DialogUtil.showToast(this, "lllll");
			break;
		case R.id.rl_yaoyiyao:
			popupWindowQieGe.showWindow(ll_parent);
			break;
		case R.id.rlScreenShot:
			setScreenShot();
			break;
		case R.id.rl_lockpasswordstate:
			setLock();
			break;
		case R.id.rl_dlna:
			cb_dlna.setChecked(!cb_dlna.isChecked());
			break;
		case R.id.rl_scan:
			scan();
			break;
		case R.id.rl_change_skin:
//			changeSkin();
			break;
		default:
			break;
		}

	}

	private void changeSkin() {
		mChangeSkinPresenter.changeSkin();
	}

	private void setLock() {
		if (tv_lockpasswordstate.getText().equals("lll")) {
			startActivity(new Intent(this,
					GuideGesturePasswordActivity.class));
		} else {
			startActivity(new Intent(this,
					GresturePasswordSetActivity.class));
		}
	}

	private void setScreenShot() {
		cbScreenShot.setChecked(!cbScreenShot.isChecked());
		SharedPreHelper.setBooleanValue(this, SCREEN_SHOT, cbScreenShot.isChecked());
		DialogUtil.showToast(this, "msg");
		if(cbScreenShot.isChecked()){
			ScreenShotUtil.getInstance().registerShakeToScrShot(this);
			ApplicationUtil.setYaoYiYao(this, true);
		}else{
			ScreenShotUtil.getInstance().unregisterShakeListener();
			ApplicationUtil.setYaoYiYao(this, false);
		}
	}

	private void scan() {
		startActivity(ScanActivity.class);
	}
	PopupWindowQieGe.PopupWindowUIOnClickListener popupWindowUIOnClickListener=new PopupWindowUIOnClickListener() {

		@Override
		public void onClick(int index) {
			setQieGeState(index);

		}
	};
	SensorManagerUtil.SensorChangedListener sensorChangedListener=new SensorChangedListener() {

		@Override
		public void onSensorChanged() {

			PlayerHelpler mp3Util= PlayerHelpler.getDefault();
			mp3Util.nextMusic(false);
			LogUtil.i(SettingActivity.this.getClass(), "onSensorChanged");
		}
	};

	public void setPresenter(ChangeSkinContract.Presenter presenter) {
		mChangeSkinPresenter=presenter;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void shoChangSkin(Drawable drawable) {
		tv_skin.setBackground(drawable);
	}

	public boolean isActive() {
		return false;
	}
}
