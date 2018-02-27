package com.dong.lib.common.base

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v7.app.AppCompatActivity

/**
 * <p> Activity基类 </p>
 * Created by Kotlin on 2018/2/26.
 */
@Keep
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //将当前Activity进行自己管理
        ViewManager.getInstance().addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //将当前的Activity移除堆栈
        ViewManager.getInstance().finishActivity(this)
    }

}