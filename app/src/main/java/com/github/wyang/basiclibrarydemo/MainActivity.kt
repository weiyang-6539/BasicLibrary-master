package com.github.wyang.basiclibrarydemo

import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.android.common.utils.ViewUtil
import com.github.wyang.basiclibrarydemo.databinding.ActivityMainBinding
import com.github.wyang.basiclibrarydemo.databinding.LayoutToolbarBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var toolbarBinding: LayoutToolbarBinding? = null
    private val mAdapter: Adapter = Adapter()
    private var mData: MutableList<Item> = ArrayList()


    override fun getViewBinding(): ActivityMainBinding {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        toolbarBinding = binding.includeToolbar
        return binding
    }

    override fun initialize() {
        toolbarBinding?.let { initToolBar(it.toolbar, "控件Demo列表", false) }

        //init RecyclerView
        mBinding?.mRecyclerView?.layoutManager = GridLayoutManager(this, 2)
        mBinding?.mRecyclerView?.addItemDecoration(ItemDecoration(2, ViewUtil.dp2px(this, 10f), true))
        mBinding?.mRecyclerView?.adapter = mAdapter

        //init 适配器
        mAdapter.setOnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, position: Int ->
            val item: Item = mData[position]
            if (item.activity != null) startActivity(Intent(this, item.activity))
        }

        //设置data
        mData.add(Item("自定义Shape", "约束布局，帧布局，线性布局，相对布局的应用", ShapeActivity::class.java))
        mData.add(Item("ComplexView", "结合google md材质设计，自定义Button", ComplexViewActivity::class.java))
        mData.add(Item("ImageView拓展", "各种ImageView应用", ImageViewActivity::class.java))
        mData.add(Item("底部导航", "结合google md材质设计，自定义Button", BottomNavigationActivity::class.java))
        mData.add(Item("轮播图-RecyclerView实现", "", BannerActivity::class.java))
        mData.add(Item("测试用例4", "", null))
        mData.add(Item("测试用例4", "", null))
        mData.add(Item("测试用例4", "", null))
        mData.add(Item("测试用例4", "", null))
        mData.add(Item("测试用例4", "", null))
        mData.add(Item("test用例", "测试", TestActivity::class.java))
        mAdapter.setNewInstance(mData)
    }
}

internal class Adapter : BaseQuickAdapter<Item, BaseViewHolder>(R.layout.item_main_recycler) {
    override fun convert(holder: BaseViewHolder, item: Item) {
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_description, item.description)
    }
}

internal class Item(var title: String, var description: String, var activity: Class<*>?)

internal class ItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}
