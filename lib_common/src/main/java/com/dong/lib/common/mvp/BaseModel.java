package com.dong.lib.common.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.dong.lib.common.integration.IRepositoryManager;

/**
 * <p>基类 Model</P>
 * Created by xiaoyulaoshi on 2018/3/20.
 */
public class BaseModel implements IModel, LifecycleObserver {

    //用于管理网络请求，以及数据缓存
    protected IRepositoryManager mRepositoryManager;

    public BaseModel(IRepositoryManager repositoryManager) {
        this.mRepositoryManager = repositoryManager;
    }

    /**
     * 在框架 {@link BasePresenter#onDestroy()} 时会调用 {@link IModel#onDestroy()}
     */
    @Override
    public void onDestroy() {
        mRepositoryManager = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
