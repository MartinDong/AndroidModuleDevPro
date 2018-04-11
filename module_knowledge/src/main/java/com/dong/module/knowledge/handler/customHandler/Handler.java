package com.dong.module.knowledge.handler.customHandler;

import android.os.SystemClock;
import android.util.Log;

/**
 * ================================================
 * 自定义的Handler
 * Created by KotlinD on 2018/4/10.
 * <p>
 * ================================================
 */
public class Handler {

    //持有当前线程的Looper
    final Looper mLooper;
    //持有Looper中的MessageQueue消息队列，sendMessage需要向队列插入消息
    final MessageQueue mQueue;

    public Handler() {
        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
    }

    /**
     * 子类需要重写这个方法写入自己的处理逻辑
     *
     * @param msg Message消息
     */
    public void handleMessage(Message msg) {
    }

    /**
     * 调度消息
     *
     * @param msg Message消息
     */
    public void dispatchMessage(Message msg) {
        //这里为了简单说明原理与原码有些不一样，删减了一些判断逻辑
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            handleMessage(msg);
        }
    }

    /**
     * 处理回调
     *
     * @param message Message消息
     */
    private static void handleCallback(Message message) {
        //取出Message中 Runnable 进行运行
        message.callback.run();
    }


    /**
     * 发送一条Message消息
     *
     * @param msg 要发送的Message消息
     * @return 是否发送成功
     */
    public final boolean sendMessage(Message msg) {
        return sendMessageDelayed(msg, 0);
    }

    /**
     * 发送延迟消息
     *
     * @param msg         要发送的Message消息
     * @param delayMillis 要延迟的时间
     * @return 是否发送成功
     */
    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        //这里很巧妙的将消息的时间点进行延长，从而达到了延迟发送的效果
        return sendMessageAtTime(msg, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 发送消息所对应的时间
     *
     * @param msg          要发送的Message消息
     * @param uptimeMillis 对应的消息所在的时间点
     * @return
     */
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        //获取当前线程对应的Looper中的MessageQueue
        MessageQueue queue = mQueue;
        if (queue == null) {
            RuntimeException e = new RuntimeException(
                    this + " sendMessageAtTime() called with no mQueue");
            Log.w("Looper", e.getMessage(), e);
            return false;
        }
        //消息压入队列操作
        return enqueueMessage(queue, msg, uptimeMillis);
    }

    /**
     * 消息压入消息队列操作
     *
     * @param queue        当前Handler对应的消息队列
     * @param msg          要发送的Message消息
     * @param uptimeMillis 压入队列的时间，系统的实习会对这个时间进行排序，从而保证消息的有序出栈 {@link MessageQueue#next()}
     * @return 是否压入队列成功
     */
    private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;
        return queue.enqueueMessage(msg, uptimeMillis);
    }
}
