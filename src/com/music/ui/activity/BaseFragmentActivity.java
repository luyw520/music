package com.music.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.music.annotation.ComputeTimeUtil;
import com.music.lu.R;
import com.music.utils.ApplicationUtil;
import com.music.utils.DeBug;
import com.music.utils.screen.ScreenShotUtil;
import com.music.utils.SystemBarTintManager;
import com.music.MusicApplication;
import com.umeng.analytics.MobclickAgent;

public class BaseFragmentActivity extends FragmentActivity {
	protected SystemBarTintManager tintManager;
	@SuppressLint("ResourceAsColor")
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//		getWindow().addFlags(
//				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		ViewUtils.inject(this);
		ComputeTimeUtil.inject(this);
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);

		tintManager.setTintColor(getResources().getColor(R.color.transparent));
	}
	/**
	 * Sets the entire activity-wide theme.
	 */
	private void setTheme() {
		//Set the UI theme.
		if (MusicApplication.getInstance().getCurrentTheme()==MusicApplication.DARK_THEME) {
			setTheme(R.style.AppTheme);
		} else {
//			setTheme(R.style.AppThemeLight);
		}

	}
	protected void startActivity(Class<?> class1) {
		startActivity(new Intent(this, class1));
	}

	protected void changeFont(ViewGroup root) {
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/a.ttf");
		for (int i = 0, count = root.getChildCount(); i < count; i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				DeBug.d(this, v.toString());
				((TextView) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFont((ViewGroup) v);
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
//		ViewGroup root = (ViewGroup) this.getWindow().getDecorView();
//		changeFont(root);
		if(ApplicationUtil.getYaoYiYao(this)){
			ScreenShotUtil.getInstance().registerShakeToScrShot(this);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
