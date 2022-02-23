package com.github.android.popup_demo.popup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.android.common.popup.core.AbsAttachPopupView;
import com.github.android.popup_demo.R;

/**
 * Created by fxb on 2020/5/19.
 */
public class ListPopupView extends AbsAttachPopupView {
    public ListPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_list;
    }
}
