package com.github.android.common.widget.shape;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fxb on 2020/9/1.
 */
@IntDef({State.NONE, State.ENABLED, State.PRESSED, State.DISABLE,
        State.SELECTED, State.FOCUSED, State.CHECKED})
@Retention(RetentionPolicy.SOURCE)
public @interface State {
    int NONE = 0;
    int ENABLED = android.R.attr.state_enabled;
    int PRESSED = android.R.attr.state_pressed;
    int DISABLE = -android.R.attr.state_enabled;
    int SELECTED = android.R.attr.state_selected;
    int FOCUSED = android.R.attr.state_focused;
    int CHECKED = android.R.attr.state_checked;
}