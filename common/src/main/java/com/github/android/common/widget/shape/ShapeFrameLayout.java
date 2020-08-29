package com.github.android.common.widget.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.github.android.common.widget.shape.helper.ShapeAttrsHelper;
import com.github.android.common.widget.shape.helper.ShapeBuilder;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeFrameLayout extends FrameLayout {
    private ShapeBuilder builder;

    public ShapeFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public ShapeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        builder = ShapeAttrsHelper.initShapeBuilderByAttrs(context, attrs);
        builder.apply(this);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        builder.onSizeChanged(this, w, h);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),
                null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        builder.dispatchDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!builder.isEffective(ev)) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }
}
