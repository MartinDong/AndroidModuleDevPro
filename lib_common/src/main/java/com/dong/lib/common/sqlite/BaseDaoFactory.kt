package com.dong.lib.common.sqlite

import android.database.sqlite.SQLiteDatabase

/**
 * <p>数据库处理工厂</p>
 * Created by Kotlin on 2018/3/1.
 */
class BaseDaoFactory {
    companion object {
        //单利工厂
        private var baseDaoFactory: BaseDaoFactory? = null
        //数据库存储路径
        private var sqLiteDatabasePath: String? = null
        //数据库操作类
        private var sqLiteDatabase: SQLiteDatabase? = null

        fun getInstance(): BaseDaoFactory {
            if (baseDaoFactory == null) {
                synchronized(BaseDaoFactory::class.java) {
                    if (baseDaoFactory == null) {
                        baseDaoFactory = BaseDaoFactory()
                    }
                }
            }
            return baseDaoFactory!!
        }
    }

    constructor() {
        sqLiteDatabasePath = "data/data/com.dong.module.app/donghongyu.db"
        sqLiteDatabase = SQLiteDatabase.openDatabase(sqLiteDatabasePath, null, SQLiteDatabase.CREATE_IF_NECESSARY)
    }

    //获取数据库操作对象
    fun <T> getBaseDao(entityClass: Class<T>): BaseDao<T>? {
        var baseDao: BaseDao<T>? = null

        baseDao = BaseDao::class.java.newInstance() as BaseDao<T>?
        baseDao!!.init(sqLiteDatabase!!, entityClass)

        return baseDao
    }
}