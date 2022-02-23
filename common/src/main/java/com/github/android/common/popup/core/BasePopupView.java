package com.github.android.common.popup.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.github.android.common.popup.animator.AbsAnimator;
import com.github.android.common.popup.animator.EmptyAnimator;
import com.github.android.common.popup.animator.ScaleAnimator;
import com.github.android.common.popup.animator.ScrollAnimator;
import com.github.android.common.popup.animator.ShadowBgAnimator;
import com.github.android.common.popup.animator.TranslateAnimator;
import com.github.android.common.popup.annotation.PopupStatus;
import com.github.android.common.popup.util.KeyboardUtils;
import com.github.android.common.popup.util.XPopupUtils;
import com.github.android.common.popup.util.navbar.NavigationBarObserver;
import com.github.android.common.popup.util.navbar.OnNavigationBarListener;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by fxb on 2020/5/8.
 * 弹窗抽象类, 大致可分为以下几类：
 * 1.居中弹窗（AbsCenterPopupView）
 * 2.底部弹窗（AbsBottomPopupView）
 * 3.左右侧滑弹窗（AbsDrawerPopupView）
 * 4.附着View弹窗（AbsAttachPopupView）
 * 5.局部阴影弹窗（AbsPartShadowPopupView）
 */
public abstract class BasePopupView extends FrameLayout implements OnNavigationBarListener {
    private static final Stack<BasePopupView> stack = new Stack<>(); //静态存储所有弹窗对象
    protected AbsAnimator popupContentAnimator;
    protected ShadowBgAnimator shadowBgAnimator;
    protected PopupAttrs popupAttrs;//弹窗属性

    @PopupStatus
    protected int status = PopupStatus.HIDE;

    private final int touchSlop;

    private boolean isCreated = false;
    private boolean hasMoveUp = false;

    private float x, y;

    private ViewGroup decorView; //每个弹窗所属的DecorView

    public ViewGroup getDecorView() {
        if (decorView == null) {
            decorView = (ViewGroup) getActivity().getWindow().getDecorView();
        }
        return decorView;
    }

    public Activity getActivity() {
        if (getContext() instanceof Activity) {
            return (Activity) getContext();
        } else {
            throw new IllegalArgumentException("You need use the context of Activity");
        }
    }

    public PopupAttrs getPopupAttrs() {
        return popupAttrs;
    }

    public void applyPopupAttrs(PopupAttrs attrs) {
        this.popupAttrs = attrs;

        shadowBgAnimator.setDuration(attrs.animDuration);
    }

