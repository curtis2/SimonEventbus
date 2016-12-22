package com.simon.eventbus;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class Subscription {
    /**
     * 订阅者对象
     */
    public Reference<Object> subscriber;

    /**
     * 事件类型对象
     */
    public EventType eventType;

    /**
     * 订阅者方法
     */
    public Method targetMethod;

    /**
     * 线程模型对象
     */
    public Threadmode threadmode;

    public Subscription(Object subscriber, TargetMethod method) {
        this.subscriber = new WeakReference<>(subscriber);
        this.targetMethod = method.method;
        this.eventType = method.eventType;
        this.threadmode =method.threadmode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subscriber == null) ? 0 : subscriber.hashCode());
        result = prime * result + ((targetMethod == null) ? 0 : targetMethod.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Subscription other = (Subscription) obj;
        if (subscriber.get() == null) {
            if (other.subscriber.get() != null)
                return false;
        } else if (!subscriber.get().equals(other.subscriber.get()))
            return false;
        if (targetMethod == null) {
            if (other.targetMethod != null)
                return false;
        } else if (!targetMethod.equals(other.targetMethod))
            return false;
        return true;
    }
}
