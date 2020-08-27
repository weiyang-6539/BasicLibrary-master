package com.github.android.popup_demo.popup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.android.common.popup.core.AbsBottomPopupView;
import com.github.android.popup_demo.DrawableCreator;
import com.github.android.popup_demo.R;

/**
 * Created by fxb on 2020/6/5.
 */
public class Comment2PopupView extends AbsBottomPopupView {
    public Comment2PopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getPopupLayoutId() {
        return R.layout.popup_comment2;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        getPopupImplView().setBackgroundDrawable(DrawableCreator.getDrawable(0xffffffff));
    }




}
