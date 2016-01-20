package com.music.view.popwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.lu.R;
import com.music.utils.ApplicationUtil;

public class PopupWindowQieGe {
	/**
	 * 弹出窗体
	 */
	private PopupWindow popupWindow;

	private Context mContext;

	/**
	 * 弹出窗体上的点击事件接口
	 */
	 private PopupWindowUIOnClickListener p;

	private RelativeLayout rl_nouse;
	private RelativeLayout rl_wenrou;
	private RelativeLayout rl_putong;
	private RelativeLayout rl_yongli;

	private TextView tv_nouse;
	private TextView tv_wenrou;
	private TextView tv_putong;
	private TextView tv_yongli;

	private ImageView iv_nouse;
	private ImageView iv_wenrou;
	private ImageView iv_putong;
	private ImageView iv_yongli;
	
	private TextView oldtv;
	private ImageView oldiv;
    
	private int selectColor=-1;
	public PopupWindowQieGe(Context mContext) {
		this.mContext = mContext;
	}
	public void setPopupWindowUIOnClickListener(PopupWindowUIOnClickListener p){
		this.p=p;
	}
	/**
	 * 显示弹出窗体
	 * 
	 * @param parent
	 *            在该控件下显示弹出窗体
	 */
	@SuppressWarnings("deprecation")
	public void showWindow(View parent) {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater
					.inflate(R.layout.popupwindow_qiege, null);
			initPopupWindowUI(view);
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

		}

		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击空白的地方关闭PopupWindow
		popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

		popupWindow.setAnimationStyle(R.style.popwin_anim_qiege_style);
		popupWindow.update();

	}

	/**
	 * 初始化弹出窗体的控件
	 * 
	 * @param view
	 */
	private void initPopupWindowUI(View view) {
		rl_nouse = (RelativeLayout) view.findViewById(R.id.rl_nouse);
		rl_wenrou = (RelativeLayout) view.findViewById(R.id.rl_wenrou);
		rl_putong = (RelativeLayout) view.findViewById(R.id.rl_putong);
		rl_yongli = (RelativeLayout) view.findViewById(R.id.rl_yongli);

		tv_nouse = (TextView) view.findViewById(R.id.tv_nouse);
		tv_wenrou = (TextView) view.findViewById(R.id.tv_wenrou);
		tv_yongli = (TextView) view.findViewById(R.id.tv_yongli);
		tv_putong = (TextView) view.findViewById(R.id.tv_putong);

		iv_nouse = (ImageView) view.findViewById(R.id.iv_nouse);
		iv_wenrou = (ImageView) view.findViewById(R.id.iv_wenrou);
		iv_putong = (ImageView) view.findViewById(R.id.iv_putong);
		iv_yongli = (ImageView) view.findViewById(R.id.iv_yongli);
		
		rl_nouse.setOnClickListener(l);
		rl_wenrou.setOnClickListener(l);
		rl_putong.setOnClickListener(l);
		rl_yongli.setOnClickListener(l);
		
		
		selectColor=mContext.getResources().getColor(R.color.qiege_font_color);
		setOldView(ApplicationUtil.getQieGeIndex(mContext));

		oldtv.setTextColor(selectColor);
		oldiv.setVisibility(View.VISIBLE);
	}
	
	private OnClickListener l=new OnClickListener() {
		
		@SuppressLint("ResourceAsColor") @Override
		public void onClick(View v) {
			int index=0;
			
			oldtv.setTextColor(Color.BLACK);
			oldiv.setVisibility(View.INVISIBLE);
			switch (v.getId()) {
			case R.id.rl_nouse:
				index=0;
				tv_nouse.setTextColor(selectColor);
				iv_nouse.setVisibility(View.VISIBLE);
				break;
			case R.id.rl_wenrou:
				index=1;
				tv_wenrou.setTextColor(selectColor);
				iv_wenrou.setVisibility(View.VISIBLE);
				break;
			case R.id.rl_putong:
				index=2;
				tv_putong.setTextColor(selectColor);
				iv_putong.setVisibility(View.VISIBLE);
				break;
			case R.id.rl_yongli:
				index=3;
				tv_yongli.setTextColor(selectColor);
				iv_yongli.setVisibility(View.VISIBLE);
				break;
			
				
			default:
				break;
			}
			ApplicationUtil.setQieGeIndex(mContext, index);
			setOldView(index);
			if(p!=null){
				p.onClick(index);
			}
			
			
			popupWindow.dismiss();
		}
	};
	public void dismiss(){
		popupWindow.dismiss();
	}
	private void setOldView(int index){
		switch (index) {
		case 0:
			oldiv=iv_nouse;
			oldtv=tv_nouse;
			break;
		case 1:
			oldiv=iv_wenrou;
			oldtv=tv_wenrou;
			break;
		case 2:
			oldiv=iv_putong;
			oldtv=tv_putong;
			break;
		case 3:
			oldiv=iv_yongli;
			oldtv=tv_yongli;
			break;

		default:
			break;
		}
	}
	
	public interface PopupWindowUIOnClickListener{
		public void onClick(int index);
	} 
}
