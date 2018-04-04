package com.dong.lib.common.di.module;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.dong.lib.common.http.BaseUrl;
import com.dong.lib.common.http.GlobalHttpHandler;
import com.dong.lib.common.http.imageloader.BaseImageLoaderStrategy;
import com.dong.lib.common.http.imageloader.glide.GlideImageLoaderStrategy;
import com.dong.lib.common.http.log.DefaultFormatPrinter;
import com.dong.lib.common.http.log.FormatPrinter;
import com.dong.lib.common.http.log.RequestInterceptor;
import com.dong.lib.common.integration.cache.Cache;
import com.dong.lib.common.integration.cache.CacheType;
import com.dong.lib.common.integration.cache.LruCache;
import com.dong.lib.common.utils.DataHelper;
import com.dong.lib.common.utils.Preconditions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

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
 * 框架独创的建造者模式 {@link Module},可向框架中注入外部配置的自定义参数
 * <p>
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
@Module
public class GlobalConfigModule {
    private HttpUrl mApiUrl;
    private BaseUrl mBaseUrl;
    private BaseImageLoaderStrategy mLoaderStrategy;
    private GlobalHttpHandler mHandler;
    private List<Interceptor> mInterceptors;
    private ResponseErrorListener mErrorListener;
    private File mCacheFile;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkhttpConfiguration mOkhttpConfiguration;
    private ClientModule.RxCacheConfiguration mRxCacheConfiguration;
    private AppModule.GsonConfiguration mGsonConfiguration;
    private RequestInterceptor.Level mPrintHttpLogLevel;
    private FormatPrinter mFormatPrinter;
    private Cache.Factory mCacheFactory;

    private GlobalConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mBaseUrl = builder.baseUrl;
        this.mLoaderStrategy = builder.loaderStrategy;
        this.mHandler = builder.handler;
        this.mInterceptors = builder.interceptors;
        this.mErrorListener = builder.responseErrorListener;
        this.mCacheFile = builder.cacheFile;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkhttpConfiguration = builder.okhttpConfiguration;
        this.mRxCacheConfiguration = builder.rxCacheConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;
        this.mPrintHttpLogLevel = builder.printHttpLogLevel;
        this.mFormatPrinter = builder.formatPrinter;
        this.mCacheFactory = builder.cacheFactory;
    }

    public static Builder builder() {
        return new Builder();
    }


    @Singleton
    @Provides
    @Nullable
    List<Interceptor> provideInterceptors() {
        return mInterceptors;
    }


    /**
     * 提供 BaseUrl,默认使用 <"https://api.github.com/">
     *
     * @return
     */
    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        if (mBaseUrl != null) {
            HttpUrl httpUrl = mBaseUrl.url();
            if (httpUrl != null) {
                return httpUrl;
            }
        }
        return mApiUrl == null ? HttpUrl.parse("https://api.github.com/") : mApiUrl;
    }


    /**
     * 提供图片加载框架,默认使用 {@link Glide}
     *
     * @return
     */
    @Singleton
    @Provides
    BaseImageLoaderStrategy provideImageLoaderStrategy() {
        return mLoaderStrategy == null ? new GlideImageLoaderStrategy() : mLoaderStrategy;
    }


    /**
     * 提供处理 Http 请求和响应结果的处理类
     *
     * @return
     */
    @Singleton
    @Provides
    @Nullable
    GlobalHttpHandler provideGlobalHttpHandler() {
        return mHandler;
    }


    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return mCacheFile == null ? DataHelper.getCacheFile(application) : mCacheFile;
    }


    /**
     * 提供处理 RxJava 错误的管理器的回调
     *
     * @return
     */
    @Singleton
    @Provides
    ResponseErrorListener provideResponseErrorListener() {
        return mErrorListener == null ? ResponseErrorListener.EMPTY : mErrorListener;
    }


    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkhttpConfiguration provideOkhttpConfiguration() {
        return mOkhttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return mRxCacheConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    AppModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    RequestInterceptor.Level providePrintHttpLogLevel() {
        return mPrintHttpLogLevel == null ? RequestInterceptor.Level.ALL : mPrintHttpLogLevel;
    }

    @Singleton
    @Provides
    FormatPrinter provideFormatPrinter() {
        return mFormatPrinter == null ? new DefaultFormatPrinter() : mFormatPrinter;
    }

    @Singleton
    @Provides
    Cache.Factory provideCacheFactory(Application application) {
        return mCacheFactory == null ? new Cache.Factory() {
            @NonNull
            @Override
            public Cache build(CacheType type) {
                //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
                //并使用 GlobalConfigModule.Builder#cacheFactory() 扩展
                return new LruCache(type.calculateCacheSize(application));
            }
        } : mCacheFactory;
    }


    public static final class Builder {
        private HttpUrl apiUrl;
        private BaseUrl baseUrl;
        private BaseImageLoaderStrategy loaderStrategy;
        private GlobalHttpHandler handler;
        private List<Interceptor> interceptors;
        private ResponseErrorListener responseErrorListener;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkhttpConfiguration okhttpConfiguration;
        private ClientModule.RxCacheConfiguration rxCacheConfiguration;
        private AppModule.GsonConfiguration gsonConfiguration;
        private RequestInterceptor.Level printHttpLogLevel;
        private FormatPrinter formatPrinter;
        private Cache.Factory cacheFactory;

        private Builder() {
        }

        public Builder baseurl(String baseUrl) {//基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw new NullPointerException("BaseUrl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseurl(BaseUrl baseUrl) {
            this.baseUrl = Preconditions.checkNotNull(baseUrl, BaseUrl.class.getCanonicalName() + "can not be null.");
            return this;
        }

        public Builder imageLoaderStrategy(BaseImageLoaderStrategy loaderStrategy) {//用来请求网络图片
            this.loaderStrategy = loaderStrategy;
            return this;
        }

        public Builder globalHttpHandler(GlobalHttpHandler handler) {//用来处理http响应结果
            this.handler = handler;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {//动态添加任意个interceptor
            if (interceptors == null)
                interceptors = new ArrayList<>();
            this.interceptors.add(interceptor);
            return this;
        }


        public Builder responseErrorListener(ResponseErrorListener listener) {//处理所有RxJava的onError逻辑
            this.responseErrorListener = listener;
            return this;
        }


        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okhttpConfiguration(ClientModule.OkhttpConfiguration okhttpConfiguration) {
            this.okhttpConfiguration = okhttpConfiguration;
            return this;
        }

        public Builder rxCacheConfiguration(ClientModule.RxCacheConfiguration rxCacheConfiguration) {
            this.rxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        public Builder gsonConfiguration(AppModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public Builder printHttpLogLevel(RequestInterceptor.Level printHttpLogLevel) {//是否让框架打印 Http 的请求和响应信息
            this.printHttpLogLevel = Preconditions.checkNotNull(printHttpLogLevel, "The printHttpLogLevel can not be null, use RequestInterceptor.Level.NONE instead.");
            return this;
        }

        public Builder formatPrinter(FormatPrinter formatPrinter) {
            this.formatPrinter = Preconditions.checkNotNull(formatPrinter, FormatPrinter.class.getCanonicalName() + "can not be null.");
            return this;
        }

        public Builder cacheFactory(Cache.Factory cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }

    }
}
