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
//T必须指明上界是Any且不为null，下面会用到反射获取对象实例，默认是Any?
class BaseDao<T : Any> : IBaseDao<T> {

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

    override fun init(sqLiteDatabase: SQLiteDatabase, entityClass: Class<T>): Boolean {
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
        val cursor = sqLiteDatabase!!.rawQuery(sqlQuery, null)
        //获取所有的列名
        val columnNames = cursor.columnNames
        //关闭资源
        cursor.close()
        //2.取所有成员名
        val columnFields = entityClass!!.declaredFields
        //3.通过两层循环，进行对应关系建立
        columnNames.forEach ColumnFor@{ columnName ->
            columnFields.forEach FieldFor@{ columnField ->
                if (columnName == columnField.getAnnotation(DbField::class.java).fieldName) {
                    columnField.isAccessible = true
                    cacheField!![columnName] = columnField
                }
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
            val columnName = field.getAnnotation(DbField::class.java).fieldName
            //获取成员变量数据类型
            val fieldType = field.type

            when (fieldType) {
                String::class.java -> {
                    sqlCreateTable.append("$columnName TEXT,")
                }
                Integer::class.java -> {
                    sqlCreateTable.append("$columnName INTEGER,")
                }
                Long::class.java -> {
                    sqlCreateTable.append("$columnName BIGINT,")
                }
                Double::class.java -> {
                    sqlCreateTable.append("$columnName DOUBLE,")
                }
                ByteArray::class.java -> {
                    sqlCreateTable.append("$columnName BLOB,")
                }
                else -> {
                    //未知类型
                    throw UnsupportedOperationException("未定义的数据类型：fieldName= $columnName fieldType= $fieldType")
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
        //2、设置插入的内容
        val values: ContentValues = getContentValuesForInsert(entity)
        //3、执行插入
        return sqLiteDatabase!!.insert(tableName, null, values)
    }

    /**
     * 删除数据
     */
    override fun delete(where: T): Int {
        val condition = Condition(getContentValuesForQuery(where))

        //受影响行数
        return sqLiteDatabase!!
                .delete(
                        tableName,
                        condition.getWhereCause(),
                        condition.getWhereArgs()
                )
    }

    /**
     * 更新数据
     */
    override fun update(where: T, newEntity: T): Int {
        val condition = Condition(getContentValuesForQuery(where))

        //受影响行数
        return sqLiteDatabase!!
                .update(
                        tableName,
                        getContentValuesForInsert(newEntity),
                        condition.getWhereCause(),
                        condition.getWhereArgs()
                )
    }

    /**
     * 查询数据
     * @param where 查询条件对象,同时也用来初始化对象使用
     */
    override fun query(where: T): MutableList<T> {
        return query(where, null, null, null)
    }

    /**
     * 查询数据
     * @param where 查询条件对象
     * @param orderBy 排序规则
     * @param startIndex 开始的位置
     * @param limit 限制查询得到的数据个数
     */
    fun query(where: T, orderBy: String?, startIndex: Int?, limit: Int?): MutableList<T> {
        //拼接分页语句
        var limitString: String? = null
        if (startIndex != null && limit != null) {
            limitString = "$startIndex,$limit"
        }

        val condition = Condition(getContentValuesForQuery(where))

        var cursor: Cursor? = null

        //定义查询结果
        val result = mutableListOf<T>()
        try {
            //查询数据库
            cursor = sqLiteDatabase!!
                    .query(
                            tableName,
                            null,
                            condition.getWhereCause(),
                            condition.getWhereArgs(),
                            null,
                            null,
                            orderBy,
                            limitString
                    )
            //将查到结果添加到返回集合中
            result.addAll(getQueryResult(cursor, where))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return result
    }

    /**
     * 获取查询使用的ContentValues
     */
    private fun getContentValuesForQuery(entity: T): ContentValues {
        val contentValues = ContentValues()
        try {
            cacheField!!.forEach {
                if (it.value.get(entity) == null) {
                    return@forEach
                }
                contentValues.put(it.key, it.value.get(entity).toString())
            }
        } catch (e: IllegalAccessError) {
            e.printStackTrace()
        }

        return contentValues
    }

    /**
     * 条件拼接
     */
    class Condition(whereContent: ContentValues) {
        /**
         * 条件拼接
         * _id=?&&name=?
         */
        private var whereCause: String? = null

        private var whereArgs: Array<String>? = null

        //根据传入的contentValues转换成查询条件
        init {
            //记录后面填充到查询语句“？”上的数据参数
            val argList = mutableListOf<String>()
            //拼接查询语句
            val whereCaseSb = StringBuilder()

            /**
             * 是为了链接下面的查询条件条件，也或者是替换没有查询条件的语句。
             * 比如：要把检索条件作为一个参数传递给SQL，
             * 那么，当这个检索语句不存在的话就可以给它赋值为1=1.
             * 这样就避免了SQL出错，也就可以把加条件的SQL和不加条件的SQL合二为一。
             */
            whereCaseSb.append(" 1=1 ")

            val keys = whereContent.keySet()
            val iterator = keys.iterator()

            //因为使用了“1=1”，所以即便是这里没有任何数据拼接，也是可以正常
            while (iterator.hasNext()) {
                val key = iterator.next() as String
                val valueObject = whereContent.get(key)
                if (valueObject != null) {
                    val value = valueObject as String
                    //拼接查询条件语句
                    //1:1 and _id=? and name=?
                    whereCaseSb.append(" and  $key =?")

                    //记录？对应的value
                    argList.add(value)
                }
            }
            //集合转成数组
            this.whereArgs = argList.toTypedArray()
            this.whereCause = whereCaseSb.toString()
        }

        fun getWhereCause(): String {
            return this.whereCause!!
        }

        fun getWhereArgs(): Array<String> {
            return this.whereArgs!!
        }
    }

    /**
     * 获取查询db结果
     */
    private fun getQueryResult(cursor: Cursor, where: T): MutableList<T> {
        //定义查询结果
        val result = mutableListOf<T>()
        //Cursor从头读到尾
        //游标从头读到尾
        cursor.moveToFirst()
        //移动游标获取下一行数据
        while (!cursor.isAfterLast) {
            //通过反射构建一个查询结果对象
            val item = where.javaClass.newInstance()

            //拿到缓存的当前数据对象的成员变量与数据库的键值关系
            val fieldIterator = cacheField!!.entries.iterator()
            fieldIterator.forEach IteratorFor@{
                //获取数据库字段名称
                val columnName = it.key
                //数据库字段名对应的数据对象的成员变量
                val field = it.value
                //获取指定列名对应的索引
                val columnIndex = cursor.getColumnIndex(columnName)
                //获取成员变量数据类型
                val fieldType = field.type

                if (columnIndex != -1) {
                    when (fieldType) {
                        String::class.java -> {
                            field.set(item, cursor.getString(columnIndex))
                        }
                        Integer::class.java -> {
                            field.set(item, cursor.getInt(columnIndex))
                        }
                        Long::class.java -> {
                            field.set(item, cursor.getLong(columnIndex))
                        }
                        Double::class.java -> {
                            field.set(item, cursor.getDouble(columnIndex))
                        }
                        ByteArray::class.java -> {
                            field.set(item, cursor.getBlob(columnIndex))
                        }
                        else -> {
                            //未知类型
                            throw UnsupportedOperationException("未定义的数据类型：columnName= $columnName fieldType= $fieldType")
                        }
                    }
                }
            }
            //添加到结果集
            result.add(item)
            //移动到下一个位置
            cursor.moveToNext()
        }
        cursor.close()
        return result
    }

    /**
     * 获取插入使用的ContentValues
     */
    private fun getContentValuesForInsert(entity: T): ContentValues {
        val contentValues = ContentValues()

        val fieldIterator = cacheField!!.entries.iterator()

        fieldIterator.forEach IteratorFor@{
            try {
                //获取变量的值
                val valueObject = it.value.get(entity) ?: return@IteratorFor
                //获取列名
                val columnName = it.key
                //获取成员变量数据类型
                val fieldType = it.value.type
                when (fieldType) {
                    String::class.java -> {
                        contentValues.put(columnName, valueObject as String)
                    }
                    Integer::class.java -> {
                        contentValues.put(columnName, valueObject as Int)
                    }
                    Long::class.java -> {
                        contentValues.put(columnName, valueObject as Long)
                    }
                    Double::class.java -> {
                        contentValues.put(columnName, valueObject as Double)
                    }
                    ByteArray::class.java -> {
                        contentValues.put(columnName, valueObject as ByteArray)
                    }
                    else -> {
                        //未知类型
                        throw UnsupportedOperationException("未定义的数据类型：columnName= $columnName fieldType= $fieldType")
                    }
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }

        return contentValues
    }

}