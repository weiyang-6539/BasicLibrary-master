package com.github.android.popup_demo.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.android.common.popup.XPopup;
import com.github.android.common.popup.core.AbsBottomPopupView;
import com.github.android.common.popup.util.XPopupUtils;
import com.github.android.popup_demo.DrawableCreator;
import com.github.android.popup_demo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fxb on 2020/6/5.
 */
public class CommentPopupView extends AbsBottomPopupView {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    public CommentPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_comment;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ButterKnife.bind(this, getPopupContentView());

        mRecyclerView.setAdapter(mAdapter);
        List<String> mData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mData.add("");
        }
        mAdapter.setNewData(mData);

        getPopupImplView().setBackgroundDrawable(DrawableCreator.getDrawable(0xff111111));
    }

    @OnClick(R.id.cv_show_edit)
    public void onClickShowEdit() {
        new XPopup.Builder(getContext())
                .hasShadowBg(true)
                .asCustom(new Comment2PopupView(getContext()))
                .show();
    }

    private BaseQuickAdapter<String, BaseViewHolder> mAdapter =
            new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_douyin_comment) {
                @Override
                protected void convert(BaseViewHolder helper, String item) {

                }
            };

    @Override
    protected int getMaxHeight() {
        return (int) (0.75f * XPopupUtils.getWindowHeight(getContext()));
    }
}
