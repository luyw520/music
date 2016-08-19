package com.music.view.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.music.utils.ApplicationUtil;
import com.music.utils.ConstantUtil;
import com.music.utils.DeBug;
import com.music.utils.ScreenShotUtil;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity implements ConstantUtil{
	
//	protected SystemBarTintManager mTintManager;
	@SuppressLint("ResourceAsColor")
	@TargetApi(19) @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		//Í¸Ã÷×´Ì¬À¸  
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
//      //Í¸Ã÷µ¼º½À¸  
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		mTintManager=new SystemBarTintManager(this);
//		mTintManager.setStatusBarTintEnabled(true);
//		mTintManager.setNavigationBarTintEnabled(true);
//		int color = Color.argb(153, 123, 123,123);
//		mTintManager.setTintColor(color);
//		StatusBarCompat.compat(this);
//		
        
        
	}
	
	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(this,clazz));
	}
	protected void changeFont(ViewGroup root){
		Typeface tf=Typeface.createFromAsset(getAssets(), "fonts/a.ttf");
		for(int i=0,count=root.getChildCount();i<count;i++){
			View v=root.getChildAt(i);
			if(v instanceof TextView){
				DeBug.d(this, v.toString());
				((TextView)v).setTypeface(tf);
			}else if(v instanceof ViewGroup){
				changeFont((ViewGroup)v);
			}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
//		ViewGroup root=(ViewGroup) this.getWindow().getDecorView();
//		long start=System.currentTimeMillis();
//		changeFont(root);
//		DeBug.d(this, "changeFont spend time :"+(System.currentTimeMillis()-start)/1000.0+" s");
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
