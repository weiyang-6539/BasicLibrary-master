package com.github.android.common.popup.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fxb on 2020/5/8.
 */
@IntDef({Position.LEFT, Position.TOP, Position.RIGHT, Position.BOTTOM})
@Retention(RetentionPolicy.SOURCE)
public @interface Position {
    int LEFT = 1;
    int TOP = 2;
    int RIGHT = 3;
    int BOTTOM = 4;
}
