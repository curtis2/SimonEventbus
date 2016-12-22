package com.simon.eventbus.matchpolicy;


import com.simon.eventbus.EventType;

import java.util.LinkedList;
import java.util.List;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class DefaultMatchPolicy implements MatchPolicy {

    @Override
    public List<EventType> findMatchEventTypes(EventType type, Object aEvent) {
        Class<?> eventClass = aEvent.getClass();
        List<EventType> resultList=new LinkedList<>();
        while (eventClass!=null){
            resultList.add(new EventType(eventClass,type.tag));
            addInterfaces(resultList,eventClass,type.tag);
            eventClass=eventClass.getSuperclass();
        }
        return resultList;
    }

    /**
     * 获取该事件的所有接口类型
     * @param resultList
     * @param eventClass
     * @param tag
     */
    private void addInterfaces(List<EventType> resultList, Class<?> eventClass, String tag) {
        Class<?>[] interfacesClasses = eventClass.getInterfaces();
        for (Class<?> interfaceClass : interfacesClasses) {
            if (!resultList.contains(interfaceClass)) {
                resultList.add(new EventType(interfaceClass, tag));
                addInterfaces(resultList, interfaceClass, tag);
            }
        }
    }



}
