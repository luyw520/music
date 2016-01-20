package com.music.view.popwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.music.lu.R;
import com.music.utils.DialogUtil;

public class PopupWindowUtil {
	/**
	 * 弹出窗体
	 */
	private PopupWindow popupWindow;
	
	private Context mContext;
	
	/**
	 * 弹出窗体上的点击事件接口
	 */
	private PopupWindowUIOnClickListener popupWindowUIOnClickListener;
	private PopupWindowUI popupWindowUI;
	
	public PopupWindowUtil (Context mContext ){
		this.mContext=mContext;
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
			LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.popupwindow, null);
			initPopupWindowUI(view);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			
//			initPopupWindowUI();
			
		}
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击空白的地方关闭PopupWindow
		popupWindow.showAsDropDown(parent);
		popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		popupWindow.update();
	
	}
	
	/**
	 * 初始化弹出窗体的控件
	 * 
	 * @param view
	 */
	private void initPopupWindowUI(View view) {
		
		
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		
		popupWindowUI = new PopupWindowUI();
		popupWindowUI.ll_menu_singer = (LinearLayout) view
				.findViewById(R.id.ll_menu_singer);

		popupWindowUI.ll_menu_folder = (LinearLayout) view
				.findViewById(R.id.ll_menu_folder);
		popupWindowUI.ll_menu_scan = (LinearLayout) view
				.findViewById(R.id.ll_menu_scan);
		popupWindowUI.ll_menu_match = (LinearLayout) view
				.findViewById(R.id.ll_menu_match);
		popupWindowUI.ll_menu_on_time = (LinearLayout) view
				.findViewById(R.id.ll_menu_on_time);
		popupWindowUI.ll_menu_on_music = (LinearLayout) view
				.findViewById(R.id.ll_menu_on_music);
		popupWindowUI.iv_on_time = (ImageView) view
				.findViewById(R.id.iv_on_time);
		popupWindowUI.iv_on_music = (ImageView) view
				.findViewById(R.id.iv_on_music);

		popupWindowUI.ll_menu_singer
				.setOnClickListener(click);
		popupWindowUI.ll_menu_folder
				.setOnClickListener(click);
		popupWindowUI.ll_menu_scan
				.setOnClickListener(click);
		popupWindowUI.ll_menu_match
				.setOnClickListener(click);
		popupWindowUI.ll_menu_on_time
				.setOnClickListener(click);
		popupWindowUI.ll_menu_on_music
				.setOnClickListener(click);
	}
	class PopupWindowUI {
		ImageView iv_on_music;
		ImageView iv_on_time;
		LinearLayout ll_menu_folder;
		LinearLayout ll_menu_match;
		LinearLayout ll_menu_on_music;
		LinearLayout ll_menu_on_time;

		LinearLayout ll_menu_scan;
		LinearLayout ll_menu_singer;
	}
	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_menu_singer:
				toast("按歌手查看");
				break;
			case R.id.ll_menu_folder:
				toast("按文件夹查看");
				break;
			case R.id.ll_menu_scan:
				toast("扫描本地歌曲");
				break;
			case R.id.ll_menu_match:
				toast("一键匹配词图");
				break;
			case R.id.ll_menu_on_time:
				toast("按添加时间排序");
				popupWindowUI.iv_on_time.setVisibility(View.VISIBLE);
				popupWindowUI.iv_on_music.setVisibility(View.INVISIBLE);

				
				if(popupWindowUIOnClickListener!=null){
					popupWindowUIOnClickListener.sortByTime();
				}
				break;
			case R.id.ll_menu_on_music:
				toast("按歌曲名称排序");
				popupWindowUI.iv_on_music.setVisibility(View.VISIBLE);
				popupWindowUI.iv_on_time.setVisibility(View.INVISIBLE);
				
				if(popupWindowUIOnClickListener!=null){
					popupWindowUIOnClickListener.sortByName();
				}
				break;

			default:
				break;
			}
		}
	};
	private void toast(String message) {
		DialogUtil.showToast(mContext, message);
	}
	public PopupWindowUIOnClickListener getPopupWindowUIOnClickListener() {
		return popupWindowUIOnClickListener;
	}

	public void setPopupWindowUIOnClickListener(
			PopupWindowUIOnClickListener popupWindowUIOnClickListener) {
		this.popupWindowUIOnClickListener = popupWindowUIOnClickListener;
	}
	public interface PopupWindowUIOnClickListener{
		
		public void sortByTime();
		public void sortByName();
	};
}
