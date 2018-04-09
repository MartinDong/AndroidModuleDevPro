package com.dong.lib.base.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dong.lib.base.R;
import com.dong.lib.common.BuildConfig;
import com.dong.lib.common.base.delegate.AppLifecycle;
import com.dong.lib.common.di.module.GlobalConfigModule;
import com.dong.lib.common.http.HttpsUtils;
import com.dong.lib.common.http.log.RequestInterceptor;
import com.dong.lib.common.integration.ConfigModule;
import com.dong.lib.common.utils.Utils;
import com.squareup.leakcanary.RefWatcher;

import java.io.InputStream;
import java.util.List;

import me.jessyan.progressmanager.ProgressManager;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * ================================================
 * App 的全局配置信息在此配置, 需要将此实现类声明到 AndroidManifest 中
 * ConfigModule 的实现类可以有无数多个, 在 Application 中只是注册回调, 并不会影响性能 (多个 ConfigModule 在多 Module 环境下尤为受用)
 * 不过要注意 ConfigModule 接口的实现类对象是通过反射生成的, 这里会有些性能损耗
 * <p>
 * Created by xiaoyulaoshi on 2018/3/22.
 * <p>
 * ================================================
 */
@Keep
public final class GlobalConfiguration implements ConfigModule {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        // Release 时,让框架不再打印 Http 请求和响应的信息
        if (!BuildConfig.LOG_DEBUG) {
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
        }

        //自定义请求头
        Interceptor interceptorHeader = chain -> {
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("Accept", "application/x-protobuf")
                    .addHeader("Content-Type", "application/x-protobuf")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("HOST", "api.7u1.cn")
                    .build();

            return chain.proceed(request);
        };

        builder.baseurl(Api.APP_DOMAIN)
                // 设置全局的Http请求处理器
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))
                // 添加请求头的插入器
                .addInterceptor(interceptorHeader)
                // 用来处理 rxjava 中发生的所有错误,rxjava 中发生的每个错误都会回调此接口
                // Rxjava必要要使用 ErrorHandleSubscriber (默认实现 Subscriber 的 onError 方法),此监听才生效
                .responseErrorListener(new ResponseErrorListenerImpl())
                // 这里可以自己自定义配置Retrofit的参数,甚至你可以替换系统配置好的okhttp对象
                .retrofitConfiguration((context1, retrofitBuilder) -> {
                    // 比如使用fastjson替代gson
                    // retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());
                })
                // 这里可以自己自定义配置Okhttp的参数
                .okhttpConfiguration((context1, okhttpBuilder) -> {

                    InputStream stream = Utils.getApp().getResources().openRawResource(R.raw.server_ddf);

                    //使用SSL双向认证构建安全通讯信道
                    okhttpBuilder.sslSocketFactory(HttpsUtils.getSocketFactory(stream));

                    //错误重连
                    okhttpBuilder.retryOnConnectionFailure(true);

                    // 使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,
                    // 以及 Glide 加载进度监听 详细使用方法查看 https://github.com/JessYanCoding/ProgressManager
                    ProgressManager.getInstance().with(okhttpBuilder);
                    // 让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl.
                    // 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
                    // RetrofitUrlManager.getInstance().with(okhttpBuilder);
                })
                // 这里可以自己自定义配置 RxCache 的参数
                .rxCacheConfiguration((context1, rxCacheBuilder) -> {
                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                    // 想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson,
                    // 请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                    // 否则请 return null;
                    return null;
                })
        ;
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycle> lifecycles) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecycleImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        // ActivityLifecycleCallbacks 的所有方法都会在 Activity (包括三方库) 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建时重复利用已经创建的 Fragment。
                // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
                // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
                f.setRetainInstance(true);
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                ((RefWatcher) Utils
                        .obtainAppComponentFromContext(f.getActivity())
                        .extras()
                        .get(RefWatcher.class.getName()))
                        .watch(f);
            }
        });
    }
}
