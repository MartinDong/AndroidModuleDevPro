package com.dong.module.app

import android.content.Context
import android.support.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.dong.lib.common.base.BaseApplication
import com.dong.lib.common.utils.Utils

/**
 * <p>这里仅需做一些初始化的工作</p>
 *
 * Created by Kotlin on 2018/2/25.
 */
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (Utils.isAppDebug()) {// 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openDebug()// 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openLog()// 打印日志
        }
        ARouter.init(this)// 尽可能早，推荐在Application中初始化
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // dex突破65535的限制
        MultiDex.install(this)
    }

}