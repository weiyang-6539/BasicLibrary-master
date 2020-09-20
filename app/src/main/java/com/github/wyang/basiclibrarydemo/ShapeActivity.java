package com.github.wyang.basiclibrarydemo;

import android.support.v7.widget.Toolbar;

import butterknife.BindView;

/**
 * Created by fxb on 2020/8/27.
 */
public class ShapeActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shape_view;
    }

    @Override
    protected void initView() {
        initToolBar(mToolBar, "自定义Shape", true);
    }

}
