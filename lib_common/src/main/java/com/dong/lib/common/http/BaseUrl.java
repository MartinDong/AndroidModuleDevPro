package com.dong.lib.common.http;

import okhttp3.HttpUrl;

/**
 * ================================================
 * 针对于 BaseUrl 在 App 启动时不能确定,需要请求服务器接口动态获取的应用场景
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
public interface BaseUrl {
    /**
     * 在调用 Retrofit API 接口之前,使用 Okhttp 或其他方式,请求到正确的 BaseUrl 并通过此方法返回
     *
     * @return HttpUrl
     */
    HttpUrl url();
}
