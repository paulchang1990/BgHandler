package com.paul.handlerthreaddemo;

import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.paul.handlerthreaddemo.process.MessageHandler;
import com.paul.handlerthreaddemo.process.RunnableHandler;

import java.util.concurrent.atomic.AtomicInteger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MessageHandler
        .OnMessageHandledListener {

    private static final int BG_FINISH = 1;
    private static final int MAIN_FINISH = BG_FINISH << 1;
    @InjectView(R.id.tv_main_notice)
    TextView tvMainNotice;
    @InjectView(R.id.tv_bg_notice)
    TextView tvBgNotice;
    private RunnableHandler mRunnableHandler;
    private MessageHandler mMessageHandler;

    private static final String TAG = "MainActivity";
    private final AtomicInteger atomInt = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mRunnableHandler = new RunnableHandler();
        mMessageHandler = new MessageHandler();
        mMessageHandler.setOnMessageHandledListener(this);
    }

    @OnClick(R.id.btn_bg)
    public void handlerThreadProcess() {
        mRunnableHandler.postRunnable(mBgRunnable);
    }

    @OnClick(R.id.btn_main)
    public void mainThreadProcess() {
        mRunnableHandler.postAtFrontOfQueue(mMainRunnable);
    }

    Runnable mMainRunnable = new Runnable() {
        @Override
        public void run() {
            if (BuildConfig.DEBUG) {
                for (int i = 0; i < 5; i++) {
                    Log.d(TAG, "mainThread---" + String.valueOf(atomInt.incrementAndGet()));
                    SystemClock.sleep(1000);
                }
                mMessageHandler.sendEmptyMessage(MAIN_FINISH);
            }
        }
    };

    Runnable mBgRunnable = new Runnable() {
        @Override
        public void run() {
            if (BuildConfig.DEBUG) {
                for (int i = 0; i < 5; i++) {
                    Log.d(TAG, "bgThread---" + String.valueOf(atomInt.incrementAndGet()));
                    SystemClock.sleep(1000);
                }
                mMessageHandler.sendEmptyMessage(BG_FINISH);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mRunnableHandler.removeAllRunnable();
        mRunnableHandler.quit();//must do that in case activity leaked.
        mMessageHandler.quit();// must do that in case activity leaked.
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MAIN_FINISH:
                //do sth on bg thread.
                break;
            case BG_FINISH:
                //do sth on bg thread.
                break;
            default:
                break;
        }
    }


}
