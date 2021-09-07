package com.github.android.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;

/**
 * Created by fxb on 2020/5/25.
 */
public abstract class BaseFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private View rootView;
    private final AtomicBoolean isInitialize = new AtomicBoolean(false);//页面初始化的标记

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        safeInit();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        safeInit();
    }

    /**
     * 布局Id，每个子类需要返回
     */
    protected abstract int getLayoutId();

    /**
     * 页面初始化，点击事件初始化
     */
    protected abstract void initialize();

    private void safeInit() {
        if (rootView != null) {
            if (getUserVisibleHint()) {
                if (isInitialize.compareAndSet(false, true)) {
                    initialize();
                }
            }
            onFragmentVisibleChange(getUserVisibleHint());
        }
    }

    /**
     * 当前碎片可见变化，一般用于页面可见时重新网络请求
     */
    protected void onFragmentVisibleChange(boolean isVisibleToUser) {
    }
}
