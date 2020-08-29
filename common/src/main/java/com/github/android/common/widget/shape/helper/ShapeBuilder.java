package com.github.android.common.widget.shape.helper;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

/**
 * Created by fxb on 2020/8/28.
 * View的背景设置类，这里只使用到shape中的rectangle（ring oval line 均未使用）
 */
public class ShapeBuilder {
    /**
     * shape背景状态色
     */
    private int selectorNormalColor;
    private int selectorPressedColor;
    private int selectorDisableColor;
    private int selectorSelectedColor;
    private int selectorFocusedColor;
    private int selectorCheckedColor;
    /**
     * 渐变色属性
     */
    private int gradientStartColor;
    private int gradientCenterColor;
    private int gradientEndColor;
    private int gradientAngle;
    private boolean gradientUseLevel;
    /**
     * shape圆角半径
     */
    private float[] radii = new float[8];
    /**
     * shape边框线，颜色与shape背景色数量一样
     */
    private int strokeWidth;
    private int strokeNormalColor;
    private int strokePressedColor;
    private int strokeDisableColor;
    private int strokeSelectedColor;
    private int strokeFocusedColor;
    private int strokeCheckedColor;
    private int strokeDashWidth;
    private int strokeDashGap;
    /**
     * 阴影
     */
    private int elevation;
    /**
     * 是否可以被点击
     */
    private boolean clickable;
    /**
     * 按压效果是否启用波纹动画，需要selectorPressedColor!=Color.TRANSPARENT 和 5.0及以上系统才有效
     */
    private boolean ripple;

    ShapeBuilder setSelectorNormalColor(int selectorNormalColor) {
        this.selectorNormalColor = selectorNormalColor;
        return this;
    }

    ShapeBuilder setSelectorPressedColor(int selectorPressedColor) {
        this.selectorPressedColor = selectorPressedColor;
        return this;
    }

    ShapeBuilder setSelectorDisableColor(int selectorDisableColor) {
        this.selectorDisableColor = selectorDisableColor;
        return this;
    }

    ShapeBuilder setSelectorSelectedColor(int selectorSelectedColor) {
        this.selectorSelectedColor = selectorSelectedColor;
        return this;
    }

    ShapeBuilder setSelectorFocusedColor(int selectorFocusedColor) {
        this.selectorFocusedColor = selectorFocusedColor;
        return this;
    }

    ShapeBuilder setSelectorCheckedColor(int selectorCheckedColor) {
        this.selectorCheckedColor = selectorCheckedColor;
        return this;
    }

    ShapeBuilder setGradientStartColor(int gradientStartColor) {
        this.gradientStartColor = gradientStartColor;
        return this;
    }

    ShapeBuilder setGradientCenterColor(int gradientCenterColor) {
        this.gradientCenterColor = gradientCenterColor;
        return this;
    }

    ShapeBuilder setGradientEndColor(int gradientEndColor) {
        this.gradientEndColor = gradientEndColor;
        return this;
    }

    ShapeBuilder setGradientAngle(int gradientAngle) {
        this.gradientAngle = gradientAngle;
        return this;
    }

    ShapeBuilder setGradientUseLevel(boolean gradientUseLevel) {
        this.gradientUseLevel = gradientUseLevel;
        return this;
    }

    ShapeBuilder setCornersTopLeftRadius(float cornersTopLeftRadius) {
        this.radii[0] = cornersTopLeftRadius;
        this.radii[1] = cornersTopLeftRadius;
        return this;
    }

    ShapeBuilder setCornersTopRightRadius(float cornersTopRightRadius) {
        this.radii[2] = cornersTopRightRadius;
        this.radii[3] = cornersTopRightRadius;
        return this;
    }

    ShapeBuilder setCornersBotRightRadius(float cornersBotRightRadius) {
        this.radii[4] = cornersBotRightRadius;
        this.radii[5] = cornersBotRightRadius;
        return this;
    }

    ShapeBuilder setCornersBotLeftRadius(float cornersBotLeftRadius) {
        this.radii[6] = cornersBotLeftRadius;
        this.radii[7] = cornersBotLeftRadius;
        return this;
    }

    ShapeBuilder setCornersRadius(float cornersRadius) {
        if (cornersRadius != 0) {
            Arrays.fill(radii, cornersRadius);
        }
        return this;
    }

