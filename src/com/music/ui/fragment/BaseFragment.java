package com.music.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.bean.MessageEvent;
import com.music.lu.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class BaseFragment extends Fragment {
	private LayoutInflater inflater;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater=inflater;
		return null;
	}

	@SuppressLint("InflateParams")
	public View getFoodView(String text){
//		LayoutInflater inflater=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(R.layout.item_food, null);
		TextView tv_size=(TextView) view.findViewById(R.id.tv_size);
		tv_size.setText(text);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
	}
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void messageEventBus(MessageEvent event){
	}
	@Override
	public void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
	}
}
