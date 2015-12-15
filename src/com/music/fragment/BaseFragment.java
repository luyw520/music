package com.music.fragment;


import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.lidroid.xutils.ViewUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		ViewUtils.inject(this);
	}
	
}
