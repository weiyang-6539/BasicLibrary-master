package com.github.wyang.basiclibrarydemo;

import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.android.common.widget.shape.extend.ComplexView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by weiyang on 2019-10-12.
 *
 * @url https://www.jb51.net/article/145309.htm
 */
public class ComplexViewActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sc_enable)
    SwitchCompat sc_enable;

    @BindView(R.id.ll_group)
    LinearLayout ll_group;

    @Override
    public int getLayoutId() {
        return R.layout.activity_complex_view;
    }

    @Override
    protected void initView() {
        initToolBar(toolbar, "ComplexView", true);

        sc_enable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ViewGroup parent = (ViewGroup) sc_enable.getParent();
            applyEnable(parent, isChecked);
        });

        setSelectListener(ll_group);
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
        }
    }

    private void setSelectListener(View view) {
        if (view instanceof LinearLayout) {
            LinearLayout group = (LinearLayout) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                setSelectListener(group.getChildAt(i));
            }
        } else if (view instanceof ComplexView) {
            view.setOnClickListener(v -> {
                applySelected(ll_group, false);
                v.setSelected(true);
            });
        }
    }

    private void applySelected(View view, boolean selected) {
        if (view instanceof LinearLayout) {
            LinearLayout parent = (LinearLayout) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                applySelected(parent.getChildAt(i), selected);
            }
        } else if (view instanceof ComplexView) {
            view.setSelected(selected);
        }
    }

    @BindView(R.id.cv_prompt)
    ComplexView cv_prompt;

    @OnClick(R.id.cv_prompt)
    public void onClickPrompt() {
        cv_prompt.setSelected(!cv_prompt.isSelected());
        cv_prompt.setText(cv_prompt.isSelected() ? "已启用" : "启用提醒");
    }
}
