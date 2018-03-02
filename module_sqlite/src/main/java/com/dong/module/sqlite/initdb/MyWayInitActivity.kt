package com.dong.module.sqlite.initdb

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.sqlite.BaseDaoFactory
import com.dong.lib.common.utils.ToastUtils
import com.dong.module.sqlite.R
import com.dong.module.sqlite.bean.UserEntity
import kotlinx.android.synthetic.main.initdb_old_way_init_activity.*

/**
 *  <p>使用自己编写的方式进行数据出连接以及数据表创建</p>
 *  Created by Kotlin on 2018/2/26.
 */
@Route(path = "/sqlite/initdb/my_way_init")
class MyWayInitActivity : BaseActivity() {
    private val TAG = MyWayInitActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initdb_my_way_init_activity)

        btn_init_db.setOnClickListener {
            //建立数据库连接，并创建数据表User
            val userBaseDao = BaseDaoFactory.getInstance().getBaseDao(UserEntity::class.java)

            if (userBaseDao != null) {
                ToastUtils.showShortToast("数据库连接成功")
            } else {
                ToastUtils.showShortToast("数据库连接失败")
            }
        }
    }
}
