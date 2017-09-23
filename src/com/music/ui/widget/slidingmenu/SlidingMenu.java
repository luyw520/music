package com.music.ui.widget.slidingmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.music.lu.R;
import com.music.utils.DeBug;
import com.nineoldandroids.view.ViewHelper;
/**
 * 
 *从左向右滚动的抽屉菜单 
 */
public class SlidingMenu extends HorizontalScrollView {
	private static final String TAG = "SlidingMenu";
	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	/**
	 * dp
	 */
	private int mMenuRightPadding;
	/**
	 */
	private int mMenuWidth;
	private int mHalfMenuWidth;

	private boolean isOpen;

	public boolean isOpen() {
		return isOpen;
	}

	private boolean once;

	private ViewGroup mMenu;
	private ViewGroup mContent;
	
	@SuppressWarnings("unused")
	private int x;
	
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				// 默认50
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));//
				break;
			}
		}
		a.recycle();
	}

	public SlidingMenu(Context context) {
		this(context, null, 0);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
//		return super.onInterceptTouchEvent(ev);
//		MyLog.d(TAG, "LocalMusicActivity.isFirst");
		int x=(int) ev.getX();
//		System.out.println("x="+x);
//		DeBug.d(TAG, "onInterceptTouchEvent...............x:"+x);
//		if(x<100 || isOpen){
//			return super.onInterceptTouchEvent(ev);
//		}
		if(isOpen||x<200){
			return super.onInterceptTouchEvent(ev);
		}
		return false;
		
//		if(LocalMusicActivity.isFirst){
			
//		}
//		return false;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 */
		if (!once) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) wrapper.getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(1);

			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐
		case MotionEvent.ACTION_DOWN:
			x=(int) ev.getX();
//			y=(int) ev.getY();
//			System.out.println("x="+x);
			break;
//		case MotionEvent.ACTION_MOVE:
////			System.out.println("move");
//			x=(int) ev.getX();
//			
//			System.out.println("x="+x);
////			if(x<100){
////				return false;
////			}
////			
//			return true;
//			break;
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
			if (scrollX > mHalfMenuWidth) {
				this.smoothScrollTo(mMenuWidth, 0);
				DeBug.d(TAG, "smoothScrollTo(mMenuWidth, 0)");
				isOpen=false;
				
			} else {
				this.smoothScrollTo(0, 0);
				DeBug.d(TAG, "smoothScrollTo(0, 0)");
				isOpen=true;
			}
			DeBug.d(TAG, "ontouch isOpen="+isOpen);
			 return false;
		}
//		return false;
		return super.onTouchEvent(ev);
	}

	/**
	 * 打开菜单
	 */
	public void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu() {
		if (isOpen) {
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}
	}

	/**
	 * 切换菜单状
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;
		float leftScale = 1 - 0.3f * scale;
		float rightScale = 0.8f + scale * 0.2f;

		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);

	}

}
