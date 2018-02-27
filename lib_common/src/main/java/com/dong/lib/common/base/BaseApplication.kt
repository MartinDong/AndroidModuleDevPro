package com.dong.lib.common.base

import android.app.Application
import com.dong.lib.common.utils.Utils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

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

        //标记app的根包名
        val ROOT_PACKAGE = "com.dong.common"

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

        //日志工具类
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("QYD_LOG").build()
        //设置日志适配器
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }
    // endregion
}