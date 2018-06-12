package com.music.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridViewAdapter extends BaseAdapter{
	Context context;
	LayoutInflater inInflater;
	int res;
	private int[] imgs;
	public String[] textString;

	public MyGridViewAdapter(Context context,int[] imgs,String[] textString,int res){
		this.context=context;
		inInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imgs=imgs;
		this.textString=textString;
		this.res=res;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgs.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("NewApi") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
//		ImageView image=null;
//		TextView textView=null;
		ViewHolder viewHolder=null;
		if(convertView==null){
			convertView=inInflater.inflate(res, null);
			viewHolder=new ViewHolder();
//			viewHolder.image=(ImageView)convertView.findViewById(R.id.img_griditem);
//			viewHolder.textView=(TextView) convertView.findViewById(R.id.tv__griditem);
			convertView.setTag(viewHolder);
		}
		viewHolder=(ViewHolder) convertView.getTag();
//		if(viewHolder.image)
		viewHolder.image.setBackground(null);
		viewHolder.image.setImageResource(imgs[position]);
		viewHolder.textView.setText(textString[position]);

		return convertView;
	}
	class ViewHolder {
		ImageView image;
		TextView textView;
	}
}
