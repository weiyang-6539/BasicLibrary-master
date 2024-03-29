package com.github.android.common.popup.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.github.android.common.popup.XPopup;
import com.github.android.common.popup.animator.ShadowBgAnimator;
import com.github.android.common.popup.annotation.DragStatus;
import com.github.android.common.popup.annotation.Position;
import com.github.android.common.popup.util.XPopupUtils;

/**
 * Created by fxb on 2020/5/14.
 * 弹窗定制侧滑效果控件
 */
public class PopupDrawerLayout extends FrameLayout {
    @DragStatus
    int status = DragStatus.CLOSE;
    ViewDragHelper dragHelper;
    View placeHolder, mChild;
    @Position
    public int position = Position.LEFT;
    ShadowBgAnimator bgAnimator = new ShadowBgAnimator();
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    int defaultColor = Color.TRANSPARENT;
    public boolean isDrawStatusBarShadow = false;
    float fraction = 0f;
    public boolean enableShadow = true;

    public PopupDrawerLayout(Context context) {
        this(context, null);
    }

    public PopupDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragHelper = ViewDragHelper.create(this, callback);
    }

    public void setDrawerPosition(@Position int position) {
        this.position = position;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        placeHolder = getChildAt(0);
        mChild = getChildAt(1);
    }

    float ty;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ty = getTranslationY();
    }

    boolean hasLayout = false;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        placeHolder.layout(0, 0, placeHolder.getMeasuredWidth(), placeHolder.getMeasuredHeight());
        if (!hasLayout) {
            if (position == Position.LEFT) {
                mChild.layout(-mChild.getMeasuredWidth(), 0, 0, getMeasuredHeight());
            } else {
                mChild.layout(getMeasuredWidth(), 0, getMeasuredWidth() + mChild.getMeasuredWidth(), getMeasuredHeight());
            }
            hasLayout = true;
        } else {
            mChild.layout(mChild.getLeft(), mChild.getTop(), mChild.getRight(), mChild.getBottom());
        }
    }

    boolean isIntercept = false;
    float x, y;
    boolean isToLeft, canChildScrollLeft;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        isToLeft = ev.getX() < x;
        x = ev.getX();
        y = ev.getY();
