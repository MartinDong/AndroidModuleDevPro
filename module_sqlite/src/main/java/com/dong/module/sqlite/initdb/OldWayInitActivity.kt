package com.dong.module.sqlite.initdb

import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.utils.ToastUtils
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
    //数据库操作实例
    private var sqLiteDatabase: SQLiteDatabase? = null
    //记录数据库是否连接
    private var isInit = false

    //数据库里的表SQL
    private var CREAT_TABLE_SQL = ("create table if not exists bookStore ("
            + "id integer primary key autoincrement,"
            + "book_name text, "
            + "author text, "
            + "price real)")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initdb_old_way_init_activity)

        //创建SQLIteOpenHelper对象（1）
        customerSQLiteOpenHelper = CustomerSQLiteOpenHelper(this)

        btn_init_db.setOnClickListener {
            //通过getWritableDatabase()方式来新建SQLite数据库（2）
            try {
                sqLiteDatabase = customerSQLiteOpenHelper!!.writableDatabase
                if (sqLiteDatabase != null) {
                    isInit = true
                    ToastUtils.showShortToast(getText(R.string.module_sqlite_toast_init_db_success))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btn_init_db_table.setOnClickListener {
            if (isInit) {
                try {
                    //执行创建表SQL语句
                    sqLiteDatabase!!.execSQL(CREAT_TABLE_SQL)
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            } else {
                ToastUtils.showShortToast(getText(R.string.module_sqlite_toast_init_db_tab_success))
            }
        }
    }
}
