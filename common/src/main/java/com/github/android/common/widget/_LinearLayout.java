package com.github.android.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

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
