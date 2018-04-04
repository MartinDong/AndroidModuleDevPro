package com.dong.lib.common.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.dong.lib.common.base.delegate.AppDelegate;
import com.dong.lib.common.base.delegate.AppLifecycle;
import com.dong.lib.common.di.component.AppComponent;
import com.dong.lib.common.utils.Utils;
import com.dong.lib.common.utils.Preconditions;

/**
 * ================================================
 * 类说明
 * Created by xiaoyulaoshi on 2018/3/23.
 * <p>
 * ================================================
 */

public class BaseApplication extends Application implements App {
    //标记app的根包名
    private String ROOT_PACKAGE = "com.qyd.module";

    private AppLifecycle mAppDelegate;

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null)
            this.mAppDelegate = new AppDelegate(base);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null)
            this.mAppDelegate.onCreate(this);

        //初始化工具类
        Utils.init(this);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     * <p>
     * 此方法适用于仿真过程环境。
     * 它将永远不会在生产型Android设备上被调用，通过简单地杀死进程就可以清除进程;
     * 此时不执行用户代码（包括此回调）*。
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(this);
    }

    //内存过低,用来兼容api<14的机器
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mAppDelegate != null)
            mAppDelegate.onLowMemory(this);
    }

    //Android 4.0之后引入的，指导应用程序在不同的情况下进行自身的内存释放，以避免被系统直接杀掉，提高应用程序的用户体验.
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (mAppDelegate != null)
            mAppDelegate.onTrimMemory(level);
    }

    /**
     * 将 {@link AppComponent} 返回出去, 供其它地方使用, {@link AppComponent} 接口中声明的方法所返回的实例, 在 {@link #getAppComponent()} 拿到对象后都可以直接使用
     *
     * @return AppComponent
     * @see Utils#obtainAppComponentFromContext(Context) 可直接获取 {@link AppComponent}
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null", AppDelegate.class.getName());
        Preconditions.checkState(mAppDelegate instanceof App, "%s must be implements %s", AppDelegate.class.getName(), App.class.getName());
        return ((App) mAppDelegate).getAppComponent();
    }

}
