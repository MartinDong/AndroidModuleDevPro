package com.dong.lib.common.sqlite

import android.database.sqlite.SQLiteDatabase

/**
 * <p>操作SQLite数据库的顶层接口</p>
 * Created by Kotlin on 2018/2/27.
 */
interface IBaseDao<T> {
    /**
     * 初始化数据库连接
     */
    fun init(sqLiteDatabase: SQLiteDatabase, entityClass: Class<T>): Boolean

    /**
     * 将 [entity] 进行数据插入
     */
    fun insert(entity: T): Long

    /**
     * 根据条件 [where] 进行数据删除
     */
    fun delete(where: T): Int

    /**
     * 根据条件 [where] 进行数据更新，如果[where]==null 则代表删除所有数据
     */
    fun update(where: T, newEntity: T): Int

    /**
     * 根据条件 [where] 进行数据查询，如果[where]==null 则代表查询所有数据
     */
    fun query(where: T): MutableList<T>

}