package com.github.wyang.basiclibrarydemo;

import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by fxb on 2020/9/4.
 */
public class TestActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        RadioGroup rg = findViewById(R.id.rg);
        rg.addView(createBtn("按钮1", true, false));
        rg.addView(createBtn("按钮2", false, false));
        rg.addView(createBtn("按钮3", false, false));
        rg.addView(createBtn("按钮4", false, false));
        rg.addView(createBtn("按钮5", false, false));
        rg.addView(createBtn("按钮6", false, true));
    }

    private RadioButton createBtn(String text, boolean isLeft, boolean isRight) {
        RadioButton btn = new RadioButton(this);
        btn.setText(text);
        btn.setTextColor(getResources().getColorStateList(R.color.selector_segment_text));
        btn.setButtonDrawable(null);
        if (!isLeft && !isRight)
            btn.setBackgroundResource(R.drawable.selector_segment_middle_bg);
        else if (isLeft)
            btn.setBackgroundResource(R.drawable.selector_segment_left_bg);
        else
            btn.setBackgroundResource(R.drawable.selector_segment_right_bg);
        btn.setGravity(Gravity.CENTER);
        btn.setLayoutParams(new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.MATCH_PARENT, 1));
        return btn;
    }

}