    public BasePopupView(@NonNull Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        shadowBgAnimator = new ShadowBgAnimator(this);

        //1.子类弹窗或需要再加一级父容器，用来实现复杂效果（局部阴影，侧滑抽屉可手动拖拽消失..）
        ViewGroup rootView = createPopupRootView();
        if (rootView != null) {
            addView(rootView);

            View contentView = LayoutInflater.from(context).inflate(getPopupLayoutId(), rootView, false);
            rootView.addView(contentView);
            rootView.requestLayout();
        } else {
            //  添加Popup窗体内容View
            View contentView = LayoutInflater.from(context).inflate(getPopupLayoutId(), this, false);
            addView(contentView);
        }

        // 事先隐藏，等测量完毕恢复，避免View影子跳动现象。
        getPopupContentView().setAlpha(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果自己接触到了点击，并且不在PopupContentView范围内点击，则进行判断是否是点击事件,如果是，则dismiss
        Rect rect = new Rect();
        getPopupContentView().getGlobalVisibleRect(rect);
        if (!XPopupUtils.isInRect(event.getX(), event.getY(), rect)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (distance < touchSlop && popupAttrs.isDismissOnTouchOutside) {
                        dismiss();
                    }
                    x = 0;
                    y = 0;
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stack.clear();
        removeCallbacks(doAfterShowTask);
        removeCallbacks(doAfterDismissTask);
        KeyboardUtils.removeLayoutChangeListener(getDecorView(), this);
        if (showSoftInputTask != null) removeCallbacks(showSoftInputTask);
        status = PopupStatus.HIDE;
        showSoftInputTask = null;
        hasMoveUp = false;
    }

    protected ViewGroup createPopupRootView() {
        return null;
    }

    protected abstract int getPopupLayoutId();

    protected void onCreate() {
    }

    /**
     * 获取内容View，本质上PopupView显示的内容都在这个View内部。
     * 而且我们对PopupView执行的动画，也是对它执行的动画
     *
     * @return
     */
    public View getPopupContentView() {
        return getChildAt(0);
    }

    public View getPopupImplView() {
        return ((ViewGroup) getPopupContentView()).getChildAt(0);
    }

    protected View getTargetSizeView() {
        return getPopupContentView();
    }

    public int getAnimationDuration() {
        if (popupContentAnimator == null)
            return 10;

        return popupAttrs.animDuration;
    }

    /**
     * 弹窗的最大宽度，一般用来限制布局宽度为wrap或者match时的最大宽度
     *
     * @return
     */
    protected int getMaxWidth() {
        return 0;
    }

    /**
     * 弹窗的最大高度，一般用来限制布局高度为wrap或者match时的最大宽度
     *
     * @return
     */
    protected int getMaxHeight() {
        return popupAttrs.maxHeight;
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     *
     * @return
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     *
     * @return
     */
    protected int getPopupHeight() {
        return 0;
    }

    /**
     * 执行初始化
     */
    protected void init() {
        if (status == PopupStatus.EXECUTING)
            return;

        status = PopupStatus.EXECUTING;
        NavigationBarObserver.getInstance().register(getContext());
        NavigationBarObserver.getInstance().addOnNavigationBarListener(this);

        //1. 初始化Popup
        if (!isCreated) {
            isCreated = true;
            onCreate();
            if (popupAttrs.xPopupCallback != null)
                popupAttrs.xPopupCallback.onCreated();
        }
        //apply size dynamic
        XPopupUtils.setWidthHeight(getTargetSizeView(),
                (getMaxWidth() != 0 && getPopupWidth() > getMaxWidth()) ? getMaxWidth() : getPopupWidth(),
                (getMaxHeight() != 0 && getPopupHeight() > getMaxHeight()) ? getMaxHeight() : getPopupHeight()
        );

        postDelayed(() -> {
            // 如果有导航栏，则不能覆盖导航栏，判断各种屏幕方向
            applySize(false);
            getPopupContentView().setAlpha(1f);

            //2. 收集动画执行器
            collectAnimator();

            if (popupAttrs.xPopupCallback != null)
                popupAttrs.xPopupCallback.beforeShow();

            //3. 执行动画
            doShowAnimation();

            doAfterShow();

            //目前全屏弹窗快速弹出输入法有问题，暂时用这个方案
            /*if (!(BasePopupView.this instanceof FullScreenPopupView))
                focusAndProcessBackPress();*/
            focusAndProcessBackPress();
        }, 50);
    }

    private void collectAnimator() {
        if (popupContentAnimator == null) {
            // 优先使用自定义的动画器
            if (popupAttrs.customAnimator != null) {
                popupContentAnimator = popupAttrs.customAnimator;
                popupContentAnimator.targetView = getPopupContentView();
            } else {
                //根据
                popupContentAnimator = getPopupAnimator();
            }

            //3. 初始化动画执行器
            shadowBgAnimator.initAnimator();
            if (popupContentAnimator != null) {
                popupContentAnimator.initAnimator();
                popupContentAnimator.setDuration(popupAttrs.animDuration);
            }
        }
    }

    @Override
    public void onNavigationBarChange(boolean show) {
        if (show) {
            applySize(true);
        } else {
            applyFull();
        }
    }

    protected void applyFull() {
        FrameLayout.LayoutParams params = (LayoutParams) getLayoutParams();
        params.topMargin = 0;
        params.leftMargin = 0;
        params.bottomMargin = 0;
        params.rightMargin = 0;
        setLayoutParams(params);
    }

    protected void applySize(boolean isShowNavBar) {
        FrameLayout.LayoutParams params = (LayoutParams) getLayoutParams();
        int rotation = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        boolean isNavBarShown = isShowNavBar || XPopupUtils.isNavBarVisible(getContext());
        if (rotation == 0) {
            params.leftMargin = 0;
            params.rightMargin = 0;
            params.bottomMargin = isNavBarShown ? XPopupUtils.getNavBarHeight() : 0;
        } else if (rotation == 1) {
            params.bottomMargin = 0;
            params.rightMargin = isNavBarShown ? XPopupUtils.getNavBarHeight() : 0;
            params.leftMargin = 0;
        } else if (rotation == 3) {
            params.bottomMargin = 0;
            params.leftMargin = 0;
            params.rightMargin = isNavBarShown ? XPopupUtils.getNavBarHeight() : 0;
        }
        setLayoutParams(params);
    }

    public void show() {
        if (getParent() != null)
            return;

        KeyboardUtils.registerSoftInputChangedListener(getActivity(), this, height -> {
            if (height == 0) { // 说明对话框隐藏
                XPopupUtils.moveDown(this);
                hasMoveUp = false;
            } else {
                //when show keyboard, move up
                XPopupUtils.moveUpToKeyboard(height, this);
                hasMoveUp = true;
            }
        });

        // 1. add PopupView to its decorView after measured.
        getDecorView().post(() -> {
            if (getParent() != null) {
                ((ViewGroup) getParent()).removeView(this);
            }
            getDecorView().addView(this, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            //2. do init，game start.
            init();
        });
    }

    protected void doAfterShow() {
        removeCallbacks(doAfterShowTask);
        postDelayed(doAfterShowTask, getAnimationDuration());
    }

    private Runnable doAfterShowTask = () -> {
        status = PopupStatus.SHOW;
        //onShow();
        if (popupAttrs != null && popupAttrs.xPopupCallback != null)
            popupAttrs.xPopupCallback.onShow();
        if (XPopupUtils.getDecorViewInvisibleHeight((Activity) getContext()) > 0 && !hasMoveUp) {
            XPopupUtils.moveUpToKeyboard(XPopupUtils.getDecorViewInvisibleHeight((Activity) getContext()), this);
        }
    };

    private ShowSoftInputTask showSoftInputTask;

    public void focusAndProcessBackPress() {
        if (popupAttrs.isRequestFocus) {
            setFocusableInTouchMode(true);
            requestFocus();
            if (!stack.contains(this))
                stack.push(this);
        }
        // 此处焦点可能被内容的EditText抢走，也需要给EditText也设置返回按下监听
        setOnKeyListener(new BackPressListener());
        if (!popupAttrs.autoFocusEditText)
            showSoftInput(this);

        //let all EditText can process back pressed.
        ArrayList<EditText> list = new ArrayList<>();
        XPopupUtils.findAllEditText(list, (ViewGroup) getPopupContentView());
        for (int i = 0; i < list.size(); i++) {
            final EditText et = list.get(i);
            et.setOnKeyListener(new BackPressListener());
            if (i == 0 && popupAttrs.autoFocusEditText) {
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
                et.requestFocus();
                showSoftInput(et);
            }
        }
    }

    protected void showSoftInput(View focusView) {
        if (popupAttrs.autoOpenSoftInput) {
            if (showSoftInputTask == null) {
                showSoftInputTask = new ShowSoftInputTask(focusView);
            } else {
                removeCallbacks(showSoftInputTask);
            }
            postDelayed(showSoftInputTask, 10);
        }
    }

    protected void dismissOrHideSoftInput() {
        if (KeyboardUtils.sDecorViewInvisibleHeightPre == 0)
            dismiss();
        else
            KeyboardUtils.hideSoftInput(BasePopupView.this);
    }

    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doShowAnimation() {
        if (popupAttrs.hasShadowBg) {
            if (popupAttrs.popupAnimation == null || popupAttrs.popupAnimation.isEmptyType())
                shadowBgAnimator.isZeroDuration = true;
            shadowBgAnimator.animateShow();
        }
        if (popupContentAnimator != null)
            popupContentAnimator.animateShow();
    }

    /**
     * 执行消失动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doDismissAnimation() {
        if (popupAttrs.hasShadowBg) {
            shadowBgAnimator.animateDismiss();
        }
        if (popupContentAnimator != null)
            popupContentAnimator.animateDismiss();
    }


    /**
     * 消失
     */
    public void dismiss() {
        if (status == PopupStatus.EXECUTING || status == PopupStatus.HIDE)
            return;
        status = PopupStatus.EXECUTING;

        clearFocus();
        doDismissAnimation();
        doAfterDismiss();
    }

    public void delayDismiss(long delay) {
        if (delay < 0) delay = 0;
        postDelayed(this::dismiss, delay);
    }

    public void delayDismissWith(long delay, Runnable runnable) {
        this.dismissWithRunnable = runnable;
        delayDismiss(delay);
    }

    protected void doAfterDismiss() {
        if (popupAttrs.autoOpenSoftInput) KeyboardUtils.hideSoftInput(this);
        removeCallbacks(doAfterDismissTask);
        postDelayed(doAfterDismissTask, getAnimationDuration());
    }

    private Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            //onDismiss();
            if (popupAttrs != null && popupAttrs.xPopupCallback != null) {
                popupAttrs.xPopupCallback.onDismiss();
            }
            if (dismissWithRunnable != null) {
                dismissWithRunnable.run();
                dismissWithRunnable = null;//no cache, avoid some bad edge effect.
            }
            status = PopupStatus.HIDE;
            NavigationBarObserver.getInstance().removeOnNavigationBarListener(BasePopupView.this);

            if (!stack.isEmpty()) stack.pop();
            if (popupAttrs != null && popupAttrs.isRequestFocus) {
                if (!stack.isEmpty()) {
                    //stack.get(stack.size() - 1).focusAndProcessBackPress();
                } else {
                    // 让根布局拿焦点，避免布局内RecyclerView类似布局获取焦点导致布局滚动
                    View needFocusView = ((Activity) getContext()).findViewById(android.R.id.content);
                    if (needFocusView != null) {
                        needFocusView.setFocusable(true);
                        needFocusView.setFocusableInTouchMode(true);
                    }
                }
            }

            // 移除弹窗，GameOver
            if (popupAttrs != null && getDecorView() != null) {
                getDecorView().removeView(BasePopupView.this);
                KeyboardUtils.removeLayoutChangeListener(getDecorView(), BasePopupView.this);
            }
        }
    };

    Runnable dismissWithRunnable;

    public void dismissWith(Runnable runnable) {
        this.dismissWithRunnable = runnable;
        dismiss();
    }


    /**
     * 根据PopupInfo的popupAnimation字段来生成对应的内置的动画执行器
     */
    protected AbsAnimator getPopupAnimator() {
        if (popupAttrs == null || popupAttrs.popupAnimation == null)
            return null;
        if (popupAttrs.popupAnimation.isEmptyType())
            return new EmptyAnimator();
        else if (popupAttrs.popupAnimation.isScaleType())
            return new ScaleAnimator(getPopupContentView(), popupAttrs.popupAnimation);
        else if (popupAttrs.popupAnimation.isTranslateType())
            return new TranslateAnimator(getPopupContentView(), popupAttrs.popupAnimation);
        else if (popupAttrs.popupAnimation.isScrollType())
            return new ScrollAnimator(getPopupContentView(), popupAttrs.popupAnimation);

        return null;
    }

    private static class ShowSoftInputTask implements Runnable {
        View focusView;
        boolean isDone = false;

        ShowSoftInputTask(View focusView) {
            this.focusView = focusView;
        }

        @Override
        public void run() {
            if (focusView != null && !isDone) {
                isDone = true;
                KeyboardUtils.showSoftInput(focusView);
            }
        }
    }

    private class BackPressListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                if (popupAttrs.isDismissOnBackPressed &&
                        (popupAttrs.xPopupCallback == null || !popupAttrs.xPopupCallback.onBackPressed()))

                    if (KeyboardUtils.sDecorViewInvisibleHeightPre == 0)
                        dismiss();
                    else
                        KeyboardUtils.hideSoftInput(BasePopupView.this);
                return true;
            }
            return false;
        }
    }

}
