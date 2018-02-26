package com.dong.module.news.main

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity
import com.dong.module.news.R

/**
 *  <p>新闻模块主页</p>
 *  Created by Kotlin on 2018/2/26.
 */
@Route(path = "/news/center")
class NewsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }

}
