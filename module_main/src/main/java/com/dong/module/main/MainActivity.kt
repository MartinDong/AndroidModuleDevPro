package com.dong.module.main

import android.os.Bundle
import android.view.KeyEvent
import com.alibaba.android.arouter.launcher.ARouter
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.base.ViewManager
import com.dong.lib.common.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

/**
 *  Created by Kotlin on 2018/2/26.
 */
class MainActivity : BaseActivity() {

    //记录按返回键时间
    private var mExitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_news.setOnClickListener {
            //跳转到新闻主页
            ARouter.getInstance().build("/news/center").navigation()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtils.showShortToast(getString(R.string.app_exit_hint))
                mExitTime = System.currentTimeMillis()
            } else {
                ViewManager.getInstance().exitApp(this)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
