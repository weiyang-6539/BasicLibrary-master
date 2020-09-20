package com.github.android.common.widget.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeRelativeLayout extends RelativeLayout {
    private ShapeHelper shapeHelper = new ShapeHelper();

    public ShapeRelativeLayout(Context context) {
        this(context, null);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
}
