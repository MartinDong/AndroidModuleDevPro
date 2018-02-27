package com.dong.module.login

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity

@Route(path = "/login/login_page")
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
    }
}
