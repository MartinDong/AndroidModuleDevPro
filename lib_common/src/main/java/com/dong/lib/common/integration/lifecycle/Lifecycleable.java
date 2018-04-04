package com.dong.lib.common.integration.lifecycle;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.dong.lib.common.utils.RxLifecycleUtils;
import com.trello.rxlifecycle2.RxLifecycle;

import io.reactivex.subjects.Subject;

/**
 * ================================================
 * 让 {@link Activity}/{@link Fragment} 实现此接口,即可正常使用 {@link RxLifecycle}
 * 无需再继承 {@link RxLifecycle} 提供的 Activity/Fragment ,扩展性极强
 *
 * @see RxLifecycleUtils 详细用法请查看此类
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */

public interface Lifecycleable<E> {
    @NonNull
    Subject<E> provideLifecycleSubject();
}
