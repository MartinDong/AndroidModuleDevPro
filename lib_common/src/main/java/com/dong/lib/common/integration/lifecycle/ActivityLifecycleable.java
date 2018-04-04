package com.dong.lib.common.integration.lifecycle;

import android.app.Activity;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * ================================================
 * 让 {@link Activity} 实现此接口,即可正常使用 {@link RxLifecycle}
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */

public interface ActivityLifecycleable  extends Lifecycleable<ActivityEvent> {
}