package com.volodya.domain.events;

import com.volodya.domain.events.events.Event;

public interface EventNotifier {

    void notifyListeners(Event event);
}