    ShapeBuilder setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    ShapeBuilder setStrokeNormalColor(int strokeNormalColor) {
        this.strokeNormalColor = strokeNormalColor;
        return this;
    }

    ShapeBuilder setStrokePressedColor(int strokePressedColor) {
        this.strokePressedColor = strokePressedColor;
        return this;
    }

    ShapeBuilder setStrokeDisableColor(int strokeDisableColor) {
        this.strokeDisableColor = strokeDisableColor;
        return this;
    }

    ShapeBuilder setStrokeSelectedColor(int strokeSelectedColor) {
        this.strokeSelectedColor = strokeSelectedColor;
        return this;
    }

    ShapeBuilder setStrokeFocusedColor(int strokeFocusedColor) {
        this.strokeFocusedColor = strokeFocusedColor;
        return this;
    }

    ShapeBuilder setStrokeCheckedColor(int strokeCheckedColor) {
        this.strokeCheckedColor = strokeCheckedColor;
        return this;
    }

    ShapeBuilder setStrokeDashWidth(int strokeDashWidth) {
        this.strokeDashWidth = strokeDashWidth;
        return this;
    }

    ShapeBuilder setStrokeDashGap(int strokeDashGap) {
        this.strokeDashGap = strokeDashGap;
        return this;
    }

    ShapeBuilder setElevation(int elevation) {
        this.elevation = elevation;
        return this;
    }

    ShapeBuilder setClickable(boolean clickable) {
        this.clickable = clickable;
        return this;
    }

    ShapeBuilder setRipple(boolean ripple) {
        this.ripple = ripple;
        return this;
    }

