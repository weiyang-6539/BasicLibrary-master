package com.github.android.common.popup.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.github.android.common.R;
import com.github.android.common.popup.widget.PopupDrawerLayout;

/**
 * Created by fxb on 2020/5/15.
 * 左右侧滑弹窗，支持手势拖拽
 */
public abstract class AbsDrawerPopupView extends BasePopupView {
    PopupDrawerLayout drawerLayout;
    protected FrameLayout drawerContentContainer;

    public AbsDrawerPopupView(@NonNull Context context) {
        super(context);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerContentContainer = findViewById(R.id.drawerContentContainer);

        View child = drawerLayout.getChildAt(2);
        drawerLayout.removeView(child);

        drawerContentContainer.addView(child);
    }

    @Override
    protected ViewGroup createPopupRootView() {
        return (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.popup_drawer_container, this, false);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        drawerLayout.enableShadow = popupAttrs.hasShadowBg;
        drawerLayout.isCanClose = popupAttrs.isDismissOnTouchOutside;
        drawerLayout.setOnCloseListener(new PopupDrawerLayout.OnCloseListener() {
            @Override
            public void onClose() {
                AbsDrawerPopupView.super.dismiss();
            }

            @Override
            public void onOpen() {
                AbsDrawerPopupView.super.doAfterShow();
            }

            @Override
            public void onDismissing(float fraction) {
                drawerLayout.isDrawStatusBarShadow = popupAttrs.hasStatusBarShadow;
            }
        });
        getPopupImplView().setTranslationX(popupAttrs.offsetX);
        getPopupImplView().setTranslationY(popupAttrs.offsetY);
        drawerLayout.setDrawerPosition(popupAttrs.position);
        drawerLayout.setOnClickListener(v -> drawerLayout.close());
    }

    @Override
    protected void doAfterShow() {
        //do nothing self.
    }

    @Override
    public void doShowAnimation() {
        drawerLayout.open();
    }

    @Override
    public void doDismissAnimation() {
        drawerLayout.close();
    }

    /**
     * 动画是跟随手势发生的，所以不需要额外的动画器，因此动画时间也清零
     *
     * @return
     */
    @Override
    public int getAnimationDuration() {
        return 0;
    }

    @Override
    public void dismiss() {
        // 关闭Drawer，由于Drawer注册了关闭监听，会自动调用dismiss
        drawerLayout.close();
    }

    @Override
    protected View getTargetSizeView() {
        return getPopupImplView();
    }
}
