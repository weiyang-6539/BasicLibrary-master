package com.github.android.popup_demo.popup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.android.popup_demo.R;
import com.github.android.common.popup.core.AbsCenterPopupView;

/**
 * Created by fxb on 2020/5/12.
 */
public class LoadingPopupView extends AbsCenterPopupView {
    public LoadingPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_loading;
    }
}
