package com.github.android.common.popup.annotation;

import androidx.annotation.IntDef;

/**
 * Created by fxb on 2020/5/8.
 */
@IntDef({AnimType.EMPTY, AnimType.TRANSLATE, AnimType.SCALE, AnimType.SCROLL})
public @interface AnimType {
    int EMPTY = 0;
    int TRANSLATE = 0x1;
    int SCALE = 0x2;
    int SCROLL = 0x4;
}
