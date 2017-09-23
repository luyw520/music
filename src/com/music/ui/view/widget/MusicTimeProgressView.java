package com.music.ui.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MusicTimeProgressView extends View {

	private  int suggestHeight=5;
	private long maxProgress;
	private Paint paint;
	private int viewW;
	private float precent=0;
	public void setMaxProgress(long maxProgress){
		if(maxProgress<0){
			throw new IllegalArgumentException("maxprogress must be >0");
		}
		this.maxProgress=maxProgress;
	}
	public void setCurrentProgress(long progress){
		if(progress<0){
			throw new IllegalArgumentException("progress must be >=0");
		}
		precent=(float)progress/maxProgress;
		postInvalidate();
	}
	public MusicTimeProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint=new Paint();
		paint.setColor(Color.GREEN);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), suggestHeight);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		viewW=w;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		int right=(int) (viewW*precent);
//		DeBug.d(this, "right:"+right+",suggestHeight:"+suggestHeight);
		canvas.drawRect(0, 0, right, suggestHeight, paint);
	}
}
