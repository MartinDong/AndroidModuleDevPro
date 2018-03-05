package com.dong.lib.common.utils;

import com.dong.lib.common.BuildConfig;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * <p>自己实现的日志记录策略</P>
 * Created by xiaoyulaoshi on 2018/3/5.
 */

public class MyAndroidLogAdapter implements LogAdapter {

    private final FormatStrategy formatStrategy;

    public MyAndroidLogAdapter() {
        this.formatStrategy = PrettyFormatStrategy.newBuilder().tag("QYQ_LOG").build();
    }

    public MyAndroidLogAdapter(FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return BuildConfig.LOG_DEBUG;
    }

    @Override
    public void log(int priority, String tag, String message) {
        formatStrategy.log(priority, tag, message);
    }
}