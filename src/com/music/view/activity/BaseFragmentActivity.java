package com.music.view.activity;

import com.lidroid.xutils.ViewUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class BaseFragmentActivity extends FragmentActivity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			ViewUtils.inject(this);
		}
		
		protected void startActivity(Class<?> class1) {
			startActivity(new Intent(this,class1));
		}
}
