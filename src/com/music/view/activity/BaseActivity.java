package com.music.view.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.lidroid.xutils.ViewUtils;
import com.music.utils.ConstantUtil;

public class BaseActivity extends Activity implements ConstantUtil{
	
	
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
	
	protected void startActivity(Class<?> clazz) {
		startActivity(new Intent(this,clazz));
	}
}
