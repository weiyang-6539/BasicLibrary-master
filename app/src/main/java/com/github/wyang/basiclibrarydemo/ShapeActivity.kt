package com.github.wyang.basiclibrarydemo

import com.github.wyang.basiclibrarydemo.databinding.ActivityShapeViewBinding
import com.github.wyang.basiclibrarydemo.databinding.LayoutToolbarBinding

class ShapeActivity : BaseActivity<ActivityShapeViewBinding>() {
    private var toolbarBinding: LayoutToolbarBinding? = null

    override fun getViewBinding(): ActivityShapeViewBinding {
        val binding = ActivityShapeViewBinding.inflate(layoutInflater)
        toolbarBinding = binding.includeToolbar
        return binding
    }

    override fun initialize() {
        toolbarBinding?.let { initToolBar(it.toolbar, "自定义Shape", true) }
    }
}