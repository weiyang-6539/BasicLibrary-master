package com.github.android.common.popup.animator;

import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * Created by fxb on 2020/5/8.
 * 缩放动画
 */
public class ScaleAnimator extends AbsAnimator {
    public ScaleAnimator(View target, Animation animation) {
        super(target, animation);
    }

    @Override
    public void initAnimator() {
        targetView.setScaleX(isScaleX() ? 0f : 1f);
        targetView.setScaleY(isScaleY() ? 0f : 1f);
        targetView.setAlpha(animation.isAlpha() ? 0f : 1f);

        // 设置动画参考点
        targetView.post(() -> {
            if (animation.isCenter()) {
                targetView.setPivotX(targetView.getMeasuredWidth() >> 1);
                targetView.setPivotY(targetView.getMeasuredHeight() >> 1);
            } else {
                if (animation.isLeft())
                    targetView.setPivotX(0);
                else if (animation.isRight())
                    targetView.setPivotX(targetView.getMeasuredWidth());
                else if (animation.isTop())
                    targetView.setPivotY(0f);
                else if (animation.isBottom())
                    targetView.setPivotY(targetView.getMeasuredHeight());
                else if (animation.isLeftTop()) {
                    targetView.setPivotX(0);
                    targetView.setPivotY(0f);
                } else if (animation.isRightTop()) {
                    targetView.setPivotX(targetView.getMeasuredWidth());
                    targetView.setPivotY(0f);
                } else if (animation.isLeftBottom()) {
                    targetView.setPivotX(0);
                    targetView.setPivotY(targetView.getMeasuredHeight());
                } else if (animation.isRightBottom()) {
                    targetView.setPivotX(targetView.getMeasuredWidth());
                    targetView.setPivotY(targetView.getMeasuredHeight());
                }
            }
        });
    }

    @Override
    public void animateShow() {
        targetView.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(duration)
                .setInterpolator(new OvershootInterpolator(1f))
                .start();
    }

    @Override
    public void animateDismiss() {
        targetView.animate()
                .scaleX(isScaleX() ? 0f : 1f)
                .scaleY(isScaleY() ? 0f : 1f)
                .alpha(animation.isAlpha() ? 0f : 1f)
                .setDuration(duration)
                .setInterpolator(new FastOutSlowInInterpolator()).start();
    }

    private boolean isScaleX() {
        return !(animation.isTop() || animation.isBottom());
    }

    private boolean isScaleY() {
        return !(animation.isLeft() || animation.isRight());
    }
}
