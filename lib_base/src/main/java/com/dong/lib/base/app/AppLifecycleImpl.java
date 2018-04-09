package com.dong.lib.base.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dong.lib.common.BuildConfig;
import com.dong.lib.common.base.delegate.AppLifecycle;
import com.dong.lib.common.http.log.Timber;
import com.dong.lib.common.utils.Utils;
import com.dong.lib.common.utils.MyAndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * ================================================
 * {@link AppLifecycle} 实现类，用来处理监听App的运行的声明周期
 * Created by xiaoyulaoshi on 2018/3/22.
 * <p>
 * ================================================
 */

public class AppLifecycleImpl implements AppLifecycle {

    @Override
    public void attachBaseContext(Context base) {
        //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
        MultiDex.install(base);
    }

    @Override
    public void onCreate(Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        if (BuildConfig.LOG_DEBUG) {//Timber初始化
            // Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            // 并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            // 比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            Timber.plant(new Timber.DebugTree());
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
            // 日志工具类，配置自定义的日志记录策略
            Logger.addLogAdapter(new MyAndroidLogAdapter());
//            Timber.plant(new Timber.DebugTree() {
//                @Override
//                protected void log(int priority, String tag, String message, Throwable t) {
////                    Logger.log(priority, tag, message, t);
//                }
//            });

            // ===================ARouter=============================
            ARouter.openDebug();// 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openLog();// 打印日志
            ARouter.printStackTrace();// 打印日志的时候打印线程堆栈
            // ===================ARouter=============================
        }

        // 尽可能早，推荐在Application中初始化
        ARouter.init(application);

        // leakCanary内存泄露检查
        Utils.obtainAppComponentFromContext(application)
                .extras().put(RefWatcher.class.getName(),
                BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);

        //扩展 AppManager 的远程遥控功能
        Utils.obtainAppComponentFromContext(application).appManager().setHandleListener((appManager, message) -> {
            switch (message.what) {
                //case 0:
                //do something ...
                //   break;
            }
        });
    }

    @Override
    public void onTerminate(Application application) {

    }

    @Override
    public void onLowMemory(Application application) {

    }

    @Override
    public void onTrimMemory(int level) {

    }
}
