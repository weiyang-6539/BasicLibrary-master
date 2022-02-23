package com.github.android.common.widget.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeFrameLayout extends FrameLayout {
    private final ShapeHelper shapeHelper = new ShapeHelper();

    public ShapeFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public ShapeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        shapeHelper.initAttrs(context, attrs).apply(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        shapeHelper.onSizeChanged(w, h);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(shapeHelper.getRectF(), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        shapeHelper.clipCanvas(canvas);
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!builder.isEffective(ev)) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }*/
}
