package com.simon.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscriber {
    /**
     * 默认的tag
     * @return
     */
    String tag() default EventType.DEFAULT_TAG;

    /**
     * 默认在发布的线程中执行
     */
    Threadmode mode() default Threadmode.Post;

}
