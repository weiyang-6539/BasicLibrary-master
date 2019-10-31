package com.github.wyang.basiclibrarydemo;

import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;

/**
 * Created by weiyang on 2019-10-12.
 *
 * @url https://www.jb51.net/article/145309.htm
 */
public class ComplexButtonActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sc_enable)
    SwitchCompat sc_enable;

    @Override
    public int getLayoutId() {
        return R.layout.activity_complex_button;
    }

    @Override
    protected void initView() {
        initToolBar(toolbar, "ComplexButton", true);


        sc_enable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ViewGroup parent = (ViewGroup) sc_enable.getParent();
            applyEnable(parent, isChecked);
        });
    }

    private void applyEnable(View view, boolean enable) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                applyEnable(parent.getChildAt(i), enable);
            }
        } else {
            if (!(view instanceof SwitchCompat))
                view.setEnabled(enable);
            Log.e(TAG, view.getClass().getSimpleName());
        }
    }

}
