package com.github.android.popup_demo.popup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.android.common.popup.core.AbsDrawerPopupView;
import com.github.android.popup_demo.R;

/**
 * Created by fxb on 2020/5/15.
 */
public class DrawerPopupView extends AbsDrawerPopupView {
    public DrawerPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_drawer_test;
    }
}
