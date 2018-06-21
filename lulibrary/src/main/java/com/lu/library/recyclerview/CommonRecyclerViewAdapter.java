package com.lu.library.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;

import com.lu.library.recyclerview.base.CommonRecyclerViewHolder;
import com.lu.library.recyclerview.base.ItemViewDelegateForRV;

import java.util.List;


/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonRecyclerViewAdapter<T> extends MultiItemTypeAdapterForRV<T>
{
    protected Context mContext;
    protected int mLayoutId;
//    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonRecyclerViewAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
        addItemViewDelegate(0,new ItemViewDelegateForRV<T>()
        {

            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public int getViewType( T item, int position)
            {
                return 0;
            }

            @Override
            public void convert(CommonRecyclerViewHolder holder, T t, int position)
            {
                CommonRecyclerViewAdapter.this.convert(holder, t, position);
            }
        });
    }
    public void addAll(List<T> all){
        mDatas.clear();
        mDatas.addAll(all);
        notifyDataSetChanged();
    }
    public void addMore(List<T> datas){
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }
    public void remove(int position){
        if (mDatas==null || mDatas.isEmpty()||mDatas.size()<position){
            return;
        }
        mDatas.remove(position);
        notifyDataSetChanged();
    }
    protected abstract void convert(CommonRecyclerViewHolder holder, T t, int position);

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
}
