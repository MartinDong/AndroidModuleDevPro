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

    // 这里使用BlockingQueue集合进行模拟Native层的队列
    // 后续在进行讲解Android中的C++如何实现的Message队列操作
    final BlockingQueue<Message> mMessages;

    MessageQueue() {
        //创建固定大小的消息队列
        mMessages = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
    }

    /**
     * 取出队列中的下一个消息
     *
     * @return Message消息
     */
    Message next() {
        Message msg = null;
        try {
            //从队列中取出头部的消息，并从队列中移除，通知 {@link BlockingQueue#put()} 可以入栈
            msg = mMessages.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //将取出的消息返回
        return msg;
    }

    /**
     * 消息压入队列操作
     *
     * @param msg  要操作的消息
     * @param when 压入的时间
     * @return 是否要入成功
     */
    boolean enqueueMessage(Message msg, long when) {
        try {
            //压入消息队列，如果消息队列处于饱和状态，这里则会出现 block,直到 有调用 {@link BlockingQueue#take()}
            mMessages.put(msg);

            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
