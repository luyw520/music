package com.music.ui.view.widget;

import com.music.lu.R;
import com.music.ui.activity.LockScreenActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SliderRelativeLayout extends RelativeLayout {
	private final static String TAG = "SliderRelativeLayout";
	private Context context;
	private Bitmap dragBitmap = null;
	private int locationX = 0; //
	private ImageView heartView = null; //
	private ImageView leftRingView = null;
	private ImageView rightRingView = null;
	private Handler handler = null; //
	private static int BACK_DURATION = 10 ;   //  20ms
	private static float VE_HORIZONTAL = 0.9f ;  //

	public SliderRelativeLayout(Context context) {
		super(context);
		SliderRelativeLayout.this.context = context;
		intiDragBitmap();
	}

	public SliderRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		SliderRelativeLayout.this.context = context;
		intiDragBitmap();
	}

	public SliderRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		SliderRelativeLayout.this.context = context;
		intiDragBitmap();
	}

	/**
	 */
	private void intiDragBitmap() {
		if(dragBitmap == null){
			dragBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.love);
		}
	}

	/**
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		heartView = (ImageView) findViewById(R.id.loveView);
		leftRingView = (ImageView) findViewById(R.id.leftRing);
		rightRingView = (ImageView) findViewById(R.id.rightRing);
	}

	/**
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int X = (int) event.getX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			locationX = (int) event.getX();
			Log.i(TAG, "0=" + isActionDown(event));
			return isActionDown(event);

		case MotionEvent.ACTION_MOVE:
			locationX = X;
			invalidate();
			return true;

		case MotionEvent.ACTION_UP:
			if(!isLocked()){
				handleActionUpEvent(event);
			}
			return true;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * @param event
	 */
	private void handleActionUpEvent(MotionEvent event) {
		int x = (int) event.getX();
		int toLeft = leftRingView.getWidth();

		locationX = x - toLeft;
		if(locationX >= 0){
			handler.postDelayed(ImageBack, BACK_DURATION);
		}
	}

	/**
	 */
	private Runnable ImageBack = new Runnable() {
		@Override
		public void run() {
			locationX = locationX - (int) (VE_HORIZONTAL*BACK_DURATION);
			if(locationX >= 0){
				handler.postDelayed(ImageBack, BACK_DURATION);
				invalidate();
			}
		}
	};

	/**
	 * @param event
	 * @return
	 */
	private boolean isActionDown(MotionEvent event) {
		Rect rect = new Rect();
		heartView.getHitRect(rect);
		boolean isIn = rect.contains((int)event.getX()-heartView.getWidth(), (int)event.getY());
		if(isIn){
			heartView.setVisibility(View.GONE);
			return true;
		}
		return false;
	}

	/**
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		invalidateDragImg(canvas);
	}

	/**
	 * @param canvas
	 */
	private void invalidateDragImg(Canvas canvas) {
		int drawX = locationX - dragBitmap.getWidth();
		int drawY = heartView.getTop();

		if(drawX < leftRingView.getWidth()){
			return;
		} else {
			if(isLocked()){
				return;
			}
			heartView.setVisibility(View.GONE);
			canvas.drawBitmap(dragBitmap, drawX < 0 ? 5 : drawX,drawY,null);
		}
	}

	/**
	 */
	private boolean isLocked(){
		if(locationX > (getScreenWidth() - rightRingView.getWidth())){
			handler.obtainMessage(LockScreenActivity.MSG_LOCK_SUCESS).sendToTarget();
			return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	private int getScreenWidth(){
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		return width;
	}

	/**
	 * @param handler
	 */
	public void setMainHandler(Handler handler){
		this.handler = handler;
	}
}
