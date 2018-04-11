package com.dong.module.knowledge.handler.customHandler;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ================================================
 * 自定义消息载体
 * Created by KotlinD on 2018/4/10.
 * <p>
 * ================================================
 */
public class Message implements Parcelable {
    //用户定义的消息代码，以便收件人可以识别此消息的内容
    public int what;
    //如果只是传递整形数据可以使用 arg1、arg2
    public int arg1;
    public int arg2;

    //如果传递复杂数据可以使用这个
    public Object obj;

    //标记当前的Message要作用在那个Handler中
    Handler target;

    //当前的Message要执行的子线程
    Runnable callback;

    // sometimes we store linked lists of these things
    Message next;

    private static final Object sPoolSync = new Object();
    private static Message sPool;

    public Message() {
    }


    /**
     * 这里是性能优化，从线程池获取一个对象，避免重新创建对象，本案例中没有使用到这个特性
     *
     * @return Message
     */
    public static Message obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                Message m = sPool;
                sPool = m.next;
                m.next = null;
                return m;
            }
        }
        return new Message();
    }

    //////////////////////////////////////下面是实现Parcelable固定的写法与逻辑关系不大/////////////////////////////////////////////
    public static final Parcelable.Creator<Message> CREATOR
            = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel source) {
            Message msg = Message.obtain();
            msg.readFromParcel(source);
            return msg;
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (callback != null) {
            throw new RuntimeException(
                    "Can't marshal callbacks across processes.");
        }
        dest.writeInt(what);
        dest.writeInt(arg1);
        dest.writeInt(arg2);
        if (obj != null) {
            try {
                Parcelable p = (Parcelable) obj;
                dest.writeInt(1);
                dest.writeParcelable(p, flags);
            } catch (ClassCastException e) {
                throw new RuntimeException(
                        "Can't marshal non-Parcelable objects across processes.");
            }
        } else {
            dest.writeInt(0);
        }
    }

    private void readFromParcel(Parcel source) {
        what = source.readInt();
        arg1 = source.readInt();
        arg2 = source.readInt();
        if (source.readInt() != 0) {
            obj = source.readParcelable(getClass().getClassLoader());
        }
    }
}
