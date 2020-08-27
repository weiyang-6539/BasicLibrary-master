package com.github.android.common.widget.simple;

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

import com.github.android.common.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by weiyang on 2019-10-11.
 * 综合型View，支持文本 图标 或者 文本 + 图标的模式
 */
public class ComplexView extends AppCompatTextView {

    @IntDef({Selector.NONE, Selector.STANDARD, Selector.RIPPLE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Selector {
        int NONE = 0;
        int STANDARD = 1;
        int RIPPLE = 2;
    }

    @Selector
    private int cvSelector = Selector.NONE;

    private int cvNormalBgColor = Color.TRANSPARENT;//默认背景色
    private int cvPressedBgColor = Color.parseColor("#e5e5e5");//按下背景色
    private int cvDisableBgColor = Color.parseColor("#d5d5d5");//禁用背景色

    private float cvCornersRadius;//圆角半径
    private float cvCornersTopLeftRadius;
    private float cvCornersTopRightRadius;
    private float cvCornersBottomLeftRadius;
    private float cvCornersBottomRightRadius;

    private int cvStrokeWidth;
    private int cvStrokeNormalColor = cvNormalBgColor;
    private int cvStrokePressedColor = cvPressedBgColor;
    private int cvStrokeDisableColor = cvDisableBgColor;

    private int cvGravity;
    private float cvElevation;//使用阴影效果时，需要结合margin使用
    private boolean cvClickable;//点击是否穿透

    private Drawable cvIconStart;
    private int cvIconStartSize = 18;
    private ColorStateList cvIconStartTint;

    private Drawable cvIconTop;
    private int cvIconTopSize = 18;
    private ColorStateList cvIconTopTint;

    private Drawable cvIconEnd;
    private int cvIconEndSize = 18;
    private ColorStateList cvIconEndTint;

    private Drawable cvIconBottom;
    private int cvIconBottomSize = 18;
    private ColorStateList cvIconBottomTint;

    private int cvIconPadding = 8;//单位dp

    public ComplexView(Context context) {
        this(context, null);
    }

    public ComplexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComplexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
        apply();
    }

    /**
     * 初始化xml中设置属性
     */
    private void parseAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ComplexView);

            cvSelector = typedArray.getInt(R.styleable.ComplexView_cvSelector, cvSelector);

            cvNormalBgColor = typedArray.getColor(R.styleable.ComplexView_cvNormalBgColor, cvNormalBgColor);
            cvPressedBgColor = typedArray.getColor(R.styleable.ComplexView_cvPressedBgColor, cvPressedBgColor);
            cvDisableBgColor = typedArray.getColor(R.styleable.ComplexView_cvDisableBgColor, cvDisableBgColor);

            cvCornersRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvCornersRadius, 0);
            cvCornersTopLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvCornersTopLeftRadius, 0);
            cvCornersTopRightRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvCornersTopRightRadius, 0);
            cvCornersBottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvCornersBottomLeftRadius, 0);
            cvCornersBottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvCornersBottomRightRadius, 0);

            cvStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvStrokeWidth, 0);

            cvStrokeNormalColor = typedArray.getColor(R.styleable.ComplexView_cvStrokeNormalColor, cvStrokeNormalColor);
            cvStrokePressedColor = typedArray.getColor(R.styleable.ComplexView_cvStrokePressedColor, cvStrokePressedColor);
            cvStrokeDisableColor = typedArray.getColor(R.styleable.ComplexView_cvStrokeDisableColor, cvStrokeDisableColor);

            cvGravity = typedArray.getInt(R.styleable.ComplexView_cvGravity, cvGravity);
            cvElevation = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvElevation, 0);
            cvClickable = typedArray.getBoolean(R.styleable.ComplexView_cvClickable, true);

            cvIconStart = typedArray.getDrawable(R.styleable.ComplexView_cvIconStart);
            cvIconTop = typedArray.getDrawable(R.styleable.ComplexView_cvIconTop);
            cvIconEnd = typedArray.getDrawable(R.styleable.ComplexView_cvIconEnd);
            cvIconBottom = typedArray.getDrawable(R.styleable.ComplexView_cvIconBottom);

            cvIconStartSize = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvIconStartSize, dp2px(cvIconStartSize));
            cvIconTopSize = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvIconTopSize, dp2px(cvIconTopSize));
            cvIconEndSize = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvIconEndSize, dp2px(cvIconEndSize));
            cvIconBottomSize = typedArray.getDimensionPixelSize(R.styleable.ComplexView_cvIconBottomSize, dp2px(cvIconBottomSize));

            cvIconStartTint = typedArray.getColorStateList(R.styleable.ComplexView_cvIconStartTint);
            cvIconTopTint = typedArray.getColorStateList(R.styleable.ComplexView_cvIconTopTint);
            cvIconEndTint = typedArray.getColorStateList(R.styleable.ComplexView_cvIconEndTint);
            cvIconBottomTint = typedArray.getColorStateList(R.styleable.ComplexView_cvIconBottomTint);

            cvIconPadding = typedArray.getDimensionPixelOffset(R.styleable.ComplexView_cvIconPadding, dp2px(cvIconPadding));

            typedArray.recycle();
        }
    }

    private void apply() {
        setClickable(cvClickable);
        setIncludeFontPadding(true);

        switch (cvSelector) {
            case Selector.NONE:
                ViewCompat.setBackground(this, getDrawable(cvNormalBgColor, cvStrokeNormalColor));
                break;
            case Selector.STANDARD:
                ViewCompat.setBackground(this, getStandard());
                break;
            case Selector.RIPPLE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ViewCompat.setBackground(this, getRipple());
                } else {
                    ViewCompat.setBackground(this, getStandard());
                }
                break;
        }

        //设置文本位置
        switch (cvGravity) {
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

        setCompoundDrawablePadding(cvIconPadding);
        updateIcon(!isIconNull());

        //设置阴影
        if (cvElevation > 0)
            ViewCompat.setElevation(this, cvElevation);

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
                - (cvIconStart == null ? 0 : cvIconStartSize + cvIconPadding)
                - (cvIconEnd == null ? 0 : cvIconEndSize + cvIconPadding)
                >> 1;

        offsetY = getMeasuredHeight()
                - getLayout().getHeight()
                - getPaddingTop()
                - getPaddingBottom()
                - (cvIconTop == null ? 0 : cvIconTopSize + cvIconPadding)
                - (cvIconBottom == null ? 0 : cvIconBottomSize + cvIconPadding)
                >> 1;

        updateIcon(false);
    }

    private int offsetX, offsetY;

    private void updateIcon(boolean needsIconUpdate) {
        if (cvIconStart != null) {
            cvIconStart = DrawableCompat.wrap(cvIconStart).mutate();
            DrawableCompat.setTintList(cvIconStart, cvIconStartTint);

            cvIconStart.setBounds(offsetX, 0, offsetX + cvIconStartSize, cvIconStartSize);
        }
        if (cvIconTop != null) {
            cvIconTop = DrawableCompat.wrap(cvIconTop).mutate();
            DrawableCompat.setTintList(cvIconTop, cvIconTopTint);

            cvIconTop.setBounds(0, offsetY, cvIconTopSize, offsetY + cvIconTopSize);
        }
        if (cvIconEnd != null) {
            cvIconEnd = DrawableCompat.wrap(cvIconEnd).mutate();
            DrawableCompat.setTintList(cvIconEnd, cvIconEndTint);

            cvIconEnd.setBounds(-offsetX, 0, -offsetX + cvIconEndSize, cvIconEndSize);
        }

        if (cvIconBottom != null) {
            cvIconBottom = DrawableCompat.wrap(cvIconBottom).mutate();
            DrawableCompat.setTintList(cvIconBottom, cvIconBottomTint);

            cvIconBottom.setBounds(0, -offsetY, cvIconBottomSize, -offsetY + cvIconBottomSize);
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

        if (cvIconStart != drawableStart || cvIconTop != drawableTop ||
                cvIconEnd != drawableEnd || cvIconBottom != drawableBottom) {
            resetIconDrawable();
        }
    }

    private void resetIconDrawable() {
        TextViewCompat.setCompoundDrawablesRelative(this, cvIconStart, cvIconTop, cvIconEnd, cvIconBottom);
    }

    private GradientDrawable getDrawable(@ColorInt int color, @ColorInt int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setStroke(cvStrokeWidth, strokeColor);

        if (cvCornersRadius != 0)
            drawable.setCornerRadius(cvCornersRadius);
        else
            drawable.setCornerRadii(new float[]{
                    cvCornersTopLeftRadius, cvCornersTopLeftRadius,
                    cvCornersTopRightRadius, cvCornersTopRightRadius,
                    cvCornersBottomRightRadius, cvCornersBottomRightRadius,
                    cvCornersBottomLeftRadius, cvCornersBottomLeftRadius
            });
        return drawable;
    }

    private Drawable getStandard() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, getDrawable(cvPressedBgColor, cvStrokePressedColor));
        drawable.addState(new int[]{-android.R.attr.state_enabled}, getDrawable(cvDisableBgColor, cvStrokeDisableColor));
        drawable.addState(new int[]{}, getDrawable(cvNormalBgColor, cvStrokeNormalColor));
        return drawable;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Drawable getRipple() {
        /*int[][] stateList = new int[][]{
                new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled},
                new int[]{android.R.attr.state_focused, android.R.attr.state_enabled},
                new int[]{android.R.attr.state_activated, android.R.attr.state_enabled},
                new int[]{-android.R.attr.state_enabled},
                new int[]{}
        };

        int[] stateColorList = new int[]{
                cvPressedBgColor,
                cvPressedBgColor,
                cvPressedBgColor,
                cvDisableBgColor,
                cvPressedBgColor
        };
        ColorStateList colorStateList = new ColorStateList(stateList, stateColorList);*/

        GradientDrawable maskDrawable = getDrawable(cvPressedBgColor, cvStrokePressedColor);
        //GradientDrawable contentDrawable = getDrawable(cvNormalBgColor);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-android.R.attr.state_enabled}, getDrawable(cvDisableBgColor, cvStrokeDisableColor));
        drawable.addState(new int[]{}, getDrawable(cvNormalBgColor, cvStrokeNormalColor));
        //contentDrawable实际是默认初始化时展示的；maskDrawable 控制了rippleDrawable的范围
        return new RippleDrawable(ColorStateList.valueOf(cvPressedBgColor), drawable, maskDrawable);
    }

    public int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private boolean isIconNull() {
        return cvIconStart == null && cvIconTop == null && cvIconEnd == null && cvIconBottom == null;
    }

    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

}
