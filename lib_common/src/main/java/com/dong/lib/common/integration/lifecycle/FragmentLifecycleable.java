package com.dong.lib.common.integration.lifecycle;

import android.support.v4.app.Fragment;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * ================================================
 * 让 {@link Fragment} 实现此接口,即可正常使用 {@link RxLifecycle}
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */

public interface FragmentLifecycleable extends Lifecycleable<FragmentEvent> {
}
