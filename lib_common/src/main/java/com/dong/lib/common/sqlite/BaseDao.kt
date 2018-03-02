package com.dong.lib.common.sqlite

import android.database.sqlite.SQLiteDatabase
import com.dong.lib.common.sqlite.annotation.DbField
import com.dong.lib.common.sqlite.annotation.DbTable

/**
 * <p>数据类操作实现</p>
 * Created by Kotlin on 2018/3/1.
 */
class BaseDao<T> : IBaseDao<T> {
    //数据库操作的引用
    private var sqLiteDatabase: SQLiteDatabase? = null
    //要操作的数据实体的引用
    private var entityClass: Class<T>? = null
    //要操作的数据表名称
    private var tableName: String? = null
    //记录数据表是否存在
    private var isInit = false

    fun init(sqLiteDatabase: SQLiteDatabase, entityClass: Class<T>): Boolean {
        this.sqLiteDatabase = sqLiteDatabase
        this.entityClass = entityClass
        this.tableName = tableName
        //自动建表（只创建一次）
        if (!isInit) {
            //获取表名
            tableName = entityClass.getAnnotation(DbTable::class.java).tableName

            //如果数据库没有建立连接跳出操作防止异常信息
            if (!sqLiteDatabase.isOpen) {
                return false
            }

            //执行Sql进行自动建表
            val createTableSql = getCreateTableSql()
            sqLiteDatabase.execSQL(createTableSql)
            isInit = true
        }

        return isInit
    }

    /**
     * 拼装创建数据表的SQL语句
     */
    private fun getCreateTableSql(): String? {
        //create table if not exists tb_name(_id integer,name varchar2(20))
        val sqlCreateTable = StringBuffer()
        sqlCreateTable.append("create table if not exists ")
        sqlCreateTable.append("$tableName (")
        //反射获取所有的数据对象内的成员变量
        val fields = entityClass!!.declaredFields

        fields.forEachIndexed { index, field ->
            //字段名称
            val fieldName = field.getAnnotation(DbField::class.java).fieldName
            //获取成员变量数据类型
            val fieldType = field.type

            when (fieldType) {
                String::class.java -> {
                    sqlCreateTable.append("$fieldName TEXT,")
                }
                Int::class.java -> {
                    sqlCreateTable.append("$fieldName INTEGER,")
                }
                Long::class.java -> {
                    sqlCreateTable.append("$fieldName BIGINT,")
                }
                Double::class.java -> {
                    sqlCreateTable.append("$fieldName DOUBLE,")
                }
                Float::class.java -> {
                    sqlCreateTable.append("$fieldName FLOAT,")
                }
                Boolean::class.java -> {
                    sqlCreateTable.append("$fieldName BOOLEAN,")
                }
                Byte::class.java -> {
                    sqlCreateTable.append("$fieldName BLOB,")
                }
                else -> {
                    //未知类型
                    throw UnsupportedOperationException("未定义的数据类型：fieldName= $fieldName fieldType= $fieldType")
                }
            }

            if (index == fields.size - 1) {
                if (sqlCreateTable.endsWith(","))
                    sqlCreateTable.deleteCharAt(sqlCreateTable.length - 1)
            }
        }
        sqlCreateTable.append(")")

        return sqlCreateTable.toString()
    }

    override fun insert(entity: T): Long {

        return 0
    }
}