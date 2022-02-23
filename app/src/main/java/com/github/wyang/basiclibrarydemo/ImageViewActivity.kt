package com.github.wyang.basiclibrarydemo

import com.github.wyang.basiclibrarydemo.databinding.ActivityImageViewBinding

class ImageViewActivity : BaseActivity<ActivityImageViewBinding>() {
    override fun getViewBinding(): ActivityImageViewBinding {
        return ActivityImageViewBinding.inflate(layoutInflater)
    }

    override fun initialize() {
    }
}