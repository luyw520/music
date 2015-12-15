package com.music.lrc;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LyricView_New extends View {
//	private static final String TAG = "LyricView";
	private Paint mPaint;
	/** 半宽 */
	private float mX;
	private Paint mPathPaint;
	/** 控件的高 */
	private int mY;
	/** 当前歌词持续时间 */
	private int currentDuringtime2;
	/** Y轴中间 */
	private float middleY;//
	private final int DY = 40;// 每一行的间隔

	/** x偏移量 */
	public float driftx;// x偏移量
	/** y偏移量 */
	public float drifty;//
	public int index = 0;
	public float mTouchHistoryY;
	private LyricViewClickListener lyricViewClickListener;

	public void setLyricViewClickListener(LyricViewClickListener l) {
		this.lyricViewClickListener = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (isClick) {
			lyricViewClickListener.lyricViewClick();
		}
		return super.onTouchEvent(event);
	}

	private float drift_r = 0;
	/** 滑动时显示进度 */
	public boolean showprogress;//
	public int temp = 0;
	@SuppressWarnings("unused")
	private Context mContext;

	public LyricView_New(Context context) {
		super(context);

		init(context);
	}

	public LyricView_New(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	public LyricView_New(Context context, AttributeSet attr, int i) {
		super(context, attr, i);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		setFocusable(true);
		// 非高亮部分
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(25);
		mPaint.setColor(Color.WHITE);
		mPaint.setTypeface(Typeface.SERIF);
		// 高亮部分 当前歌词
		mPathPaint = new Paint();
		mPathPaint.setAntiAlias(true);
		mPathPaint.setTextSize(40);
		mPathPaint.setColor(Color.RED);
		mPathPaint.setTypeface(Typeface.SANS_SERIF);

	}

	public List<Map<String, String>> maps;

	public void setMaps(List<Map<String, String>> maps) {
		this.maps = maps;
	}

	/**
	 * 将原来的背景清除
	 */
	public void clear() {
		index = 0;
	}
	private boolean isClick = false;

	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// Log.i("LyricView", "onDraw()方法调用");
		// 滑动相关
		// 显示进度相关
		int j = (int) (-drifty / 40);
		if (temp < j) {
			temp++;
		} else if (temp > j) {
			temp--;
		}

		drift_r = drifty;

		canvas.drawColor(0xEFeffff);
		Paint p = mPaint;
		Paint p2 = mPathPaint;
		p.setTextAlign(Paint.Align.CENTER);

		if (index == -1)
			return;
		p2.setTextAlign(Paint.Align.CENTER);

		isClick = false;
		if (maps == null || maps.size() == 0) {
			isClick = true;
			canvas.drawText("暂无歌词", mX, middleY, p2);
			return;
		}
//		Log.i(TAG, "middleY=" + middleY + ",drift_r=" + drift_r);
//		Log.i(TAG, "x=" + mX + ",y=" + (middleY + drift_r) + ",lrc="
//				+ maps.get(index).get("lrc"));
		// 先画当前行，之后再画他的前面和后面，这样就保持了当前行在中间的位置
		// canvas.drawText(text[index], mX, middleY + drift_r, p2);
		canvas.drawText(maps.get(index).get("lrc"), mX, middleY + drift_r, p2);
		// canvas.drawText(maps.get(index).get("lrc"), mX, middleY , p2);

		// if (showprogress && index + temp < maps.size() - 1) {
		// p2.setTextAlign(Paint.Align.LEFT);
		// if (index + temp >= 0) {
		// canvas.drawText(
		// TimeParseTool.MsecParseTime(maps.get(index + temp).get("time2")), 0,
		// middleY, p2);
		// } else
		// canvas.drawText("00:00", 0, middleY, p2);
		//
		//
		// canvas.drawLine(0, middleY + 1, mX * 2, middleY + 1, p2);
		// }
		float tempY = middleY + drift_r;
		// 画出本句之前的句子
		for (int i = index - 1; i >= 0; i--) {
			// 向上推移
			tempY = tempY - DY;
			if (tempY < 0) {
				break;
			}
			canvas.drawText(maps.get(i).get("lrc"), mX, tempY, p);
			// canvas.drawText(text[i], mX, tempY, p);
		}
		tempY = middleY + drift_r;
		// 画出本句之后的句子
		for (int i = index + 1; i < maps.size(); i++) {
			// 向下推移
			tempY = tempY + DY;
			if (tempY > mY) {
				break;
			}
			canvas.drawText(maps.get(i).get("lrc"), mX, tempY, p);
		}

	}

	/**
	 * @param w
	 *            Current width of this view.
	 * @param h
	 *            Current height of this view.
	 * @param oldw
	 *            Old width of this view.
	 * @param oldh
	 *            Old height of this view.
	 * 
	 */
	protected void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);
		mX = w * 0.5f;// 屏幕中心坐标(转换为float?)
		mY = h;
		middleY = h * 0.5f;
//		Log.i("LyricView", "onSizeChanged方法调用");
	}

	/**
	 * @author younger
	 * @param CurrentPosition
	 *            当前歌词的时间轴
	 * @return drift 可以返回数据（已经废弃）
	 */
	public float updateindex(int CurrentPosition) {
		// Log.i("LyricView", "updateindex()方法调用");
		if (maps != null) {
			if (index < maps.size() - 1) {
				// 歌词数组的序号

//				if (index == 0) {
//					
//				} else {
					
					
					if (CurrentPosition >= Integer.parseInt(maps.get(index + 1)
							.get("time2"))) {

						currentDuringtime2 = Integer.parseInt(maps.get(
								index + 1).get("time2"))
								- Integer
										.parseInt(maps.get(index).get("time2"));
						index++;
						drifty = 0;
						driftx = 0;
					} else if(index==0){
						currentDuringtime2 = Integer.parseInt(maps.get(index).get(
								"time2"));
					}else if(CurrentPosition < Integer.parseInt(maps.get(
							index - 1).get("time2"))) {
						
						for (int i = 0,size= maps.size()-1; i <size; i++) {
							if (CurrentPosition >= Integer.parseInt(maps.get(
									i).get("time2"))&&CurrentPosition<Integer.parseInt(maps.get(
											i+1).get("time2"))) {
								currentDuringtime2 = Integer.parseInt(maps.get(
										i + 1).get("time2"))
										- Integer.parseInt(maps.get(i).get(
												"time2"));
								index = i;
//								Log.i(TAG, "index=" + index);
								break;
							}

						}
					}

				}
//			}
		}

		// Log.i(TAG, "CurrentPoition"+CurrentPosition);
		// Log.i(TAG, "drifty="+drifty);
//		Log.i(TAG, "index=" + index);
		if (drifty > -40.0)
			if (currentDuringtime2 > 100) {
				drifty = (float) (drifty - 40.0 / (currentDuringtime2 / 100));
			} else {
				drifty = 0;
			}
		// Log.i(TAG, "drifty="+drifty);
		if (index == -1)
			return -1;
		return drifty;
	}
	/**
	 * @author younger
	 * @param CurrentPosition
	 *            当前歌词的时间轴
	 * @return drift 可以返回数据（已经废弃）
	 */
	public float updateIndex(int currentPosition) {
	
		return drifty;
	}

	public boolean repair() {
		// Log.i("LyricView", "repair()方法调用");
		if (index <= 0) {
			index = 0;
			return false;
		}
		if (index > maps.size() - 1)
			index = maps.size() - 1;
		return true;
	}

	public interface LyricViewClickListener {
		void lyricViewClick();
	}
}
