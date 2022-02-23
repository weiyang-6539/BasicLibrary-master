package com.github.android.popup_demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.android.popup_demo.fragment._01Fragment;
import com.github.android.popup_demo.fragment._02Fragment;
import com.github.android.popup_demo.fragment._03Fragment;
import com.github.android.popup_demo.fragment._04Fragment;
import com.github.android.popup_demo.fragment._05Fragment;
import com.github.android.popup_demo.fragment._06Fragment;
import com.google.android.material.tabs.TabLayout;

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
