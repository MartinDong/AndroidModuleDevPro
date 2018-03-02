package com.dong.module.sqlite

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.sqlite.BaseDaoFactory
import com.dong.module.sqlite.bean.UserEntity
import kotlinx.android.synthetic.main.activity_sqlite.*

/**
 *  <p>新闻模块主页</p>
 *  Created by Kotlin on 2018/2/26.
 */
@Route(path = "/sqlite/center")
class SQLiteActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)

        btn_init_user.setOnClickListener {
            BaseDaoFactory.getInstance().getBaseDao(UserEntity::class.java)
        }
    }

}
