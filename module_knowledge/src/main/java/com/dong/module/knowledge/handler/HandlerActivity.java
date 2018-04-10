package com.dong.module.knowledge.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dong.module.knowledge.R;

/**
 * ================================================
 * 类说明
 * Created by KotlinD on 2018/4/9.
 * <p>
 * ================================================
 */
@Route(path = "/knowledge/handler")
public class HandlerActivity extends AppCompatActivity {
    public Button btnStartCountDown;

    public TextView tvCountDown;

    private static final int UPDATE = 0x1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE:
                    tvCountDown.setText("还有"+String.valueOf(msg.arg1)+"秒");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_handler);

        btnStartCountDown = findViewById(R.id.btn_start_count_down);
        tvCountDown = findViewById(R.id.tv_count_down);

        btnStartCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                begin();
            }
        });
    }

    public void begin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 60; i > 0; i--) {
                    Message msg = new Message();
                    msg.what = UPDATE;
                    msg.arg1 = i;
                    mHandler.sendMessage(msg);

                    try {
                        //休眠1秒
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //打印Log
                    Log.i("TAG", "还有 " + i + " 秒");
                }

                finish();
            }
        }).start();
    }
}
