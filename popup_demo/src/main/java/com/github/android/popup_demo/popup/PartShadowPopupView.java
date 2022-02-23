package com.github.android.popup_demo.popup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.android.common.popup.core.AbsPartShadowPopupView;
import com.github.android.popup_demo.R;

/**
 * Created by fxb on 2020/6/9.
 */
public class PartShadowPopupView extends AbsPartShadowPopupView {
    public PartShadowPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_part_demo;
    }
}
