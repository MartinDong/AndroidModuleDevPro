package com.dong.lib.common.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.dong.lib.common.integration.ActivityLifecycle;
import com.dong.lib.common.integration.FragmentLifecycle;
import com.dong.lib.common.integration.IRepositoryManager;
import com.dong.lib.common.integration.RepositoryManager;
import com.dong.lib.common.integration.cache.Cache;
import com.dong.lib.common.integration.cache.CacheType;
import com.dong.lib.common.integration.lifecycle.ActivityLifecycleForRxLifecycle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * {@link Module}:这个注解用来标记类（一般类名以Module结尾）。
 * Module主要的作用是用来集中管理@Provide标记的方法。
 * 我们定义一个被@Module注解的类，Dagger就会知道在哪里找到依赖来满足创建类的实例。
 * modules的一个重要特征是被设计成区块并可以组合在一起。
 * （例如可以在App中看到多个组合在一起的modules）
 * <p>
 * {@link Provides}: 对方法进行注解，都是有返回类型的。
 * 用来告诉Dagger我们想如何创建并提供该类型的依赖实例（一般会在方法中new出实例）。
 * 用@Provides 标记的方法，谷歌推荐采用 provide 为前缀。
 * <p>
 * <p>
 * 提供一些框架必须的实例的 {@link Module}
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
@Module
public abstract class AppModule {

    @Singleton
    @Provides
    static Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    @Binds
    abstract IRepositoryManager bindRepositoryManager(RepositoryManager repositoryManager);

    @Singleton
    @Provides
    static Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS);
    }

    @Binds
    @Named("ActivityLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycle(ActivityLifecycle activityLifecycle);

    @Binds
    @Named("ActivityLifecycleForRxLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycleForRxLifecycle(ActivityLifecycleForRxLifecycle activityLifecycleForRxLifecycle);

    @Binds
    abstract FragmentManager.FragmentLifecycleCallbacks bindFragmentLifecycle(FragmentLifecycle fragmentLifecycle);

    @Singleton
    @Provides
    static List<FragmentManager.FragmentLifecycleCallbacks> provideFragmentLifecycles() {
        return new ArrayList<>();
    }

    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }
}
