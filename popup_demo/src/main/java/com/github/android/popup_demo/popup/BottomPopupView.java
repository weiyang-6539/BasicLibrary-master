package com.github.android.popup_demo.popup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.android.common.popup.core.AbsBottomPopupView;
import com.github.android.popup_demo.R;

/**
 * Created by fxb on 2020/5/14.
 */
public class BottomPopupView extends AbsBottomPopupView {
    public BottomPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_bottom_demo;
    }
}
