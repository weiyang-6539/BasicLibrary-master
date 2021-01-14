package com.github.android.common.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * Created by fxb on 2020/7/6.
 */
public class GlideImageLoader {

    /**
     * 创建Glide RequestOptions
     */
    private static RequestOptions createOptions(@DrawableRes int placeId, @DrawableRes int errorId) {
        return new RequestOptions()
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .format(DecodeFormat.PREFER_RGB_565)
                .placeholder(placeId)
                .error(errorId)
                .dontAnimate()
                .dontTransform();
    }

    /**
     * 客服按钮加载图片
     */
    public static void loadService(Context context, ImageView view, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .apply(createOptions(0, 0).fitCenter())
                .into(view);
    }

    /**
     * 加载用户头像
     */
    public static void loadAvatar(Context context, ImageView view, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .apply(createOptions(0, 0).centerCrop())
                .into(view);
    }

    /**
     * 默认无填充方式
     */
    public static void loadNoScaleType(Context context, ImageView view, String url) {
        Glide.with(context)
                .setDefaultRequestOptions(createOptions(0, 0))
                .load(url)
                .into(view);
    }

    /**
     * 裁剪中间区域
     */
    public static void loadCenterCrop(Context context, ImageView view, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .apply(createOptions(0, 0).fitCenter())
                .into(view);
    }

    /**
     * 缩放填充在中间
     */
    public static void loadFitCenter(Context context, ImageView view, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .apply(createOptions(0, 0).fitCenter())
                .into(view);
    }
}
