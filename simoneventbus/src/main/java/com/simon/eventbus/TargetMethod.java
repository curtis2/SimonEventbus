package com.simon.eventbus;


import java.lang.reflect.Method;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class TargetMethod {
    /**
     * 目标订阅方法对象
     */
    public Method method;
    /**
     *事件类型
     */
    public EventType eventType;

    /**
     *处理事件的线程模型
     */
    public Threadmode threadmode ;

    public TargetMethod(Method method, EventType eventType, Threadmode threadmode) {
        this.method = method;
        this.eventType = eventType;
        this.threadmode = threadmode;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + ((method == null) ? 0 : method.getName().hashCode());
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
        TargetMethod other = (TargetMethod) obj;
        if (eventType == null) {
            if (other.eventType != null)
                return false;
        } else if (!eventType.equals(other.eventType))
            return false;
        if (method == null) {
            if (other.method != null)
                return false;
        } else if (!method.getName().equals(other.method.getName()))
            return false;
        return true;
    }

}
