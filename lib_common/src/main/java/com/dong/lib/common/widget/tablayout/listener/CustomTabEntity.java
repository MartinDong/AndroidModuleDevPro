package com.dong.lib.common.widget.tablayout.listener;

import android.support.annotation.DrawableRes;

/**
 * <p>类说明</P>
 * Created by xiaoyulaoshi on 2018/3/19.
 */
public interface CustomTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}