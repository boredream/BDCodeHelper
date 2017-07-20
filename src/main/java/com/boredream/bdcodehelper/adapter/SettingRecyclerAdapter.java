package com.boredream.bdcodehelper.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.view.SettingItemView;

import java.util.List;

/**
 * 设置选项列表适配器
 * <p>
 * Item通用样式为：左侧图标、中间文字、右侧文字、右侧图标。
 */
public class SettingRecyclerAdapter extends RecyclerView.Adapter<SettingRecyclerAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_SETTING_ITEM = 0x10001;

    protected List<SettingItem> datas;
    protected AdapterView.OnItemClickListener mOnItemClickListener;

    public SettingRecyclerAdapter(List<SettingItem> datas, AdapterView.OnItemClickListener listener) {
        this.datas = datas;
        mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_VIEW_TYPE_SETTING_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public SettingItemView item;

        public ViewHolder(final View itemView) {
            super(itemView);

            item = (SettingItemView) itemView;
        }
    }

    @Override
    public SettingRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new SettingItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(SettingRecyclerAdapter.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(null, view, position, -1);
                }
            }
        });
        SettingItem data = datas.get(position);

        holder.item.setLeftImg(data.leftImgRes)
            .setMidText(data.midText)
            .setRightText(data.rightText)
            .setRightImg(data.rightImage);
    }

}
