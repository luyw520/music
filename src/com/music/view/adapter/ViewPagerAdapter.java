package com.music.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.music.utils.DeBug;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	private static final String TAG = "ViewPagerAdapter";
	List<Fragment> list = new ArrayList<Fragment>();
	public ViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	public Fragment getItem(int arg0) {
		DeBug.d(TAG, "getItem:"+arg0);
		return list.get(arg0);
	}

	public int getCount() {
		return list.size();
	}

}
