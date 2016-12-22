package com.simon.eventbus;

import android.support.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 *
 */

public class SubsciberMethodHunter {

    Map<EventType, CopyOnWriteArrayList<Subscription>> mSubscriberMap;

    public SubsciberMethodHunter(Map<EventType, CopyOnWriteArrayList<Subscription>> subscriberMap) {
        this.mSubscriberMap = subscriberMap;
    }

    /**
     * 从订阅者对象中找到每一个订阅函数，订阅函数只能有一个参数。 找到订阅函数后，构建Subsription,存储到map中
     * @param scriber
     */
    public void findSubscriberMethods(@Nullable Object scriber){
         if(mSubscriberMap==null){
             throw  new NullPointerException("the subscriberMap cannot be null");
         }
        Class<?> aClass = scriber.getClass();
        while (aClass!=null&&!isSyetemClass(aClass.getName())){
            //取出该对象的所有方法
             final Method[] methods=aClass.getDeclaredMethods();
            for (int i = 0; i <methods.length ; i++) {
                Method method = methods[i];
                Subscriber annotation = method.getAnnotation(Subscriber.class);
                //如果该对象的方法是使用Subscriber注解的
                if(annotation!=null){
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if(parameterTypes!=null&&parameterTypes.length==1){
                        Class<?> paramType = convertType(parameterTypes[0]);
                        //构建事件类型对象
                        EventType mEventType=new EventType(paramType,annotation.tag());
                        //构建目标方法
                        TargetMethod targetMethod=new TargetMethod(method,mEventType,annotation.mode());

                        subscibe(mEventType, targetMethod, scriber);
                    }
                }
            }
            // 获取父类,以继续查找父类中符合要求的方法
            aClass=aClass.getSuperclass();
        }
    }

    /**
     *构建map对象
     * @param mEventType
     * @param targetMethod
     * @param scriber
     */
    private void subscibe(EventType mEventType, TargetMethod targetMethod, Object scriber) {
        CopyOnWriteArrayList<Subscription> subscriptions = mSubscriberMap.get(mEventType);
        if(subscriptions==null){
            subscriptions=new CopyOnWriteArrayList<>();
        }
        Subscription newSubscription=new Subscription(scriber,targetMethod);

        //判断订阅者对象是否订阅者集合中
        if(subscriptions.contains(newSubscription)){
            return;
        }

        subscriptions.add(newSubscription);
        // 将事件类型key和订阅者信息存储到map中
        mSubscriberMap.put(mEventType, subscriptions);
    }

    /**
     * 取消订阅，从订阅者集合中找到该订阅者对象，从订阅者集合中移除
     * @param subsriber
     */
    public void removeMethodsFromMap(@Nullable Object subsriber) {
        //构建事件类型对象
        Iterator<CopyOnWriteArrayList<Subscription>> iterator = mSubscriberMap.values().iterator();
         while (iterator.hasNext()){
             CopyOnWriteArrayList<Subscription>  subscriptions = iterator.next();
             if(subscriptions!=null){
                 List<Subscription> foundsubscriptions=new LinkedList<>();
                 Iterator<Subscription> subIterator = subscriptions.iterator();
                   while (subIterator.hasNext()){
                       Subscription subscription = subIterator.next();
                       final Object cacheObject = subscription.subscriber.get();
                       if(cacheObject==null||isObjectEqual(cacheObject,subsriber)){
                           foundsubscriptions.add(subscription);
                       }
                   }
                 // 移除该subscriber的相关的Subscription
                 subscriptions.removeAll(foundsubscriptions);
             }

             // 如果针对某个Event的订阅者数量为空了,那么需要从map中清除
             if (subscriptions == null || subscriptions.size() == 0) {
                 iterator.remove();
             }
         }

    }

    private boolean isObjectEqual(Object cachedObj,Object subsriber) {
        return cachedObj != null
                && cachedObj.equals(subsriber);
    }

    /**
     *
     * @param eventType
     * @return
     */
    private Class<?> convertType(Class<?> eventType) {
        Class<?> returnClass = eventType;
        if (eventType.equals(boolean.class)) {
            returnClass = Boolean.class;
        } else if (eventType.equals(int.class)) {
            returnClass = Integer.class;
        } else if (eventType.equals(float.class)) {
            returnClass = Float.class;
        } else if (eventType.equals(double.class)) {
            returnClass = Double.class;
        }
        return returnClass;
    }

    private boolean isSyetemClass(String name) {
        return name.startsWith("java.")||name.startsWith("javax.")||name.startsWith("android.");
    }

}
