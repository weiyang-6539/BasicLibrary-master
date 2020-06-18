package com.github.android.common.widget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by fxb on 2020/5/27.
 * 带分割线或空白距离的线性布局
 */
public class _LinearLayout extends LinearLayout {
    public _LinearLayout(Context context) {
        this(context, null);
    }

    public _LinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public _LinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @IntDef({Divider.LINE, Divider.SPACE})
    public @interface Divider {
        int LINE = 1;
        int SPACE = 2;
    }
}
