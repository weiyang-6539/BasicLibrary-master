package com.github.android.common.popup.core;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.github.android.common.popup.animator.AbsAnimator;
import com.github.android.common.popup.animator.AnimationFactory;
import com.github.android.common.popup.animator.TranslateAnimator;
import com.github.android.common.popup.annotation.Position;
import com.github.android.common.popup.util.XPopupUtils;

/**
 * Created by fxb on 2020/5/18.
 * 局部区域阴影的弹窗
 */
public abstract class AbsPartShadowPopupView extends BasePopupView {
    private boolean isShowUp;

    public AbsPartShadowPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected ViewGroup createPopupRootView() {
        FrameLayout layout = new FrameLayout(getContext());
        layout.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        return layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        getPopupImplView().setTranslationX(popupAttrs.offsetX);
        getPopupImplView().setTranslationY(popupAttrs.offsetY);

        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(), this::doAttach);
    }

    @Override
    public void onNavigationBarChange(boolean show) {
        super.onNavigationBarChange(show);
        if (!show) {
            FrameLayout.LayoutParams params = (LayoutParams) getPopupContentView().getLayoutParams();
            params.height = XPopupUtils.getWindowHeight(getContext());
            getPopupContentView().setLayoutParams(params);
        }
    }

    protected void doAttach() {
        if (popupAttrs.atView == null)
            throw new IllegalArgumentException("atView must not be null for PartShadowPopupView！");

        // 指定阴影动画的目标View
        shadowBgAnimator.targetView = getPopupContentView();

        //1. apply width and height
        int rotation = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getPopupContentView().getLayoutParams();
        if (rotation == 0) {
            params.width = getMeasuredWidth(); // 满宽
        } else if (rotation == 1 || rotation == 3) {
            params.width = getMeasuredWidth() - (XPopupUtils.isNavBarVisible(getContext()) ? XPopupUtils.getNavBarHeight() : 0);
        }

        //水平居中
        if (popupAttrs.isCenterHorizontal && getPopupImplView() != null) {
            getPopupImplView().setTranslationX(XPopupUtils.getWindowWidth(getContext()) / 2f - getPopupContentView().getMeasuredWidth() / 2f);
        }

        //1. 获取atView在屏幕上的位置
        int[] locations = new int[2];
        popupAttrs.atView.getLocationOnScreen(locations);
        Rect rect = new Rect(locations[0], locations[1], locations[0] + popupAttrs.atView.getMeasuredWidth(),
                locations[1] + popupAttrs.atView.getMeasuredHeight());
        int centerY = rect.top + rect.height() / 2;
        if ((centerY > getMeasuredHeight() / 2 || popupAttrs.position == Position.TOP) && popupAttrs.position != Position.BOTTOM) {
            // 说明atView在Window下半部分，PartShadow应该显示在它上方，计算atView之上的高度
            params.height = rect.top;
            isShowUp = true;
            params.topMargin = -popupAttrs.offsetY;
            // 同时自定义的impl View应该Gravity居于底部
            View implView = ((ViewGroup) getPopupContentView()).getChildAt(0);
            FrameLayout.LayoutParams implParams = (LayoutParams) implView.getLayoutParams();
            implParams.gravity = Gravity.BOTTOM;
            if (getMaxHeight() != 0)
                implParams.height = Math.min(implView.getMeasuredHeight(), getMaxHeight());
            implView.setLayoutParams(implParams);

        } else {
            // atView在上半部分，PartShadow应该显示在它下方，计算atView之下的高度
            params.height = getMeasuredHeight() - rect.bottom;
            // 防止伸到导航栏下面
            if (XPopupUtils.isNavBarVisible(getContext())) {
                params.height -= XPopupUtils.getNavBarHeight();
            }
            isShowUp = false;
            params.topMargin = rect.bottom + popupAttrs.offsetY;

            // 同时自定义的impl View应该Gravity居于顶部
            View implView = ((ViewGroup) getPopupContentView()).getChildAt(0);
            FrameLayout.LayoutParams implParams = (LayoutParams) implView.getLayoutParams();
            implParams.gravity = Gravity.TOP;
            if (getMaxHeight() != 0)
                implParams.height = Math.min(implView.getMeasuredHeight(), getMaxHeight());
            implView.setLayoutParams(implParams);
        }
        getPopupContentView().setLayoutParams(params);
        getPopupContentView().setOnTouchListener(new OnTouchListener() {
            private float x, y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 计算implView的Rect
                View implView = ((ViewGroup) getPopupContentView()).getChildAt(0);
                int[] location = new int[2];
                implView.getLocationInWindow(location);
                Rect implViewRect = new Rect(location[0], location[1], location[0] + implView.getMeasuredWidth(),
                        location[1] + implView.getMeasuredHeight());
                if (!XPopupUtils.isInRect(event.getRawX(), event.getRawY(), implViewRect)) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = event.getX();
                            y = event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            float dx = event.getX() - x;
                            float dy = event.getY() - y;
                            float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                            if (distance < ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                                if (popupAttrs.isDismissOnTouchOutside)
                                    dismiss();
                            }
                            x = 0;
                            y = 0;
                            break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected AbsAnimator getPopupAnimator() {
        return new TranslateAnimator(getPopupImplView(), isShowUp ?
                AnimationFactory.translateFromBottom(false) :
                AnimationFactory.translateFromTop(false));
    }

}
