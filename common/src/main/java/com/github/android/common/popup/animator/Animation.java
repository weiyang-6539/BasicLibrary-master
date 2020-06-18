package com.github.android.common.popup.animator;

import com.github.android.common.popup.annotation.AnimType;
import com.github.android.common.popup.annotation.Gravity;

/**
 * Created by fxb on 2020/5/8.
 */
public class Animation {
    @AnimType
    private int type;
    private int gravity;
    /**
     * 弹窗是否有透明度变化
     */
    private boolean alpha;

    public Animation(@AnimType int type) {
        this.type = type;
    }

    public Animation(@AnimType int type, int gravity, boolean alpha) {
        this.type = type;
        this.gravity = gravity;
        this.alpha = alpha;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

    public void addGravity(int gravity) {
        this.gravity |= gravity;
    }

    public int getType() {
        return type;
    }

    public int getGravity() {
        return gravity;
    }

    public boolean isEmptyType() {
        return type == AnimType.EMPTY;
    }

    public boolean isTranslateType() {
        return type == AnimType.TRANSLATE;
    }

    public boolean isScaleType() {
        return type == AnimType.SCALE;
    }

    public boolean isScrollType() {
        return type == AnimType.SCROLL;
    }

    public boolean isAlpha() {
        return alpha;
    }

    public boolean isCenter() {
        return gravity == Gravity.CENTER;
    }

    public boolean isLeft() {
        return gravity == Gravity.LEFT;
    }

    public boolean isTop() {
        return gravity == Gravity.TOP;
    }

    public boolean isRight() {
        return gravity == Gravity.RIGHT;
    }

    public boolean isBottom() {
        return gravity == Gravity.BOTTOM;
    }

    public boolean isLeftTop() {
        int temp = Gravity.LEFT | Gravity.TOP;
        return temp == (temp & gravity);
    }

    public boolean isRightTop() {
        int temp = Gravity.RIGHT | Gravity.TOP;
        return temp == (temp & gravity);
    }

    public boolean isLeftBottom() {
        int temp = Gravity.LEFT | Gravity.BOTTOM;
        return temp == (temp & gravity);
    }

    public boolean isRightBottom() {
        int temp = Gravity.RIGHT | Gravity.BOTTOM;
        return temp == (temp & gravity);
    }
}
