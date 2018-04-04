package com.dong.lib.common.di.component;

import android.app.Application;
import android.content.Context;

import com.dong.lib.common.base.delegate.AppDelegate;
import com.dong.lib.common.di.module.AppModule;
import com.dong.lib.common.di.module.ClientModule;
import com.dong.lib.common.di.module.GlobalConfigModule;
import com.dong.lib.common.http.imageloader.ImageLoader;
import com.dong.lib.common.integration.AppManager;
import com.dong.lib.common.integration.IRepositoryManager;
import com.dong.lib.common.integration.cache.Cache;
import com.dong.lib.common.utils.Utils;
import com.google.gson.Gson;

import java.io.File;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import okhttp3.OkHttpClient;

/**
 * ================================================
 * Component:是组件，也可以称为注入器。
 * 是@Inject和@Module之间的桥梁，主要职责是把二者组合在一起。
 * <p>
 * Component 注解用来标记接口或者抽象类。
 * 所有的components都可以通过它的 modules 知道它所提供的依赖范围。
 * 一个Component可以依赖一个或多个Component，并拿到被依赖Component暴露出来的实例，
 * Component的 dependencies 属性就是确定依赖关系的实现。
 * <p>
 * 可通过 {@link Utils#obtainAppComponentFromContext(Context)} 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    //Application单例
    Application application();

    //用于管理所有 activity
    AppManager appManager();

    //用于管理网络请求层,以及数据缓存层
    IRepositoryManager repositoryManager();

    //RxJava 错误处理管理类
    RxErrorHandler rxErrorHandler();

    //图片管理器,用于加载图片的管理类,默认使用 Glide ,使用策略模式,可在运行时替换框架
    ImageLoader imageLoader();

    OkHttpClient okHttpClient();

    //gson
    Gson gson();

    //缓存文件根目录(RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下),应该将所有缓存都放到这个根目录下,便于管理和清理,可在 GlobalConfigModule 里配置
    File cacheFile();

    //用来存取一些整个App公用的数据,切勿大量存放大容量数据
    Cache<String, Object> extras();

    //用于创建框架所需缓存对象的工厂
    Cache.Factory cacheFactory();

    void inject(AppDelegate delegate);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder globalConfigModule(GlobalConfigModule globalConfigModule);

        AppComponent build();
    }
}
