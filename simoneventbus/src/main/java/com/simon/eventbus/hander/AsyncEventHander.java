package com.simon.eventbus.hander;


import android.os.Handler;
import android.os.HandlerThread;

import com.simon.eventbus.Subscription;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 * 事件处理线程，事件执行在异步线程
 */

public class AsyncEventHander implements EventHandler {

    private DispatchThread mDispatchThread=new DispatchThread("mDispatchThread");
    /**
     * 事件处理线程
     */
    private EventHandler mEventHandler=new DefaultEventHander();


    public AsyncEventHander() {
        this.mDispatchThread = new DispatchThread("mDispatchThread");
        mDispatchThread.start();
    }

    @Override
    public void handleEvent(final Subscription subscription,final Object event) {
        if(subscription==null||subscription.subscriber.get()==null)
            return;
            //执行
        mDispatchThread.post(new Runnable() {
             @Override
             public void run() {
               mEventHandler.handleEvent(subscription,event);
             }
         });
    }

    /**
     * 异步线程的执行器
     */
    private class DispatchThread extends HandlerThread{
        private Handler mAsyncHandler;

        public DispatchThread(String name) {
            super(name);
        }

        @Override
        public synchronized void start() {
            super.start();
            mAsyncHandler=new Handler(this.getLooper());
        }

        public void post(Runnable runnable) {
            if (mAsyncHandler == null) {
                throw new NullPointerException("mAsyncHandler == null, please call start() first.");
            }
            mAsyncHandler.post(runnable);
        }
    }

}