package com.dong.module.sqlite.initdb

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.sqlite.BaseDao
import com.dong.lib.common.sqlite.BaseDaoFactory
import com.dong.lib.common.utils.ToastUtils
import com.dong.module.sqlite.R
import com.dong.module.sqlite.bean.UserEntity
import kotlinx.android.synthetic.main.initdb_my_way_init_activity.*

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
        //User数据表操作引用
        var userBaseDao: BaseDao<UserEntity>? = null

        btn_init_db.setOnClickListener {
            //建立数据库连接，并创建数据表User
            userBaseDao = BaseDaoFactory.getInstance().getBaseDao(UserEntity::class.java)
            if (userBaseDao != null) {
                ToastUtils.showShortToast("数据库连接成功")
            } else {
                ToastUtils.showShortToast("数据库连接失败")
            }
        }

        btn_insert_test_data.setOnClickListener {
            if (userBaseDao != null) {
                val userEntity = UserEntity()
                userEntity.id = 1
                userEntity.name = "測試數據"
                userEntity.password = "lalala"
                val result = userBaseDao!!.insert(userEntity)
                if (result > 0) {
                    ToastUtils.showShortToast("数据插入成功")
                } else {
                    ToastUtils.showShortToast("数据插入失败")
                }
            } else {
                ToastUtils.showShortToast("请先连接数据库")
            }
        }

        btn_query_all_data.setOnClickListener {
            if (userBaseDao != null) {
                val result = userBaseDao!!.queryAll(UserEntity())
                if (result.size > 0) {
                    tv_show_result.text = result.toString()
                } else {
                    tv_show_result.text = "没有查询到数据"
                }
            } else {
                ToastUtils.showShortToast("请先连接数据库")
            }
        }
    }
}
