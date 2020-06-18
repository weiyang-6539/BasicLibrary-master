package com.github.android.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.android.common.R;

/**
 * Created by fxb on 2020/5/25.
 * 公用组件，支持状态：加载中 异常错误 无数据 返回成功
 */
public class StateLayout extends FrameLayout {

    private View loadingView;
    private View errorView;
    private View emptyView;
    private View successView;

    private int loadingLayoutId;
    private int errorLayoutId;
    private int emptyLayoutId;
    private int successLayoutId;

    public StateLayout(@NonNull Context context) {
        this(context, null);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateLayout);
            loadingLayoutId = a.getResourceId(R.styleable.StateLayout_slLoadingLayoutId, 0);
            errorLayoutId = a.getResourceId(R.styleable.StateLayout_slErrorLayoutId, 0);
            emptyLayoutId = a.getResourceId(R.styleable.StateLayout_slEmptyLayoutId, 0);
            successLayoutId = a.getResourceId(R.styleable.StateLayout_slSuccessLayoutId, 0);
            a.recycle();
        }
    }

    public View getLoadingView() {
        if (loadingLayoutId != 0) {
            loadingView = LayoutInflater.from(getContext()).inflate(loadingLayoutId, this, false);
        } else {
            loadingView = new ProgressBar(getContext());
        }
        return loadingView;
    }

    public View getErrorView() {
        if (errorLayoutId != 0) {
            errorView = LayoutInflater.from(getContext()).inflate(errorLayoutId, this, false);
        } else {
            TextView view = new TextView(getContext());
            view.setText("加载出错，请重试");
            view.setGravity(Gravity.CENTER);
            view.setOnClickListener(v -> {

            });
            errorView = view;
        }
        return errorView;
    }

    public View getEmptyView() {
        if (emptyLayoutId != 0) {
            emptyView = LayoutInflater.from(getContext()).inflate(emptyLayoutId, this, false);
        } else {
            TextView view = new TextView(getContext());
            view.setText("暂无数据");
            view.setGravity(Gravity.CENTER);

            emptyView = view;
        }
        return emptyView;
    }

    public View getSuccessView() {
        if (successLayoutId != 0){

        }
        return successView;
    }

    @IntDef({State.LOADING, State.EMPTY, State.ERROR, State.SUCCESS})
    public @interface State {
        int LOADING = 0;//默认状态
        int ERROR = 1;//加载过程出现异常
        int EMPTY = 2;//返回结果无数据
        int SUCCESS = 3;//返回结果有数据
    }
}
