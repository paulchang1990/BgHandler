package com.paul.handlerthreaddemo.process;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import java.util.LinkedList;

/**
 * use HandlerThread process the simple time-cost runnable on a background thread.
 * 使用HandlerThread类在一个子线程中处理简单的略耗时操作，如本地化小数据量的读写，以防止阻塞，
 * 不可进行如网络请求等阻塞式的耗时操作。
 * Created by Paul Chang on 2015/12/4.
 */
public class RunnableHandler {

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private final LinkedList<Runnable> mRunnables = new LinkedList<>();

    public RunnableHandler(@NonNull HandlerThread handlerThread) {
        this.mHandlerThread = handlerThread;
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setPriority(int priority) {
        mHandlerThread.setPriority(priority);
    }

    public RunnableHandler() {
        mHandlerThread = new HandlerThread("runnable_handler", android.os.Process
                .THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public void postRunnable(Runnable runnable) {
        mHandler.post(runnable);
        mRunnables.add(runnable);
    }

    public void postAtFrontOfQueue(Runnable runnable) {
        mHandler.postAtFrontOfQueue(runnable);
        mRunnables.addFirst(runnable);
    }

    public void postAtTime(Runnable runnable, long uptimeMillis) {
        mHandler.postAtTime(runnable, uptimeMillis);
        mRunnables.add(runnable);
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
        mRunnables.add(runnable);
    }

    public void removeRunnable(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
        mRunnables.remove(runnable);
    }

    public void removeAllRunnable() {
        for (Runnable runnable : mRunnables) {
            mHandler.removeCallbacks(runnable);
        }
        mRunnables.clear();
    }

    public void quit(){
        mHandlerThread.quit();
    }
}
