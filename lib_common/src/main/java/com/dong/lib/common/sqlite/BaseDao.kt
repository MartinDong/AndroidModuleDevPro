package com.dong.lib.common.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dong.lib.common.sqlite.annotation.DbField
import com.dong.lib.common.sqlite.annotation.DbTable
import java.lang.reflect.Field
import java.util.*

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

    //因为反射会消耗时间，这里使用缓存，进行性能优化
    //缓存空间（key-字段名,标注的自定义注解 value-成员变量）
    private var cacheField: HashMap<String, Field>? = null

    fun init(sqLiteDatabase: SQLiteDatabase, entityClass: Class<T>): Boolean {
        this.sqLiteDatabase = sqLiteDatabase
        this.entityClass = entityClass
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

            //初始化缓存空间
            cacheField = HashMap()
            initCacheField()

            //标记已经创建过数据表
            isInit = true
        }

        return isInit
    }

    /**
     * 初始化字段缓存
     */
    private fun initCacheField() {
        //1.取到所有的列名（查询一个空表获取表结构，不影响性能）
        val sqlQuery = "select * from $tableName limit 1,0"
        val cursor: Cursor = sqLiteDatabase!!.rawQuery(sqlQuery, null)
        //获取所有的列名
        val columnNames = cursor.columnNames
        //关闭资源
        cursor.close()
        //2.取所有成员名
        val columnFields = entityClass!!.declaredFields
        //3.通过两层循环，进行对应关系建立
        columnNames.forEach ColumnFor@{ columnName ->
            columnFields.forEach FieldFor@{ columnField ->
                if (columnName == columnField.getAnnotation(DbField::class.java).fieldName)
                    cacheField!![columnName] = columnField
                return@FieldFor
            }
        }

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

    /**
     * 插入数据
     */
    override fun insert(entity: T): Long {
        //1、准备好ContentValues中的数据
        val map: Map<String, String> = getValues(entity)
        //2、设置插入的内容
        val values: ContentValues = getContentValues(map)
        //3、执行插入
        return sqLiteDatabase!!.insert(tableName, null, values)
    }

    /**
     * 根据[getValues]获取ContentValues
     */
    private fun getContentValues(map: Map<String, String>): ContentValues {
        val contentValues = ContentValues()
        map.entries.iterator().forEach {
            val key = it.key
            val value = it.value
            //设置数据表每一列对应的数据
            contentValues.put(key, value)
        }
        return contentValues
    }

    /**
     * 构建<数据表列名,数据对象成员变量值>集合
     */
    private fun getValues(entity: T): Map<String, String> {
        val map = HashMap<String, String>()

        val fieldIterator = cacheField!!.entries.iterator()

        fieldIterator.forEach IteratorFor@{
            //设置字段可访问
            it.value.isAccessible = true
            try {
                //获取变量的值
                val valueObject = it.value.get(entity) ?: return@IteratorFor

                //將获取的数据转成String类型
                val value: String = valueObject.toString()

                //获取列名
                val key = it.key

                if (key.isNotEmpty()) {
                    map[key] = value
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }

        return map
    }
}