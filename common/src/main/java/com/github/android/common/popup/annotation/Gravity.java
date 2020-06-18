package com.github.android.common.popup.annotation;

/**
 * Created by fxb on 2020/5/7.
 * 用途：
 * 1.用于动画参考弹窗本身
 * 2.弹窗依附目标View时参照物为目标View
 */
public class Gravity {
    public static final int LEFT = 0x0001;
    public static final int TOP = 0x0002;
    public static final int RIGHT = 0x0004;
    public static final int BOTTOM = 0x0008;
    public static final int CENTER = LEFT | TOP | RIGHT | BOTTOM;
}
