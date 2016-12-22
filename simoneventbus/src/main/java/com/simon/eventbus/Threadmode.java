package com.simon.eventbus;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public enum  Threadmode {
    /**
     * 在发布的线程执行
     */
    Post,
    /**
     * 在主线程执行
     */
    Main,
    /**
     * 在异步线程执行
     */
    Async

}
