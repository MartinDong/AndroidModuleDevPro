package com.dong.lib.common.mvp;

import android.content.Intent;
import android.support.annotation.NonNull;


/**
 * 框架要求框架中的每个 View 都需要实现此类,以满足规范</P>
 * <p>
 * 对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
 * Created by xiaoyulaoshi on 2018/3/20.
 */

public interface IView {

    /**
     * 显示加载视图
     * <p>
     * LoadingDialog
     * RefreshHead
     * LoadingMoreFooter
     */
    void showLoading();

    /**
     * 关闭加载视图
     */
    void hideLoading();

    /**
     * 显示提示信息
     * <p>
     * 一般是加载出现网络异常，或者是没有满足某种规则，需要进行显示的提示出来
     */
    void showMessage(@NonNull String message);

    /**
     * 跳转指定的Activity {@link android.app.Activity}
     *
     * @param intent {@code intent} 转跳页面使用的意图，用来传输数据
     */
    void launchActivity(@NonNull Intent intent);

    /**
     * 关闭自己
     */
    void killSelf();
}
