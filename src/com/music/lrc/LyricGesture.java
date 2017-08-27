package com.music.lrc;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class LyricGesture implements OnTouchListener, OnGestureListener {
//	private MainActivity context;
	private GestureDetector mGestureDetector;
	private int way;
	private boolean starttoggle;
	private boolean updatetoggle;

//	public LyricGesture(MainActivity context) {
//		this.context = context;
//		mGestureDetector = new GestureDetector(context, this);
//	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		//ACTION_UP
		if (arg1.getAction() == 1) {
			way = 0;
			if (updatetoggle) {
//				context.updateplayer();
				updatetoggle = false;
			}
		}
		return mGestureDetector.onTouchEvent(arg1);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// starttoggle = true;
		return true;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {

		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent arg0) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

}
