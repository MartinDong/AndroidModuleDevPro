package com.dong.lib.common.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dong.lib.common.utils.Utils;

import org.simple.eventbus.EventBus;

/**
 * ================================================
 * {@link ActivityDelegate} 默认实现类
 * <p>
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */

public class ActivityDelegateImpl implements ActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity.useEventBus()) {
            //注册到事件主线
            EventBus.getDefault().register(mActivity);
        }

        //这里提供 AppComponent 对象给 BaseActivity 的子类, 用于 Dagger2 的依赖注入
        iActivity.setupActivityComponent(Utils.obtainAppComponentFromContext(mActivity));
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity != null && iActivity.useEventBus())
            EventBus.getDefault().unregister(mActivity);
        this.iActivity = null;
        this.mActivity = null;
    }
}
