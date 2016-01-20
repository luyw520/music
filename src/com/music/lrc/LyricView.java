package com.music.lrc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.music.bean.LyricSentence;
import com.music.utils.DeBug;

public class LyricView extends View {
	@SuppressWarnings("unused")
	private static final String TAG = "LyricView";
	/**
	 * 没有歌词
	 */
	public static final int NO_LRC = 0;
	/**
	 * 歌词正在加载...
	 */
	public static final int LRC_LOADIGN = 1;
	/**
	 * 歌词加载成功
	 */
	public static final int LRC_LOADED = 2;
	private Paint mPaint;
	/** 半宽 */
	private float mX;
	private Paint mPathPaint;
	/** 控件的高 */
	private int mY;
	/** 当前歌词持续时间 */
	private long currentDuringtime2;
	/** Y轴中间 */
	private float middleY;//
	private final int DY = 60;// 每一行的间隔

	/** x偏移量 */
	public float driftx;// x偏移量
	/** y偏移量 */
	public float drifty;//
	public int index = 0;
	public float mTouchHistoryY;
	private int loadLrc = 0;
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

	public LyricView(Context context) {
		super(context);

		init(context);
	}

	public LyricView(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	public LyricView(Context context, AttributeSet attr, int i) {
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

	public void setLyricLoadHelper(LyricLoadHelper loadHelper) {
	}

	private List<LyricSentence> lyricSentences=new ArrayList<LyricSentence>();

	public void setLyricSentences(List<LyricSentence> l) {
		lyricSentences.clear();
		if(l!=null){
			lyricSentences.addAll(l);
		}
		loadLrc=LRC_LOADED;
		index = 0;
	}
	public void reset(){
		index = 0;
		
	}
	/**
	 * 将原来的背景清除
	 */
	public void clear() {
		index = 0;
		loadLrc=LRC_LOADIGN;
		lyricSentences.clear();
	}

	private boolean isClick = false;

	protected void onDraw(Canvas canvas) {
//		DeBug.d(this, "onDraw,,,,,,,,,,,,,,,,,,,,,,,,,,index:"+index);
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
		if (lyricSentences == null || lyricSentences.size() == 0 || loadLrc == 0) {
			isClick = true;

			canvas.drawText("暂无歌词", mX, middleY, p2);
			return;
		}
		if (loadLrc == 1) {

			canvas.drawText("歌词正在加载中...", mX, middleY, p2);
			return;
		}
		p2.setTextSize(60);

		if (index > lyricSentences.size()) {
			return;
		}
		canvas.drawText(lyricSentences.get(index).getContentText(), mX, middleY + drift_r, p2);

		p.setTextSize(40);
		float tempY = middleY + drift_r;
		// 画出本句之前的句子
		for (int i = index - 1; i >= 0; i--) {
			// 向上推移
			tempY = tempY - DY;
			if (tempY < 0) {
				break;
			}
			canvas.drawText(lyricSentences.get(i).getContentText(), mX, tempY, p);
		}
		tempY = middleY + drift_r;
		// 画出本句之后的句子
		for (int i = index + 1; i < lyricSentences.size(); i++) {
			// 向下推移
			tempY = tempY + DY;
			if (tempY > mY) {
				break;
			}
			canvas.drawText(lyricSentences.get(i).getContentText(), mX, tempY, p);
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
		// Log.i("LyricView", "onSizeChanged方法调用");
	}

	/**
	 * @param CurrentPosition
	 *            当前歌词的时间轴
	 */
	public void updateindex(int CurrentPosition) {
		if (lyricSentences != null) {
			// if (index < maps.size() - 1) {
			if (index < lyricSentences.size() - 1) {
				// 歌词数组的序号

				if (CurrentPosition >= (lyricSentences.get(index + 1).getStartTime())) {

					currentDuringtime2 = (lyricSentences.get(index + 1).getStartTime()) - (lyricSentences.get(index).getStartTime());
					index++;
					drifty = 0;
					driftx = 0;
				} else if (index == 0) {
					currentDuringtime2 = (lyricSentences.get(index).getStartTime());
				} else if (CurrentPosition < (lyricSentences.get(index - 1).getStartTime())) {

					for (int i = 0, size = lyricSentences.size() - 1; i < size; i++) {
						if (CurrentPosition >= (lyricSentences.get(i).getStartTime()) && CurrentPosition < (lyricSentences.get(i + 1).getStartTime())) {
							currentDuringtime2 = (lyricSentences.get(i + 1).getStartTime()) - (lyricSentences.get(i).getStartTime());
							index = i;
							
							break;
						}

					}
				}

			}
			// }
		}
		 Log.i(TAG, "index=" + index);
		if (drifty > -40.0)
			if (currentDuringtime2 > 100) {
				drifty = (float) (drifty - 40.0 / (currentDuringtime2 / 100));
			} else {
				drifty = 0;
			}
		invalidate();
	}

	public boolean repair() {
		if (index <= 0) {
			index = 0;
			return false;
		}
		if (index > lyricSentences.size() - 1)
			index = lyricSentences.size() - 1;
		return true;
	}

	public int getLoadLrc() {
		return loadLrc;
	}

	public void setLoadLrc(int loadLrc) {
		this.loadLrc = loadLrc;
	}

	public interface LyricViewClickListener {
		void lyricViewClick();
	}
}
