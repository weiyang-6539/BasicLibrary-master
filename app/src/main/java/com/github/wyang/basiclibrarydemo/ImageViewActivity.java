package com.github.wyang.basiclibrarydemo;

import com.github.android.common.widget.image.AvatarImageView;

import butterknife.BindView;

/**
 * Created by fxb on 2020/9/6.
 */
public class ImageViewActivity extends BaseActivity {
    @BindView(R.id.iv_avatar)
    AvatarImageView iv_avatar;


    @Override
    public int getLayoutId() {
        return R.layout.activity_image_view;
    }

    @Override
    protected void initView() {
        //iv_avatar.setImageResource(R.drawable.img_avatar);
    }
}
