package com.dong.lib.common.integration;

import android.app.Application;
import android.content.Context;

import com.dong.lib.common.integration.cache.Cache;
import com.dong.lib.common.integration.cache.CacheType;
import com.dong.lib.common.mvp.IModel;
import com.dong.lib.common.utils.Preconditions;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * ================================================
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 {@link IModel} 层必要的 Api 做数据处理
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {
    @Inject
    Lazy<Retrofit> mRetrofit;
    @Inject
    Lazy<RxCache> mRxCache;
    @Inject
    Application mApplication;
    @Inject
    Cache.Factory mCachefactory;
    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;

    @Inject
    public RepositoryManager() {
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    @Override
    public synchronized <T> T obtainRetrofitService(Class<T> service) {
        if (mRetrofitServiceCache == null)
            mRetrofitServiceCache = mCachefactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        Preconditions.checkNotNull(mRetrofitServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService = (T) mRetrofitServiceCache.get(service.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = mRetrofit.get().create(service);
            mRetrofitServiceCache.put(service.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    @Override
    public synchronized <T> T obtainCacheService(Class<T> cache) {
        if (mCacheServiceCache == null)
            mCacheServiceCache = mCachefactory.build(CacheType.CACHE_SERVICE_CACHE);
        Preconditions.checkNotNull(mCacheServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService = (T) mCacheServiceCache.get(cache.getCanonicalName());
        if (cacheService == null) {
            cacheService = mRxCache.get().using(cache);
            mCacheServiceCache.put(cache.getCanonicalName(), cacheService);
        }
        return cacheService;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        mRxCache.get().evictAll();
    }

    @Override
    public Context getContext() {
        return mApplication;
    }
}
