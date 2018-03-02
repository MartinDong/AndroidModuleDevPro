package com.dong.module.sqlite

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dong.lib.common.base.BaseActivity
import kotlinx.android.synthetic.main.sqlite_main_activity.*

/**
 *  <p>新闻模块主页</p>
 *  Created by Kotlin on 2018/2/26.
 */
@Route(path = "/sqlite/center")
class SQLiteMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sqlite_main_activity)

        btn_init_db_for_old.setOnClickListener {
            ARouter.getInstance().build("/sqlite/initdb/old_way_init").navigation()
        }

        btn_init_db_for_my.setOnClickListener {
            ARouter.getInstance().build("/sqlite/initdb/my_way_init").navigation()
        }
    }

}
