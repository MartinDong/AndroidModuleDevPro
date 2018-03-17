package com.dong.lib.common.base

import android.app.Application
import com.dong.lib.common.utils.MyAndroidLogAdapter
import com.dong.lib.common.utils.Utils
import com.orhanobut.logger.Logger

/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 *
 * Created by Kotlin on 2018/2/25.
 */

open class BaseApplication : Application() {

    companion object {
        private var sInstance: BaseApplication? = null

        private var mAppDelegateList: List<IApplicationDelegate>? = null

        //标记app的根包名
        val ROOT_PACKAGE = "com.qyd.module"

        /**
         * 返回应用的上下文
         */
        fun getIns(): BaseApplication? {
            return sInstance
        }
    }

    // region Application初始化的同时，去初始化一些必要的信息，如果耗时的需要丢到子线程或者别的生命周期中
    override fun onCreate() {
        super.onCreate()
        sInstance = this
        Utils.init(this)

        //获取单一路径下所有实现了接口的类对象
        mAppDelegateList = ClassUtils.getObjectsWithInterface(this,
                IApplicationDelegate::class.java, ROOT_PACKAGE)
        for (delegate in mAppDelegateList!!) {
            //调用代理实例中的 onCreate 方法
            delegate.onCreate()
        }

        //日志工具类，配置自定义的日志记录策略
        Logger.addLogAdapter(MyAndroidLogAdapter())
    }
    // endregion

    //终止
    /*
    此方法适用于仿真过程环境。
    它将永远不会在生产型Android设备上被调用，通过简单地杀死进程就可以清除进程;
    此时不执行用户代码（包括此回调）*。
     */
    override fun onTerminate() {
        super.onTerminate()
        for (delegate in mAppDelegateList!!) {
            delegate.onTerminate()
        }
    }

    //内存过低,用来兼容api<14的机器
    override fun onLowMemory() {
        super.onLowMemory()
        for (delegate in mAppDelegateList!!) {
            delegate.onLowMemory()
        }
    }

    //Android 4.0之后引入的，指导应用程序在不同的情况下进行自身的内存释放，以避免被系统直接杀掉，提高应用程序的用户体验.
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        for (delegate in mAppDelegateList!!) {
            delegate.onTrimMemory(level)
        }
    }
}