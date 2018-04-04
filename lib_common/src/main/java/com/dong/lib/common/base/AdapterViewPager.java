package com.dong.lib.common.base;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 基类 {@link FragmentStatePagerAdapter}
 * Created by xiaoyulaoshi on 2018/3/14.
 */

public class AdapterViewPager extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    private CharSequence[] mTitles;


    public AdapterViewPager(FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        this.mFragments = fragments;
    }

    public AdapterViewPager(FragmentManager fragmentManager, List<Fragment> fragments, CharSequence[] titles) {
        super(fragmentManager);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null) {
            return mTitles[position];
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment f = (Fragment) super.instantiateItem(container, position);
        View view = f.getView();
        if (view != null) {
            container.addView(view);
        }
        return f;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mFragments.get(position).getView();
        if (view != null) {
            container.removeView(view);
        }
    }
}
