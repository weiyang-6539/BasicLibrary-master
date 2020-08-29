package com.github.android.common.widget.shape.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.android.common.R;

/**
 * Created by fxb on 2020/8/27.
 */
public class ShapeAttrsHelper {
    public static ShapeBuilder initShapeBuilderByAttrs(Context context, AttributeSet attrs) {
        ShapeBuilder builder = new ShapeBuilder();
        if (attrs == null)
            return builder;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
        int selectorNormalColor = a.getColor(R.styleable.ShapeView_shapeSelectorNormalColor, Color.TRANSPARENT);
        int selectorPressedColor = a.getColor(R.styleable.ShapeView_shapeSelectorPressedColor, Color.TRANSPARENT);
        int selectorDisableColor = a.getColor(R.styleable.ShapeView_shapeSelectorDisableColor, Color.TRANSPARENT);
        int selectorSelectedColor = a.getColor(R.styleable.ShapeView_shapeSelectorSelectedColor, Color.TRANSPARENT);
        int selectorFocusedColor = a.getColor(R.styleable.ShapeView_shapeSelectorFocusedColor, Color.TRANSPARENT);
        int selectorCheckedColor = a.getColor(R.styleable.ShapeView_shapeSelectorCheckedColor, Color.TRANSPARENT);
        int gradientStartColor = a.getColor(R.styleable.ShapeView_shapeGradientStartColor, Color.TRANSPARENT);
        int gradientCenterColor = a.getColor(R.styleable.ShapeView_shapeGradientCenterColor, Color.TRANSPARENT);
        int gradientEndColor = a.getColor(R.styleable.ShapeView_shapeGradientEndColor, Color.TRANSPARENT);
        int gradientAngle = a.getInt(R.styleable.ShapeView_shapeGradientAngle, 0);
        boolean gradientUseLevel = a.getBoolean(R.styleable.ShapeView_shapeGradientUseLevel, false);
        float cornersRadius = a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersRadius, 0);
        float cornersTopLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersTopLeftRadius, 0);
        float cornersTopRightRadius = a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersTopRightRadius, 0);
        float cornersBotLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersBotLeftRadius, 0);
        float cornersBotRightRadius = a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersBotRightRadius, 0);
        int strokeWidth = a.getDimensionPixelSize(R.styleable.ShapeView_shapeStrokeWidth, 0);
        int strokeNormalColor = a.getColor(R.styleable.ShapeView_shapeStrokeNormalColor, Color.TRANSPARENT);
        int strokePressedColor = a.getColor(R.styleable.ShapeView_shapeStrokePressedColor, Color.TRANSPARENT);
        int strokeDisableColor = a.getColor(R.styleable.ShapeView_shapeStrokeDisableColor, Color.TRANSPARENT);
        int strokeSelectedColor = a.getColor(R.styleable.ShapeView_shapeStrokeSelectedColor, Color.TRANSPARENT);
        int strokeFocusedColor = a.getColor(R.styleable.ShapeView_shapeStrokeFocusedColor, Color.TRANSPARENT);
        int strokeCheckedColor = a.getColor(R.styleable.ShapeView_shapeStrokeCheckedColor, Color.TRANSPARENT);
        int strokeDashWidth = a.getDimensionPixelSize(R.styleable.ShapeView_shapeStrokeDashWidth, 0);
        int strokeDashGap = a.getDimensionPixelSize(R.styleable.ShapeView_shapeStrokeDashGap, 0);
        int elevation = a.getDimensionPixelSize(R.styleable.ShapeView_shapeElevation, 0);
        boolean clickable = a.getBoolean(R.styleable.ShapeView_shapeClickable, true);
        boolean ripple = a.getBoolean(R.styleable.ShapeView_shapeRipple, false);
        a.recycle();

        return builder
                .setSelectorNormalColor(selectorNormalColor)
                .setSelectorPressedColor(selectorPressedColor)
                .setSelectorDisableColor(selectorDisableColor)
                .setSelectorSelectedColor(selectorSelectedColor)
                .setSelectorFocusedColor(selectorFocusedColor)
                .setSelectorCheckedColor(selectorCheckedColor)
                .setGradientStartColor(gradientStartColor)
                .setGradientCenterColor(gradientCenterColor)
                .setGradientEndColor(gradientEndColor)
                .setGradientAngle(gradientAngle)
                .setGradientUseLevel(gradientUseLevel)
                .setCornersTopLeftRadius(cornersTopLeftRadius)
                .setCornersTopRightRadius(cornersTopRightRadius)
                .setCornersBotLeftRadius(cornersBotLeftRadius)
                .setCornersBotRightRadius(cornersBotRightRadius)
                .setCornersRadius(cornersRadius)
                .setStrokeWidth(strokeWidth)
                .setStrokeNormalColor(strokeNormalColor)
                .setStrokePressedColor(strokePressedColor)
                .setStrokeDisableColor(strokeDisableColor)
                .setStrokeSelectedColor(strokeSelectedColor)
                .setStrokeFocusedColor(strokeFocusedColor)
                .setStrokeCheckedColor(strokeCheckedColor)
                .setStrokeDashWidth(strokeDashWidth)
                .setStrokeDashGap(strokeDashGap)
                .setElevation(elevation)
                .setClickable(clickable)
                .setRipple(ripple);
    }
}
