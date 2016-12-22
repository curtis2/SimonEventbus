package com.simon.eventbus.hander;


import android.os.Handler;
import android.os.Looper;

import com.simon.eventbus.Subscription;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 * 事件处理线程，事件执行在ui线程
 */

public class UIThreadEventHandler implements EventHandler {
    /**
     * ui线程的handlder
     */
    private Handler mUIHandler=new Handler(Looper.getMainLooper());

    /**
     * 事件处理线程
     */
    private EventHandler mEventHandler=new DefaultEventHander();

    @Override
    public void handleEvent(final Subscription subscription, final Object event) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                mEventHandler.handleEvent(subscription,event);
            }
        });
    }
}
