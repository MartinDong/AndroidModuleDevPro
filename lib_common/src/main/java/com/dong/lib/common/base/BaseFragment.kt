package com.dong.lib.common.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.Keep
import android.support.v4.app.Fragment
import com.dong.lib.common.utils.Utils

/**
 * <p>Fragment基类</p>
 * Created by Kotlin on 2018/2/27.
 */
@Keep
abstract class BaseFragment : Fragment() {

    protected lateinit var _mActivity: BaseActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this._mActivity = context as BaseActivity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewsData(savedInstanceState)
        initViewsEvent(savedInstanceState)
    }

    /**
     * 初始化视图数据
     */
    abstract fun initViewsData(savedInstanceState: Bundle?)

    /**
     * 初始化视图事件
     */
    abstract fun initViewsEvent(savedInstanceState: Bundle?)

    /**
     * 获取宿主Activity
     *
     * @return BaseActivity
     */
    protected fun getHoldingActivity(): BaseActivity {
        return _mActivity
    }

    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected fun addFragment(fragment: BaseFragment, @IdRes frameId: Int) {
        Utils.checkNotNull(fragment)
        getHoldingActivity().addFragment(fragment, frameId)
    }

    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected fun replaceFragment(fragment: BaseFragment, @IdRes frameId: Int) {
        Utils.checkNotNull(fragment)
        getHoldingActivity().replaceFragment(fragment, frameId)
    }


    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    protected fun hideFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        getHoldingActivity().hideFragment(fragment)
    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected fun showFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        getHoldingActivity().showFragment(fragment)
    }


    /**
     * 移除Fragment
     *
     * @param fragment
     */
    protected fun removeFragment(fragment: BaseFragment) {
        Utils.checkNotNull(fragment)
        getHoldingActivity().removeFragment(fragment)

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected fun popFragment() {
        getHoldingActivity().popFragment()
    }
}