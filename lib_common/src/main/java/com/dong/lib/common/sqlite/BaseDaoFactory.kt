package com.dong.lib.common.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.dong.lib.common.sqlite.proxy.BaseDaoProxy
import com.dong.lib.common.utils.Utils

/**
 * <p>数据库处理工厂</p>
 * Created by Kotlin on 2018/3/1.
 */
class BaseDaoFactory {

    //默认数据库名称
    private var dbName = "myApp.db"

    init {
        //openOrCreateDatabase 如果不存在则先创建再打开数据库，如果存在则直接打开。
        sqLiteDatabasePath = "${Utils.getContext().getDir("database", Context.MODE_APPEND).path}/$dbName"
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqLiteDatabasePath, null)
    }

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

    //获取数据库操作对象
    fun <T : Any> getBaseDao(entityClass: Class<T>): BaseDaoProxy<T>? {
        var baseDao: BaseDaoProxy<T>? = null

        try {
            baseDao = BaseDaoProxy<T>(BaseDao<T>())

            baseDao.init(sqLiteDatabase!!, entityClass)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return baseDao
    }

}