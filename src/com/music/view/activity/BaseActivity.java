package com.music.view.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.music.lu.R;
import com.music.utils.ApplicationUtil;
import com.music.utils.ConstantUtil;
import com.music.utils.DeBug;
import com.music.utils.ScreenShotUtil;
import com.music.utils.SystemBarTintManager;
import com.music.view.animator.ActivityAnimator;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity implements ConstantUtil {

	protected SystemBarTintManager mTintManager;

	@SuppressLint("ResourceAsColor")
	@TargetApi(19)
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

		// int color = Color.argb(153, 123, 123,123);
		// mTintManager.setTintColor(color);
		// StatusBarCompat.compat(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

//		mTintManager = new SystemBarTintManager(this);
//		mTintManager.setStatusBarTintEnabled(true);
//		mTintManager.setNavigationBarTintEnabled(true);
//		mTintManager.setTintColor(getResources().getColor(R.color.header_bg));
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(this, clazz));
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
	protected void startActivityWithAnimator(Class<?> clazz) {
		startActivity(new Intent(this, clazz));
		startActivityAnimator();
	}
	protected void startActivityAnimator(){
		ActivityAnimator activityAnimator=new ActivityAnimator();
		try {
			activityAnimator.getClass().getMethod(activityAnimator.randomAnimator(), Activity.class).invoke(activityAnimator, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onResume(this);
//		if (ApplicationUtil.getYaoYiYao(this)) {
//			ScreenShotUtil.getInstance().registerShakeToScrShot(this);
//		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickAgent.onPause(this);
	}
}
