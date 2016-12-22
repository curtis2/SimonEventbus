package com.simon.eventbus;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class EventType {
    public static final String DEFAULT_TAG="default_tag";

    /**
     * 参数类型
     */
    Class<?> paramClass;

    /**
     *事件tag类型
     */
    public String tag=DEFAULT_TAG;

    public EventType(Class<?> paramsType, String tag) {
        this.paramClass = paramsType;
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((paramClass == null) ? 0 : paramClass.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
       EventType other = (EventType) obj;
        if (paramClass == null) {
            if (other.paramClass != null)
                return false;
        } else if (!paramClass.equals(other.paramClass))
            return false;
        if (tag == null) {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        return true;
    }


}
