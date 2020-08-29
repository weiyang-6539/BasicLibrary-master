package com.github.android.common.widget.shape;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.android.common.widget.shape.helper.ShapeAttrsHelper;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeLinearLayout extends LinearLayout {
    public ShapeLinearLayout(Context context) {
        this(context, null);
    }

    public ShapeLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ShapeAttrsHelper.initShapeBuilderByAttrs(context, attrs).apply(this);
    }
}
