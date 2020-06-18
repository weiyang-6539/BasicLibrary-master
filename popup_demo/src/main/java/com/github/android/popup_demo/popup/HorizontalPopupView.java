package com.github.android.popup_demo.popup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.android.common.popup.core.AbsAttachPopupView;
import com.github.android.popup_demo.R;

/**
 * Created by fxb on 2020/6/18.
 */
public class HorizontalPopupView extends AbsAttachPopupView {
    public HorizontalPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_horizontal;
    }

}
