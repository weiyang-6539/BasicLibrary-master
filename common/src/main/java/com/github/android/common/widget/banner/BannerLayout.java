package com.github.android.common.widget.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.android.common.R;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BannerLayout extends FrameLayout {
    protected int mInterval = 4000;//刷新间隔时间

    @IntDef({Indicator.NONE, Indicator.CIRCLE, Indicator.TEXT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Indicator {
        int NONE = 0;
        int CIRCLE = 1;
        int TEXT = 2;
    }

    @Indicator
    protected int indicator;//是否显示指示器
    protected RecyclerView indicatorContainer;
    protected TextView indicatorTv;
    protected Drawable mSelectedDrawable;
    protected Drawable mUnselectedDrawable;
    protected IndicatorAdapter indicatorAdapter;
    protected int indicatorMargin;//指示器间距
    private boolean isRect;
    protected RecyclerView mRecyclerView;
    protected BannerAdapter adapter;
    protected BannerLinearLayoutManager mLayoutManager;

    protected int WHAT_AUTO_PLAY = 1000;

    protected boolean hasInit;
    protected int bannerSize = 1;
    protected int currentIndex;
    protected boolean isPlaying;

    protected boolean isAutoPlaying;

    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                mRecyclerView.smoothScrollToPosition(++currentIndex);

                refreshIndicator();
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mInterval);
            }
            return false;
        }
    });

    public BannerLayout(Context context) {
        this(context, null);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerLayout);
        indicator = a.getInt(R.styleable.BannerLayout_blIndicator, Indicator.NONE);
        mInterval = a.getInt(R.styleable.BannerLayout_blInterval, mInterval);
        isAutoPlaying = a.getBoolean(R.styleable.BannerLayout_blAutoPlaying, true);
        mSelectedDrawable = a.getDrawable(R.styleable.BannerLayout_blIndicatorSelectedSrc);
        mUnselectedDrawable = a.getDrawable(R.styleable.BannerLayout_blIndicatorUnselectedSrc);
        if (!isRect) {
            if (mSelectedDrawable == null) {
                //绘制默认选中状态图形
                GradientDrawable selectedGradientDrawable = new GradientDrawable();
                selectedGradientDrawable.setShape(GradientDrawable.OVAL);
                selectedGradientDrawable.setColor(Color.parseColor("#1296db"));
                selectedGradientDrawable.setSize(dp2px(10), dp2px(10));
                selectedGradientDrawable.setCornerRadius(dp2px(2) >> 1);
                mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
            }
            if (mUnselectedDrawable == null) {
                //绘制默认未选中状态图形
                GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
                unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
                unSelectedGradientDrawable.setColor(Color.parseColor("#ffffff"));
                unSelectedGradientDrawable.setSize(dp2px(10), dp2px(10));
                unSelectedGradientDrawable.setCornerRadius(dp2px(2) >> 1);
                mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
            }
        } else {
            if (mSelectedDrawable == null) {
                //绘制默认选中状态图形
                GradientDrawable selectedGradientDrawable = new GradientDrawable();
                selectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                selectedGradientDrawable.setColor(Color.parseColor("#0ca18b"));
                selectedGradientDrawable.setSize(dp2px(14), dp2px(9));
                selectedGradientDrawable.setCornerRadius(dp2px(8) >> 1);
                mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
            }
            if (mUnselectedDrawable == null) {
                //绘制默认未选中状态图形
                GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
                unSelectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                unSelectedGradientDrawable.setColor(Color.parseColor("#dddddd"));
                unSelectedGradientDrawable.setSize(dp2px(9), dp2px(9));
                unSelectedGradientDrawable.setCornerRadius(dp2px(9) >> 1);
                mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
            }
        }

        indicatorMargin = a.getDimensionPixelSize(R.styleable.BannerLayout_blIndicatorSpace, dp2px(2));
        int marginLeft = a.getDimensionPixelSize(R.styleable.BannerLayout_blIndicatorMarginLeft, dp2px(16));
        int marginRight = a.getDimensionPixelSize(R.styleable.BannerLayout_blIndicatorMarginRight, dp2px(0));
        int marginBottom = a.getDimensionPixelSize(R.styleable.BannerLayout_blIndicatorMarginBottom, dp2px(11));
        int g = a.getInt(R.styleable.BannerLayout_blIndicatorGravity, 0);
        int gravity;
        if (g == 0) {
            gravity = GravityCompat.START;
        } else if (g == 2) {
            gravity = GravityCompat.END;
        } else {
            gravity = Gravity.CENTER;
        }
        int orientation = a.getInt(R.styleable.BannerLayout_blOrientation, 0);
        a.recycle();
        //recyclerView部分
        mRecyclerView = new RecyclerView(context);
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);
        mLayoutManager = new BannerLinearLayoutManager(context);
        mLayoutManager.setOrientation(orientation == 0 ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                //解决连续滑动时指示器不更新的问题
                if (bannerSize < 2) {
                    return;
                }
                int firstReal = mLayoutManager.findFirstVisibleItemPosition();
                View viewFirst = mLayoutManager.findViewByPosition(firstReal);
                float width = getWidth();
                if (width != 0 && viewFirst != null) {
                    float right = viewFirst.getRight();
                    float ratio = right / width;
                    if (ratio > 0.8) {
                        if (currentIndex != firstReal) {
                            currentIndex = firstReal;
                            refreshIndicator();
                        }
                    } else if (ratio < 0.2) {
                        if (currentIndex != firstReal + 1) {
                            currentIndex = firstReal + 1;
                            refreshIndicator();
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                int first = mLayoutManager.findFirstVisibleItemPosition();
                int last = mLayoutManager.findLastVisibleItemPosition();
                if (currentIndex != first && first == last) {
                    currentIndex = first;
                    refreshIndicator();
                }
            }
        });
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mRecyclerView, vpLayoutParams);
        //指示器部分
        indicatorContainer = new RecyclerView(context);

        LinearLayoutManager indicatorLayoutManager = new LinearLayoutManager(context, orientation, false);
        indicatorContainer.setLayoutManager(indicatorLayoutManager);
        indicatorAdapter = new IndicatorAdapter();
        indicatorContainer.setAdapter(indicatorAdapter);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | gravity;
        params.setMargins(marginLeft, 0, marginRight, marginBottom);
        addView(indicatorContainer, params);

        //文字指示器
        indicatorTv = new TextView(context);
        indicatorTv.setTextColor(Color.WHITE);
        indicatorTv.setTextSize(10);
        indicatorTv.setPadding(dp2px(8), dp2px(2), dp2px(8), dp2px(2));
        GradientDrawable selectedGradientDrawable = new GradientDrawable();
        selectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
        selectedGradientDrawable.setColor(Color.parseColor("#88888888"));
        selectedGradientDrawable.setCornerRadius(100);
        indicatorTv.setBackgroundDrawable(selectedGradientDrawable);
        LayoutParams paramsTv = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTv.gravity = Gravity.BOTTOM | gravity;
        paramsTv.setMargins(marginLeft, 0, marginRight, marginBottom);
        addView(indicatorTv, paramsTv);

        setIndicator(indicator);
    }

    /**
     * 设置轮播间隔时间
     *
     * @param millisecond 时间毫秒
     */
    public void setInterval(int millisecond) {
        this.mInterval = millisecond;
    }

    /**
     * 设置是否为圆点
     *
     * @param isRect 设置是否为圆点
     */
    public void setIsRect(boolean isRect) {
        this.isRect = isRect;
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    protected synchronized void setPlaying(boolean playing) {
        if (isAutoPlaying && hasInit) {
            if (!isPlaying && playing) {
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mInterval);
                isPlaying = true;
            } else if (isPlaying && !playing) {
                mHandler.removeMessages(WHAT_AUTO_PLAY);
                isPlaying = false;
            }
        }
    }

    /**
     * 设置是否禁止滚动播放
     */
    public void setAutoPlaying(boolean isAutoPlaying) {
        this.isAutoPlaying = isAutoPlaying;
        setPlaying(this.isAutoPlaying);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setIndicator(@Indicator int indicator) {
        this.indicator = indicator;
        if (indicator == Indicator.NONE) {
            indicatorContainer.setVisibility(GONE);
            indicatorTv.setVisibility(GONE);
        } else if (indicator == Indicator.CIRCLE) {
            indicatorContainer.setVisibility(VISIBLE);
            indicatorTv.setVisibility(GONE);
        } else if (indicator == Indicator.TEXT) {
            indicatorContainer.setVisibility(GONE);
            indicatorTv.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置轮播数据集
     */
    public void setBannerAdapter(BannerAdapter adapter) {
        setVisibility(VISIBLE);
        setPlaying(false);
        this.adapter = adapter;
        mRecyclerView.setAdapter(this.adapter);
        bannerSize = adapter.getDataSize();
        if (bannerSize > 1) {
            indicatorContainer.setVisibility(VISIBLE);
            currentIndex = bannerSize * 10000;
            mRecyclerView.scrollToPosition(currentIndex);

            indicatorAdapter.notifyDataSetChanged();
            setPlaying(true);
        } else {
            indicatorContainer.setVisibility(GONE);
            currentIndex = 0;
        }
        hasInit = true;

        setIndicator(indicator);
        refreshIndicator();
    }

    public void setSelect(int position) {
        this.currentIndex += position;

        mRecyclerView.scrollToPosition(currentIndex);
        if (adapter != null)
            adapter.notifyDataSetChanged();

        refreshIndicator();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPlaying(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);
                break;
        }
        //解决recyclerView嵌套问题
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //解决recyclerView嵌套问题
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //解决recyclerView嵌套问题
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            setPlaying(true);
        } else {
            setPlaying(false);
        }
    }

    /**
     * 标示点适配器
     */
    protected class IndicatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        int currentPosition = 0;

        public void setPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            ImageView bannerPoint = new ImageView(getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
            bannerPoint.setLayoutParams(lp);
            return new RecyclerView.ViewHolder(bannerPoint) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView bannerPoint = (ImageView) holder.itemView;
            bannerPoint.setImageDrawable(currentPosition == position ? mSelectedDrawable : mUnselectedDrawable);
        }

        @Override
        public int getItemCount() {
            return bannerSize;
        }
    }

    /**
     * 改变导航的指示点
     */
    @SuppressLint("DefaultLocale")
    protected synchronized void refreshIndicator() {
        if (indicator != Indicator.NONE && bannerSize >= 1) {
            indicatorAdapter.setPosition(currentIndex % bannerSize);
            indicatorAdapter.notifyDataSetChanged();

            indicatorTv.setText(String.format("%d/%d", currentIndex % bannerSize + 1, bannerSize));
        }
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private static class BannerLinearLayoutManager extends LinearLayoutManager {

        public BannerLinearLayoutManager(Context context) {
            super(context);
        }

        public BannerLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public BannerLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            RecyclerView.SmoothScroller smoothScroller = new BannerLinearSmoothScroller(recyclerView.getContext());
            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }

    }

    private static class BannerLinearSmoothScroller extends LinearSmoothScroller {
        public BannerLinearSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            //这里控制滑动速度，值越大速度越慢，暂定放慢10倍
            return super.calculateSpeedPerPixel(displayMetrics) * 10;
        }
    }
}