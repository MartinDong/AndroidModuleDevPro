package com.dong.module.sqlite.bean

import com.dong.lib.common.sqlite.annotation.DbField
import com.dong.lib.common.sqlite.annotation.DbTable

/**
 * <p>说明信息</p>
 * Created by Kotlin on 2018/3/1.
 */
@DbTable("tb_user")
class UserEntity {
    @DbField("_id")
    var id: Int = 0
    @DbField("_name")
    var name: String? = null
    @DbField("_password")
    var password: String? = null

    override fun toString(): String {
        return "UserEntity(id=$id, name=$name, password=$password)"
    }
}