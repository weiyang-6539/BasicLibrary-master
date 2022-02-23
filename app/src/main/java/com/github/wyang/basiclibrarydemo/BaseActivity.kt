package com.github.wyang.basiclibrarydemo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected val TAG: String = javaClass.simpleName
    protected var mBinding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = getViewBinding()
        setContentView(mBinding!!.root)

        initialize()
    }

    protected abstract fun getViewBinding(): T

    protected abstract fun initialize()

    open fun initToolBar(toolbar: Toolbar, name: String?, showHomeAsUp: Boolean) {
        toolbar.title = name
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}