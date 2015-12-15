package com.music;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.activity.gesturepassword.GresturePasswordSetActivity;
import com.music.activity.gesturepassword.GuideGesturePasswordActivity;
import com.music.lu.R;
import com.music.lu.utils.ApplicationUtil;
import com.music.lu.utils.BitmapCacheUtil;
import com.music.lu.utils.DialogUtil;
import com.music.lu.utils.LogUtil;
import com.music.lu.utils.Mp3Util;
import com.music.lu.utils.ScreenShotUtil;
import com.music.lu.utils.SensorManagerUtil;
import com.music.lu.utils.SensorManagerUtil.SensorChangedListener;
import com.music.popupwindow.PopupWindowQieGe;
import com.music.popupwindow.PopupWindowQieGe.PopupWindowUIOnClickListener;

@ContentView(value = R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

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

	@ViewInject(value = R.id.cb_automatic_down_lrc)
	private CheckBox cb_automatic_down_lrc;
	@ViewInject(value = R.id.cb_dlna)
	private CheckBox cb_dlna;
	@ViewInject(value = R.id.cb_qiege)
	private CheckBox cb_qiege;
	@ViewInject(value = R.id.cb_listener_down)
	private CheckBox cb_listener_down;

	private PopupWindowQieGe popupWindowQieGe;

	@ViewInject(value = R.id.ll_parent)
	private View ll_parent;
	
	private SensorManagerUtil sensorManagerUtil;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		
		popupWindowQieGe = new PopupWindowQieGe(this);
		popupWindowQieGe.setPopupWindowUIOnClickListener(popupWindowUIOnClickListener);
		sensorManagerUtil=SensorManagerUtil.getInstance(this);
		sensorManagerUtil.setS(sensorChangedListener);
		initWidget();

	};

	private void initWidget() {
		iv_more.setVisibility(View.GONE);
		iv_search.setVisibility(View.GONE);
		tv_title.setText("设置");
		
		tv_cache.setText(BitmapCacheUtil.getDefalut().formatFileSize());
		
		cb_qiege.setChecked(ApplicationUtil.getYaoYiYao(this));
		
		if(ApplicationUtil.getYaoYiYao(this)){
			ScreenShotUtil.getInstance(this).registerShakeToScrShot();
		}
		
	}
	public void onCancel(View view){
		popupWindowQieGe.dismiss();
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		switch (ApplicationUtil.getAppLockState(this)) {
		case 0:
			tv_lockpasswordstate.setText("未设置");
			break;
		case 1:
			tv_lockpasswordstate.setText("已开启");
			break;
		case 2:
			tv_lockpasswordstate.setText("已关闭");
			break;
			

		default:
			break;
		}

		setQieGeState(ApplicationUtil.getQieGeIndex(this));
		
		
		
	}
	
	private void setQieGeState(int index) {
		LogUtil.log(this.getClass(), "index="+index);
		sensorManagerUtil.registerListener();
		switch (index) {
		case 0:
			tv_yaoyiyao.setText("不开启");
			sensorManagerUtil.unRegisterListener();
			break;
		case 1:
			tv_yaoyiyao.setText("温柔甩");
			sensorManagerUtil.setValue(14);
			break;
		case 2:
			tv_yaoyiyao.setText("普通甩");
			sensorManagerUtil.setValue(17);
			break;
		case 3:
			tv_yaoyiyao.setText("用力甩");
			sensorManagerUtil.setValue(20);
			break;

		default:
			break;
		}
	}
	
	
	@OnClick({ R.id.iv_back, R.id.rl_down, R.id.rl_clear_memory,
			R.id.rl_refresh, R.id.rl_about_software, R.id.rl_trylistener,
			R.id.rl_automatic_down_lrc, R.id.rl_listener_down,
			R.id.rl_yaoyiyao, R.id.rl_qiege, R.id.rl_lockpasswordstate,
			R.id.rl_dlna,R.id.rl_scan })
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.rl_down:
			break;
		case R.id.rl_clear_memory:
			DialogUtil.showWaitDialog(this,"缓存清理","清理缓存中...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			DialogUtil.closeAlertDialog();
			tv_cache.setText("0M");
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
			break;
		case R.id.rl_listener_down:
			cb_listener_down.setChecked(!cb_listener_down.isChecked());
			break;
		case R.id.rl_yaoyiyao:
			popupWindowQieGe.showWindow(ll_parent);
			break;
		case R.id.rl_qiege:
			cb_qiege.setChecked(!cb_qiege.isChecked());
			if(cb_qiege.isChecked()){
				ScreenShotUtil.getInstance(this).registerShakeToScrShot();
				ApplicationUtil.setYaoYiYao(this, true);
			}else{
				ScreenShotUtil.getInstance(this).unregisterShakeListener();
				ApplicationUtil.setYaoYiYao(this, false);
			}
			break;
		case R.id.rl_lockpasswordstate:
			if (tv_lockpasswordstate.getText().equals("未设置")) {
				startActivity(new Intent(this,
						GuideGesturePasswordActivity.class));
			} else {
				startActivity(new Intent(this,
						GresturePasswordSetActivity.class));
			}
			break;
		case R.id.rl_dlna:
			cb_dlna.setChecked(!cb_dlna.isChecked());
			break;
		case R.id.rl_scan:
			scan();
			break;
		default:
			break;
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
			
			Mp3Util mp3Util=Mp3Util.getDefault();
			mp3Util.nextMusic();
			LogUtil.i(SettingActivity.this.getClass(), "onSensorChanged");
		}
	};
	
}
