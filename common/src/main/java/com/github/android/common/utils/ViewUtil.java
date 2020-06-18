package com.github.android.common.utils;

import android.content.Context;

/**
 * Created by fxb on 2020/6/9.
 */
public class ViewUtil {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
