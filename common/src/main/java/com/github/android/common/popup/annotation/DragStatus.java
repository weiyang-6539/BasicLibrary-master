package com.github.android.common.popup.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fxb on 2020/5/13.
 * 可拖拽布局拖拽状态
 */
@IntDef({DragStatus.OPEN, DragStatus.CLOSE, DragStatus.DRAGGING})
@Retention(RetentionPolicy.SOURCE)
public @interface DragStatus {
    int OPEN = 1;
    int CLOSE = 2;
    int DRAGGING = 3;
}
