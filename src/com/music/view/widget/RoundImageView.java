package com.music.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.music.lu.R;
import com.music.utils.DeBug;

/**
 * 
 * @author Alan
 */
public class RoundImageView extends ImageView {
	private int mBorderThickness = 0;
	private Context mContext;
	private int defaultColor = 0xFFFFFFFF;
	private int mBorderOutsideColor = 0;
	private int mBorderInsideColor = 0;
	private int defaultWidth = 0;
	private int defaultHeight = 0;
	
	private Paint circleBorderPaint,roundBitmapPaint;
	public RoundImageView(Context context) {
		super(context);
		mContext = context;
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setCustomAttributes(attrs);
		init();
	}

	private void init() {
		circleBorderPaint = new Paint();
		circleBorderPaint.setAntiAlias(true);
		circleBorderPaint.setFilterBitmap(true);
		circleBorderPaint.setDither(true);
		circleBorderPaint.setStyle(Paint.Style.STROKE);
		circleBorderPaint.setStrokeWidth(mBorderThickness);
		
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		DeBug.d(this, "onMeasure............");
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		defaultWidth=getMeasuredWidth();
		defaultHeight=getMeasuredHeight();
		radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - 2 * mBorderThickness;
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		DeBug.d(this, "onSizeChanged............");
		defaultWidth=w;
		defaultHeight=h;
		
		radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - 2 * mBorderThickness;
	}
	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setCustomAttributes(attrs);
		init();
	}

	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.roundedimageview);
		mBorderThickness = a.getDimensionPixelSize(R.styleable.roundedimageview_border_thickness, 0);
		mBorderOutsideColor = a.getColor(R.styleable.roundedimageview_border_outside_color, defaultColor);
		mBorderInsideColor = a.getColor(R.styleable.roundedimageview_border_inside_color, defaultColor);
		a.recycle();
	}
	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
//		super.setImageBitmap(bm);
		
		roundBitmap = getCroppedRoundBitmap(bm, radius);
		invalidate();
	}
	
	Bitmap roundBitmap;
	int radius = 0;
	@Override
	protected void onDraw(Canvas canvas) {
//		DeBug.d(this, "onDraw.....................");
//		Drawable drawable = getDrawable();
//		if (drawable == null) {
//			return;
//		}
//		if (drawable.getClass() == NinePatchDrawable.class)
//			return;
//		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
//		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
		
		if (mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {// 
			radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - 2 * mBorderThickness;
			drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
			drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor);
		} else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {// 
			radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;
			drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
		} else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {// 
			radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;
			drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor);
		} else {//
			radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2;
		}
		
		if(roundBitmap!=null){
			canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight / 2 - radius, null);
		}
		
	}

	/**
	 * 
	 * @param radius
	 */
	public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;
		DeBug.d(this, "getCroppedRoundBitmap,diameter:"+diameter);
		if(radius==0){
			return null;
		}
		
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth) {
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
		} else if (bmpHeight < bmpWidth) {
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
		} else {
			squareBitmap = bmp;
		}
		if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true);
		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		roundBitmapPaint=new Paint();
		roundBitmapPaint.setAntiAlias(true);
		roundBitmapPaint.setFilterBitmap(true);
		roundBitmapPaint.setDither(true);
		
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2, roundBitmapPaint);
		roundBitmapPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, roundBitmapPaint);
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}

	private void drawCircleBorder(Canvas canvas, int radius, int color) {
		circleBorderPaint.setColor(color);
		canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, circleBorderPaint);
	}

}
