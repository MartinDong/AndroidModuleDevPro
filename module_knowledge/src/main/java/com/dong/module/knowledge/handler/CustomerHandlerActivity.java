package com.dong.module.knowledge.handler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dong.module.knowledge.R;
import com.dong.module.knowledge.handler.customHandler.Handler;
import com.dong.module.knowledge.handler.customHandler.Looper;
import com.dong.module.knowledge.handler.customHandler.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ================================================
 * 类说明
 * Created by KotlinD on 2018/4/9.
 * <p>
 * ================================================
 */
@Route(path = "/knowledge/customer_handler")
public class CustomerHandlerActivity extends AppCompatActivity {
    public Button btnStartCountDown;

    public TextView tvCountDown;

    private static final int UPDATE = 0x1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_handler);

        btnStartCountDown = findViewById(R.id.btn_start_count_down);
        tvCountDown = findViewById(R.id.tv_count_down);

        btnStartCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //模拟系统的线程Looper的开启
                Looper.prepare();

                ExecutorService exe = Executors.newCachedThreadPool();

                final Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case UPDATE:
                                tvCountDown.setText("还有" + String.valueOf(msg.obj) + "秒");

                                //打印Log
                                Log.i("TAG", "当前接收到线程 " + msg.obj + " 的消息");
                                break;
                        }
                    }
                };

                for (int i = 3; i > 0; i--) {
                    exe.execute(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                Message msg = new Message();
                                msg.what = UPDATE;
                                msg.obj = Thread.currentThread().getId();
                                mHandler.sendMessage(msg);

                                try {
                                    //休眠1秒
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }

                Looper.loop();
            }
        });

    }

}
