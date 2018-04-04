package com.dong.lib.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.dong.lib.common.R

/**
 * 倒计时按钮控件，一般用在获取验证码
 * ----- 日期 ---------- 维护人 ---------- 变更内容 ----------
 * 2017/6/16 10:29	    董宏宇
 */

class CountDownTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        AppCompatTextView(context, attrs, defStyleAttr) {

    //点击按钮之前的文字
    private var mTextBefore: String = "获取验证码"
    //点击按钮之后正在倒计时的时候的文字
    private var mTextCenter: String = ""
    //点击按钮之后的文字
    private var mTextLater: String = "获取验证码"
    //倒计时时间
    private var mCountDownTime: Int = 0
    //临时计时器
    private var mTempleCountDownTime: Int = 0

    //用于退出activity,避免countdown，造成资源浪费。
    private val mHandler = Handler()
    private val mRunnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            if (mTempleCountDownTime > 0) {
                text = "${mTempleCountDownTime--}秒"
                // 在此处添加执行的代码
                mHandler.postDelayed(this, 1000)// 1000 是延时时长
            } else {
                mTempleCountDownTime = mCountDownTime
                text = mTextLater
                isEnabled = true
            }

            //Logger.d("还有：" + text.toString())
        }
    }

    /**
     * 关闭计时器，防止资源浪费以及溢出
     */
    fun cancelCountDownTimers() {
        mHandler.removeCallbacks(mRunnable)// 关闭定时器处理
    }

    /**
     * 开始列表全局的计时器执行，对数据中的剩余时间进行--
     */
    fun startCountDownTimers() {
        mHandler.postDelayed(mRunnable, 0)// 打开定时器，执行操作
    }

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownTextView)

            //获取设置的控件文案
            mTextBefore = typedArray.getString(R.styleable.CountDownTextView_textBefore)
            mTextCenter = typedArray.getString(R.styleable.CountDownTextView_textCenter)
            mTextLater = typedArray.getString(R.styleable.CountDownTextView_textLater)
            mCountDownTime = typedArray.getInteger(R.styleable.CountDownTextView_countDownTime, 0)
            mTempleCountDownTime = typedArray.getInteger(R.styleable.CountDownTextView_countDownTime, 0)
            text = mTextBefore
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //释放资源
        cancelCountDownTimers()
    }

}