package com.github.android.popup_demo.fragment;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.android.common.base.BaseFragment;
import com.github.android.common.popup.XPopup;
import com.github.android.common.popup.animator.AnimationFactory;
import com.github.android.common.popup.annotation.AttachType;
import com.github.android.common.popup.annotation.Position;
import com.github.android.common.widget.ComplexView;
import com.github.android.popup_demo.R;
import com.github.android.popup_demo.popup.BottomInputPopupView;
import com.github.android.popup_demo.popup.BottomPopupView;
import com.github.android.popup_demo.popup.ConfirmPopupView;
import com.github.android.popup_demo.popup.DrawerPopupView;
import com.github.android.popup_demo.popup.HorizontalPopupView;
import com.github.android.popup_demo.popup.ListPopupView;
import com.github.android.popup_demo.popup.LoadingPopupView;
import com.github.android.popup_demo.popup.PartShadowPopupView;
import com.github.android.popup_demo.popup.AttachPopupView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by fxb on 2020/5/25.
 */
public class _01Fragment extends BaseFragment {
    @BindView(R.id.fl_long_click)
    FrameLayout frameLayout;
    private ListPopupView listPopup;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_01;
    }

    @Override
    protected void initialize() {
        listPopup = new XPopup.Builder(getContext())
                .hasShadowBg(false)
                .asCustom(new ListPopupView(getActivity()));
        frameLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                listPopup.resetPoint((int) event.getRawX(), (int) event.getRawY());
            return false;
        });
    }

    @BindView(R.id.cv_01)
    ComplexView cv_01;
    @BindView(R.id.cv_02)
    ComplexView cv_02;

    @OnClick(R.id.cv_01)
    public void onClickCv01() {
        new XPopup.Builder(getContext())
                .atView(cv_01)
                .animation(AnimationFactory.translateFromTop(false))
                .asCustom(new PartShadowPopupView(getContext()))
                .show();
    }

    @OnClick(R.id.cv_02)
    public void onClickCv02() {
        new XPopup.Builder(getContext())
                .atView(cv_02)
                .asCustom(new PartShadowPopupView(getContext()))
                .show();
    }

    @OnClick(R.id.btn_show_confirm)
    public void showConfirm() {
        ConfirmPopupView confirmPop = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .hasShadowBg(true)
                .animation(AnimationFactory.scaleFromCenter(false))
                .asCustom(new ConfirmPopupView(getContext()));

        confirmPop.show();
    }

    @OnClick(R.id.btn_show_loading)
    public void showLoading() {
        LoadingPopupView loadingPopup = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(true)
                .hasShadowBg(true)
                .animation(AnimationFactory.scaleFromCenter(false))
                .asCustom(new LoadingPopupView(getContext()));
        loadingPopup.show();
    }

    @OnClick(R.id.btn_show_left_drawer)
    public void onClickShowLeftDrawer() {
        DrawerPopupView drawerPopup = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .hasShadowBg(true)
                .position(Position.LEFT)
                .hasStatusBarShadow(true)
                .asCustom(new DrawerPopupView(getContext()));

        drawerPopup.show();
    }

    @OnClick(R.id.btn_show_right_drawer)
    public void onClickShowRightDrawer() {
        DrawerPopupView drawerPopup = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .hasShadowBg(true)
                .position(Position.RIGHT)
                .hasStatusBarShadow(true)
                .asCustom(new DrawerPopupView(getContext()));

        drawerPopup.show();
    }

    @OnClick({R.id.btn_show_bottom})
    public void onClickShowBottom() {
        new XPopup.Builder(getContext())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .hasShadowBg(true)
                .asCustom(new BottomPopupView(getContext()))
                .show();
    }

    @OnLongClick(R.id.fl_long_click)
    public boolean onLongClick() {
        listPopup.show();
        return false;
    }

    @BindView(R.id.tv_11)
    TextView tv_11;
    @BindView(R.id.tv_22)
    TextView tv_22;
    @BindView(R.id.tv_33)
    TextView tv_33;
    @BindView(R.id.tv_44)
    TextView tv_44;

    @OnClick(R.id.tv_11)
    public void onClickTv11() {
        new XPopup.Builder(getContext())
                .animation(AnimationFactory.scaleFromLeftTop(false))
                .asCustom(new ListPopupView(getContext()))
                .atView(tv_11, AttachType.LEFT_TO_LEFT, AttachType.TOP_TO_BOTTOM)
                .show();
    }

    @OnClick(R.id.tv_22)
    public void onClickTv22() {
        new XPopup.Builder(getContext())
                .animation(AnimationFactory.scrollFromRightTop(false))
                .asCustom(new ListPopupView(getContext()))
                .atView(tv_22, AttachType.RIGHT_TO_RIGHT, AttachType.TOP_TO_BOTTOM)
                .show();
    }

    @OnClick(R.id.tv_33)
    public void onClickTv33() {
        new XPopup.Builder(getContext())
                .animation(AnimationFactory.scrollFromLeftBottom(false))
                .asCustom(new ListPopupView(getContext()))
                .atView(tv_33, AttachType.LEFT_TO_LEFT, AttachType.BOTTOM_TO_TOP)
                .show();
    }

    @OnClick(R.id.tv_44)
    public void onClickTv44() {
        new XPopup.Builder(getContext())
                .animation(AnimationFactory.scrollFromRightBottom(false))
                .asCustom(new ListPopupView(getContext()))
                .atView(tv_44, AttachType.RIGHT_TO_RIGHT, AttachType.BOTTOM_TO_TOP)
                .show();
    }

    @OnClick(R.id.btn_show_anyway)
    public void onClickBtnShowAnyway() {
        new XPopup.Builder(getContext())
                .hasShadowBg(false)
                .offsetX(30000)
                .animation(AnimationFactory.scaleFromTop(true))
                .asCustom(new AttachPopupView(getContext()))
                .show();
    }

    @BindView(R.id.cv_show_horizontal)
    ComplexView cv_show_horizontal;

    @OnClick(R.id.cv_show_horizontal)
    public void onClickCvShowHorizontal() {
        new XPopup.Builder(getContext())
                .hasShadowBg(false)
                .animation(AnimationFactory.scaleFromRight(true))
                .asCustom(new HorizontalPopupView(getContext()))
                .atView(cv_show_horizontal, AttachType.RIGHT_TO_LEFT, AttachType.CENTER_VERTICAL)
                .show();
    }

    @OnClick(R.id.btn_show_bottom_input)
    public void onClickShowBottomInput(View v) {
        new XPopup.Builder(getContext())
                .hasShadowBg(true)
                .autoOpenSoftInput(true)
                .asCustom(new BottomInputPopupView(getContext()))
                .show();

    }
}

