package com.github.android.common.popup.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fxb on 2020/5/8.
 * 对话框的状态
 */
@IntDef({PopupStatus.SHOW, PopupStatus.HIDE, PopupStatus.EXECUTING})
@Retention(RetentionPolicy.SOURCE)
public @interface PopupStatus {
    int SHOW = 1;//显示
    int HIDE = 2;//隐藏
    int EXECUTING = 3;//动画执行中
}
