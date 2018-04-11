package com.dong.module.knowledge.handler.customHandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ================================================
 * 自定义消息队列
 * Created by KotlinD on 2018/4/10.
 * <p>
 * ================================================
 */
public class MessageQueue {
    private static final int MAX_QUEUE_SIZE = 50;

    //这里使用集合进行模拟Native层的队列，还没有看到C++层的代码
    final BlockingQueue<Message> mMessages;

    MessageQueue() {
        mMessages = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
    }

    Message next() {
        Message msg = null;

        try {
            msg = mMessages.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return msg;
    }

    boolean enqueueMessage(Message msg) {
        try {
            mMessages.put(msg);

            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
