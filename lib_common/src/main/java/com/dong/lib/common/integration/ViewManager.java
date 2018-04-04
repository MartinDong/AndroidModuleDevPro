package com.dong.lib.common.integration;

import android.support.annotation.Keep;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 管理组件化中的Fragment的相引用
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
@Keep
public class ViewManager {
    private static List<Fragment> fragmentList;

    public static ViewManager getInstance() {
        return ViewManagerHolder.sInstance;
    }


    private static class ViewManagerHolder {
        private static final ViewManager sInstance = new ViewManager();
    }

    private ViewManager() {
    }

    public void addFragment(int index, Fragment fragment) {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        fragmentList.add(index, fragment);
    }

    public Fragment getFragment(int index) {
        if (fragmentList != null) {
            return fragmentList.get(index);
        }
        return null;
    }

    public List<Fragment> getAllFragment() {
        if (fragmentList != null) {
            return fragmentList;
        }
        return null;
    }
}
