package com.github.android.common.popup.animator;

import com.github.android.common.popup.annotation.AnimType;
import com.github.android.common.popup.annotation.Gravity;

/**
 * Created by fxb on 2020/5/13.
 */
public class AnimationFactory {

    public static Animation empty() {
        return new Animation(AnimType.EMPTY);
    }

    public static Animation scaleFromLeft(boolean alpha) {
        return new Animation(AnimType.SCALE, Gravity.LEFT, alpha);
    }

    public static Animation scaleFromTop(boolean alpha) {
        return new Animation(AnimType.SCALE, Gravity.TOP, alpha);
    }

    public static Animation scaleFromRight(boolean alpha) {
        return new Animation(AnimType.SCALE, Gravity.RIGHT, alpha);
    }

    public static Animation scaleFromBottom(boolean alpha) {
        return new Animation(AnimType.SCALE, Gravity.BOTTOM, alpha);
    }

    public static Animation scaleFromCenter(boolean alpha) {
        return new Animation(AnimType.SCALE, Gravity.CENTER, alpha);
    }

    public static Animation scaleFromLeftTop(boolean alpha) {
        return new Animation(AnimType.SCALE, Gravity.LEFT | Gravity.TOP, alpha);
    }

    public static Animation scaleFromLeftBottom(boolean alpha) {
        return new Animation(AnimType.SCALE, Gravity.LEFT | Gravity.BOTTOM, alpha);
    }

    public static Animation scaleFromRightTop(boolean alpha) {
        return new Animation(AnimType.SCALE, Gravity.RIGHT | Gravity.TOP, alpha);
    }

    public static Animation scaleFromRightBottom(boolean alpha) {
        return new Animation(AnimType.SCALE, Gravity.RIGHT | Gravity.BOTTOM, alpha);
    }

    public static Animation translateFromLeft(boolean alpha) {
        return new Animation(AnimType.TRANSLATE, Gravity.LEFT, alpha);
    }

    public static Animation translateFromTop(boolean alpha) {
        return new Animation(AnimType.TRANSLATE, Gravity.TOP, alpha);
    }

    public static Animation translateFromRight(boolean alpha) {
        return new Animation(AnimType.TRANSLATE, Gravity.RIGHT, alpha);
    }

    public static Animation translateFromBottom(boolean alpha) {
        return new Animation(AnimType.TRANSLATE, Gravity.BOTTOM, alpha);
    }

    public static Animation scrollFromLeft(boolean alpha) {
        return new Animation(AnimType.SCROLL, Gravity.LEFT, alpha);
    }

    public static Animation scrollFromTop(boolean alpha) {
        return new Animation(AnimType.SCROLL, Gravity.TOP, alpha);
    }

    public static Animation scrollFromRight(boolean alpha) {
        return new Animation(AnimType.SCROLL, Gravity.RIGHT, alpha);
    }

    public static Animation scrollFromBottom(boolean alpha) {
        return new Animation(AnimType.SCROLL, Gravity.BOTTOM, alpha);
    }

    public static Animation scrollFromLeftTop(boolean alpha) {
        return new Animation(AnimType.SCROLL, Gravity.LEFT | Gravity.TOP, alpha);
    }

    public static Animation scrollFromRightTop(boolean alpha) {
        return new Animation(AnimType.SCROLL, Gravity.RIGHT | Gravity.TOP, alpha);
    }

    public static Animation scrollFromLeftBottom(boolean alpha) {
        return new Animation(AnimType.SCROLL, Gravity.LEFT | Gravity.BOTTOM, alpha);
    }

    public static Animation scrollFromRightBottom(boolean alpha) {
        return new Animation(AnimType.SCROLL, Gravity.RIGHT | Gravity.BOTTOM, alpha);
    }
}
