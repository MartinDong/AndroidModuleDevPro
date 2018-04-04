package com.dong.lib.common.mvp;

import android.app.Activity;

/**
 * <p>框架要求框架中的每个 Presenter 都需要实现此类,以满足规范</P>
 * Created by xiaoyulaoshi on 2018/3/20.
 */

public interface IPresenter {
    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 在框架中 {@link Activity#onDestroy()} 会默认调用 {@link IPresenter#onDestroy()}
     */
    void onDestroy();
}
