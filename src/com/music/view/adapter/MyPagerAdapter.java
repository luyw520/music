package com.music.view.adapter;

import java.util.List;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter{
	List<View> views;
	JazzyViewPager viewPager;
	public MyPagerAdapter(List<View> views,JazzyViewPager viewPager){
		this.views=views;
		this.viewPager=viewPager;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position));
		viewPager.setObjectForPosition(views.get(position), position);
		return views.get(position);
	}
	


}
