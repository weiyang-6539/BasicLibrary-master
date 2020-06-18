package com.github.android.common.popup.interfaces;

/**
 * Created by fxb on 2020/5/8.
 */
public interface XPopupCallback {
    /**
     * 弹窗的onCreate方法执行完调用
     */
    void onCreated();

    /**
     * 在show之前执行，由于onCreated只执行一次，如果想多次更新数据可以在该方法中
     */
    void beforeShow();

    /**
     * 完全显示的时候执行
     */
    void onShow();

    /**
     * 完全消失的时候执行
     */
    void onDismiss();

    /**
     * 暴漏返回按键的处理，如果返回true，XPopup不会处理；如果返回false，XPopup会处理，
     *
     * @return
     */
    boolean onBackPressed();

    class SimpleCallback implements XPopupCallback {

        @Override
        public void onCreated() {

        }

        @Override
        public void beforeShow() {

        }

        @Override
        public void onShow() {

        }

        @Override
        public void onDismiss() {

        }

        @Override
        public boolean onBackPressed() {
            return false;
        }
    }
}
