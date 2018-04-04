package com.dong.module.sqlite.initdb

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.di.component.AppComponent
import com.dong.lib.common.mvp.IPresenter
import com.dong.lib.common.sqlite.BaseDaoFactory
import com.dong.lib.common.sqlite.IBaseDao
import com.dong.lib.common.utils.ToastUtils
import com.dong.module.sqlite.R
import com.dong.module.sqlite.bean.UserEntity
import kotlinx.android.synthetic.main.initdb_my_way_init_activity.*

/**
 *  <p>使用自己编写的方式进行数据出连接以及数据表创建</p>
 *  Created by Kotlin on 2018/2/26.
 */
@Route(path = "/sqlite/initdb/my_way_init")
class MyWayInitActivity<P : IPresenter> : BaseActivity<P>() {
    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.initdb_my_way_init_activity
    }

    override fun initData(savedInstanceState: Bundle?) {
        //User数据表操作引用
        var userBaseDao: IBaseDao<UserEntity>? = null

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
                userEntity.name = "董宏宇"
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
                val where = UserEntity()
                where.id = 1

                val result = userBaseDao!!.query(where)
                if (result.size > 0) {
                    tv_show_result.text = result.toString()
                } else {
                    tv_show_result.text = "没有查询到数据"
                }
            } else {
                ToastUtils.showShortToast("请先连接数据库")
            }
        }

        btn_delete_data.setOnClickListener {
            if (userBaseDao != null) {
                val where = UserEntity()
//                where.name = "董宏宇"

                val result = userBaseDao!!.delete(where)
                if (result > 0) {
                    tv_show_result.text = "删除了 $result 条数据"
                } else {
                    tv_show_result.text = "没有找到要删除的数据"
                }
            } else {
                ToastUtils.showShortToast("请先连接数据库")
            }
        }

        btn_update_data.setOnClickListener {
            if (userBaseDao != null) {
                val where = UserEntity()
                where.name = "董宏宇"

                val userEntity = UserEntity()
                userEntity.id = 1
                userEntity.name = "name=董宏宇，改为了=>donghongyu"
                userEntity.password = "lalala"

                val result = userBaseDao!!.update(where, userEntity)
                if (result > 0) {
                    tv_show_result.text = "更新了 $result 条数据"
                } else {
                    tv_show_result.text = "没有找到要更新的数据"
                }
            } else {
                ToastUtils.showShortToast("请先连接数据库")
            }
        }
    }
}
