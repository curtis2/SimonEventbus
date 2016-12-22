package com.simon.eventbus.matchpolicy;


import com.simon.eventbus.EventType;

import java.util.List;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public interface MatchPolicy {
    List<EventType> findMatchEventTypes(EventType type, Object aEvent);
}
