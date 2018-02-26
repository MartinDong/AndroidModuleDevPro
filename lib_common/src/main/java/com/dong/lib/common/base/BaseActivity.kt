package com.dong.lib.common.base

import android.support.annotation.Keep
import android.support.v7.app.AppCompatActivity

/**
 * <p> Activity基类 </p>
 * Created by Kotlin on 2018/2/26.
 */
@Keep
open class BaseActivity : AppCompatActivity() {

   init {
       BaseApplication.getIns()
    }

}