//        boolean canChildScrollRight = canScroll(this, ev.getX(), ev.getY(), -1);
        canChildScrollLeft = canScroll(this, ev.getX(), ev.getY(), 1);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            x = 0;
            y = 0;
        }
        isIntercept = dragHelper.shouldInterceptTouchEvent(ev);
        if (isToLeft && !canChildScrollLeft) {
            return isIntercept;
        }

        boolean canChildScrollHorizontal = canScroll(this, ev.getX(), ev.getY());
        if (!canChildScrollHorizontal) return isIntercept;

        return super.onInterceptTouchEvent(ev);
    }

    private boolean canScroll(ViewGroup group, float x, float y, int direction) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            int[] location = new int[2];
            child.getLocationInWindow(location);
            Rect rect = new Rect(location[0], location[1], location[0] + child.getWidth(),
                    location[1] + child.getHeight());
            boolean inRect = XPopupUtils.isInRect(x, y, rect);
            if (inRect && child instanceof ViewGroup) {
                if (child instanceof ViewPager) {
                    ViewPager pager = (ViewPager) child;
                    if (direction == 0) {
                        return pager.canScrollHorizontally(-1) || pager.canScrollHorizontally(1);
                    }
                    return pager.canScrollHorizontally(direction);
                } else if (child instanceof HorizontalScrollView) {
                    HorizontalScrollView hsv = (HorizontalScrollView) child;
                    if (direction == 0) {
                        return hsv.canScrollHorizontally(-1) || hsv.canScrollHorizontally(1);
                    }
                    return hsv.canScrollHorizontally(direction);
                } else {
                    return canScroll((ViewGroup) child, x, y, direction);
                }
            }
        }
        return false;
    }

    private boolean canScroll(ViewGroup group, float x, float y) {
        return canScroll(group, x, y, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dragHelper.continueSettling(true)) return true;
        dragHelper.processTouchEvent(event);
        return true;
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return !dragHelper.continueSettling(true);
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (child == placeHolder) return left;
            return fixLeft(left);
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == placeHolder) {
                placeHolder.layout(0, 0, placeHolder.getMeasuredWidth(), placeHolder.getMeasuredHeight());
                int newLeft = fixLeft(mChild.getLeft() + dx);
                mChild.layout(newLeft, mChild.getTop(), newLeft + mChild.getMeasuredWidth(), mChild.getBottom());
                calcFraction(newLeft);
            } else {
                calcFraction(left);
            }
        }

        private void calcFraction(int left) {
            // fraction = (now - start) * 1f / (end - start)
            if (position == Position.LEFT) {
                fraction = (left + mChild.getMeasuredWidth()) * 1f / mChild.getMeasuredWidth();
                if (left == -mChild.getMeasuredWidth() && listener != null && status != DragStatus.CLOSE) {
                    status = DragStatus.CLOSE;
                    listener.onClose();
                }
            } else if (position == Position.RIGHT) {
                fraction = (getMeasuredWidth() - left) * 1f / mChild.getMeasuredWidth();
                if (left == getMeasuredWidth() && listener != null && status != DragStatus.CLOSE) {
                    status = DragStatus.CLOSE;
                    listener.onClose();
                }
            }
            if (enableShadow)
                setBackgroundColor(bgAnimator.calculateBgColor(fraction));
            if (listener != null) {
                listener.onDismissing(fraction);
                if (fraction == 1f && status != DragStatus.OPEN) {
                    status = DragStatus.OPEN;
                    listener.onOpen();
                }
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (releasedChild == placeHolder && xvel == 0) {
                close();
                return;
            }
            if (releasedChild == mChild && isToLeft && !canChildScrollLeft && xvel < -500) {
                close();
                return;
            }

            int centerLeft = 0;
            int finalLeft = 0;
            if (position == Position.LEFT) {
                if (xvel < -1000) {
                    finalLeft = -mChild.getMeasuredWidth();
                } else {
                    centerLeft = -mChild.getMeasuredWidth() / 2;
                    finalLeft = mChild.getLeft() < centerLeft ? -mChild.getMeasuredWidth() : 0;
                }
            } else {
                if (xvel > 1000) {
                    finalLeft = getMeasuredWidth();
                } else {
                    centerLeft = getMeasuredWidth() - mChild.getMeasuredWidth() / 2;
                    finalLeft = releasedChild.getLeft() < centerLeft ? getMeasuredWidth() - mChild.getMeasuredWidth() : getMeasuredWidth();
                }
            }
            dragHelper.smoothSlideViewTo(mChild, finalLeft, releasedChild.getTop());
            ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
        }
    };

    private int fixLeft(int left) {
        if (position == Position.LEFT) {
            if (left < -mChild.getMeasuredWidth()) left = -mChild.getMeasuredWidth();
            if (left > 0) left = 0;
        } else if (position == Position.RIGHT) {
            if (left < (getMeasuredWidth() - mChild.getMeasuredWidth()))
                left = (getMeasuredWidth() - mChild.getMeasuredWidth());
            if (left > getMeasuredWidth()) left = getMeasuredWidth();
        }
        return left;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(false)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    Paint paint;
    Rect shadowRect;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isDrawStatusBarShadow) {
            if (paint == null) {
                paint = new Paint();
                shadowRect = new Rect(0, 0, getMeasuredHeight(), XPopupUtils.getStatusBarHeight());
            }
            paint.setColor((Integer) argbEvaluator.evaluate(fraction, defaultColor, XPopup.statusBarShadowColor));
            canvas.drawRect(shadowRect, paint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        status = DragStatus.CLOSE;
        fraction = 0f;
        setTranslationY(ty);
    }

    /**
     * 打开Drawer
     */
    public void open() {
        post(() -> {
            dragHelper.smoothSlideViewTo(mChild, position == Position.LEFT ? 0 : (mChild.getLeft() - mChild.getMeasuredWidth()), 0);
            ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
        });
    }

    public boolean isCanClose = true;

    /**
     * 关闭Drawer
     */
    public void close() {
        if (dragHelper.continueSettling(true)) return;
        if (!isCanClose) return;
        post(() -> {
            dragHelper.smoothSlideViewTo(mChild, position == Position.LEFT ? -mChild.getMeasuredWidth() : getMeasuredWidth(), 0);
            ViewCompat.postInvalidateOnAnimation(PopupDrawerLayout.this);
        });
    }

    private OnCloseListener listener;

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public interface OnCloseListener {
        void onClose();

        void onOpen();

        /**
         * 关闭过程中执行
         *
         * @param fraction 关闭的百分比
         */
        void onDismissing(float fraction);
    }
}
