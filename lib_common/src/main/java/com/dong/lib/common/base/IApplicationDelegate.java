package com.dong.lib.common.base;

import android.support.annotation.Keep;

/**
 * <p>对Application的代理</P>
 * Created by xiaoyulaoshi on 2018/3/14.
 */
@Keep
public interface IApplicationDelegate {

    void onCreate();

    void onTerminate();

    void onLowMemory();

    void onTrimMemory(int level);

}
