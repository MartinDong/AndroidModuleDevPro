package com.dong.module.knowledge.main

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.di.component.AppComponent
import com.dong.lib.common.mvp.IPresenter
import com.dong.module.knowledge.R
import kotlinx.android.synthetic.main.activity_news.*

/**
 *  <p>新闻模块主页</p>
 *  Created by Kotlin on 2018/2/26.
 */
@Route(path = "/news/center")
class NewsActivity<P : IPresenter> : BaseActivity<P>() {
    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_news
    }

    override fun initData(savedInstanceState: Bundle?) {
        btn_to_handler.setOnClickListener {
            //跳转登录页面
            ARouter.getInstance()
                    .build("/knowledge/handler")
                    .navigation()
        }
        btn_to_customer_handler.setOnClickListener {
            //跳转登录页面
            ARouter.getInstance()
                    .build("/knowledge/customer_handler")
                    .navigation()
        }
    }
}
