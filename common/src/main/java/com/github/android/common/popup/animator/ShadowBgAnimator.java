package com.github.android.common.popup.animator;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * Created by fxb on 2020/5/8.
 * 背景Shadow动画器，负责执行半透明的渐入渐出动画
 */
public class ShadowBgAnimator extends AbsAnimator {
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private int startColor = Color.TRANSPARENT;
    private int endColor = Color.parseColor("#9F000000");

    public boolean isZeroDuration = false;

    public ShadowBgAnimator(View target) {
        super(target);
    }

    public ShadowBgAnimator() {
    }

    @Override
    public void initAnimator() {
        targetView.setBackgroundColor(startColor);
    }

    @Override
    public void animateShow() {
        ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator, startColor, endColor);
        animator.addUpdateListener(animation ->
                targetView.setBackgroundColor((Integer) animation.getAnimatedValue()));
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setDuration(isZeroDuration ? 0 : duration).start();
    }

    @Override
    public void animateDismiss() {
        ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator, endColor, startColor);
        animator.addUpdateListener(animation ->
                targetView.setBackgroundColor((Integer) animation.getAnimatedValue()));
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setDuration(isZeroDuration ? 0 : duration).start();
    }

    public int calculateBgColor(float fraction) {
        return (int) argbEvaluator.evaluate(fraction, startColor, endColor);
    }
}
