package com.simon.eventbus.hander;


import com.simon.eventbus.Subscription;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public interface EventHandler {
    /**
     * 处理事件
     *
     * @param subscription 订阅对象
     * @param event 待处理的事件
     */
    void handleEvent(Subscription subscription, Object event);

}
