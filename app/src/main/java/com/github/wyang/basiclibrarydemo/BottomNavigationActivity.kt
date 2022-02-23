package com.github.wyang.basiclibrarydemo

import com.github.android.common.widget.bottomnavigation.BottomNavigationBar
import com.github.android.common.widget.bottomnavigation.BottomNavigationItem
import com.github.android.common.widget.bottomnavigation.ShapeBadgeItem
import com.github.android.common.widget.bottomnavigation.TextBadgeItem
import com.github.wyang.basiclibrarydemo.databinding.ActivityBottomNavigationBinding

class BottomNavigationActivity : BaseActivity<ActivityBottomNavigationBinding>() {
    private var shopBadge: TextBadgeItem? = null
    private var discoverBadge: ShapeBadgeItem? = null

    override fun getViewBinding(): ActivityBottomNavigationBinding {
        return ActivityBottomNavigationBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        shopBadge = TextBadgeItem()
                .setBackgroundColor(-0x10000)
                .setBorderColor(-0x1)
                .setBorderWidth(2)
                .setTextColor(-0x1)
                .setText("999")

        discoverBadge = ShapeBadgeItem()
                .setShape(ShapeBadgeItem.SHAPE_OVAL)
                .setShapeColor(-0x10000)
                .setSizeInPixels(5, 5)

        mBinding?.bottomBar
                ?.addItem(BottomNavigationItem(R.drawable.ic_home_selected, "首页")
                        .setInactiveIconResource(R.drawable.ic_home_normal)
                        .setActiveColorResource(R.color.colorRed))
                ?.addItem(BottomNavigationItem(R.drawable.ic_search_selected, "搜索")
                        .setInactiveIconResource(R.drawable.ic_search_normal)
                        .setActiveColorResource(R.color.colorRed))
                ?.addItem(BottomNavigationItem(R.drawable.ic_discover_selected, "发现")
                        .setInactiveIconResource(R.drawable.ic_discover_normal)
                        .setActiveColorResource(R.color.colorRed)
                        .setBadgeItem(discoverBadge))
                ?.addItem(BottomNavigationItem(R.drawable.ic_shop_selected, "购物")
                        .setInactiveIconResource(R.drawable.ic_shop_normal)
                        .setActiveColorResource(R.color.colorRed)
                        .setBadgeItem(shopBadge))
                ?.addItem(BottomNavigationItem(R.drawable.ic_unlogin_selected, "未登录")
                        .setInactiveIconResource(R.drawable.ic_unlogin_normal)
                        .setActiveColorResource(R.color.colorRed))
                ?.setFirstSelectedPosition(0)
                ?.initialise()

        mBinding?.bottomBar?.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {}
            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
    }
}