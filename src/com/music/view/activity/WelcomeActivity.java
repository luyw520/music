package com.music.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.music.lu.R;
import com.music.utils.ApplicationUtil;
import com.music.view.animator.ActivityAnimator;
import com.music.view.gesturepressword.UnlockGesturePasswordActivity;

public class WelcomeActivity extends Activity{
	
	protected static final String TAG = "WelcomeActivity";
	int[] welcome={R.drawable.welcome_1,R.drawable.welcome_2,R.drawable.welcome_3};
	View[] imageViews ;
	ViewPager viewPager;
	RadioGroup radioGroup;
	RadioButton[] radioButtons;
	RadioButton radioButton1,radioButton2,radioButton3;
	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcomecactivity);
		if(!ApplicationUtil.isFirstRun(this)){
			startMainActivity();
		}else{
			initWidget();
		}
		
		
	}
	private void initWidget() {
		button=(Button) findViewById(R.id.btn_welcome);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startMainActivity();
			}
		});
		viewPager=(ViewPager) findViewById(R.id.vp_welcome);
		
		imageViews=new ImageView[]{new ImageView(this),new ImageView(this),new ImageView(this)};
		
		radioButton1=(RadioButton) findViewById(R.id.rb1);
		radioButton2=(RadioButton) findViewById(R.id.rb2);
		radioButton3=(RadioButton) findViewById(R.id.rb3);
		radioButtons=new RadioButton[]{radioButton1,radioButton2,radioButton3};
		setChecked(0);
		viewPager.setAdapter(new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				return welcome.length;
			}
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				Log.i(TAG, "instantiateItem");
				
				imageViews[position].setBackgroundResource(welcome[position]);
				container.addView(imageViews[position]);
				return imageViews[position];
			}
			

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				Log.i(TAG, "destroyItem");
				container.removeView(imageViews[position]);
			}
			
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				setChecked(arg0);
				if(arg0==(imageViews.length-1)){
					button.setVisibility(View.VISIBLE);
				}else{
					button.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	private void startMainActivity(){
//		Intent intent=new Intent(WelcomeActivity.this,UnlockGesturePasswordActivity.class);
		Intent intent=new Intent(WelcomeActivity.this,LocalMusicActivity.class);
//		Intent intent=new Intent(WelcomeActivity.this,MainContentActivity.class);
//		Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
		startActivity(intent);
		ActivityAnimator activityAnimator=new ActivityAnimator();
		
		try {
			activityAnimator.getClass().getMethod(activityAnimator.randomAnimator(), Activity.class).invoke(activityAnimator, WelcomeActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();
	}
	private void setChecked(int index){
		for(int i=0;i<radioButtons.length;i++){
			if(i==index){
				radioButtons[i].setChecked(true);
			}else{
				radioButtons[i].setChecked(false);
			}
			
		}
	}
}
