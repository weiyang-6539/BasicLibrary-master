package com.github.android.common.widget.shape.extend;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.github.android.common.R;
import com.github.android.common.utils.ViewUtil;
import com.github.android.common.widget.shape.ShapeBuilder;
import com.github.android.common.widget.shape.State;

/**
 * Created by fxb on 2020/8/31.
 */
public class ComplexView extends AppCompatTextView {
    private ShapeBuilder shapeBuilder = new ShapeBuilder();

    private int[] textColors = new int[4];
    /**
     * 对应图标资源id
     */
    private int[] iconIds = new int[4];
    /**
     * 一维对应左上右下图标，二维对应状态颜色（状态依次为：pressed,disable,selected,none）
     */
    private int[][] iconColors = new int[4][4];

    /**
     * 上下左右图标的尺寸
     */
    private int startIconSize = 18;
    private int topIconSize = 18;
    private int endIconSize = 18;
    private int bottomIconSize = 18;

    private int gravity;
    private int iconPadding = 5;

    public ComplexView(Context context) {
        this(context, null);
    }

    public ComplexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComplexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        shapeBuilder.initAttrs(context, attrs, a -> {
            textColors[3] = a.getColor(R.styleable.ShapeView_shapeTextNormalColor, Color.BLACK);
            textColors[0] = a.getColor(R.styleable.ShapeView_shapeTextPressedColor, Color.TRANSPARENT);
            textColors[1] = a.getColor(R.styleable.ShapeView_shapeTextDisableColor, Color.TRANSPARENT);
            textColors[2] = a.getColor(R.styleable.ShapeView_shapeTextSelectedColor, Color.TRANSPARENT);
            iconIds[0] = a.getResourceId(R.styleable.ShapeView_shapeStartIcon, 0);
            iconIds[1] = a.getResourceId(R.styleable.ShapeView_shapeTopIcon, 0);
            iconIds[2] = a.getResourceId(R.styleable.ShapeView_shapeEndIcon, 0);
            iconIds[3] = a.getResourceId(R.styleable.ShapeView_shapeBottomIcon, 0);
            iconColors[0][3] = a.getColor(R.styleable.ShapeView_shapeStartIconNormalColor, 0);
            iconColors[0][0] = a.getColor(R.styleable.ShapeView_shapeStartIconPressedColor, 0);
            iconColors[0][1] = a.getColor(R.styleable.ShapeView_shapeStartIconDisableColor, 0);
            iconColors[0][2] = a.getColor(R.styleable.ShapeView_shapeStartIconSelectedColor, 0);
            iconColors[1][3] = a.getColor(R.styleable.ShapeView_shapeTopIconNormalColor, 0);
            iconColors[1][0] = a.getColor(R.styleable.ShapeView_shapeTopIconPressedColor, 0);
            iconColors[1][1] = a.getColor(R.styleable.ShapeView_shapeTopIconDisableColor, 0);
            iconColors[1][2] = a.getColor(R.styleable.ShapeView_shapeTopIconSelectedColor, 0);
            iconColors[2][3] = a.getColor(R.styleable.ShapeView_shapeEndIconNormalColor, 0);
            iconColors[2][0] = a.getColor(R.styleable.ShapeView_shapeEndIconPressedColor, 0);
            iconColors[2][1] = a.getColor(R.styleable.ShapeView_shapeEndIconDisableColor, 0);
            iconColors[2][2] = a.getColor(R.styleable.ShapeView_shapeEndIconSelectedColor, 0);
            iconColors[3][3] = a.getColor(R.styleable.ShapeView_shapeBottomIconNormalColor, 0);
            iconColors[3][0] = a.getColor(R.styleable.ShapeView_shapeBottomIconPressedColor, 0);
            iconColors[3][1] = a.getColor(R.styleable.ShapeView_shapeBottomIconDisableColor, 0);
            iconColors[3][2] = a.getColor(R.styleable.ShapeView_shapeBottomIconSelectedColor, 0);
            startIconSize = a.getDimensionPixelSize(R.styleable.ShapeView_shapeStartIconSize, dp2px(startIconSize));
            topIconSize = a.getDimensionPixelSize(R.styleable.ShapeView_shapeTopIconSize, dp2px(topIconSize));
            endIconSize = a.getDimensionPixelSize(R.styleable.ShapeView_shapeEndIconSize, dp2px(endIconSize));
            bottomIconSize = a.getDimensionPixelSize(R.styleable.ShapeView_shapeBottomIconSize, dp2px(bottomIconSize));
            iconPadding = a.getDimensionPixelSize(R.styleable.ShapeView_shapeIconPadding, dp2px(iconPadding));
            gravity = a.getInt(R.styleable.ShapeView_shapeGravity, 0);
        }).apply(this);

