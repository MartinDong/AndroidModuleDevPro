package com.dong.module.login

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dong.lib.common.base.BaseActivity
import com.dong.lib.common.di.component.AppComponent
import com.dong.lib.common.mvp.IPresenter

@Route(path = "/login/login_page")
class LoginActivity<P : IPresenter> : BaseActivity<P>() {
    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_login
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}
