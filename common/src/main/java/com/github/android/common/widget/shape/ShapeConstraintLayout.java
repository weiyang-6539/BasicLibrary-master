package com.github.android.common.widget.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeConstraintLayout extends ConstraintLayout {
    private ShapeHelper shapeHelper = new ShapeHelper();

    public ShapeConstraintLayout(Context context) {
        this(context, null);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        shapeHelper.initAttrs(context, attrs).apply(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        shapeHelper.onSizeChanged(w, h);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(shapeHelper.getRectF(), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        shapeHelper.clipCanvas(canvas);
    }
}
