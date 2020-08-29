package com.github.android.common.widget.shape;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.github.android.common.widget.shape.helper.ShapeAttrsHelper;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeRelativeLayout extends RelativeLayout {
    public ShapeRelativeLayout(Context context) {
        this(context, null);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ShapeAttrsHelper.initShapeBuilderByAttrs(context, attrs).apply(this);
    }
}
