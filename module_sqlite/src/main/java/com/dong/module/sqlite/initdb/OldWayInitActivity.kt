package com.dong.module.sqlite.initdb

import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity
import com.dong.module.sqlite.R
import kotlinx.android.synthetic.main.initdb_old_way_init_activity.*

/**
 *  <p>传统方式进行数据库初始化</p>
 *  Created by Kotlin on 2018/2/26.
 */
@Route(path = "/sqlite/initdb/old_way_init")
class OldWayInitActivity : BaseActivity() {
    private val TAG = OldWayInitActivity::class.java.simpleName

    //自己实现SQLiteOpenHelper
    private var customerSQLiteOpenHelper: SQLiteOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initdb_old_way_init_activity)

        //创建SQLIteOpenHelper对象（1）
        customerSQLiteOpenHelper = CustomerSQLiteOpenHelper(this)

        btn_init_db.setOnClickListener {
            //通过getWritableDatabase()方式来新建SQLite数据库（2）
            customerSQLiteOpenHelper!!.writableDatabase
        }
    }
}
