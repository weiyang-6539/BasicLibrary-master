package com.github.android.popup_demo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.android.common.utils.ScreenUtils;
import com.github.android.popup_demo.fragment._01Fragment;
import com.github.android.popup_demo.fragment._02Fragment;
import com.github.android.popup_demo.fragment._03Fragment;
import com.github.android.popup_demo.fragment._04Fragment;
import com.github.android.popup_demo.fragment._05Fragment;
import com.github.android.popup_demo.fragment._06Fragment;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private String[] titles = new String[]{"常见使用", "APP实际应用", "常见使用", "局部阴影", "常见使用", "局部阴影"};
    private Fragment[] fragments = new Fragment[]{
            new _01Fragment(),
            new _02Fragment(),
            new _03Fragment(),
            new _04Fragment(),
            new _05Fragment(),
            new _06Fragment()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.mTabLayout);
        mViewPager = findViewById(R.id.mViewPager);

        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private class MainAdapter extends FragmentPagerAdapter {
        MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments[i];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
