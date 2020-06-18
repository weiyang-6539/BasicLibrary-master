package com.github.android.popup_demo.popup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.android.common.popup.core.AbsCenterPopupView;

/**
 * Created by fxb on 2020/5/27.
 */
public class InputPopupView extends AbsCenterPopupView {
    public InputPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return 0;
    }
}
