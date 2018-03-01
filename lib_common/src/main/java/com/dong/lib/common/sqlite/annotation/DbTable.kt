package com.dong.lib.common.sqlite.annotation


/**
 * <p>添加在要操作的数据对象名上面，用来设置数据表名称</p>
 *
 *  /**
 *  * @DbTable("tb_user")
 *  * class UserEntity {}
 *  */
 * Created by Kotlin on 2018/3/1.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DbTable(val tableName: String)