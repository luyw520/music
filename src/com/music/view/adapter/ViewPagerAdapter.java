package com.music.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.music.utils.DeBug;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

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
//	@Override
//
//	public Object instantiateItem(ViewGroup container,int position) {
//
//	   //得到缓存的fragment
//
//	    Fragment fragment = (Fragment)super.instantiateItem(container,
//
//	           position);
//
//	   //得到tag
//
//	    String fragmentTag = fragment.getTag();         
//
//	 
//
//	   if (fragmentsUpdateFlag[position %fragmentsUpdateFlag.length]) {
//
//	      //如果这个fragment需要更新
//
//	      
//
//	       FragmentTransaction ft =fm.beginTransaction();
//
//	      //移除旧的fragment
//
//	       ft.remove(fragment);
//
//	      //换成新的fragment
//
//	       fragment =fragments[position %fragments.length];
//
//	      //添加新fragment时必须用前面获得的tag
//
//	       ft.add(container.getId(), fragment, fragmentTag);
//
//	       ft.attach(fragment);
//
//	       ft.commit();
//
//	      
//
//	      //复位更新标志
//
//	      fragmentsUpdateFlag[position %fragmentsUpdateFlag.length] =false;
//
//	    }
//
//	 
//
//	   return fragment;
//
//	}
}
