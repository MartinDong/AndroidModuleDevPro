package com.dong.module.sqlite

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.di.component.AppComponent
import com.dong.lib.common.mvp.IPresenter
import kotlinx.android.synthetic.main.sqlite_main_activity.*

/**
 *  <p>新闻模块主页</p>
 *  Created by Kotlin on 2018/2/26.
 */
@Route(path = "/sqlite/center")
class SQLiteMainActivity<P : IPresenter> : BaseActivity<P>() {
    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.sqlite_main_activity
    }

    override fun initData(savedInstanceState: Bundle?) {

        btn_init_db_for_old.setOnClickListener {
            ARouter.getInstance().build("/sqlite/initdb/old_way_init").navigation()
        }

        btn_init_db_for_my.setOnClickListener {
            ARouter.getInstance().build("/sqlite/initdb/my_way_init").navigation()
        }
    }
}
