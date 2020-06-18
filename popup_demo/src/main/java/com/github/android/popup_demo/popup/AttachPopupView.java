package com.github.android.popup_demo.popup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.android.common.popup.core.AbsAttachPopupView;
import com.github.android.popup_demo.R;

/**
 * Created by fxb on 2020/6/9.
 */
public class AttachPopupView extends AbsAttachPopupView {
    public AttachPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_toast;
    }
}
