package com.dong.module.app

import android.content.Context
import android.support.multidex.MultiDex
import com.dong.lib.common.base.BaseApplication

/**
 * <p>这里仅需做一些初始化的工作</p>
 *
 * Created by Kotlin on 2018/2/25.
 */
class MyApplication : BaseApplication() {


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // dex突破65535的限制
        MultiDex.install(this)
    }

}