package com.volodya.domain.events;

import com.volodya.domain.events.events.Event;

public interface EventListener {

    void consumeEvent(Event event);
}
