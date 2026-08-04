package com.volodya.domain.events;

import com.volodya.domain.events.events.Event;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public final class EventNotifierImpl implements EventNotifier {

    private final List<EventListener> listeners;

    public EventNotifierImpl(EventListener... events) {
        listeners = Arrays.asList(events);
    }

    @Override
    public void notifyListeners(Event event) {
        for (EventListener listener : listeners) {
            listener.consumeEvent(event);
        }
    }
}
