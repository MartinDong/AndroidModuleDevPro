package com.dong.module.main

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.dong.lib.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 *  Created by Kotlin on 2018/2/26.
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_news.setOnClickListener {
            //跳转到NewsCenterActivity
            ARouter.getInstance().build("/news/center").navigation()
        }
    }

}
