package com.paul.handlerthreaddemo.process;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

/**
 * 使用HandlerThread来进行异步信息传递，通过该Handler在其他线程发送Message来在该子线程中进行相应的响应
 * 处理。
 * Created by Paul Chang on 2015/12/4.
 */
public class MessageHandler {
    private HandlerThread mHandlerThread;
    private MsgHandler mHandler;
    private OnMessageHandledListener mListener;

    public MessageHandler(@NonNull HandlerThread handlerThread) {
        this.mHandlerThread = handlerThread;
        mHandlerThread.start();
        mHandler = new MsgHandler(mHandlerThread.getLooper());
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void quit() {
        mHandlerThread.quit();
    }

    public void setPriority(int priority) {
        mHandlerThread.setPriority(priority);
    }

    public void sendEmptyMessage(int what) {
        mHandler.sendEmptyMessage(what);
    }

    public void sendMessageAtFrontOfQueue(Message msg) {
        mHandler.sendMessageAtFrontOfQueue(msg);
    }

    public void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    public MessageHandler() {
        mHandlerThread = new HandlerThread("message_handler", android.os.Process
                .THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mHandler = new MsgHandler(mHandlerThread.getLooper());
    }

    public void setOnMessageHandledListener(OnMessageHandledListener listener) {
        mHandler.setOnMessageHandledListener(listener);
    }

    private static class MsgHandler extends Handler {
        private OnMessageHandledListener mListener;

        MsgHandler(Looper looper) {
            super(looper);
        }

        public void setOnMessageHandledListener(OnMessageHandledListener listener) {
            this.mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListener != null) {
                mListener.handleMessage(msg);
            }
        }
    }

    public interface OnMessageHandledListener {
        void handleMessage(Message msg);
    }
}
