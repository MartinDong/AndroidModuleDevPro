package com.dong.lib.common.integration;

import android.content.Context;

import com.dong.lib.common.mvp.IModel;

/**
 * ================================================
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 {@link IModel} 必要的 Api 做数据处理
 * Created by xiaoyulaoshi on 2018/3/20.
 * ================================================
 */

public interface IRepositoryManager {
    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service 服务类
     * @param <T>     约束服务类
     * @return 约束类
     */
    <T> T obtainRetrofitService(Class<T> service);

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cache 缓存对象
     * @param <T>   约束
     * @return 约束
     */
    <T> T obtainCacheService(Class<T> cache);

    /**
     * 清理所有缓存
     */
    void clearAllCache();

    /**
     * 获取上下文信息
     *
     * @return 上下文
     */
    Context getContext();
}
