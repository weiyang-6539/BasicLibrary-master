package com.github.wyang.basiclibrarydemo

import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import com.github.android.common.widget.shape.extend.ComplexView
import com.github.wyang.basiclibrarydemo.databinding.ActivityComplexViewBinding
import com.github.wyang.basiclibrarydemo.databinding.LayoutDemoButton1Binding
import com.github.wyang.basiclibrarydemo.databinding.LayoutDemoButton2Binding
import com.github.wyang.basiclibrarydemo.databinding.LayoutToolbarBinding

class ComplexViewActivity : BaseActivity<ActivityComplexViewBinding>() {
    private var toolbarBinding: LayoutToolbarBinding? = null
    private var layout1Binding: LayoutDemoButton1Binding? = null
    private var layout2Binding: LayoutDemoButton2Binding? = null

    override fun getViewBinding(): ActivityComplexViewBinding {
        val binding = ActivityComplexViewBinding.inflate(layoutInflater)
        toolbarBinding = binding.includeToolbar
        layout1Binding = binding.includeLayout1
        layout2Binding = binding.includeLayout2
        return binding
    }

    override fun initialize() {
        toolbarBinding?.let { initToolBar(it.toolbar, "ComplexView", true) };

        layout1Binding?.scEnable?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            val parent = layout1Binding?.scEnable?.parent as ViewGroup
            applyEnable(parent, isChecked)
        }

        layout2Binding?.let { setSelectListener(it.llGroup) }


        layout2Binding?.cvPrompt?.setOnClickListener {
            layout2Binding?.cvPrompt?.isSelected = !layout2Binding?.cvPrompt?.isSelected!!
            layout2Binding?.cvPrompt?.text = if (layout2Binding?.cvPrompt?.isSelected!!) "已启用" else "启用提醒"
        }
    }

    private fun applyEnable(view: View, enable: Boolean) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                applyEnable(view.getChildAt(i), enable)
            }
        } else {
            if (view !is SwitchCompat) view.isEnabled = enable
        }
    }

    private fun setSelectListener(view: View) {
        if (view is LinearLayout) {
            for (i in 0 until view.childCount) {
                setSelectListener(view.getChildAt(i))
            }
        } else (view as? ComplexView)?.setOnClickListener { v: View ->
            layout2Binding?.let { applySelected(it.llGroup, false) }
            v.isSelected = true
        }
    }

    private fun applySelected(view: View, selected: Boolean) {
        if (view is LinearLayout) {
            for (i in 0 until view.childCount) {
                applySelected(view.getChildAt(i), selected)
            }
        } else (view as? ComplexView)?.isSelected = selected
    }

}