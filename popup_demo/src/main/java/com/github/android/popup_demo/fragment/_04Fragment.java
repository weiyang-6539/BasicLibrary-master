package com.github.android.popup_demo.fragment;

import com.github.android.common.base.BaseFragment;
import com.github.android.common.widget.ComplexView;
import com.github.android.popup_demo.R;

import butterknife.BindView;

/**
 * Created by fxb on 2020/5/25.
 */
public class _04Fragment extends BaseFragment {
    @BindView(R.id.cv_01)
    ComplexView cv_01;
    @BindView(R.id.cv_02)
    ComplexView cv_02;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_04;
    }

    @Override
    protected void initialize() {

    }
}