        setTextColor(getTextColor());

        setCompoundDrawablePadding(iconPadding);
        updateIcon();

        //设置文本位置
        switch (gravity) {
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
    }

    private ColorStateList getTextColor() {
        for (int i = 0; i < textColors.length; i++) {
            if (isTransparent(textColors[i]))
                textColors[i] = textColors[textColors.length - 1];
        }
        return new ColorStateList(new int[][]{
                new int[]{State.PRESSED},
                new int[]{State.DISABLE},
                new int[]{State.SELECTED},
                new int[]{}
        }, textColors);
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
            updateIcon();
            return;
        }

        Paint textPaint = getPaint();
        String text = getText().toString();
        if (getTransformationMethod() != null) {
            text = getTransformationMethod().getTransformation(text, this).toString();
        }
        int textWidth = Math.min((int) textPaint.measureText(text), getLayout().getEllipsizedWidth());

        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);

        offsetX = getMeasuredWidth()
                - textWidth
                - ViewCompat.getPaddingStart(this)
                - ViewCompat.getPaddingEnd(this)
                - (iconIds[0] == 0 ? 0 : startIconSize + iconPadding)
                - (iconIds[2] == 0 ? 0 : endIconSize + iconPadding)
                >> 1;

        offsetY = getMeasuredHeight()
                - getLayout().getHeight()
                - getPaddingTop()
                - getPaddingBottom()
                - (iconIds[1] == 0 ? 0 : topIconSize + iconPadding)
                - (iconIds[3] == 0 ? 0 : bottomIconSize + iconPadding)
                >> 1;

        updateIcon();
    }

    private int offsetX, offsetY;

    private void updateIcon() {
        Drawable startIcon = getDrawable(0);
        if (startIcon != null)
            startIcon.setBounds(offsetX, 0, offsetX + startIconSize, startIconSize);

        Drawable topIcon = getDrawable(1);
        if (topIcon != null)
            topIcon.setBounds(0, offsetY, topIconSize, offsetY + topIconSize);

        Drawable endIcon = getDrawable(2);
        if (endIcon != null)
            endIcon.setBounds(-offsetX, 0, -offsetX + endIconSize, endIconSize);

        Drawable bottomIcon = getDrawable(3);
        if (bottomIcon != null)
            bottomIcon.setBounds(0, -offsetY, bottomIconSize, -offsetY + bottomIconSize);

        TextViewCompat.setCompoundDrawablesRelative(this, startIcon, topIcon, endIcon, bottomIcon);
    }

    private BitmapDrawable getStateIcon(Bitmap bitmap, @ColorInt int color) {
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable = (BitmapDrawable) DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }

    private Drawable getDrawable(int index) {
        int iconId = iconIds[index];
        if (iconId == 0)
            return null;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iconId);

        StateListDrawable drawable = new StateListDrawable();
        if (!isTransparent(iconColors[index][0]))
            drawable.addState(new int[]{State.PRESSED}, getStateIcon(bitmap, iconColors[index][0]));
        if (!isTransparent(iconColors[index][1]))
            drawable.addState(new int[]{State.DISABLE}, getStateIcon(bitmap, iconColors[index][1]));
        if (!isTransparent(iconColors[index][2]))
            drawable.addState(new int[]{State.SELECTED}, getStateIcon(bitmap, iconColors[index][2]));
        if (!isTransparent(iconColors[index][3]))
            drawable.addState(new int[]{}, getStateIcon(bitmap, iconColors[index][3]));
        else
            drawable.addState(new int[]{}, new BitmapDrawable(bitmap));
        return drawable;
    }

    private boolean isIconNull() {
        return iconIds != null && iconIds[0] == 0 && iconIds[1] == 0 && iconIds[2] == 0 && iconIds[3] == 0;
    }

    private boolean isTransparent(int color) {
        return color == Color.TRANSPARENT;
    }

    private int dp2px(float dpValue) {
        return ViewUtil.dp2px(getContext(), dpValue);
    }
}
