package com.github.android.common.popup.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;

import com.github.android.common.popup.util.XPopupUtils;

/**
 * Created by fxb on 2020/5/9.
 */
public abstract class AbsCenterPopupView extends BasePopupView {
    public AbsCenterPopupView(@NonNull Context context) {
        super(context);

        LayoutParams params = (LayoutParams) getPopupContentView().getLayoutParams();
        params.gravity = Gravity.CENTER;
    }

    @Override
    protected void onCreate() {
        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setTranslationY(0);
    }

    protected int getMaxWidth() {
        return popupAttrs.maxWidth == 0 ? (int) (XPopupUtils.getWindowWidth(getContext()) * 0.86f)
                : popupAttrs.maxWidth;
    }

}
