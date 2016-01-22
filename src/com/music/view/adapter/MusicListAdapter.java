package com.music.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.music.lu.R;
import com.music.bean.Mp3Info;
import com.music.utils.DeBug;
import com.music.utils.Mp3Util_New;
import com.music.widget.indexablelistview.IndexableListView;

public class MusicListAdapter extends BaseAdapter implements SectionIndexer {

	public MusicListAdapter(Context context, int resource,
			List<Mp3Info> objects, IndexableListView listView) {
		// super(context, resource, objects);
		this.context = context;
		this.mp3Infos = objects;
		this.mp3Util = Mp3Util_New.getDefault();
		this.listView = listView;
	}

	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Context context;
	private List<Mp3Info> mp3Infos;
	private Mp3Info mp3Info;
	@SuppressWarnings("unused")
	private int pos = -1;
	private Mp3Util_New mp3Util;
//	private Mp3Util mp3Util;
	private IndexableListView listView;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mp3Infos.size();
	}

	@Override
	public Mp3Info getItem(int position) {
		// TODO Auto-generated method stub
		return mp3Infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview__layout, null);
			viewHolder.albumImage = (ImageView) convertView
					.findViewById(R.id.albumImage);
			viewHolder.musicTitle = (TextView) convertView
					.findViewById(R.id.music_title);
			viewHolder.catalog = (TextView) convertView
					.findViewById(R.id.catalog);
			viewHolder.music_album = (TextView) convertView
					.findViewById(R.id.music_album);
			convertView.setTag(viewHolder); // 表示给View添加一个格外的数据，

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		mp3Info = mp3Infos.get(position);

		viewHolder.musicTitle.setText(mp3Info.getTitle()); // 显示标题
		viewHolder.music_album.setText(mp3Info.getAlbum());
		if (!mp3Util.isSortByTime()) {
			viewHolder.catalog.setVisibility(View.VISIBLE);
			viewHolder.catalog.setText(mp3Info.getFisrtPinYin());
			if (position > 0) {
				if (mp3Info.getFisrtPinYin().equals(
						mp3Infos.get(position - 1).getFisrtPinYin())) {
					viewHolder.catalog.setVisibility(View.GONE);
				} else {

					viewHolder.catalog.setVisibility(View.VISIBLE);
					viewHolder.catalog.setText(mp3Info.getFisrtPinYin());
				}
			}
//			listView.setShow(true);
		} else {
			viewHolder.catalog.setVisibility(View.GONE);
//			listView.setShow(false);
		}

		return convertView;
	}

	/**
	 * 定义一个内部类 声明相应的控件引用
	 * 
	 * @author wwj
	 * 
	 */
	public class ViewHolder {
		// 所有控件对象引用
		public ImageView albumImage; // 专辑图片
		public TextView musicTitle; // 音乐标题
		public TextView catalog;
		public TextView music_album;
		// public TextView musicDuration; // 音乐时长
		// public TextView musicArtist; // 音乐艺术家
	}

	@Override
	public Object[] getSections() {
		DeBug.d(this, "getSections.....................");
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}

	@Override
	public int getPositionForSection(int section) {
		DeBug.d(this, "getPositionForSection.....................");
		int result = -1;
//		boolean isFind=false;
		String s = mSections.substring(section, section + 1);
		for (int i = 0; i < mp3Infos.size(); i++) {
			String string = mp3Infos.get(i).getTitlepingyin().substring(0, 1);
			if (string.equalsIgnoreCase(s)) {
				result = i;
//				isFind=true;
				break;
			}
		}
//		if(!isFind){
//			result=section;
//		}
		return result;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}
}
