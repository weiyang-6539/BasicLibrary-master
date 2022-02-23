package com.github.android.popup_demo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.android.common.popup.XPopup;
import com.github.android.popup_demo.popup.CommentPopupView;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fxb on 2020/5/26.
 */
public class DouyinActivity extends AppCompatActivity {
    @BindView(R.id.mVideoView)
    VideoView mVideoView;
    @BindView(R.id.iv_player)
    ImageView iv_player;
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    String path = "android.resource://com.github.android.popupdemo/" + R.raw.dy_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyin);
        ButterKnife.bind(this);

        mTabLayout.addTab(mTabLayout.newTab().setText("首页"));
        mTabLayout.addTab(mTabLayout.newTab().setText("长沙"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.button_douyin_add));
        mTabLayout.addTab(mTabLayout.newTab().setText("消息"));
        mTabLayout.addTab(mTabLayout.newTab().setText("我"));

        TabLayout.Tab tab = mTabLayout.getTabAt(2);
        if (tab != null && tab.getCustomView() != null) {
            tab.getCustomView().setOnClickListener(v -> {
                Toast.makeText(this, "点击Add！！", Toast.LENGTH_SHORT).show();
            });
        }

        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.setVideoPath(path);
        mVideoView.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        });
        mVideoView.start();

        onClickContainer();
    }

    @OnClick(R.id.fl_container)
    public void onClickContainer() {
        if (mVideoView.isPlaying()) {
            iv_player.setVisibility(View.VISIBLE);

            mVideoView.pause();
        } else {
            iv_player.setVisibility(View.INVISIBLE);

            mVideoView.start();
        }
    }

    @OnClick(R.id.cv_comment)
    public void onClickCvComment() {
        new XPopup.Builder(this)
                .hasShadowBg(true)
                .dismissOnBackPressed(true)
                .autoOpenSoftInput(true)
                .moveUpToKeyboard(false)
                .asCustom(new CommentPopupView(this))
                .show();
    }
}
