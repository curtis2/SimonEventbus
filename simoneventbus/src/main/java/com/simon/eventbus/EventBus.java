package com.simon.eventbus;

import android.util.Log;

import com.simon.eventbus.hander.AsyncEventHander;
import com.simon.eventbus.hander.DefaultEventHander;
import com.simon.eventbus.hander.EventHandler;
import com.simon.eventbus.hander.UIThreadEventHandler;
import com.simon.eventbus.matchpolicy.DefaultMatchPolicy;
import com.simon.eventbus.matchpolicy.MatchPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class EventBus{
    /**
     * default descriptor
     */
    private static final String DESCRIPTOR = EventBus.class.getSimpleName();
    /**
     * 事件总线描述符描述符
     */
    private String mDesc = DESCRIPTOR;

    /**
     * 存储的订阅者集合（是一个可以并发访问的）
     */
    private final Map<EventType, CopyOnWriteArrayList<Subscription>> mSubscriberMap =new ConcurrentHashMap<>();

    private SubsciberMethodHunter mSubsciberMethodHunter=new SubsciberMethodHunter(mSubscriberMap);

    /**
     * 事件队列， 每一个线程对象有一份
     */
    private  ThreadLocal<Queue<EventType>> mLocalEvent=new ThreadLocal<>();

    /**
     * 事件分发器
     */
    EventDispatcher mDispatcher=new EventDispatcher();

    private static EventBus sDefaultBus;

    private EventBus() {
        this(DESCRIPTOR);
    }

    public EventBus(String desc) {
        mDesc = desc;
    }
    /**
     * @return
     */
    public static EventBus getDefault() {
        if (sDefaultBus == null) {
            synchronized (EventBus.class) {
                if (sDefaultBus == null) {
                    sDefaultBus = new EventBus();
                }
            }
        }
        return sDefaultBus;
    }

    /**
     * 订阅事件
     * @param subsriber
     */
    public void register(Object subsriber){
      if(subsriber==null)
          return;
        synchronized (this){
            mSubsciberMethodHunter.findSubscriberMethods(subsriber);
        }
    }

    /**
     * 取消订阅
     * @param subsriber
     */
    public void unregister(Object subsriber){
        if(subsriber==null)
            return;
        synchronized (this){
            mSubsciberMethodHunter.removeMethodsFromMap(subsriber);
        }
    }

    public void post(Object event){
       post(event,EventType.DEFAULT_TAG);
    }

    /**
     * 发布事件
     * @param event 要发布的事件
     * @param tag  发布事件的tag
     */
    public void post(Object event,String tag){
        if (event == null) {
            Log.e(this.getClass().getSimpleName(), "The event object is null");
            return;
        }

        mLocalEvent.get().offer(new EventType(event.getClass(),tag));
        mDispatcher.dispatchEvents(event);
    }

    /**
     * 设置订阅函数匹配策略
     *
     * @param policy 匹配策略
     */
    public void setMatchPolicy(MatchPolicy policy) {
            mDispatcher.matchPolicy = policy;
    }
    /**
     * 事件分发起器
     */
    private class EventDispatcher{
        /**
         * 缓存一个事件类型对应的可EventType列表
         */
        private Map<EventType, List<EventType>> mCacheEventTypes = new ConcurrentHashMap<>();

        /**
         * 默认的事件处理器，事件在哪个线程发布，就在哪个线程执行
         */
        EventHandler mDefaultEventHander =new DefaultEventHander();

        /**
         * 事件在ui线程执行
         */
        EventHandler mUIThreadEventHander =new UIThreadEventHandler();

        /**
         * 事件在子线程执行
         */
        EventHandler mAsyncEventHander =new AsyncEventHander();

        /**
         * 事件匹配规则
         */
        MatchPolicy matchPolicy=new DefaultMatchPolicy();

        public EventDispatcher() {
        }

        /**
         * 从当前线程中拿出事件队列，进行分发
         * @param aEvent
         */
        void dispatchEvents(Object aEvent) {
            //从当前线程中拿出事件队列
            Queue<EventType> eventTypeQueue = mLocalEvent.get();
            if(eventTypeQueue.size()>0){
                deliveryEvent(eventTypeQueue.poll(),aEvent);
            }
        }

        /**
         * 根据aEvent查找到所有匹配的集合,然后处理事件
         * @param type
         * @param aEvent
         */
        private void deliveryEvent(EventType type, Object aEvent) {
            List<EventType> eventTypeList=findMatchEventTypeList(type,aEvent);
            // 迭代所有匹配的事件并且分发给订阅者
            for (EventType eventType : eventTypeList) {
                handleEvent(eventType, aEvent);
            }
        }

        /**
         * 处理事件
         * @param eventType
         * @param aEvent
         */
        private void handleEvent(EventType eventType, Object aEvent) {
            //取出事件对应的订阅者集合
            CopyOnWriteArrayList<Subscription> subscriptions = mSubscriberMap.get(eventType);
            if(subscriptions==null)return;

            for(Subscription subscription:subscriptions){
                final EventHandler eventHanlder=getEventHandler(subscription.threadmode);
                eventHanlder.handleEvent(subscription,aEvent);
            }
        }

        /**
         * 获取对应的事件处理器
         * @param threadmode
         * @return
         */
        private EventHandler getEventHandler(Threadmode threadmode) {
            switch (threadmode){
                case Main:
                    return mUIThreadEventHander;
                case Async:
                    return mAsyncEventHander;
                default:
                    return mDefaultEventHander;
            }
        }

        /**
         * 获取匹配的事件列表
         * @param eventType
         * @param aEvent
         * @return
         */
        private List<EventType> findMatchEventTypeList(EventType eventType, Object aEvent) {
            List<EventType> resultEventTypeList;
            //如果有缓存，就从缓存中取
            if(mCacheEventTypes.containsKey(eventType)){
                resultEventTypeList=mCacheEventTypes.get(eventType);
            }else{
                resultEventTypeList=matchPolicy.findMatchEventTypes(eventType,aEvent);
                mCacheEventTypes.put(eventType,resultEventTypeList);
            }
            return resultEventTypeList != null ? resultEventTypeList : new ArrayList<EventType>();
        }
    }



}
