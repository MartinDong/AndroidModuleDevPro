package com.dong.lib.common.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.Gravity
import android.view.Window
import android.view.WindowManager.LayoutParams
import com.dong.lib.common.R

/**
 * 弹框使用的基础引用
 */
open class BaseDialog : Dialog {

    var layoutParams: LayoutParams? = null
        private set

    private var mContext: Context? = null

    constructor(context: Context, themeResId: Int) :
            super(context, themeResId) {
        initView(context)
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) :
            super(context, cancelable, cancelListener) {
        initView(context)
    }

    constructor(context: Context) :
            super(context) {
        initView(context)
    }

    constructor(activity: Activity) :
            super(activity) {
        initView(activity)
    }

    private fun initView(context: Context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window!!.setBackgroundDrawableResource(R.drawable.transparent_bg)
        mContext = context
        val window = this.window
        window.setGravity(Gravity.TOP)
        layoutParams = window!!.attributes
        layoutParams!!.alpha = 1f
        window.attributes = layoutParams
        if (layoutParams != null) {
            layoutParams!!.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams!!.gravity = Gravity.CENTER
        }
    }

    /**
     * @param context
     *
     * @param alpha   透明度 0.0f--1f(不透明)
     *
     * @param gravity 方向(Gravity.BOTTOM,Gravity.TOP,Gravity.LEFT,Gravity.RIGHT)
     */
    constructor(context: Context, alpha: Float, gravity: Int) : super(context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window!!.setBackgroundDrawableResource(R.drawable.transparent_bg)
        mContext = context
        val window = this.window
        layoutParams = window!!.attributes
        layoutParams!!.alpha = alpha
        window.attributes = layoutParams
        if (layoutParams != null) {
            layoutParams!!.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams!!.gravity = gravity
        }
    }

    /**
     * 隐藏头部导航栏状态栏
     */
    fun skipTools() {
        if (Build.VERSION.SDK_INT < 19) {
            return
        }
        window!!.setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 设置全屏显示
     */
    fun setFullScreen() {
        val window = window
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = LayoutParams.MATCH_PARENT
        lp.height = LayoutParams.MATCH_PARENT
        window.attributes = lp
    }

    /**
     * 设置宽度match_parent
     */
    fun setFullScreenWidth() {
        val window = window
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = LayoutParams.MATCH_PARENT
        lp.height = LayoutParams.WRAP_CONTENT
        window.attributes = lp
    }

    /**
     * 设置高度为match_parent
     */
    fun setFullScreenHeight() {
        val window = window
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window.attributes
        lp.width = LayoutParams.WRAP_CONTENT
        lp.height = LayoutParams.MATCH_PARENT
        window.attributes = lp
    }

    fun setOnWhole() {
        window!!.setType(LayoutParams.TYPE_SYSTEM_ALERT)
    }
}
