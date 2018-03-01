package com.dong.lib.common.sqlite.annotation


/**
 * <p>添加在要处理的数据对象的字段之上，用来设置数据表的字段名称</p>
 * /**
 * *@DbTable("tb_user")
 * *class UserEntity {
 * *    @DbField("_id")
 * *    var id: Int = 0
 * *    @DbField("name")
 * *    var name: String? = null
 * *    @DbField("password")
 * *    var password: String? = null
 * *}
 * */
 * Created by Kotlin on 2018/3/1.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class DbField(val fieldName: String)