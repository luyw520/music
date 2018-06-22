package com.lu.library.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class LuAdapter<T> extends BaseAdapter{
	private Context context;
	private List<T> datas;
	private final int mItemLayoutId;
	public LuAdapter(Context context,List<T> datas,int mItemLayoutId){
		this.context=context;
		this.datas=datas;
		this.mItemLayoutId=mItemLayoutId;
	}
	@Override
	public int getCount() {
		return datas==null?0:datas.size();
	}

	@Override
	public T getItem(int position) {
		return datas==null?null:datas.get(position);
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder=getViewHolder(convertView, parent, position);
		convert(viewHolder,getItem(position));
		convert(viewHolder,position);
		return viewHolder.getConvertView();
	}
	public  void convert(ViewHolder helper, T item){};
	public abstract void convert(ViewHolder helper, int position);

//	};
	protected ViewHolder getViewHolder(View convertView, ViewGroup parent, int position){
		return ViewHolder.get(context, convertView, parent,mItemLayoutId, position);
	}
}
