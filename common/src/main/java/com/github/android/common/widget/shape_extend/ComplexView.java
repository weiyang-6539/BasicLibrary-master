package com.github.android.common.widget.shape_extend;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.github.android.common.R;
import com.github.android.common.utils.ViewUtil;
import com.github.android.common.widget.shape.ShapeBuilder;

/**
 * Created by fxb on 2020/8/31.
 */
public class ComplexView extends AppCompatTextView {
    private ShapeBuilder shapeBuilder = new ShapeBuilder();

    private int gravity;

    private Drawable startIcon;
    private Drawable topIcon;
    private Drawable endIcon;
    private Drawable bottomIcon;

    private int startIconSize = 18;
    private int topIconSize = 18;
    private int endIconSize = 18;
    private int bottomIconSize = 18;

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
            startIcon = a.getDrawable(R.styleable.ShapeView_shapeStartIcon);
            topIcon = a.getDrawable(R.styleable.ShapeView_shapeTopIcon);
            endIcon = a.getDrawable(R.styleable.ShapeView_shapeEndIcon);
            bottomIcon = a.getDrawable(R.styleable.ShapeView_shapeBottomIcon);
            startIconSize = a.getDimensionPixelSize(R.styleable.ShapeView_shapeStartIconSize, dp2px(startIconSize));
            topIconSize = a.getDimensionPixelSize(R.styleable.ShapeView_shapeTopIconSize, dp2px(topIconSize));
            endIconSize = a.getDimensionPixelSize(R.styleable.ShapeView_shapeEndIconSize, dp2px(endIconSize));
            bottomIconSize = a.getDimensionPixelSize(R.styleable.ShapeView_shapeBottomIconSize, dp2px(bottomIconSize));
            iconPadding = a.getDimensionPixelSize(R.styleable.ShapeView_shapeIconPadding, dp2px(iconPadding));
        }).apply(this);

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


        //setCompoundDrawablePadding(cvIconPadding);
        updateIcon();
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
                - (startIcon == null ? 0 : startIconSize + iconPadding)
                - (endIcon == null ? 0 : endIconSize + iconPadding)
                >> 1;

        offsetY = getMeasuredHeight()
                - getLayout().getHeight()
                - getPaddingTop()
                - getPaddingBottom()
                - (topIcon == null ? 0 : topIconSize + iconPadding)
                - (bottomIcon == null ? 0 : bottomIconSize + iconPadding)
                >> 1;

        updateIcon();
    }

    private int offsetX, offsetY;

    private void updateIcon() {
        if (startIcon != null) {
            startIcon = DrawableCompat.wrap(startIcon).mutate();
            //DrawableCompat.setTintList(startIcon, cvIconStartTint);

            startIcon.setBounds(offsetX, 0, offsetX + startIconSize, startIconSize);
        }
        if (topIcon != null) {
            topIcon = DrawableCompat.wrap(topIcon).mutate();
            //DrawableCompat.setTintList(cvIconTop, cvIconTopTint);

            topIcon.setBounds(0, offsetY, topIconSize, offsetY + topIconSize);
        }
        if (endIcon != null) {
            endIcon = DrawableCompat.wrap(endIcon).mutate();
            //DrawableCompat.setTintList(cvIconEnd, cvIconEndTint);

            endIcon.setBounds(-offsetX, 0, -offsetX + endIconSize, endIconSize);
        }

        if (bottomIcon != null) {
            bottomIcon = DrawableCompat.wrap(bottomIcon).mutate();
            //DrawableCompat.setTintList(cvIconBottom, cvIconBottomTint);

            bottomIcon.setBounds(0, -offsetY, bottomIconSize, -offsetY + bottomIconSize);
        }

        Drawable[] existingDrawables = TextViewCompat.getCompoundDrawablesRelative(this);
        Drawable drawableStart = existingDrawables[0];
        Drawable drawableTop = existingDrawables[1];
        Drawable drawableEnd = existingDrawables[2];
        Drawable drawableBottom = existingDrawables[3];

        if (startIcon != drawableStart || topIcon != drawableTop ||
                endIcon != drawableEnd || bottomIcon != drawableBottom) {
            TextViewCompat.setCompoundDrawablesRelative(this, startIcon, topIcon, endIcon, bottomIcon);
        }
    }

    private boolean isIconNull() {
        return startIcon == null && topIcon == null && endIcon == null && bottomIcon == null;
    }

    private int dp2px(float dpValue) {
        return ViewUtil.dp2px(getContext(), dpValue);
    }
}
