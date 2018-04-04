package com.dong.lib.common.base;

import android.support.annotation.NonNull;

import com.dong.lib.common.di.component.AppComponent;

/**
 * ================================================
 * 框架要求框架中的每个 {@link android.app.Application} 都需要实现此类,以满足规范
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
public interface App {
    @NonNull
    AppComponent getAppComponent();
}
