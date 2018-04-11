package com.dong.module.knowledge.handler.customHandler;

/**
 * ================================================
 * 自定义Looper
 * Created by KotlinD on 2018/4/10.
 * <p>
 * ================================================
 */
public class Looper {
    //静态常量，整个APP共享这一个
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();

    //跟当前的线程持有的Looper绑定的消息队列
    final MessageQueue mQueue;

    private Looper() {
        //初始化消息队列
        mQueue = new MessageQueue();
    }

    /**
     * 在当前的线程中准备一个消息轮询器
     */
    public static void prepare() {
        //一个线程只能对应一个轮询器
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        //向ThreadLocal添加一个轮询器
        sThreadLocal.set(new Looper());
    }

    /**
     * 返回当前线程对应的轮询器
     *
     * @return Looper
     */
    public static Looper myLooper() {
        return sThreadLocal.get();
    }

    /**
     * 开启轮询
     */
    public static void loop() {
        //获取当前线程的轮询器
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        //获取轮询器对应的消息队列
        final MessageQueue queue = me.mQueue;

        //永真循环不断地去取消息队列中的消息
        for (; ; ) {
            //取出消息队列中Message消息
            Message msg = queue.next(); // might block

            // 由于我们这里采用的是java帮我们实现的BlockingQueue，
            // 这里跟系统的实现判断有些不一样
            if (msg != null) {
                //将取出消息给发出当前消息的Handler的dispatchMessage进行消息的调度
                msg.target.dispatchMessage(msg);
            }
        }
    }
}
