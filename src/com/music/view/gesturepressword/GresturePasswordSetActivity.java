package com.music.view.gesturepressword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.lu.R;
import com.music.utils.ApplicationUtil;
import com.music.view.activity.BaseHeaderActivity;

@ContentView(value = R.layout.activity_gesturepassword_set)
public class GresturePasswordSetActivity extends BaseHeaderActivity {

	@ViewInject(value = R.id.cb_lock)
	private CheckBox cb_lock;

	@ViewInject(value = R.id.rl_updatelockpassword)
	private RelativeLayout rl_updatelockpassword;
	
	@ViewInject(value = R.id.iv_search)
	protected ImageView iv_search;
	
	@ViewInject(value = R.id.iv_more)
	protected ImageView iv_more;
	@ViewInject(value = R.id.iv_back)
	protected ImageView iv_back;

	@ViewInject(value = R.id.tv_title)
	protected TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

//		initWidget();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		initWidget();
	}
	protected void initWidget() {
//		super.initWidget();
		iv_search.setVisibility(View.GONE);
		iv_more.setVisibility(View.GONE);
		tv_title.setText(" ÷ ∆√‹¬Î…Ë÷√");
		cb_lock.setChecked(ApplicationUtil.getAppLockState(this)==1?true:false);
		
		rl_updatelockpassword.setVisibility(cb_lock.isChecked()?View.VISIBLE:View.INVISIBLE);
		
		cb_lock.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				rl_updatelockpassword.setVisibility(isChecked?View.VISIBLE:View.INVISIBLE);
				ApplicationUtil.setAppLock(GresturePasswordSetActivity.this, isChecked?1:2);
				ApplicationUtil.setAppToBack(GresturePasswordSetActivity.this, 0);
				
			}
		});
	}

	@OnClick({ R.id.rl_lock, R.id.rl_updatelockpassword,R.id.iv_back })
	public void myOnclik(View view) {
		switch (view.getId()) {
		case R.id.rl_lock:
			cb_lock.setChecked(!cb_lock.isChecked());
			rl_updatelockpassword.setVisibility(cb_lock.isChecked()?View.VISIBLE:View.INVISIBLE);
			ApplicationUtil.setAppLock(this, cb_lock.isChecked()?1:2);
			ApplicationUtil.setAppToBack(this, 0);
			break;
		case R.id.rl_updatelockpassword:
			startActivity(new Intent(this,CreateGesturePasswordActivity.class));
			break;
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}
}
