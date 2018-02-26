package com.dong.lib.common.utils

import android.app.Activity
import java.util.*

/**
 * <p>自定义视图管理器，Activity、Fragment</p>
 * Created by Kotlin on 2018/2/26.
 */
class ViewManager {

    //Activity栈
    private var activityStack: Stack<Activity>? = null

    private object ViewManagerHolder {
        internal val sInstance = ViewManager()
    }

    private fun ViewManager() {}

    fun getInstance(): ViewManager {
        return ViewManagerHolder.sInstance
    }

    /**
     * 將指定的Activity添加到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }
}