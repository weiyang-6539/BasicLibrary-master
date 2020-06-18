package com.github.android.common.popup.core;

import android.view.View;

import com.github.android.common.popup.animator.AbsAnimator;
import com.github.android.common.popup.animator.Animation;
import com.github.android.common.popup.annotation.Position;
import com.github.android.common.popup.interfaces.XPopupCallback;

/**
 * Created by fxb on 2020/5/8.
 * 弹窗基础属性
 */
public class PopupAttrs {
    public boolean isDismissOnBackPressed = true;  //按返回键是否消失
    public boolean isDismissOnTouchOutside = true; //点击外部消失
    public boolean hasShadowBg = true; // 是否有半透明的背景

    // 动画执行器，如果不指定，则会根据窗体类型popupType字段生成默认合适的动画执行器
    public Animation popupAnimation = null;
    public AbsAnimator customAnimator = null;
    public int animDuration = 300;

    public int maxWidth; // 最大宽度
    public int maxHeight; // 最大高度
    public boolean autoOpenSoftInput;//是否自动打开输入法
    public XPopupCallback xPopupCallback;

    public Boolean isMoveUpToKeyboard = true; //是否移动到软键盘上面，默认弹窗会移到软键盘上面

    public int position = Position.LEFT; //弹窗出现在目标的什么位置
    public Boolean hasStatusBarShadow = false;
    public int offsetX, offsetY;//x，y方向的偏移量
    public Boolean enableDrag = true;//是否启用拖拽
    public boolean isCenterHorizontal = false;//是否水平居中
    public boolean isRequestFocus = true; //弹窗是否强制抢占焦点
    public boolean autoFocusEditText = true; //是否让输入框自动获取焦点
    public boolean isDarkTheme = false; //是否是暗色调主题

    public View atView = null; // 依附于那个View显示

}
