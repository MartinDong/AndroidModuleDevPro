package com.dong.lib.common.http;

import com.dong.lib.common.di.module.GlobalConfigModule;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 处理 Http 请求和响应结果的处理类
 * 使用 {@link GlobalConfigModule.Builder#globalHttpHandler(GlobalHttpHandler)} 方法配置
 * <p>
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */

public interface GlobalHttpHandler {

    /**
     * 当发起Http请求之前
     *
     * @param chain   链式拦截器
     * @param request 请求信息
     * @return 请求信息
     */
    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);

    /**
     * 当Http响应结果的时候
     *
     * @param httpResult 请求响应结果
     * @param chain      链式拦截器
     * @param response   响应信息
     * @return 响应信息
     */
    Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response);


    /**
     * 空的实现，不进行拦截操作
     */
    GlobalHttpHandler EMPTY = new GlobalHttpHandler() {
        @Override
        public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
            return request;
        }

        @Override
        public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
            return response;
        }
    };
}
