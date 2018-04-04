package com.dong.lib.common.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.dong.lib.common.http.GlobalHttpHandler;
import com.dong.lib.common.http.log.RequestInterceptor;
import com.dong.lib.common.utils.DataHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

/**
 * ================================================
 * {@link Inject}：注解有两个作用，
 * 1、是在需要依赖的类（下面这样的类都会称为目标类）中标记成员变量告诉Dagger这个类型的变量需要一个实例对象。
 * 2、是标记类中的构造方法，告诉Dagger我可以提供这种类型的依赖实例。
 * <p>
 * <p>
 * {@link Module}:这个注解用来标记类（一般类名以Module结尾）。
 * Module主要的作用是用来集中管理@Provide标记的方法。
 * 我们定义一个被@Module注解的类，Dagger就会知道在哪里找到依赖来满足创建类的实例。
 * modules的一个重要特征是被设计成区块并可以组合在一起。
 * （例如可以在App中看到多个组合在一起的modules）
 * <p>
 * <p>
 * {@link Provides}: 对方法进行注解，都是有返回类型的。
 * 用来告诉Dagger我们想如何创建并提供该类型的依赖实例（一般会在方法中new出实例）。
 * 用@Provides 标记的方法，谷歌推荐采用 provide 为前缀。
 * <p>
 * <p>
 * <p>
 * 提供一些三方库客户端实例的 {@link Module}
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
@Module
public abstract class ClientModule {
    private static final int TIME_OUT = 10;

    /**
     * 提供 {@link Retrofit}
     *
     * @param application
     * @param configuration
     * @param builder
     * @param client
     * @param httpUrl
     * @return {@link Retrofit}
     */
    @Singleton
    @Provides
    static Retrofit provideRetrofit(Application application,
                                    @Nullable RetrofitConfiguration configuration,
                                    Retrofit.Builder builder,
                                    OkHttpClient client,
                                    HttpUrl httpUrl) {
        builder
                .baseUrl(httpUrl)//域名
                .client(client);//设置okhttp

        if (configuration != null)
            configuration.configRetrofit(application, builder);

        builder
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用 Rxjava
                .addConverterFactory(ProtoConverterFactory.create());//使用 Proto
        return builder.build();
    }

    /**
     * 提供 {@link OkHttpClient}
     *
     * @param application
     * @param configuration
     * @param builder
     * @param intercept
     * @param interceptors
     * @param handler
     * @return {@link OkHttpClient}
     */
    @Singleton
    @Provides
    static OkHttpClient provideClient(Application application,
                                      @Nullable OkhttpConfiguration configuration,
                                      OkHttpClient.Builder builder,
                                      Interceptor intercept,
                                      @Nullable List<Interceptor> interceptors,
                                      @Nullable GlobalHttpHandler handler) {
        builder
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept);

        /**
         *  如果想在网络请求前后对数据进行处理可以传入
         *  需要调用 {@link GlobalConfigModule.Builder#globalHttpHandler(GlobalHttpHandler handler)}
         */
        if (handler != null)
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(handler.onHttpRequestBefore(chain, chain.request()));
                }
            });

        /**
         *  如果外部提供了interceptor的集合则遍历添加
         *  需要调用 {@link GlobalConfigModule.Builder#addInterceptor(Interceptor interceptor)}
         */
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        /**
         *  如果想在自己的模块实现更多的配置
         *  需要调用 {@link GlobalConfigModule.Builder#okhttpConfiguration(OkhttpConfiguration okhttpConfiguratio)}
         */
        if (configuration != null)
            configuration.configOkhttp(application, builder);
        return builder.build();
    }

    @Singleton
    @Provides
    static Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    static OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Binds
    abstract Interceptor bindInterceptor(RequestInterceptor interceptor);

    /**
     * 提供 {@link RxCache}
     *
     * @param application
     * @param configuration
     * @param cacheDirectory cacheDirectory RxCache缓存路径
     * @return {@link RxCache}
     */
    @Singleton
    @Provides
    static RxCache provideRxCache(Application application, @Nullable RxCacheConfiguration configuration, @Named("RxCacheDirectory") File cacheDirectory) {
        RxCache.Builder builder = new RxCache.Builder();
        RxCache rxCache = null;
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder);
        }
        if (rxCache != null) return rxCache;
        return builder
                .persistence(cacheDirectory, new GsonSpeaker());
    }

    /**
     * 需要单独给 {@link RxCache} 提供缓存路径
     *
     * @param cacheDir
     * @return {@link File}
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    static File provideRxCacheDirectory(File cacheDir) {
        File cacheDirectory = new File(cacheDir, "RxCache");
        return DataHelper.makeDirs(cacheDirectory);
    }

    /**
     * 提供处理 RxJava 错误的管理器
     *
     * @param application
     * @param listener
     * @return {@link RxErrorHandler}
     */
    @Singleton
    @Provides
    static RxErrorHandler proRxErrorHandler(Application application, ResponseErrorListener listener) {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErrorListener(listener)
                .build();
    }

    public interface RetrofitConfiguration {
        void configRetrofit(Context context, Retrofit.Builder builder);
    }

    public interface OkhttpConfiguration {
        void configOkhttp(Context context, OkHttpClient.Builder builder);
    }

    public interface RxCacheConfiguration {
        /**
         * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson
         * 请 {@code return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());}, 否则请 {@code return null;}
         *
         * @param context
         * @param builder
         * @return {@link RxCache}
         */
        RxCache configRxCache(Context context, RxCache.Builder builder);
    }
}