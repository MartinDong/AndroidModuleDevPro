package com.dong.lib.common.sqlite

/**
 * <p>操作SQLite数据库的顶层接口</p>
 * Created by Kotlin on 2018/2/27.
 */
interface IBaseDao<T> {
    /**
     * 插入操作
     */
    fun insert(entity: T): Long

    /**
     * 查询所有数据
     */
    fun queryAll(entity: Class<T>): MutableList<T>
}