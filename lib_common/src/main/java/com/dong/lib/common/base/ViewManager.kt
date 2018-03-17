package com.dong.lib.common.base

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import com.orhanobut.logger.Logger
import java.util.*

/**
 * <p>自定义视图管理器，Activity、Fragment</p>
 * Created by Kotlin on 2018/2/26.
 */
class ViewManager {

    //Activity栈
    private var activityStack: Stack<Activity>? = null

    private var fragmentList: MutableList<BaseFragment>? = null

    private fun ViewManager() {}

    companion object {
        private object ViewManagerHolder {
            internal val sInstance = ViewManager()
        }

        fun getInstance(): ViewManager {
            return ViewManagerHolder.sInstance
        }
    }

    fun addFragment(index: Int, fragment: BaseFragment) {
        if (fragmentList == null) {
            fragmentList = ArrayList()
        }
        fragmentList!!.add(index, fragment)
    }


    fun getFragment(index: Int): BaseFragment? {
        return if (fragmentList != null) {
            fragmentList!![index]
        } else null
    }


    fun getAllFragment(): MutableList<BaseFragment>? {
        return if (fragmentList != null) {
            fragmentList
        } else null
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

    /**
     * 获取当前Activity
     */
    fun currentActivity(): Activity? {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity
     */
    fun finishCurrentActivity(activity: Activity) {
        var activity: Activity = activityStack!!.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束所有的Activity
     */
    fun finishAllActivity() {
        activityStack!!.forEach {
            it?.finish()
        }
        activityStack!!.clear()
    }

    /**
     * 退出APP
     */
    fun exitApp(context: Context) {
        try {
            finishAllActivity()
            //杀死后台进程需要在AndroidManifest中声明android.permission.KILL_BACKGROUND_PROCESSES；
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.killBackgroundProcesses(context.packageName)
        } catch (e: Exception) {
            Logger.e("ViewManager app exit " + e.message)
        }
    }
}