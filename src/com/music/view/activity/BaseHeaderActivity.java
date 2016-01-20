package com.music.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.music.lu.R;

/**
 * 包含头部导航栏的基类
 * @author Administrator
 *
 */
public class BaseHeaderActivity extends BaseActivity{
	
	@ViewInject(value = R.id.iv_search)
	protected ImageView iv_search;
	
	@ViewInject(value = R.id.iv_more)
	protected ImageView iv_more;
	@ViewInject(value = R.id.iv_back)
	protected ImageView iv_back;

	@ViewInject(value = R.id.tv_title)
	protected TextView tv_title;
	
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
//		initWidget();
		
	};
//	protected void initWidget() {
//		iv_search=(ImageView) findViewById(R.id.iv_search);
//		
//	}
	@com.lidroid.xutils.view.annotation.event.OnClick({R.id.iv_back})
	public void OnClick(View view){
		if(view.getId()==R.id.iv_back){
			finish();
		}
	}
}
