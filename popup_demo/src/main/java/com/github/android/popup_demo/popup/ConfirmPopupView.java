package com.github.android.popup_demo.popup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.android.common.popup.core.AbsCenterPopupView;
import com.github.android.popup_demo.R;

/**
 * Created by fxb on 2020/5/9.
 */
public class ConfirmPopupView extends AbsCenterPopupView {
    public ConfirmPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_confirm;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        getPopupContentView().findViewById(R.id.tv_confirm).setOnClickListener(v -> dismiss());
        getPopupContentView().findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
    }
}
