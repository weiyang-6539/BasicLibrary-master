package com.github.wyang.basiclibrarydemo;

import android.widget.TextView;

/**
 * Created by fxb on 2020/8/27.
 */
public class ShapeActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_shape_view;
    }

    @Override
    protected void initView() {
        //findViewById(R.id.cl_shape).setSelected(true);

        TextView tv_test = findViewById(R.id.tv_test);
        TextView tv_test2 = findViewById(R.id.tv_test2);

        tv_test2.setOnClickListener(v -> {
            if (!tv_test.isEnabled()) {
                tv_test.setEnabled(true);
            } else if (tv_test.isSelected()) {
                tv_test.setEnabled(false);
            } else {
                tv_test.setSelected(true);
            }
        });
    }

}
