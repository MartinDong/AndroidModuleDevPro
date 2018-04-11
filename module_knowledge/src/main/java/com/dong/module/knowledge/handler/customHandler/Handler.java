package com.dong.module.knowledge.handler.customHandler;

import android.util.Log;

/**
 * ================================================
 * 自定义的Handler
 * Created by KotlinD on 2018/4/10.
 * <p>
 * ================================================
 */
public class Handler {

    final Looper mLooper;
    final MessageQueue mQueue;

    public Handler() {
        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
    }

    public void handleMessage(Message msg) {
    }

    public void dispatchMessage(Message msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            handleMessage(msg);
        }
    }

    private static void handleCallback(Message message) {
        message.callback.run();
    }

    public final boolean sendMessage(Message msg) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            RuntimeException e = new RuntimeException(
                    this + " sendMessageAtTime() called with no mQueue");
            Log.w("Looper", e.getMessage(), e);
            return false;
        }
        return enqueueMessage(queue, msg);
    }

    private boolean enqueueMessage(MessageQueue queue, Message msg) {
        msg.target = this;
        return queue.enqueueMessage(msg);
    }
}
