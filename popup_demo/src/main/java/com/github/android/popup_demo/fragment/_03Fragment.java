package com.github.android.popup_demo.fragment;

import com.github.android.common.base.BaseFragment;
import com.github.android.common.widget.AvatarImageView;
import com.github.android.popup_demo.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fxb on 2020/5/25.
 */
public class _03Fragment extends BaseFragment {
    @BindView(R.id.aiv_02)
    AvatarImageView aiv_02;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_03;
    }

    @Override
    protected void initialize() {
        aiv_02.setTextAndColorSeed("安", "安卓");
    }

    @OnClick(R.id.aiv_02)
    public void onClickAiv02(){
        aiv_02.setTextAndColorSeed("团团","111");
    }
}
