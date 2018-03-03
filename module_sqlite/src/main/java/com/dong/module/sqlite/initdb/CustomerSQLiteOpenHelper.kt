package com.dong.module.sqlite.initdb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.orhanobut.logger.Logger

/**
 * <p>传统方式进行数据库初始化</p>
 * Created by xiaoyulaoshi on 2018/3/2.
 */
class CustomerSQLiteOpenHelper(context: Context?) :
        SQLiteOpenHelper(context, "OpenHelper_myAPP.db", null, 1) {

    private val TAG = CustomerSQLiteOpenHelper::class.java.simpleName
    /**
     * 初始化数据库的表结构
     */
    override fun onCreate(db: SQLiteDatabase?) {
        Logger.t(TAG).d("数据库初始化……")
    }
    /**
     * 数据库版本升级时调用
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Logger.t(TAG).d("数据库更新表结构……")
    }
}