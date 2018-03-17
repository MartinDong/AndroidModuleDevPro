package com.dong.lib.common.base;

import android.support.annotation.Keep;
import android.view.View;

/**
 * <p>进行Fragment代理</P>
 * Created by xiaoyulaoshi on 2018/3/14.
 */
@Keep
public interface IViewDelegate {
    BaseFragment getFragment(String name);

    View getView(String name);
}
