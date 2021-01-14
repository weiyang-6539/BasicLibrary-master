package com.github.android.common.widget.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.android.common.R;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created by fxb on 2020/9/23.
 * 轮播图基类封装
 */
public class BannerLayout extends FrameLayout {

    private RecyclerView mRecyclerView;

    public BannerLayout(@NonNull Context context) {
        this(context, null);
    }

    public BannerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化属性
        initAttrs(context, attrs);
        //初始化banner
        initBanner();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerLayout);

            Arrays.fill(radii, 30);
            a.recycle();
        }
    }

    private void initBanner() {
        mRecyclerView = new RecyclerView(getContext());
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {

            }
        });
        addView(mRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    private int bannerSize;
    private int currentIndex;

    public void setAdapter(BaseQuickAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        bannerSize = adapter.getData().size();
        if (bannerSize > 1) {
            currentIndex = bannerSize * 10000;
            mRecyclerView.scrollToPosition(currentIndex);
        } else {
            currentIndex = 0;
        }
    }

    /**
     * 绘制圆角的逻辑
     */
    private Path mClipPath = new Path();
    private Region mAreaRegion = new Region();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] radii = new float[8];

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RectF areas = new RectF();
        areas.left = getPaddingLeft();
        areas.top = getPaddingTop();
        areas.right = w - getPaddingRight();
        areas.bottom = h - getPaddingBottom();
        mClipPath.reset();
        mClipPath.addRoundRect(areas, radii, Path.Direction.CW);

        Region clip = new Region((int) areas.left, (int) areas.top, (int) areas.right, (int) areas.bottom);
        mAreaRegion.setPath(mClipPath, clip);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),
                null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        // 剪裁
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mClipPath, mPaint);
        canvas.restore();
    }
}
