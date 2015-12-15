package com.music;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.lidroid.xutils.ViewUtils;

public class BaseActivity extends Activity {
	@TargetApi(19) @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		//Í¸Ã÷×´Ì¬À¸  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
//        //Í¸Ã÷µ¼º½À¸  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(this,clazz));
	}
}
