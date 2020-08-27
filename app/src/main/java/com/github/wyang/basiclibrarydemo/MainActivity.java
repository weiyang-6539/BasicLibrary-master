package com.github.wyang.basiclibrarydemo;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.android.common.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private List<Item> mData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initToolBar(mToolbar, "Demo列表", false);

        //init RecyclerView
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new ItemDecoration(2, ViewUtil.dp2px(this, 10), true));

        //init 适配器
        mAdapter = new Adapter();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Item item = mData.get(position);
            if (item.activity != null)
                startActivity(new Intent(this, item.activity));
        });

        //bind RecyclerView和适配器
        mAdapter.bindToRecyclerView(mRecyclerView);

        //设置data
        mData = new ArrayList<>();
        mData.add(new Item("ComplexView", "结合google md材质设计，自定义Button", ComplexViewActivity.class));
        mData.add(new Item("底部导航", "结合google md材质设计，自定义Button", BottomNavigationActivity.class));
        mData.add(new Item("Ripple波纹动画", "xml及代码写法", RippleActivity.class));
        mData.add(new Item("自定义View", "", TestActivity.class));
        mData.add(new Item("测试用例4", "", null));
        mData.add(new Item("测试用例4", "", null));
        mData.add(new Item("测试用例4", "", null));
        mData.add(new Item("测试用例4", "", null));
        mData.add(new Item("测试用例4", "", null));
        mData.add(new Item("测试用例4", "", null));
        mData.add(new Item("测试用例4", "", null));
        mAdapter.setNewData(mData);
    }
}

class Adapter extends BaseQuickAdapter<Item, BaseViewHolder> {

    Adapter() {
        super(R.layout.item_main_recycler);
    }

    @Override
    protected void convert(BaseViewHolder helper, Item item) {
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_description, item.description);
    }
}

class Item {
    String title;
    String description;
    Class activity;

    Item(String title, String description, Class activity) {
        this.title = title;
        this.description = description;
        this.activity = activity;
    }
}

class ItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    ItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;
            if (position < spanCount) {
                outRect.top = spacing;
            }
            outRect.bottom = spacing;
        } else {
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            if (position >= spanCount) {
                outRect.top = spacing;
            }
        }
    }
}
