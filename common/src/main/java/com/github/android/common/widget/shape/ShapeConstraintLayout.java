package com.github.android.common.widget.shape;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.github.android.common.widget.shape.helper.ShapeAttrsHelper;
import com.github.android.common.widget.shape.helper.ShapeBuilder;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeConstraintLayout extends ConstraintLayout {
    private ShapeBuilder shapeBuilder;

    public ShapeConstraintLayout(Context context) {
        this(context, null);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        shapeBuilder = ShapeAttrsHelper.initShapeBuilderByAttrs(context, attrs);
        shapeBuilder.apply(this);
    }
}
