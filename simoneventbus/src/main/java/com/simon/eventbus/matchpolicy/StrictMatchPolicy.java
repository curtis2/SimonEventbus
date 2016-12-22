package com.simon.eventbus.matchpolicy;

import com.simon.eventbus.EventType;

import java.util.LinkedList;
import java.util.List;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class StrictMatchPolicy implements MatchPolicy{

    @Override
    public List<EventType> findMatchEventTypes(EventType type, Object aEvent) {
        List<EventType> resultList=new LinkedList<>();
        resultList.add(type);
        return resultList;
    }


}
