package com.dong.lib.common.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.Keep
import android.support.v7.app.AppCompatActivity
import com.dong.lib.common.utils.Utils

/**
 * <p> Activity基类 </p>
 * Created by Kotlin on 2018/2/26.
 */
@Keep
open class BaseActivity : AppCompatActivity() {

    var mActivity: BaseActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        //将当前Activity进行自己管理
        ViewManager.getInstance().addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //将当前的Activity移除堆栈
        ViewManager.getInstance().finishActivity(this)
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    fun addFragment(fragment: BaseFragment, @IdRes frameId: Int) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .add(frameId, fragment, fragment::class.java.simpleName)
                .addToBackStack(fragment::class.java.simpleName)
                .commitAllowingStateLoss()

    }


    /**
     * 替换fragment
     * @param fragment
     * @param frameId
     */
    fun replaceFragment(fragment: BaseFragment, @IdRes frameId: Int) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .replace(frameId, fragment, fragment::class.java.simpleName)
                .addToBackStack(fragment::class.java.simpleName)
                .commitAllowingStateLoss()

    }

    /**
     * 隐藏fragment
     * @param fragment
     */
    fun hideFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss()

    }

    /**
     * 显示fragment
     * @param fragment
     */
    fun showFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss()

    }

    /**
     * 移除fragment
     * @param fragment
     */
    fun removeFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()

    }

    /**
     * 弹出栈顶部的Fragment
     */
    fun popFragment() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }
}