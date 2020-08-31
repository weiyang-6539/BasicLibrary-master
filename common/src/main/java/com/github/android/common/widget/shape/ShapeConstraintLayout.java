package com.github.android.common.widget.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeConstraintLayout extends ConstraintLayout {
    private ShapeBuilder shapeBuilder = new ShapeBuilder();

    public ShapeConstraintLayout(Context context) {
        this(context, null);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        shapeBuilder.initAttrs(context, attrs).apply(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        shapeBuilder.onSizeChanged(this, w, h);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),
                null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        shapeBuilder.dispatchDraw(canvas);
    }
}
