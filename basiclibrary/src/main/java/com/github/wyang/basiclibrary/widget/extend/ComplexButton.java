package com.github.wyang.basiclibrary.widget.extend;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.github.wyang.basiclibrary.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by weiyang on 2019-10-11.
 * 综合型Button，支持文本 图标 或者 文本+图标的模式
 */
public class ComplexButton extends AppCompatTextView {
    public static final int NONE = 0;
    public static final int STANDARD = 1;
    public static final int RIPPLE = 2;

    @IntDef({NONE, STANDARD, RIPPLE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Selector {
    }

    @Selector
    private int mSelector = NONE;

    private int bgColorNormal = Color.TRANSPARENT;//默认背景色
    private int bgColorPressed = 0xFFE5E5E5;//按下背景色
    private int bgColorDisable = 0xffcccccc;//禁用背景色

    private float cornersRadius;//圆角半径
    private float cornersTopLeftRadius;
    private float cornersTopRightRadius;
    private float cornersBottomLeftRadius;
    private float cornersBottomRightRadius;

    private int strokeWidth;
    private int strokeColor = 0xff000000;

    private int mGravity;
    private float mElevation;//使用阴影效果时，需要结合margin使用

    private Drawable iconStart;
    private int iconSizeStart = 18;
    private Drawable iconTop;
    private int iconSizeTop = 18;
    private Drawable iconEnd;
    private int iconSizeEnd = 18;
    private Drawable iconBottom;
    private int iconSizeBottom = 18;
    private int iconPadding = 8;//单位dp
    private ColorStateList iconTint;

    public ComplexButton(Context context) {
        this(context, null);
    }

    public ComplexButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComplexButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
        init();
    }

    /**
     * 初始化xml中设置属性
     */
    private void parseAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ComplexButton);

            mSelector = typedArray.getInt(R.styleable.ComplexButton_cSelector, NONE);

            bgColorNormal = typedArray.getColor(R.styleable.ComplexButton_bgColorNormal, bgColorNormal);
            bgColorPressed = typedArray.getColor(R.styleable.ComplexButton_bgColorPressed, bgColorPressed);
            bgColorDisable = typedArray.getColor(R.styleable.ComplexButton_bgColorDisable, bgColorDisable);

            cornersRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_cornersRadius, 0);
            cornersTopLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_cornersTopLeftRadius, 0);
            cornersTopRightRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_cornersTopRightRadius, 0);
            cornersBottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_cornersBottomLeftRadius, 0);
            cornersBottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_cornersBottomRightRadius, 0);

            strokeWidth = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_strokeWidth, 0);

            strokeColor = typedArray.getColor(R.styleable.ComplexButton_strokeColor, strokeColor);

            mGravity = typedArray.getInt(R.styleable.ComplexButton_gravity, mGravity);
            mElevation = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_elevation, 0);

            iconStart = typedArray.getDrawable(R.styleable.ComplexButton_iconStart);
            iconTop = typedArray.getDrawable(R.styleable.ComplexButton_iconTop);
            iconEnd = typedArray.getDrawable(R.styleable.ComplexButton_iconEnd);
            iconBottom = typedArray.getDrawable(R.styleable.ComplexButton_iconBottom);
            iconSizeStart = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_iconSizeStart, dp2px(iconSizeStart));
            iconSizeTop = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_iconSizeTop, dp2px(iconSizeTop));
            iconSizeEnd = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_iconSizeEnd, dp2px(iconSizeEnd));
            iconSizeBottom = typedArray.getDimensionPixelSize(R.styleable.ComplexButton_iconSizeBottom, dp2px(iconSizeBottom));
            iconPadding = typedArray.getDimensionPixelOffset(R.styleable.ComplexButton_iconPadding, dp2px(iconPadding));

            //iconTint = getTextColors();

            typedArray.recycle();
        }
    }

    private void init() {
        setClickable(true);
        setIncludeFontPadding(true);

        switch (mSelector) {
            case NONE:
                ViewCompat.setBackground(this, getDrawable(bgColorNormal));
                break;
            case STANDARD:
                ViewCompat.setBackground(this, getStandard());
                break;
            case RIPPLE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ViewCompat.setBackground(this, getRipple());
                } else {
                    ViewCompat.setBackground(this, getStandard());
                }
                break;
        }

        //设置文本位置
        switch (mGravity) {
            case 0:
                setGravity(Gravity.CENTER);
                break;
            case 1:
                setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                break;
            case 2:
                setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                break;
            case 3:
                setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                break;
            case 4:
                setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                break;
        }

        /*if (getGravity() == Gravity.CENTER) {
            String text = getText().toString();
            setText(text.replace("\n", ""));
        }*/
        setCompoundDrawablePadding(iconPadding);
        updateIcon(!isIconNull());

        //设置阴影
        if (mElevation > 0)
            ViewCompat.setElevation(this, mElevation);

        /*setTextColor(new ColorStateList(new int[][]{
                new int[]{},
                new int[]{-android.R.attr.state_enabled}
        }, new int[]{
                0xffffffff,
                0x88888888
        }));*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        updateIconPosition();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        updateIconPosition();
    }

    private void updateIconPosition() {
        if (isIconNull() || getLayout() == null) {
            return;
        }

        if (getGravity() != Gravity.CENTER) {
            updateIcon(false);
            return;
        }

        Paint textPaint = getPaint();
        String text = getText().toString();
        if (getTransformationMethod() != null) {
            // if text is transformed, add that transformation to to ensure correct calculation
            // of icon padding.
            text = getTransformationMethod().getTransformation(text, this).toString();
        }
        int textWidth = Math.min((int) textPaint.measureText(text), getLayout().getEllipsizedWidth());

        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);

        offsetX = getMeasuredWidth()
                - textWidth
                - ViewCompat.getPaddingStart(this)
                - ViewCompat.getPaddingEnd(this)
                - (iconStart == null ? 0 : iconSizeStart + iconPadding)
                - (iconEnd == null ? 0 : iconSizeEnd + iconPadding)
                >> 1;

        offsetY = getMeasuredHeight()
                - getLayout().getHeight()
                - getPaddingTop()
                - getPaddingBottom()
                - (iconTop == null ? 0 : iconSizeTop + iconPadding)
                - (iconBottom == null ? 0 : iconSizeBottom + iconPadding)
                >> 1;

        updateIcon(false);
    }

    private int offsetX, offsetY;

    private void updateIcon(boolean needsIconUpdate) {
        if (iconStart != null) {
            iconStart = DrawableCompat.wrap(iconStart).mutate();
            DrawableCompat.setTintList(iconStart, iconTint);
            /*if (iconTintMode != null) {
                DrawableCompat.setTintMode(icon, iconTintMode);
            }*/

            iconStart.setBounds(offsetX, 0, offsetX + iconSizeStart, iconSizeStart);
        }
        if (iconTop != null) {
            iconTop = DrawableCompat.wrap(iconTop).mutate();
            DrawableCompat.setTintList(iconTop, iconTint);

            iconTop.setBounds(0, offsetY, iconSizeTop, offsetY + iconSizeTop);
        }
        if (iconEnd != null) {
            iconEnd = DrawableCompat.wrap(iconEnd).mutate();
            DrawableCompat.setTintList(iconEnd, iconTint);

            iconEnd.setBounds(-offsetX, 0, -offsetX + iconSizeEnd, iconSizeEnd);
        }

        if (iconBottom != null) {
            iconBottom = DrawableCompat.wrap(iconBottom).mutate();
            DrawableCompat.setTintList(iconBottom, iconTint);

            iconBottom.setBounds(0, -offsetY, iconSizeBottom, -offsetY + iconSizeBottom);
        }

        // Reset icon drawable if needed
        // Forced icon update
        if (needsIconUpdate) {
            resetIconDrawable();
            return;
        }

        // Otherwise only update if the icon or the position has changed
        Drawable[] existingDrawables = TextViewCompat.getCompoundDrawablesRelative(this);
        Drawable drawableStart = existingDrawables[0];
        Drawable drawableTop = existingDrawables[1];
        Drawable drawableEnd = existingDrawables[2];
        Drawable drawableBottom = existingDrawables[3];

        if (iconStart != drawableStart || iconTop != drawableTop ||
                iconEnd != drawableEnd || iconBottom != drawableBottom) {
            resetIconDrawable();
        }
    }

    private void resetIconDrawable() {
        TextViewCompat.setCompoundDrawablesRelative(this, iconStart, iconTop, iconEnd, iconBottom);
    }

    private GradientDrawable getDrawable(@ColorInt int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setStroke(strokeWidth, strokeColor);

        if (cornersRadius != 0)
            drawable.setCornerRadius(cornersRadius);
        else
            drawable.setCornerRadii(new float[]{
                    cornersTopLeftRadius, cornersTopLeftRadius,
                    cornersTopRightRadius, cornersTopRightRadius,
                    cornersBottomRightRadius, cornersBottomRightRadius,
                    cornersBottomLeftRadius, cornersBottomLeftRadius
            });
        return drawable;
    }

    private Drawable getStandard() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, getDrawable(bgColorPressed));
        drawable.addState(new int[]{-android.R.attr.state_enabled}, getDrawable(bgColorDisable));
        drawable.addState(new int[]{}, getDrawable(bgColorNormal));
        return drawable;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Drawable getRipple() {
        int[][] stateList = new int[][]{
                new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled},
                new int[]{android.R.attr.state_focused, android.R.attr.state_enabled},
                new int[]{android.R.attr.state_activated, android.R.attr.state_enabled},
                new int[]{-android.R.attr.state_enabled},
                new int[]{}
        };

        int[] stateColorList = new int[]{
                bgColorPressed,
                bgColorPressed,
                bgColorPressed,
                bgColorDisable,
                bgColorPressed
        };
        ColorStateList colorStateList = new ColorStateList(stateList, stateColorList);

        GradientDrawable maskDrawable = getDrawable(bgColorPressed);
        GradientDrawable contentDrawable = getDrawable(bgColorNormal);

        //contentDrawable实际是默认初始化时展示的；maskDrawable 控制了rippleDrawable的范围
        return new RippleDrawable(colorStateList, contentDrawable, maskDrawable);
    }

    public int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private boolean isIconNull() {
        return iconStart == null && iconTop == null && iconEnd == null && iconBottom == null;
    }

    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

}
