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
import com.github.android.common.widget.shape.ShapeHelper;
import com.github.android.common.widget.shape.State;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fxb on 2020/8/31.
 * 综合型View，支持单文本 单图标 或者 文本 + 图标的模式，支持图标，文字，线框变色
 */
public class ComplexView extends AppCompatTextView {
    private ShapeHelper shapeHelper = new ShapeHelper();

    private int textNormColor;
    private int textPressedColor;
    private int textDisableColor;
    private int textSelectedColor;
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

        shapeHelper.initAttrs(context, attrs, a -> {
            textNormColor = a.getColor(R.styleable.ShapeView_shapeTextNormalColor, Color.BLACK);
            textPressedColor = a.getColor(R.styleable.ShapeView_shapeTextPressedColor, Color.TRANSPARENT);
            textDisableColor = a.getColor(R.styleable.ShapeView_shapeTextDisableColor, Color.TRANSPARENT);
            textSelectedColor = a.getColor(R.styleable.ShapeView_shapeTextSelectedColor, Color.TRANSPARENT);
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

        //设置文本颜色
        setTextColor(createTextColor());
        //设置文字图标间距
        setCompoundDrawablePadding(iconPadding);
        //设置icon
        //updateIcon();
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

    private ColorStateList createTextColor() {
        LinkedHashMap<int[], Integer> stateMap = new LinkedHashMap<>();
        if (isNotTransparent(textPressedColor))
            stateMap.put(new int[]{State.PRESSED}, textPressedColor);
        if (isNotTransparent(textDisableColor))
            stateMap.put(new int[]{State.DISABLE}, textDisableColor);
        if (isNotTransparent(textSelectedColor))
            stateMap.put(new int[]{State.SELECTED}, textSelectedColor);
        if (isNotTransparent(textNormColor))
            stateMap.put(new int[]{}, textNormColor);

        int[][] states = new int[stateMap.size()][1];
        int[] colors = new int[stateMap.size()];
        int index = 0;
        for (Map.Entry<int[], Integer> entry : stateMap.entrySet()) {
            states[index] = entry.getKey();
            colors[index] = entry.getValue();
            index++;
        }
        return new ColorStateList(states, colors);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        updateIcon();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        updateIcon();
    }

    private void updateIcon() {
        if (isIconNull() || getLayout() == null) {
            return;
        }

        Drawable startIcon = getDrawable(0);

        Drawable topIcon = getDrawable(1);

        Drawable endIcon = getDrawable(2);

        Drawable bottomIcon = getDrawable(3);

        Paint textPaint = getPaint();
        String text = getText().toString();
        if (getTransformationMethod() != null) {
            text = getTransformationMethod().getTransformation(text, this).toString();
        }
        int textWidth = Math.min((int) textPaint.measureText(text), getLayout().getEllipsizedWidth());

        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);

        int offsetX = getMeasuredWidth()
                - textWidth
                - ViewCompat.getPaddingStart(this)
                - ViewCompat.getPaddingEnd(this)
                - (startIcon == null ? 0 : startIcon.getBounds().width() + (textWidth == 0 ? 0 : iconPadding))
                - (endIcon == null ? 0 : endIcon.getBounds().width() + (textWidth == 0 ? 0 : iconPadding))
                >> 1;

        int offsetY = getMeasuredHeight()
                - getLayout().getHeight()
                - getPaddingTop()
                - getPaddingBottom()
                - (topIcon == null ? 0 : topIcon.getBounds().height() + (textWidth == 0 ? 0 : iconPadding))
                - (bottomIcon == null ? 0 : bottomIcon.getBounds().height() + (textWidth == 0 ? 0 : iconPadding))
                >> 1;

        if (startIcon != null) {
            Rect bounds = startIcon.copyBounds();
            bounds.left += offsetX;
            bounds.right += offsetX;
            startIcon.setBounds(bounds);
        }
        if (topIcon != null) {
            Rect bounds = topIcon.copyBounds();
            bounds.top += offsetY;
            bounds.bottom += offsetY;
            topIcon.setBounds(bounds);
        }
        if (endIcon != null) {
            Rect bounds = endIcon.copyBounds();
            bounds.left -= offsetX;
            bounds.right -= offsetX;
            endIcon.setBounds(bounds);
        }
        if (bottomIcon != null) {
            Rect bounds = bottomIcon.copyBounds();
            bounds.top -= offsetY;
            bounds.bottom -= offsetY;
            bottomIcon.setBounds(bounds);
        }

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
        if (isNotTransparent(iconColors[index][0]))
            drawable.addState(new int[]{State.PRESSED}, getStateIcon(bitmap, iconColors[index][0]));
        if (isNotTransparent(iconColors[index][1]))
            drawable.addState(new int[]{State.DISABLE}, getStateIcon(bitmap, iconColors[index][1]));
        if (isNotTransparent(iconColors[index][2]))
            drawable.addState(new int[]{State.SELECTED}, getStateIcon(bitmap, iconColors[index][2]));
        if (isNotTransparent(iconColors[index][3]))
            drawable.addState(new int[]{}, getStateIcon(bitmap, iconColors[index][3]));
        else
            drawable.addState(new int[]{}, new BitmapDrawable(bitmap));

        //计算图片的宽高比,解决非方形icon的留白问题
        float ratio = bitmap.getWidth() * 1f / bitmap.getHeight();
        int left, top, right, bottom;
        switch (index) {
            case 0:
                left = 0;
                right = (int) (startIconSize * Math.min(ratio, 1f));
                top = (int) (startIconSize * (1 - 1f / Math.max(ratio, 1f)) / 2);
                bottom = startIconSize - top;
                drawable.setBounds(left, top, right, bottom);
                break;
            case 1:
                left = (int) (topIconSize * (1 - Math.min(ratio, 1f)) / 2);
                top = 0;
                right = topIconSize - left;
                bottom = (int) (topIconSize / Math.max(ratio, 1f));
                drawable.setBounds(left, top, right, bottom);
                break;
            case 2:
                left = 0;
                top = (int) (endIconSize * (1 - 1f / Math.max(ratio, 1f)) / 2);
                right = (int) (endIconSize * Math.min(ratio, 1f));
                bottom = endIconSize - top;
                drawable.setBounds(left, top, right, bottom);
                break;
            case 3:
                left = (int) (bottomIconSize * (1 - Math.min(ratio, 1f)) / 2);
                top = 0;
                right = bottomIconSize - left;
                bottom = (int) (bottomIconSize / Math.max(ratio, 1f));
                drawable.setBounds(left, top, right, bottom);
                break;
        }

        return drawable;
    }


    private boolean isIconNull() {//是否没有设置图标
        return iconIds != null && iconIds[0] == 0 && iconIds[1] == 0 && iconIds[2] == 0 && iconIds[3] == 0;
    }

    private boolean isNotTransparent(int color) {//判断某个颜色是否为透明色
        return color != Color.TRANSPARENT;
    }

    private int dp2px(float dpValue) {
        return ViewUtil.dp2px(getContext(), dpValue);
    }
}
