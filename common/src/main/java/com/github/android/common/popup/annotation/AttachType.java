package com.github.android.common.popup.annotation;

import androidx.annotation.IntDef;

/**
 * Created by fxb on 2020/6/18.
 * 弹窗基于依附View的 附着类型
 */
@IntDef({AttachType.LEFT_TO_LEFT, AttachType.LEFT_TO_RIGHT,
        AttachType.TOP_TO_TOP, AttachType.TOP_TO_BOTTOM,
        AttachType.RIGHT_TO_LEFT, AttachType.RIGHT_TO_RIGHT,
        AttachType.BOTTOM_TO_TOP, AttachType.BOTTOM_TO_BOTTOM,
        AttachType.CENTER_VERTICAL, AttachType.CENTER_HORIZONTAL
})
public @interface AttachType {
    int LEFT_TO_LEFT = 1;
    int LEFT_TO_RIGHT = 2;

    int TOP_TO_TOP = 3;
    int TOP_TO_BOTTOM = 4;

    int RIGHT_TO_LEFT = 5;
    int RIGHT_TO_RIGHT = 6;

    int BOTTOM_TO_TOP = 7;
    int BOTTOM_TO_BOTTOM = 8;

    int CENTER_VERTICAL = 9;
    int CENTER_HORIZONTAL = 10;
}
