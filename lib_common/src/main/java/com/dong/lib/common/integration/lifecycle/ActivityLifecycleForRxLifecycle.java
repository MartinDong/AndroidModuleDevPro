package com.dong.lib.common.integration.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dong.lib.common.base.BaseActivity;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.reactivex.subjects.Subject;

/**
 * ================================================
 * 配合 {@link ActivityLifecycleable} 使用,使 {@link Activity} 具有 {@link RxLifecycle} 的特性
 * <p>
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
@Singleton
public class ActivityLifecycleForRxLifecycle implements Application.ActivityLifecycleCallbacks {
    @Inject
    Lazy<FragmentLifecycleForRxLifecycle> mFragmentLifecycle;

    @Inject
    public ActivityLifecycleForRxLifecycle() {
    }

    /**
     * 通过桥梁对象 {@code BehaviorSubject<ActivityEvent> mLifecycleSubject}
     * 在每个 Activity 的生命周期中发出对应的生命周期事件
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.CREATE);
            if (activity instanceof FragmentActivity) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycle.get(), true);
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.START);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.RESUME);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.PAUSE);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.STOP);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(ActivityEvent.DESTROY);
        }
    }

    /**
     * 从 {@link BaseActivity} 中获得桥梁对象 {@code BehaviorSubject<ActivityEvent> mLifecycleSubject}
     *
     * @see <a href="https://mcxiaoke.gitbooks.io/rxdocs/content/Subject.html">BehaviorSubject 官方中文文档</a>
     */
    private Subject<ActivityEvent> obtainSubject(Activity activity) {
        return ((ActivityLifecycleable) activity).provideLifecycleSubject();
    }
}
