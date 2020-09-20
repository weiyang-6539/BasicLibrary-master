package com.github.android.common.widget.shape;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.github.android.common.R;

import java.util.Arrays;

/**
 * Created by fxb on 2020/8/28.
 * View的背景设置类，这里只使用到shape中的rectangle（ring oval line 均未使用）
 */
public class ShapeHelper {
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
     * shape圆角半径
     */
    private float[] radii = new float[8];
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

    public ShapeHelper initAttrs(Context context, AttributeSet attrs) {
        return initAttrs(context, attrs, null);
    }

    public ShapeHelper initAttrs(Context context, AttributeSet attrs, ExposeListener listener) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
            selectorNormalColor = a.getColor(R.styleable.ShapeView_shapeSelectorNormalColor, Color.TRANSPARENT);
            selectorPressedColor = a.getColor(R.styleable.ShapeView_shapeSelectorPressedColor, Color.TRANSPARENT);
            selectorDisableColor = a.getColor(R.styleable.ShapeView_shapeSelectorDisableColor, Color.TRANSPARENT);
            selectorSelectedColor = a.getColor(R.styleable.ShapeView_shapeSelectorSelectedColor, Color.TRANSPARENT);
            selectorFocusedColor = a.getColor(R.styleable.ShapeView_shapeSelectorFocusedColor, Color.TRANSPARENT);
            selectorCheckedColor = a.getColor(R.styleable.ShapeView_shapeSelectorCheckedColor, Color.TRANSPARENT);
            gradientStartColor = a.getColor(R.styleable.ShapeView_shapeGradientStartColor, Color.TRANSPARENT);
            gradientCenterColor = a.getColor(R.styleable.ShapeView_shapeGradientCenterColor, Color.TRANSPARENT);
            gradientEndColor = a.getColor(R.styleable.ShapeView_shapeGradientEndColor, Color.TRANSPARENT);
            gradientAngle = a.getInt(R.styleable.ShapeView_shapeGradientAngle, 0);
            gradientUseLevel = a.getBoolean(R.styleable.ShapeView_shapeGradientUseLevel, false);
            strokeWidth = a.getDimensionPixelSize(R.styleable.ShapeView_shapeStrokeWidth, 0);
            strokeNormalColor = a.getColor(R.styleable.ShapeView_shapeStrokeNormalColor, Color.TRANSPARENT);
            strokePressedColor = a.getColor(R.styleable.ShapeView_shapeStrokePressedColor, Color.TRANSPARENT);
            strokeDisableColor = a.getColor(R.styleable.ShapeView_shapeStrokeDisableColor, Color.TRANSPARENT);
            strokeSelectedColor = a.getColor(R.styleable.ShapeView_shapeStrokeSelectedColor, Color.TRANSPARENT);
            strokeFocusedColor = a.getColor(R.styleable.ShapeView_shapeStrokeFocusedColor, Color.TRANSPARENT);
            strokeCheckedColor = a.getColor(R.styleable.ShapeView_shapeStrokeCheckedColor, Color.TRANSPARENT);
            strokeDashWidth = a.getDimensionPixelSize(R.styleable.ShapeView_shapeStrokeDashWidth, 0);
            strokeDashGap = a.getDimensionPixelSize(R.styleable.ShapeView_shapeStrokeDashGap, 0);
            //设置圆角
            setCornersTopLeftRadius(a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersTopLeftRadius, 0));
            setCornersTopRightRadius(a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersTopRightRadius, 0));
            setCornersBotLeftRadius(a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersBotLeftRadius, 0));
            setCornersBotRightRadius(a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersBotRightRadius, 0));
            setCornersRadius(a.getDimensionPixelSize(R.styleable.ShapeView_shapeCornersRadius, 0));
            elevation = a.getDimensionPixelSize(R.styleable.ShapeView_shapeElevation, 0);
            clickable = a.getBoolean(R.styleable.ShapeView_shapeClickable, true);
            ripple = a.getBoolean(R.styleable.ShapeView_shapeRipple, false);

            //拓展非Shape属性
            if (listener != null)
                listener.expose(a);

            a.recycle();
        }
        return this;
    }

    public interface ExposeListener {
        void expose(TypedArray a);
    }

    private void setCornersTopLeftRadius(float cornersTopLeftRadius) {
        radii[0] = radii[1] = cornersTopLeftRadius;
    }

    private void setCornersTopRightRadius(float cornersTopRightRadius) {
        radii[2] = radii[3] = cornersTopRightRadius;
    }

    private void setCornersBotRightRadius(float cornersBotRightRadius) {
        radii[4] = radii[5] = cornersBotRightRadius;
    }

    private void setCornersBotLeftRadius(float cornersBotLeftRadius) {
        radii[6] = radii[7] = cornersBotLeftRadius;
    }

    private void setCornersRadius(float cornersRadius) {
        if (cornersRadius != 0) {
            Arrays.fill(radii, cornersRadius);
        }
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
        if (!ripple && !isTransparent(selectorPressedColor) || !isTransparent(strokePressedColor))
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

    private Path mClipPath = new Path();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();

    RectF getRectF() {
        return rectF;
    }

    void onSizeChanged(int w, int h) {
        rectF.set(0, 0, w, h);
        mClipPath.reset();
        mClipPath.addRoundRect(rectF, radii, Path.Direction.CW);

        // 剪裁
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 切割确保子View不超出圆角范围
     */
    void clipCanvas(Canvas canvas) {
        canvas.drawPath(mClipPath, mPaint);
        canvas.restore();
    }
}
