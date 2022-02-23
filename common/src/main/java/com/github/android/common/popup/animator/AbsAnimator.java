package com.github.android.common.popup.animator;

import android.view.View;

import androidx.annotation.IntRange;

/**
 * Created by fxb on 2020/5/7.
 * 弹窗动画抽象类
 */
public abstract class AbsAnimator {
    public View targetView;
    protected Animation animation; // 内置的动画
    int duration = 300;//动画时间，默认300ms

    public int getDuration() {
        return duration;
    }

    public void setDuration(@IntRange(from = 300) int duration) {
        this.duration = duration;
    }

    public AbsAnimator() {
    }

    public AbsAnimator(View target) {
        this(target, null);
    }

    public AbsAnimator(View target, Animation animation) {
        this.targetView = target;
        this.animation = animation;
    }

    public abstract void initAnimator();

    public abstract void animateShow();

    public abstract void animateDismiss();
}
