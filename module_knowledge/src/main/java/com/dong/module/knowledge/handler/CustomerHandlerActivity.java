package com.dong.module.knowledge.handler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dong.module.knowledge.R;
import com.dong.module.knowledge.handler.customHandler.Handler;
import com.dong.module.knowledge.handler.customHandler.Looper;
import com.dong.module.knowledge.handler.customHandler.Message;

import java.lang.ref.WeakReference;

/**
 * ================================================
 * 测试自定义Handler机制
 * Created by KotlinD on 2018/4/9.
 * <p>
 * ================================================
 */
@Route(path = "/knowledge/customer_handler")
public class CustomerHandlerActivity extends AppCompatActivity {
    private static final int UPDATE = 0x1;

    //默认是在主线程中执行
    private MyHandler mHandler = null;

    //继承实现自己的Handler，处理子线程与主线程的通讯交互
    static class MyHandler extends Handler {
        //使用弱引用，防止内存泄露
        private final WeakReference<CustomerHandlerActivity> mActivity;

        private MyHandler(CustomerHandlerActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        //重写handleMessage进行处理接收到的Message
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获取引用的UI主线程Activity，用来获取UI线程的控件等
            CustomerHandlerActivity activity = mActivity.get();
            if (activity != null) {
                //分发处理消息
                switch (msg.what) {
                    case UPDATE:
                        //打印Log
                        Log.i("TAG", "还有 " + String.valueOf(msg.arg1) + " 秒");
                        break;
                }
            }
        }
    }

    //模拟子线程进行耗时任务
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //这里做的是一个倒计时定时一秒发送一次数据
            for (int i = 60; i > 0; i--) {
                //构建属于子线程的Message
                Message msg = new Message();
                msg.what = UPDATE;
                msg.arg1 = i;

                //通过主线程中的Handler实例进行消息发送，将子线程中的消息发送到主线程中
                mHandler.sendMessage(msg);
                try {
                    //休眠1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        //在当前的线程中准备一个消息轮询器
        Looper.prepare();

        //创建主线程的Handler
        mHandler = new MyHandler(this);

        //开启子线程运行耗时操作
        new Thread(mRunnable).start();

        //这里开启消息轮询器会造成UI的绘制阻塞属于正常现象
        Looper.loop();
    }
}