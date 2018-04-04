package com.dong.lib.common.base.delegate;

import android.app.Application;
import android.content.Context;

/**
 * ================================================
 * 用于代理 {@link Application} 的生命周期
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */

public interface AppLifecycle {

    /**
     * 这里会在 {@link Application#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base 新的基础上下文
     */
    void attachBaseContext(Context base);

    void onCreate(Application application);

    /**
     * 在模拟环境中程序终止时会被调用
     *
     * @param application 应用程序
     */
    void onTerminate(Application application);

    /**
     * 内存过低,用来兼容api<14的机器
     *
     * @param application 应用程序
     */
    void onLowMemory(Application application);

    /**
     * 只能用在 api 14及以上版本
     * Android 4.0之后引入的，指导应用程序在不同的情况下进行自身的内存释放，
     * 以避免被系统直接杀掉，提高应用程序的用户体验.
     * <p>
     * 当app在前台运行时,该函数的level(从低到高)有:
     * <p>
     * {@link Application#TRIM_MEMORY_RUNNING_MODERATE}  os开始运行在低内存状态下,app正在运行,不会被杀掉
     * {@link Application#TRIM_MEMORY_RUNNING_LOW}       os运行在更加低内存状态下,app在运行,不会被杀掉 app可以清理一些资源来保证系统的流畅.
     * {@link Application#TRIM_MEMORY_RUNNING_CRITICAL}  os运行在相当低内存状态下,app在运行,且os不认为可以杀掉此app. os要开始杀掉后台进程. 此时,app应该去释放一些不重要的资源.
     * <p>
     * <p>
     * 当app在后台运行时,level状态有:
     * <p>
     * {@link Application#TRIM_MEMORY_UI_HIDDEN}   app的 UI不可见,app可以清理UI使用的较大的资源
     * <p>
     * 当app进入后台LRU list时:
     * {@link Application#TRIM_MEMORY_BACKGROUND}  os运行在低内存下,app进程在LRU list开始处附近,尽管app没有被杀掉的风险, 但是系统也许已经正在杀后台进程.app应该清理一些容易恢复的资源
     * {@link Application#TRIM_MEMORY_MODERATE}    os运行在低内存下,app进程在LRU list中间处附件, app此时有被杀的可能
     * {@link Application#TRIM_MEMORY_COMPLETE}    os运行在低内存下, app是首先被杀的选择之一, app应该及时清理掉恢复app到前台状态,不重要的所有资源.另外,一个app占用内存越多,则系统清理后台LRU list时,越可能优先被清理.所以,内存使用我们要谨慎使用.
     *
     * @param level 级别
     */
    void onTrimMemory(int level);
}
