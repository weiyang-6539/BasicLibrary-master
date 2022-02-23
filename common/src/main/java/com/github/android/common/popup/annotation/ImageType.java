package com.github.android.common.popup.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fxb on 2020/5/9.
 */
@IntDef({ImageType.GIF, ImageType.JPEG, ImageType.RAW, ImageType.PNG_A,
        ImageType.PNG, ImageType.WEBP_A, ImageType.WEBP, ImageType.UNKNOWN})
@Retention(RetentionPolicy.SOURCE)
public @interface ImageType {
    int GIF = 1;
    int JPEG = 2;
    int RAW = 3;
    int PNG_A = 4;
    int PNG = 5;
    int WEBP_A = 6;
    int WEBP = 7;
    int UNKNOWN = 8;
}
