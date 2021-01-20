package com.github.wyang.basiclibrarydemo;

import com.github.android.common.utils.GlideImageLoader;
import com.github.android.common.widget.banner.BannerAdapter;
import com.github.android.common.widget.banner.BannerLayout;
import com.github.android.common.widget.banner.BannerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fxb on 2020/9/23.
 */
public class BannerActivity extends BaseActivity {
    @BindView(R.id.bl_01)
    BannerLayout bl_01;

    @BindView(R.id.bl_02)
    BannerLayout bl_02;

    @Override
    public int getLayoutId() {
        return R.layout.activity_banner;
    }

    @Override
    protected void initView() {
        List<String> list = new ArrayList<>();
        list.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2738234332,2025993528&fm=26&gp=0.jpg");
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2824970053,2177995476&fm=26&gp=0.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560162277510&di=f3baf93e28ada9d1fe5fe57329972788&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201310%2F16%2F224046ups8zp1jg31uz82g.jpg");
        //list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560162277510&di=b1974297cd0a721956b3784fdc00e4a8&imgtype=0&src=http%3A%2F%2Fi3.sinaimg.cn%2Fgm%2Fo%2Fi%2F2008-09-03%2FU1901P115T41D148033F757DT20080903105357.jpg");
        //list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560162277510&di=466abb19de3081f9782a22e63eb7a53c&imgtype=0&src=http%3A%2F%2Fs10.sinaimg.cn%2Fmw690%2F0061prGBgy6RGOKQuqRf9%26690");
        //list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560162277510&di=0324c2d44474b8087ab2f9c780f8ecd3&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F9bbc284bgw1f2tzaf85l2j20rs0hx78y.jpg");
        //list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560162277510&di=9d74a822ed852a90204701203bafada2&imgtype=0&src=http%3A%2F%2Fpic41.nipic.com%2F20140603%2F18347945_133954199143_2.jpg");

        adapter.setNewData(list);
        bl_01.setBannerAdapter(adapter);


        List<String> ls = new ArrayList<>();
        ls.add("1.虽然世事难料，但是只要付出真心，去努力了，那结果也没那么重要了;如果真的有一天，在自己老了的时候有个人问我，这辈子有没有遗憾的事，我的答案至少不是这一件事情");
        ls.add("2.虽然世事难料，但是只要付出真心，去努力了，那结果也没那么重要了;如果真的有一天，在自己老了的时候有个人问我，这辈子有没有遗憾的事，我的答案至少不是这一件事情");
        ls.add("3.虽然世事难料，但是只要付出真心，去努力了，那结果也没那么重要了;如果真的有一天，在自己老了的时候有个人问我，这辈子有没有遗憾的事，我的答案至少不是这一件事情");

        adapter2.setNewData(ls);
        bl_02.setBannerAdapter(adapter2);
    }

    private BannerAdapter<String> adapter = new BannerAdapter<String>(R.layout.item_banner_img) {
        @Override
        protected void convert(BannerViewHolder helper, String item) {
            GlideImageLoader.loadCenterCrop(mContext, helper.getView(R.id.iv_banner), item);
        }
    };


    private BannerAdapter<String> adapter2 = new BannerAdapter<String>(R.layout.item_banner_tv) {
        @Override
        protected void convert(BannerViewHolder helper, String item) {
            helper.setText(R.id.tv_content, item);
        }
    };
}
