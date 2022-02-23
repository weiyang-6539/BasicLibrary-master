package com.github.android.common.popup.animator;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * Created by fxb on 2020/5/15.
 */
public class ScrollAnimator extends AbsAnimator {
    private FloatEvaluator floatEvaluator = new FloatEvaluator();
    private IntEvaluator intEvaluator = new IntEvaluator();
    private int startScrollX, startScrollY;
    private float startAlpha = .2f;
    private float startScale = 0f;

    public boolean isOnlyScaleX = false;

    public ScrollAnimator(View target, Animation animation) {
        super(target, animation);
    }

    @Override
    public void initAnimator() {
        targetView.setAlpha(startAlpha);
        targetView.setScaleX(startScale);
        if (!isOnlyScaleX) {
            targetView.setScaleY(startScale);
        }

        targetView.post(() -> {
            // 设置参考点
            applyPivot();
            targetView.scrollTo(startScrollX, startScrollY);
            if (targetView.getBackground() != null)
                targetView.getBackground().setAlpha(0);
        });
    }

    @Override
    public void animateShow() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            targetView.setAlpha(floatEvaluator.evaluate(fraction, startAlpha, 1f));
            targetView.scrollTo(intEvaluator.evaluate(fraction, startScrollX, 0),
                    intEvaluator.evaluate(fraction, startScrollY, 0));
            float scale = floatEvaluator.evaluate(fraction, startScale, 1f);
            targetView.setScaleX(scale);
            if (!isOnlyScaleX) targetView.setScaleY(scale);
            if (fraction >= .9f && targetView.getBackground() != null) {
                float alphaFraction = (fraction - .9f) / .1f;
                targetView.getBackground().setAlpha((int) (alphaFraction * 255));
            }
        });
        animator.setDuration(duration)
                .setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
    }

    @Override
    public void animateDismiss() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            targetView.setAlpha(floatEvaluator.evaluate(fraction, 1f, startAlpha));
            targetView.scrollTo(intEvaluator.evaluate(fraction, 0, startScrollX),
                    intEvaluator.evaluate(fraction, 0, startScrollY));
            float scale = floatEvaluator.evaluate(fraction, 1f, startScale);
            targetView.setScaleX(scale);
            if (!isOnlyScaleX) targetView.setScaleY(scale);
            if (targetView.getBackground() != null)
                targetView.getBackground().setAlpha((int) (fraction * 255));
        });
        animator.setDuration(duration)
                .setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
    }
    
    private void applyPivot() {
        if (animation.isLeft()) {
            targetView.setPivotX(0f);
            targetView.setPivotY(targetView.getMeasuredHeight() >> 1);

            startScrollX = targetView.getMeasuredWidth();
            startScrollY = 0;
        } else if (animation.isTop()) {
            targetView.setPivotX(targetView.getMeasuredWidth() >> 1);
            targetView.setPivotY(0f);

            startScrollX = 0;
            startScrollY = targetView.getMeasuredHeight();
        } else if (animation.isRight()) {
            targetView.setPivotX(targetView.getMeasuredWidth());
            targetView.setPivotY(targetView.getMeasuredHeight() >> 1);

            startScrollX = -targetView.getMeasuredWidth();
            startScrollY = 0;
        } else if (animation.isBottom()) {
            targetView.setPivotX(targetView.getMeasuredWidth() >> 1);
            targetView.setPivotY(targetView.getMeasuredHeight());

            startScrollX = 0;
            startScrollY = -targetView.getMeasuredHeight();
        } else if (animation.isLeftTop()) {
            targetView.setPivotX(0f);
            targetView.setPivotY(0f);

            startScrollX = targetView.getMeasuredWidth();
            startScrollY = targetView.getMeasuredHeight();
        } else if (animation.isRightTop()) {
            targetView.setPivotX(targetView.getMeasuredWidth());
            targetView.setPivotY(0f);

            startScrollX = -targetView.getMeasuredWidth();
            startScrollY = targetView.getMeasuredHeight();
        } else if (animation.isLeftBottom()) {
            targetView.setPivotX(0);
            targetView.setPivotY(targetView.getMeasuredHeight());

            startScrollX = targetView.getMeasuredWidth();
            startScrollY = -targetView.getMeasuredHeight();
        } else if (animation.isRightBottom()) {
            targetView.setPivotX(targetView.getMeasuredWidth());
            targetView.setPivotY(targetView.getMeasuredHeight());

            startScrollX = -targetView.getMeasuredWidth();
            startScrollY = -targetView.getMeasuredHeight();
        }

    }
}
