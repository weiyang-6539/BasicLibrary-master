package com.github.android.common.popup.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.github.android.common.popup.animator.AbsAnimator;
import com.github.android.common.popup.animator.Animation;
import com.github.android.common.popup.animator.ScaleAnimator;
import com.github.android.common.popup.annotation.AnimType;
import com.github.android.common.popup.annotation.AttachType;
import com.github.android.common.popup.annotation.Gravity;
import com.github.android.common.popup.util.XPopupUtils;
import com.github.android.common.utils.ScreenUtils;

/**
 * Created by fxb on 2020/5/17.
 */
public abstract class AbsAttachPopupView extends BasePopupView {

    /**
     * 依附某个点
     */
    private int x, y;

    /**
     * 依附某个View所用属性
     */
    private View atView;
    private int xType, yType;

    public AbsAttachPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        XPopupUtils.applyPopupSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(), this::calculateTranslation);
    }

    public void resetPoint(int x, int y) {
        if (this.x != x || this.y != y) {
            this.x = x;
            this.y = y;

            post(this::calculateTranslation);
        }
    }

    private void calculateTranslation() {
        int transX = popupAttrs.offsetX;
        int transY = popupAttrs.offsetY;

        //依附View弹窗
        if (atView != null) {
            int[] ints = XPopupUtils.getLocation(atView);

            if (xType == AttachType.CENTER_HORIZONTAL) {
                transX += ints[0] - (getPopupContentView().getMeasuredWidth() - atView.getMeasuredWidth()) / 2;
            } else if (xType == AttachType.LEFT_TO_LEFT) {
                transX += ints[0];
            } else if (xType == AttachType.LEFT_TO_RIGHT) {
                transX += ints[0] + atView.getMeasuredWidth();
            } else if (xType == AttachType.RIGHT_TO_LEFT) {
                transX += ints[0] - getPopupContentView().getMeasuredWidth();
            } else if (xType == AttachType.RIGHT_TO_RIGHT) {
                transX += ints[0] + atView.getMeasuredWidth() - getPopupContentView().getMeasuredWidth();
            }

            if (yType == AttachType.CENTER_VERTICAL) {
                transY += ints[1] - (getPopupContentView().getMeasuredHeight() - atView.getMeasuredHeight()) / 2;
            } else if (yType == AttachType.TOP_TO_TOP) {
                transY += ints[1];
            } else if (yType == AttachType.TOP_TO_BOTTOM) {
                transY += ints[1] + atView.getMeasuredHeight();
            } else if (yType == AttachType.BOTTOM_TO_TOP) {
                transY += ints[1] - getPopupContentView().getMeasuredHeight();
            } else if (yType == AttachType.BOTTOM_TO_BOTTOM) {
                transY += ints[1] + atView.getMeasuredHeight() - getPopupContentView().getMeasuredHeight();
            }
        }

        //依附点弹窗
        if (x != 0 || y != 0) {
            transX += x;
            transY += y;

            int centerX = XPopupUtils.getWindowWidth(getContext()) / 2;
            int centerY = (XPopupUtils.getWindowHeight(getContext()) - XPopupUtils.getStatusBarHeight()) / 2;

            boolean isShowRight = transX > centerX;
            boolean isShowBottom = transY > centerY;
            if (isShowRight) {
                transX -= getPopupContentView().getMeasuredWidth();
            }

            if (isShowBottom) {
                transY -= getPopupContentView().getMeasuredHeight();
            }

            int hGravity = isShowRight ? Gravity.RIGHT : Gravity.LEFT;
            int vGravity = isShowBottom ? Gravity.BOTTOM : Gravity.TOP;

            int gravity = hGravity | vGravity;
            popupContentAnimator = new ScaleAnimator(getPopupContentView(), new Animation(AnimType.SCALE, gravity, true));
            popupContentAnimator.setDuration(popupAttrs.animDuration);
            popupContentAnimator.initAnimator();
        }

        //修正横纵偏移
        if (transX < 0) {
            transX = 0;
        }
        if (transX + getPopupContentView().getMeasuredWidth() > ScreenUtils.getScreenWidth(getContext())) {
            transX -= transX + getPopupContentView().getMeasuredWidth() - ScreenUtils.getScreenWidth(getContext());
        }

        //修正纵轴偏移
        if (transY < XPopupUtils.getStatusBarHeight()) {
            transY = XPopupUtils.getStatusBarHeight();
        }
        if (transY + getPopupContentView().getMeasuredHeight() > ScreenUtils.getScreenHeight(getContext())) {
            transY -= transY + getPopupContentView().getMeasuredHeight() - ScreenUtils.getScreenHeight(getContext());
        }

        getPopupContentView().setTranslationX(transX);
        getPopupContentView().setTranslationY(transY);
    }

    @Override
    protected AbsAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }

    public AbsAttachPopupView atView(View view, @AttachType int xType, @AttachType int yType) {
        this.atView = view;
        this.xType = xType;
        this.yType = yType;
        return this;
    }
}
