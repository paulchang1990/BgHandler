/*
 * Copyright 2015 Paul Chang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paul.bghandler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * 使用HandlerThread来进行异步信息传递，通过该Handler在其他线程发送Message来在该子线程中进行相应的响应
 * 处理。
 * Created by Paul Chang on 2015/12/4.
 */
public class MessageHandler {
    private static final String TAG = "MsgHandler";
    private static final boolean DEBUG = false;
    private HandlerThread mHandlerThread;
    private MsgHandler mHandler;
    private OnMessageHandledListener mListener;
    private boolean mQuit;//this flag to void the exception:java.lang.RuntimeException:
    // Handler sending message to a Handler on a dead thread

    public MessageHandler(@NonNull HandlerThread handlerThread) {
        this.mHandlerThread = handlerThread;
        mHandlerThread.start();
        mHandler = new MsgHandler(mHandlerThread.getLooper());
    }

    public boolean isQuit() {
        return mQuit;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void quit() {
        mHandlerThread.quit();
        mQuit = true;
    }

    public void setPriority(int priority) {
        mHandlerThread.setPriority(priority);
    }

    public void sendEmptyMessage(int what) {
        if (!mQuit) {
            mHandler.sendEmptyMessage(what);
        } else {
            Log.w(TAG, "Warning:catch a runtime exception: Handler sending message to a " +
                    "Handler on a dead thread");
        }
    }

    public void sendMessageAtFrontOfQueue(Message msg) {
        if (!mQuit) {
            mHandler.sendMessageAtFrontOfQueue(msg);
        } else {
            Log.w(TAG, "Warning:catch a runtime exception: Handler sending message to a " +
                    "Handler on a dead thread");
        }
    }

    public void sendMessage(Message msg) {

        if (!mQuit) {
            mHandler.sendMessage(msg);
        } else {
            Log.w(TAG, "Warning:catch a runtime exception: Handler sending message to a " +
                    "Handler on a dead thread");
        }
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
