package com.github.android.common.widget.banner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by fxb on 2020/11/4.
 */
public abstract class BannerAdapter<T> extends RecyclerView.Adapter<BannerViewHolder> {
    protected int mLayoutResId;
    protected Context mContext;
    private List<T> mData;

    public BannerAdapter(List<T> mData, @LayoutRes int layoutResId) {
        this.mData = mData;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    @NotNull
    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();

        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, parent, false);

        BannerViewHolder holder = new BannerViewHolder(itemView);
        if (getOnItemClickListener() != null) {
            holder.itemView.setOnClickListener(v -> setOnItemClick(v, holder.getLayoutPosition() % mData.size()));
        }
        holder.setAdapter(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder bannerViewHolder, int position) {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        convert(bannerViewHolder, mData.get(position % mData.size()));
    }

    protected abstract void convert(BannerViewHolder helper, T item);

    @Override
    public int getItemCount() {
        if (mData.size() == 1) {
            return mData.size();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public List<T> getData() {
        return mData;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClick(View v, int position) {
        getOnItemClickListener().onItemClick(this, v, position);
    }

    public interface OnItemClickListener {
        void onItemClick(BannerAdapter adapter, View view, int position);
    }

    private OnItemChildClickListener mOnItemChildClickListener;

    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(BannerAdapter adapter, View view, int position);
    }
}
