package com.music.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 
 *
 */
public class CustomScrollView extends ScrollView {

	private View inner;//

	private float y;//

	private Rect normal = new Rect();//

	private boolean isCount = false;//

	private boolean isMoveing = false;//

//	private View View;

	@SuppressWarnings("unused")
	private int initTop, initbottom;//
	@SuppressWarnings("unused")
	private int top, bottom;//
	
//	private int 

//	public void setView(View View) {
//		this.View = View;
//	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner != null) {
			commOnTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	/***
	 * �����¼�
	 * 
	 * @param ev
	 */
	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
//			top = initTop = View.getTop();
//			bottom = initbottom = View.getBottom();
			top = initTop = inner.getTop();
			bottom = initbottom = inner.getBottom();
			break;

		case MotionEvent.ACTION_UP:

			isMoveing = false;
			// ��ָ�ɿ�.
			if (isNeedAnimation()) {

				animation();

			}
			break;
		/***
		 */
		case MotionEvent.ACTION_MOVE:

			final float preY = y;//

			float nowY = ev.getY();//
			int deltaY = (int) (nowY - preY);//
			if (!isCount) {
				deltaY = 0; //
			}

//			if (deltaY < 0 && top <= initTop)
//				return;
//			if (top <= initTop)
//				return;

			isNeedMove();
			System.out.println("isMoveing="+isMoveing);
			if (isMoveing) {
				if (normal.isEmpty()) {
					normal.set(inner.getLeft(), inner.getTop(),
							inner.getRight(), inner.getBottom());
				}

				inner.layout(inner.getLeft(), inner.getTop() + deltaY / 3,
						inner.getRight(), inner.getBottom() + deltaY / 3);

				top += (deltaY / 6);
				bottom += (deltaY / 6);
//				View.layout(View.getLeft(), top,
//						View.getRight(), bottom);
			}

			isCount = true;
			y = nowY;
			break;

		default:
			break;

		}
	}

	/***
	 */
	public void animation() {

//		TranslateAnimation taa = new TranslateAnimation(0, 0, top + 200,
//				initTop + 200);
//		taa.setDuration(200);
//		View.startAnimation(taa);
//		View.layout(View.getLeft(), initTop, View.getRight(),
//				initbottom);

		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
				normal.top);
		ta.setDuration(200);
		inner.startAnimation(ta);
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);
		normal.setEmpty();

		isCount = false;

	}

	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	/***
	 *
	 * @return
	 */
	public void isNeedMove() {
		int offset = inner.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		// Log.e("jj", "scrolly=" + scrollY);
		if (scrollY == 0 || scrollY == offset) {
			isMoveing = true;
		}
	}

}
