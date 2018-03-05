package com.dong.lib.common.sqlite.proxy

import com.orhanobut.logger.Logger
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 *
 * 使用java实现动态代理数据库操作类加入日志功能
 * Created by xiaoyulaoshi on 2018/3/5.
 */

class BaseDaoProxyForJava : InvocationHandler {

    private var target: Any? = null

    /**
     * 绑定委托对象并返回一个【代理占位】
     *
     * @param target 真实对象
     * @return 代理对象【占位】
     */
    fun bind(target: Any): Any {
        this.target = target
        //取得代理对象
        return Proxy.newProxyInstance(target.javaClass.classLoader,
                target.javaClass.interfaces, this)
    }

    /**
     * 同过代理对象调用方法首先进入这个方法.
     *
     * @param proxy  --代理对象
     * @param method -- 方法,被调用方法.
     * @param args   -- 方法的参数
     */
    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        var result: Any? = null
        //反射方法前调用
        Logger.i("当前执行>>${method.name}")
        //反射执行方法  相当于调用target.sayHelllo;
        result = method.invoke(target, *args)
        //反射方法后调用.
        Logger.i("执行结果>>$result")
        return result
    }
}
