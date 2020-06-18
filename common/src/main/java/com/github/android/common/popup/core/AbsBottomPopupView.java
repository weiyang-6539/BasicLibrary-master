package com.github.android.common.popup.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.android.common.R;
import com.github.android.common.popup.animator.AbsAnimator;
import com.github.android.common.popup.annotation.PopupStatus;
import com.github.android.common.popup.util.KeyboardUtils;
import com.github.android.common.popup.util.XPopupUtils;
import com.github.android.common.popup.widget.SmartDragLayout;

/**
 * Created by fxb on 2020/5/13.
 */
public abstract class AbsBottomPopupView extends BasePopupView {
    private SmartDragLayout root;

    public AbsBottomPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected ViewGroup createPopupRootView() {
        return (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.popup_bottom_container, this, false);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        root = (SmartDragLayout) getPopupContentView();

        root.setDuration(popupAttrs.animDuration);
        root.enableDrag(popupAttrs.enableDrag);
        root.dismissOnTouchOutside(popupAttrs.isDismissOnTouchOutside);
        root.hasShadowBg(popupAttrs.hasShadowBg);

        getPopupImplView().setTranslationX(popupAttrs.offsetX);
        getPopupImplView().setTranslationY(popupAttrs.offsetY);

        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight());

        root.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                doAfterDismiss();
            }

            @Override
            public void onOpen() {
                AbsBottomPopupView.super.doAfterShow();
            }
        });

        root.setOnClickListener(v1 -> dismiss());
    }

    @Override
    protected void doAfterShow() {
        if (popupAttrs.enableDrag) {
            //do nothing self.
        } else {
            super.doAfterShow();
        }
    }

    @Override
    public void doShowAnimation() {
        if (popupAttrs.enableDrag) {
            root.open();
        } else {
            super.doShowAnimation();
        }
    }

    @Override
    public void doDismissAnimation() {
        if (popupAttrs.enableDrag) {
            root.close();
        } else {
            super.doDismissAnimation();
        }
    }

    /**
     * 动画是跟随手势发生的，所以不需要额外的动画器，因此动画时间也清零
     *
     * @return
     */
    @Override
    public int getAnimationDuration() {
        return popupAttrs.enableDrag ? 0 : super.getAnimationDuration();
    }

    @Override
    protected AbsAnimator getPopupAnimator() {
        // 移除默认的动画器
        return popupAttrs.enableDrag ? null : super.getPopupAnimator();
    }

    @Override
    public void dismiss() {
        if (popupAttrs.enableDrag) {
            if (status == PopupStatus.EXECUTING)
                return;
            status = PopupStatus.EXECUTING;
            if (popupAttrs.autoOpenSoftInput) KeyboardUtils.hideSoftInput(this);
            clearFocus();
            // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
            root.close();
        } else {
            super.dismiss();
        }
    }

    protected int getMaxWidth() {
        return popupAttrs.maxWidth == 0 ? XPopupUtils.getWindowWidth(getContext())
                : popupAttrs.maxWidth;
    }

    @Override
    protected View getTargetSizeView() {
        return getPopupImplView();
    }
}
