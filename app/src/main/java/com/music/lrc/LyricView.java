package com.music.lrc;

import java.util.List;
import java.util.Map;

import com.music.bean.LyricSentence;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LyricView extends View {
	private static final String TAG = "LyricView";
	/**
	 * û�и��
	 */
	public static final int NO_LRC=0;
	/**
	 * ������ڼ���...
	 */
	public static final int LRC_LOADIGN=1;
	/**
	 * ��ʼ��سɹ�
	 */
	public static final int LRC_LOADED=2;
	private Paint mPaint;
	/** ��� */
	private float mX;
	private Paint mPathPaint;
	/** �ؼ��ĸ� */
	private int mY;
	/** ��ǰ��ʳ���ʱ�� */
	private long currentDuringtime2;
	/** Y���м� */
	private float middleY;//
	private final int DY = 60;// ÿһ�еļ��

	/** xƫ���� */
	public float driftx;// xƫ����
	/** yƫ���� */
	public float drifty;//
	public int index = 0;
	public float mTouchHistoryY;
	private int loadLrc=0;
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
	/** ����ʱ��ʾ���� */
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
		// �Ǹ�������
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(25);
		mPaint.setColor(Color.WHITE);
		mPaint.setTypeface(Typeface.SERIF);
		// �������� ��ǰ���
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
	private LyricLoadHelper loadHelper;
	public void setLyricLoadHelper(LyricLoadHelper loadHelper){
		this.loadHelper=loadHelper;
	}
	private List<LyricSentence> lyricSentences;
	public void setLyricSentences(List<LyricSentence> lyricSentences){
		this.lyricSentences=lyricSentences;
	}
	/**
	 * ��ԭ���ı������
	 */
	public void clear() {
		index = 0;
	}
	private boolean isClick = false;

	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// Log.i("LyricView", "onDraw()��������");
		// �������
		// ��ʾ�������
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
//		if (maps == null || maps.size() == 0) {
//			isClick = true;
//			canvas.drawText("���޸��", mX, middleY, p2);
//			return;
//		}
		if (lyricSentences == null || lyricSentences.size() == 0 || loadLrc==0) {
			isClick = true;
			
			canvas.drawText("���޸��", mX, middleY, p2);
			return;
		}
		if(loadLrc==1){
			
			canvas.drawText("������ڼ�����...", mX, middleY, p2);
			return;
		}
//		Log.i(TAG, "middleY=" + middleY + ",drift_r=" + drift_r);
//		Log.i(TAG, "x=" + mX + ",y=" + (middleY + drift_r) + ",lrc="
//				+ maps.get(index).get("lrc"));
		// �Ȼ���ǰ�У�֮���ٻ�����ǰ��ͺ��棬�����ͱ����˵�ǰ�����м��λ��
		// canvas.drawText(text[index], mX, middleY + drift_r, p2);
//		canvas.drawText(maps.get(index).get("lrc"), mX, middleY + drift_r, p2);
		p2.setTextSize(60);
		
		if(index>lyricSentences.size()){
			return;
		}
		canvas.drawText(lyricSentences.get(index).getContentText(), mX, middleY + drift_r, p2);
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
		p.setTextSize(40);
		float tempY = middleY + drift_r;
		// ��������֮ǰ�ľ���
		for (int i = index - 1; i >= 0; i--) {
			// ��������
			tempY = tempY - DY;
			if (tempY < 0) {
				break;
			}
//			canvas.drawText(maps.get(i).get("lrc"), mX, tempY, p);
			canvas.drawText(lyricSentences.get(i).getContentText(), mX, tempY, p);
			// canvas.drawText(text[i], mX, tempY, p);
		}
		tempY = middleY + drift_r;
		// ��������֮��ľ���
		for (int i = index + 1; i < lyricSentences.size(); i++) {
			// ��������
			tempY = tempY + DY;
			if (tempY > mY) {
				break;
			}
			canvas.drawText(lyricSentences.get(i).getContentText(), mX, tempY, p);
//			canvas.drawText(maps.get(i).get("lrc"), mX, tempY, p);
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
		mX = w * 0.5f;// ��Ļ��������(ת��Ϊfloat?)
		mY = h;
		middleY = h * 0.5f;
//		Log.i("LyricView", "onSizeChanged��������");
	}

	/**
	 * @param CurrentPosition
	 *            ��ǰ��ʵ�ʱ����
	 */
	public void updateindex(int CurrentPosition) {
		// Log.i("LyricView", "updateindex()��������");
		if (lyricSentences != null) {
//			if (index < maps.size() - 1) {
				if (index < lyricSentences.size() - 1) {
				// �����������

//				if (index == 0) {
//					
//				} else {
					
					
					if (CurrentPosition >= (lyricSentences.get(index + 1).getStartTime())) {

						currentDuringtime2 = (lyricSentences.get(index + 1).getStartTime())
								- (lyricSentences.get(index).getStartTime());
						index++;
						drifty = 0;
						driftx = 0;
					} else if(index==0){
						currentDuringtime2 = (lyricSentences.get(index).getStartTime());
					}else if(CurrentPosition < (lyricSentences.get(
							index - 1).getStartTime())) {
						
						for (int i = 0,size= lyricSentences.size()-1; i <size; i++) {
							if (CurrentPosition >= (lyricSentences.get(
									i).getStartTime())&&CurrentPosition<(lyricSentences.get(
											i+1).getStartTime())) {
								currentDuringtime2 = (lyricSentences.get(
										i + 1).getStartTime())
										- (lyricSentences.get(i).getStartTime());
								index = i;
//								Log.i(TAG, "index=" + index);
								break;
							}

						}
					}

				}
//			}
		}

//		 Log.i(TAG, "CurrentPoition"+CurrentPosition);
		// Log.i(TAG, "drifty="+drifty);
//		Log.i(TAG, "index=" + index);
		if (drifty > -40.0)
			if (currentDuringtime2 > 100) {
				drifty = (float) (drifty - 40.0 / (currentDuringtime2 / 100));
			} else {
				drifty = 0;
			}
//		 Log.i(TAG, "drifty="+drifty);
		invalidate();
	}

	public boolean repair() {
		// Log.i("LyricView", "repair()��������");
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