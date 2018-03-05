package com.dong.lib.common.sqlite.proxy

import android.database.sqlite.SQLiteDatabase
import com.dong.lib.common.sqlite.IBaseDao
import com.orhanobut.logger.Logger

/**
 * <p>静态代理数据库操作类，用来记录日志</P>
 * Created by xiaoyulaoshi on 2018/3/5.
 */
class BaseDaoProxy<T>(var iBaseDao: IBaseDao<T>) : IBaseDao<T> by iBaseDao {
    override fun init(sqLiteDatabase: SQLiteDatabase, entityClass: Class<T>): Boolean {
        Logger.i("初始化数据库连接……")
        val isInitSuccess = iBaseDao.init(sqLiteDatabase, entityClass)
        if (isInitSuccess) {
            Logger.i("数据库连接成功……")
        } else {
            Logger.e("数据库连接失败……")
        }
        return isInitSuccess
    }

    override fun insert(entity: T): Long {
        Logger.i("开始插入数据……")
        val result = iBaseDao.insert(entity)
        Logger.i("插入数据索引位置> $result <……")
        return result
    }

    override fun delete(where: T): Int {
        Logger.i("开始删除数据……")
        val result = iBaseDao.delete(where)
        Logger.i("删除了> $result <条数据……")
        return result
    }

    override fun update(where: T, newEntity: T): Int {
        Logger.i("开始更新数据……")
        val result = iBaseDao.update(where, newEntity)
        Logger.i("更新了> $result <条数据……")
        return result
    }

    override fun query(where: T): MutableList<T> {
        Logger.i("开始查询数据……")
        val result = iBaseDao.query(where)
        Logger.i("查询到> ${result.size} <条数据……")
        return result
    }

}