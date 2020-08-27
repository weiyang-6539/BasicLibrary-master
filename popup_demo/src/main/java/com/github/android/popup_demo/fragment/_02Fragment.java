package com.github.android.popup_demo.fragment;

import android.content.Intent;

import com.github.android.common.base.BaseFragment;
import com.github.android.common.popup.XPopup;
import com.github.android.popup_demo.DouyinActivity;
import com.github.android.popup_demo.R;
import com.github.android.popup_demo.popup.CommentPopupView;

import butterknife.OnClick;

/**
 * Created by fxb on 2020/5/25.
 */
public class _02Fragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_02;
    }

    @Override
    protected void initialize() {

    }

    @OnClick(R.id.btn_douyin)
    public void onClickDouYin() {
        startActivity(new Intent(getContext(), DouyinActivity.class));
    }

    @OnClick(R.id.btn_test)
    public void onClickTest() {
        new XPopup.Builder(getContext())
                .hasShadowBg(true)
                .dismissOnBackPressed(true)
                .autoOpenSoftInput(true)
                .moveUpToKeyboard(false)
                .asCustom(new CommentPopupView(getContext()))
                .show();
    }
}
