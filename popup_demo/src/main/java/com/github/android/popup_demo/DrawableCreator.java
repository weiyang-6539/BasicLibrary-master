package com.github.android.popup_demo;

import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;

/**
 * Created by fxb on 2020/6/5.
 */
public class DrawableCreator {
    public static GradientDrawable getDrawable(@ColorInt int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadii(new float[]{
                20, 20,
                20, 20,
                0, 0,
                0, 0
        });
        return drawable;
    }
}
