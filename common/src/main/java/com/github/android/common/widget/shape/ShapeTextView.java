package com.github.android.common.widget.shape;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.github.android.common.widget.shape.helper.ShapeAttrsHelper;

/**
 * Created by fxb on 2020/8/28.
 */
public class ShapeTextView extends AppCompatTextView {
    public ShapeTextView(Context context) {
        this(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ShapeAttrsHelper.initShapeBuilderByAttrs(context, attrs).apply(this);
    }
}
