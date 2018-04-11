package com.dong.module.knowledge.handler.customHandler;

/**
 * ================================================
 * 自定义Looper
 * Created by KotlinD on 2018/4/10.
 * <p>
 * ================================================
 */
public class Looper {
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();

    final MessageQueue mQueue;

    private Looper() {
        mQueue = new MessageQueue();
    }

    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper());
    }

    public static Looper myLooper() {
        return sThreadLocal.get();
    }

    public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;

        for (; ; ) {
            Message msg = queue.next(); // might block
            if (msg != null) {
                msg.target.dispatchMessage(msg);
            }
        }
    }

}
