package com.github.android.common.widget.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeRelativeLayout extends RelativeLayout {
    private ShapeBuilder shapeBuilder = new ShapeBuilder();

    public ShapeRelativeLayout(Context context) {
        this(context, null);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
}
