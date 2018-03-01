package com.dong.module.login

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.sqlite.BaseDaoFactory
import com.dong.module.bean.UserEntity
import kotlinx.android.synthetic.main.activity_login.*

@Route(path = "/login/login_page")
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        btn_init_user.setOnClickListener {
            BaseDaoFactory.getInstance().getBaseDao(UserEntity::class.java)
        }
    }
}
