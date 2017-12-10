package com.music.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.music.bean.MusicInfo;
import com.music.lu.R;
import com.music.utils.DeBug;
import com.music.helpers.PlayerHelpler;
import com.music.ui.widget.indexablelistview.IndexableListView;

public class MusicListAdapter extends BaseAdapter implements SectionIndexer {

	public MusicListAdapter(Context context, int resource,
							List<MusicInfo> objects, IndexableListView listView) {
		// super(context, resource, objects);
		this.context = context;
		this.mp3Infos = objects;
		this.mp3Util = PlayerHelpler.getDefault();
		this.listView = listView;
	}

	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Context context;
	private List<MusicInfo> mp3Infos;
	private MusicInfo mp3Info;
	@SuppressWarnings("unused")
	private int pos = -1;
	private PlayerHelpler mp3Util;
//	private Mp3Util mp3Util;
	@SuppressWarnings("unused")
	private IndexableListView listView;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mp3Infos==null?0:mp3Infos.size();
	}

	@Override
	public MusicInfo getItem(int position) {
		return mp3Infos==null?null:mp3Infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
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
			convertView.setTag(viewHolder); //

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		mp3Info = mp3Infos.get(position);

		viewHolder.musicTitle.setText(mp3Info.getTitle()); //
		viewHolder.music_album.setText(mp3Info.getArtist());
		if (!mp3Util.isSortByTime()) {
			viewHolder.catalog.setVisibility(View.VISIBLE);
			String firstPin=mp3Info.getTitleKey().substring(0, 1);
			viewHolder.catalog.setText(firstPin);
			if (position > 0) {
				String preFirstPin=mp3Infos.get(position - 1).getTitleKey().substring(0,1);
				if (firstPin.equals(preFirstPin)) {
					viewHolder.catalog.setVisibility(View.GONE);
				} else {

					viewHolder.catalog.setVisibility(View.VISIBLE);
					viewHolder.catalog.setText(firstPin);
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
	 *
	 * @author wwj
	 * 
	 */
	public class ViewHolder {
		public ImageView albumImage; //
		public TextView musicTitle; //
		public TextView catalog;
		public TextView music_album;
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
			String string = mp3Infos.get(i).getTitleKey().substring(0, 1);
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
