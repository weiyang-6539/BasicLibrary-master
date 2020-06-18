package com.github.android.common.popup.animator;

import android.animation.ValueAnimator;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;

/**
 * Created by fxb on 2020/5/15.
 */
public class TranslateAnimator extends AbsAnimator {
    //动画起始坐标
    private float startTranslationX, startTranslationY;
    private int oldWidth, oldHeight;
    private float initTranslationX, initTranslationY;
    private boolean hasInitDefTranslation = false;

    public TranslateAnimator(View target, Animation animation) {
        super(target, animation);
    }

    @Override
    public void initAnimator() {
        if (!hasInitDefTranslation) {
            initTranslationX = targetView.getTranslationX();
            initTranslationY = targetView.getTranslationY();
            hasInitDefTranslation = true;
        }
        // 设置起始坐标
        applyTranslation();
        startTranslationX = targetView.getTranslationX();
        startTranslationY = targetView.getTranslationY();

        oldWidth = targetView.getMeasuredWidth();
        oldHeight = targetView.getMeasuredHeight();
    }

    private void applyTranslation() {
        if (animation.isLeft())
            targetView.setTranslationX(-targetView.getRight());
        else if (animation.isTop())
            targetView.setTranslationY(-targetView.getBottom());
        else if (animation.isRight())
            targetView.setTranslationX(((View) targetView.getParent()).getMeasuredWidth() - targetView.getLeft());
        else if (animation.isBottom())
            targetView.setTranslationY(((View) targetView.getParent()).getMeasuredHeight() - targetView.getTop());
    }

    @Override
    public void animateShow() {
        targetView.animate()
                .translationX(initTranslationX)
                .translationY(initTranslationY)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(duration)
                .start();
    }

    @Override
    public void animateDismiss() {
        //执行消失动画的时候，宽高可能改变了，所以需要修正动画的起始值

        if (animation.isLeft())
            startTranslationX -= targetView.getMeasuredWidth() - oldWidth;
        else if (animation.isTop())
            startTranslationY -= targetView.getMeasuredHeight() - oldHeight;
        else if (animation.isRight())
            startTranslationX += targetView.getMeasuredWidth() - oldWidth;
        else if (animation.isBottom())
            startTranslationY += targetView.getMeasuredHeight() - oldHeight;

        targetView.animate()
                .translationX(startTranslationX)
                .translationY(startTranslationY)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(duration)
                .start();
    }
}
