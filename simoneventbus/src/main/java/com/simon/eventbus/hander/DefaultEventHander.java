package com.simon.eventbus.hander;


import com.simon.eventbus.Subscription;

import java.lang.reflect.InvocationTargetException;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 *
 * 事件处理线程，事件发送在哪个线程，就执行在哪个线程
 */

public class DefaultEventHander implements EventHandler {

    @Override
    public void handleEvent(Subscription subscription, Object event) {
        if(subscription==null||subscription.subscriber.get()==null)
            return;
        try {
            //执行
            subscription.targetMethod.invoke(subscription.subscriber.get(),event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }



}