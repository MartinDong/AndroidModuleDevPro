package com.dong.lib.common.utils

import android.os.Handler
import android.os.Looper
import android.support.annotation.StringRes
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast

/**
 * <p>Toast相关工具类</p>
 * Created by Kotlin on 2018/2/26.
 */
class ToastUtils {

    companion object {
        //当连续弹出吐司时，是要弹出新吐司还是只修改文本内容
        private var isJumpWhenMore: Boolean = false

        //使用单一的Toast对象进行控制
        private var sToast: Toast? = null

        //在应用的主线程中处理Toast消息处理队列
        private val sHandler = Handler(Looper.getMainLooper())

        /**
         * 吐司初始化
         *
         * @param isJumpWhenMore 当连续弹出吐司时，是要弹出新吐司还是只修改文本内容
         *
         * `true`: 弹出新吐司
         * `false`: 只修改文本内容
         *
         * 如果为`false`的话可用来做显示任意时长的吐司
         */
        fun init(isJumpWhenMore: Boolean) {
            ToastUtils.isJumpWhenMore = isJumpWhenMore
        }

        /**
         * 安全地显示短时吐司
         *
         * @param text 文本
         */
        fun showShortToastSafe(text: CharSequence) {
            sHandler.post { showToastFinal(text, Toast.LENGTH_SHORT) }
        }

        /**
         * 安全地显示短时吐司
         *
         * @param resId 资源Id
         */
        fun showShortToastSafe(@StringRes resId: Int) {
            sHandler.post { showToast(resId, Toast.LENGTH_SHORT) }
        }

        /**
         * 安全地显示短时吐司
         *
         * @param resId 资源Id
         * @param args  参数
         */
        fun showShortToastSafe(@StringRes resId: Int, vararg args: Any) {
            sHandler.post { showToast(resId, Toast.LENGTH_SHORT, *args) }
        }

        /**
         * 安全地显示短时吐司
         *
         * @param format 格式
         * @param args   参数
         */
        fun showShortToastSafe(format: String, vararg args: Any) {
            sHandler.post { showToast(format, Toast.LENGTH_SHORT, *args) }
        }

        /**
         * 安全地显示长时吐司
         *
         * @param text 文本
         */
        fun showLongToastSafe(text: CharSequence) {
            sHandler.post { showToastFinal(text, Toast.LENGTH_LONG) }
        }

        /**
         * 安全地显示长时吐司
         *
         * @param resId 资源Id
         */
        fun showLongToastSafe(@StringRes resId: Int) {
            sHandler.post { showToast(resId, Toast.LENGTH_LONG) }
        }

        /**
         * 安全地显示长时吐司
         *
         * @param resId 资源Id
         * @param args  参数
         */
        fun showLongToastSafe(@StringRes resId: Int, vararg args: Any) {
            sHandler.post { showToast(resId, Toast.LENGTH_LONG, *args) }
        }

        /**
         * 安全地显示长时吐司
         *
         * @param format 格式
         * @param args   参数
         */
        fun showLongToastSafe(format: String, vararg args: Any) {
            sHandler.post { showToast(format, Toast.LENGTH_LONG, *args) }
        }

        /**
         * 显示短时吐司
         *
         * @param text 文本
         */
        fun showShortToast(text: CharSequence) {
            showToastFinal(text, Toast.LENGTH_SHORT)
        }

        /**
         * 显示短时吐司
         *
         * @param resId 资源Id
         */
        fun showShortToast(@StringRes resId: Int) {
            showToast(resId, Toast.LENGTH_SHORT)
        }

        /**
         * 显示短时吐司
         *
         * @param resId 资源Id
         * @param args  参数
         */
        fun showShortToast(@StringRes resId: Int, vararg args: Any) {
            showToast(resId, Toast.LENGTH_SHORT, *args)
        }

        /**
         * 显示短时吐司
         *
         * @param format 格式
         * @param args   参数
         */
        fun showShortToast(format: String, vararg args: Any) {
            showToast(format, Toast.LENGTH_SHORT, *args)
        }

        /**
         * 显示长时吐司
         *
         * @param text 文本
         */
        fun showLongToast(text: CharSequence) {
            showToastFinal(text, Toast.LENGTH_LONG)
        }

        /**
         * 显示长时吐司
         *
         * @param resId 资源Id
         */
        fun showLongToast(@StringRes resId: Int) {
            showToast(resId, Toast.LENGTH_LONG)
        }

        /**
         * 显示长时吐司
         *
         * @param resId 资源Id
         * @param args  参数
         */
        fun showLongToast(@StringRes resId: Int, vararg args: Any) {
            showToast(resId, Toast.LENGTH_LONG, *args)
        }

        /**
         * 显示长时吐司
         *
         * @param format 格式
         * @param args   参数
         */
        fun showLongToast(format: String, vararg args: Any) {
            showToast(format, Toast.LENGTH_LONG, *args)
        }

        /**
         * 显示吐司
         *
         * @param resId    资源Id
         * @param duration 显示时长
         */
        private fun showToast(@StringRes resId: Int, duration: Int) {
            showToastFinal(Utils.getApp().resources.getText(resId).toString(), duration)
        }

        /**
         * 显示吐司
         *
         * @param resId    资源Id
         * @param duration 显示时长
         * @param args     参数
         */
        private fun showToast(@StringRes resId: Int, duration: Int, vararg args: Any) {
            showToastFinal(String.format(Utils.getApp().resources.getString(resId), args), duration)
        }

        /**
         * 显示吐司
         *
         * @param format   格式
         * @param duration 显示时长
         * @param args     参数
         */
        private fun showToast(format: String, duration: Int, vararg args: Any) {
            showToastFinal(String.format(format, *args), duration)
        }

        /**
         * 显示吐司
         *
         * @param text     文本
         * @param duration 显示时长
         */
        private fun showToastFinal(text: CharSequence, duration: Int) {
            if (isJumpWhenMore) cancelToast()
            if (sToast == null) {
                sToast = Toast.makeText(Utils.getApp(), text, duration)
                //取出Toast中的文本控件进行自定义
                var tv = sToast!!.view.findViewById<TextView>(android.R.id.message)
                tv.textSize = 12F
                sToast!!.setGravity(Gravity.CENTER, 0, 0)
            } else {
                sToast!!.setText(text)
                sToast!!.duration = duration
            }
            sToast!!.show()
        }

        /**
         * 取消吐司显示
         */
        fun cancelToast() {
            if (sToast != null) {
                //主动取消并释放资源
                sToast!!.cancel()
                sToast = null
            }
        }
    }

    private fun ToastUtils() {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}