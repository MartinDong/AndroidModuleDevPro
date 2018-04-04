package com.dong.lib.common.http.imageloader;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ================================================
 * {@link ImageLoader} 使用策略模式和建造者模式,可以动态切换图片请求框架(比如说切换成 Picasso )
 * 当需要切换图片请求框架或图片请求框架升级后变更了 Api 时
 * 这里可以将影响范围降到最低,所以封装 {@link ImageLoader} 是为了屏蔽这个风险
 * <p>
 * Created by xiaoyulaoshi on 2018/3/21.
 * <p>
 * ================================================
 */
@Singleton
public final class ImageLoader {
    @Inject
    BaseImageLoaderStrategy mStrategy;

    @Inject
    public ImageLoader() {
    }


    /**
     * 加载图片
     *
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void loadImage(Context context, T config) {
        this.mStrategy.loadImage(context, config);
    }

    /**
     * 停止加载或清理缓存
     *
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void clear(Context context, T config) {
        this.mStrategy.clear(context, config);
    }

    /**
     * 可在运行时随意切换 {@link BaseImageLoaderStrategy}
     *
     * @param strategy
     */
    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }

    public BaseImageLoaderStrategy getLoadImgStrategy() {
        return mStrategy;
    }
}
