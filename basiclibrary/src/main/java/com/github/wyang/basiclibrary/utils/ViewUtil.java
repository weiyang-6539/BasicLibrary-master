package com.github.wyang.basiclibrary.utils;

import android.content.Context;

/**
 * Created by weiyang on 2019-09-20.
 */
public class ViewUtil {
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
