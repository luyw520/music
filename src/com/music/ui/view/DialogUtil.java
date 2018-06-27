package com.music.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.lu.library.recyclerview.CommonRecyclerViewAdapter;
import com.lu.library.recyclerview.MultiItemTypeAdapterForRV;
import com.lu.library.recyclerview.base.CommonRecyclerViewHolder;
import com.music.lu.R;
import com.music.utils.ShareListener;

import java.util.ArrayList;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music.ui.view
 * @description: ${TODO}{ 类注释}
 * @date: 2018/6/27 0027
 */

public class DialogUtil {


    public static Dialog showShareActivityDialog(final Context context, final ShareListener listener) {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_share_activity);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        TextView cancle = dialog.findViewById(R.id.cancle);
        ArrayList<Integer> list = new ArrayList<Integer>();
//        list.add(R.drawable.share_facebook);
//        list.add(R.drawable.share_instargram);
//        list.add(R.drawable.share_whatsapp);
//        list.add(R.drawable.share_twitter);
//        list.add(R.mipmap.share_snapchat);
        list.add(R.drawable.umeng_socialize_wechat);
        list.add(R.drawable.umeng_socialize_qq);
        list.add(R.drawable.umeng_socialize_wxcircle);
//        list.add(R.mipmap.share_sina);

        CommonRecyclerViewAdapter adapter = new CommonRecyclerViewAdapter<Integer>(context, R.layout.item_shareactivity, list) {
            @Override
            protected void convert(CommonRecyclerViewHolder holder, Integer res, int position) {
                holder.setImageResource(R.id.image, res);
            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        recyclerView.setAdapter(adapter);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        adapter.setOnItemClickListener(new MultiItemTypeAdapterForRV.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                listener.shareImageSelect(position);
                dialog.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        dialog.show();
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom);
        recyclerView.setLayoutAnimation(animation);
        adapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
        return dialog;
    }
}
