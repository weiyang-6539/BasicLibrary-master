package com.github.android.common.popup.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by fxb on 2020/5/9.
 */
public interface XPopupImageLoader {
    void loadImage(int position, @NonNull Object uri, @NonNull ImageView imageView);

    /**
     * 获取图片对应的文件
     *
     * @param context
     * @param uri
     * @return
     */
    File getImageFile(@NonNull Context context, @NonNull Object uri);
}
