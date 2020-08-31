package com.github.android.common.widget.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeFrameLayout extends FrameLayout {
    private ShapeBuilder shapeBuilder = new ShapeBuilder();

    public ShapeFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public ShapeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        shapeBuilder.initAttrs(context, attrs).apply(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        shapeBuilder.onSizeChanged(this, w, h);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),
                null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        shapeBuilder.dispatchDraw(canvas);
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!builder.isEffective(ev)) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }*/
}