    private GradientDrawable getDrawable(@State int state) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        //设置填充颜色，优先填充solidColor和渐变色，若状态选择器有值，会覆盖这一操作
        if (!isTransparent(gradientStartColor) || !isTransparent(gradientEndColor)) {
            //渐变色和默认背景色不同时使用
            selectorNormalColor = Color.TRANSPARENT;
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (isTransparent(gradientCenterColor)) {
                    gradientDrawable.setColors(new int[]{gradientStartColor, gradientEndColor});
                } else {
                    gradientDrawable.setColors(new int[]{gradientStartColor, gradientCenterColor, gradientEndColor});
                }

                if (gradientAngle == 0)
                    gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                else if (gradientAngle == 1)
                    gradientDrawable.setOrientation(GradientDrawable.Orientation.BL_TR);
                else if (gradientAngle == 2)
                    gradientDrawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                else if (gradientAngle == 3)
                    gradientDrawable.setOrientation(GradientDrawable.Orientation.BR_TL);
                else if (gradientAngle == 4)
                    gradientDrawable.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                else if (gradientAngle == 5)
                    gradientDrawable.setOrientation(GradientDrawable.Orientation.TR_BL);
                else if (gradientAngle == 6)
                    gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                else if (gradientAngle == 7)
                    gradientDrawable.setOrientation(GradientDrawable.Orientation.TL_BR);

            } else if (!isTransparent(gradientStartColor)) {
                gradientDrawable.setColor(gradientStartColor);
            } else if (!isTransparent(gradientEndColor)) {
                gradientDrawable.setColor(gradientEndColor);
            }
            gradientDrawable.setUseLevel(gradientUseLevel);
        }

        //填充状态选择器的颜色
        switch (state) {
            case State.NONE:
            case State.ENABLED:
                if (!isTransparent(selectorNormalColor))
                    gradientDrawable.setColor(selectorNormalColor);
                break;
            case State.PRESSED:
                gradientDrawable.setColor(selectorPressedColor);
                break;
            case State.DISABLE:
                gradientDrawable.setColor(selectorDisableColor);
                break;
            case State.SELECTED:
                gradientDrawable.setColor(selectorSelectedColor);
                break;
            case State.FOCUSED:
                gradientDrawable.setColor(selectorFocusedColor);
                break;
            case State.CHECKED:
                gradientDrawable.setColor(selectorCheckedColor);
                break;
        }

        //设置边框线
        switch (state) {
            case State.NONE:
            case State.ENABLED:
                gradientDrawable.setStroke(strokeWidth, strokeNormalColor, strokeDashWidth, strokeDashGap);
                break;
            case State.PRESSED:
                gradientDrawable.setStroke(strokeWidth, strokePressedColor, strokeDashWidth, strokeDashGap);
                break;
            case State.DISABLE:
                gradientDrawable.setStroke(strokeWidth, strokeDisableColor, strokeDashWidth, strokeDashGap);
                break;
            case State.SELECTED:
                gradientDrawable.setStroke(strokeWidth, strokeSelectedColor, strokeDashWidth, strokeDashGap);
                break;
            case State.FOCUSED:
                gradientDrawable.setStroke(strokeWidth, strokeFocusedColor, strokeDashWidth, strokeDashGap);
                break;
            case State.CHECKED:
                gradientDrawable.setStroke(strokeWidth, strokeCheckedColor, strokeDashWidth, strokeDashGap);
                break;
        }

        //设置圆角
        gradientDrawable.setCornerRadii(radii);
        return gradientDrawable;
    }

    private Drawable getBackground() {
        StateListDrawable background = new StateListDrawable();
        //添加按压色
        if (!isTransparent(selectorPressedColor) || !isTransparent(strokePressedColor))
            background.addState(new int[]{State.PRESSED, State.ENABLED}, getDrawable(State.PRESSED));
        //禁用填充色
        if (!isTransparent(selectorDisableColor) || !isTransparent(strokeDisableColor))
            background.addState(new int[]{State.DISABLE}, getDrawable(State.DISABLE));
        //选中填充色1
        if (!isTransparent(selectorSelectedColor) || !isTransparent(strokeSelectedColor))
            background.addState(new int[]{State.SELECTED, State.ENABLED}, getDrawable(State.SELECTED));
        //焦点填充色
        if (!isTransparent(selectorFocusedColor) || !isTransparent(strokeFocusedColor))
            background.addState(new int[]{State.FOCUSED, State.ENABLED}, getDrawable(State.FOCUSED));
        //选中填充色2
        if (!isTransparent(selectorCheckedColor) || !isTransparent(strokeCheckedColor))
            background.addState(new int[]{State.CHECKED, State.ENABLED}, getDrawable(State.CHECKED));
        //默认填充色，最后添加
        background.addState(new int[]{-State.SELECTED, -State.FOCUSED, -State.CHECKED}, getDrawable(State.ENABLED));

        if (ripple && !isTransparent(selectorPressedColor) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(selectorPressedColor), background, getDrawable(State.PRESSED));
        } else
            return background;
    }

    public void apply(View view) {
        //5.0版本及以上
        if (elevation > 0)
            ViewCompat.setElevation(view, elevation);

        view.setClickable(clickable);
        ViewCompat.setBackground(view, getBackground());
    }

    private boolean isTransparent(int color) {
        return color == Color.TRANSPARENT;
    }

    @IntDef({State.NONE, State.ENABLED, State.PRESSED, State.DISABLE, State.SELECTED, State.FOCUSED, State.CHECKED})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {
        int NONE = 0;
        int ENABLED = android.R.attr.state_enabled;
        int PRESSED = android.R.attr.state_pressed;
        int DISABLE = -android.R.attr.state_enabled;
        int SELECTED = android.R.attr.state_selected;
        int FOCUSED = android.R.attr.state_focused;
        int CHECKED = android.R.attr.state_checked;
    }

    private Path mClipPath = new Path();
    private Region mAreaRegion = new Region();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public void onSizeChanged(View view, int w, int h) {
        RectF areas = new RectF();
        areas.left = view.getPaddingLeft();
        areas.top = view.getPaddingTop();
        areas.right = w - view.getPaddingRight();
        areas.bottom = h - view.getPaddingBottom();
        mClipPath.reset();
        mClipPath.addRoundRect(areas, radii, Path.Direction.CW);

        Region clip = new Region((int) areas.left, (int) areas.top,
                (int) areas.right, (int) areas.bottom);
        mAreaRegion.setPath(mClipPath, clip);
    }

    public void dispatchDraw(Canvas canvas) {
        // 剪裁
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mClipPath, mPaint);
        canvas.restore();
    }

    public boolean isEffective(MotionEvent ev) {
        return mAreaRegion.contains((int) ev.getX(), (int) ev.getY());
    }
}
