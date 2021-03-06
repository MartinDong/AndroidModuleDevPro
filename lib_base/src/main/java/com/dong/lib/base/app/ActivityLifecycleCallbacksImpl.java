package com.dong.lib.base.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.dong.lib.common.http.log.Timber;

/**
 * ================================================
 * {@link Application.ActivityLifecycleCallbacks} 实现Activity的生命周期监听回调
 * Created by xiaoyulaoshi on 2018/3/23.
 * <p>
 * ================================================
 */

public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Timber.w(activity + " - onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Timber.w(activity + " - onActivityStarted");
//        if (!activity.getIntent().getBooleanExtra("isInitToolbar", false)) {
//            // 由于加强框架的兼容性,故将 setContentView 放到 onActivityCreated 之后, onActivityStarted 之前执行
//            // 而 findViewById 必须在 Activity setContentView() 后才有效,
//            // 所以将以下代码从之前的 onActivityCreated 中移动到 onActivityStarted 中执行
//            activity.getIntent().putExtra("isInitToolbar", true);
//            // 这里全局给 Activity 设置 toolbar 和 title ,你想象力有多丰富,这里就有多强大,以前放到 BaseActivity 的操作都可以放到这里
//            if (activity.findViewById(R.id.toolbar) != null) {
//                if (activity instanceof AppCompatActivity) {
//                    ((AppCompatActivity) activity).setSupportActionBar((Toolbar) activity.findViewById(R.id.toolbar));
//                    ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
//                } else {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        activity.setActionBar(activity.findViewById(R.id.toolbar));
//                        activity.getActionBar().setDisplayShowTitleEnabled(false);
//                    }
//                }
//            }
//            if (activity.findViewById(R.id.toolbar_title) != null) {
//                ((TextView) activity.findViewById(R.id.toolbar_title)).setText(activity.getTitle());
//            }
//            if (activity.findViewById(R.id.toolbar_back) != null) {
//                activity.findViewById(R.id.toolbar_back).setOnClickListener(v -> {
//                    activity.onBackPressed();
//                });
//            }
//        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Timber.w(activity + " - onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Timber.w(activity + " - onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Timber.w(activity + " - onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Timber.w(activity + " - onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Timber.w(activity + " - onActivityDestroyed");
        //横竖屏切换或配置改变时, Activity 会被重新创建实例, 但 Bundle 中的基础数据会被保存下来,移除该数据是为了保证重新创建的实例可以正常工作
        activity.getIntent().removeExtra("isInitToolbar");
    }
}
