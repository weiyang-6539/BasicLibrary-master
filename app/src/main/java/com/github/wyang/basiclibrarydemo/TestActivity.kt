package com.github.wyang.basiclibrarydemo

import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import com.github.wyang.basiclibrarydemo.databinding.ActivityTestBinding

class TestActivity : BaseActivity<ActivityTestBinding>() {
    override fun getViewBinding(): ActivityTestBinding {
        return ActivityTestBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        mBinding?.rg?.addView(createBtn("按钮1", isLeft = true, isRight = false))
        mBinding?.rg?.addView(createBtn("按钮2", isLeft = false, isRight = false))
        mBinding?.rg?.addView(createBtn("按钮3", isLeft = false, isRight = false))
        mBinding?.rg?.addView(createBtn("按钮4", isLeft = false, isRight = false))
        mBinding?.rg?.addView(createBtn("按钮5", isLeft = false, isRight = false))
        mBinding?.rg?.addView(createBtn("按钮6", isLeft = false, isRight = true))
    }

    private fun createBtn(text: String, isLeft: Boolean, isRight: Boolean): RadioButton? {
        val btn = RadioButton(this)
        btn.text = text
        btn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_segment_text))
        btn.buttonDrawable = null
        if (!isLeft && !isRight)
            btn.setBackgroundResource(R.drawable.selector_segment_middle_bg)
        else if (isLeft)
            btn.setBackgroundResource(R.drawable.selector_segment_left_bg)
        else
            btn.setBackgroundResource(R.drawable.selector_segment_right_bg)
        btn.gravity = Gravity.CENTER
        btn.layoutParams = RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.MATCH_PARENT, 1F)
        return btn
    }
}