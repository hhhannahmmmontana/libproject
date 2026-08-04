package com.volodya.domain.events.events;

import java.util.UUID;

public record DeleteBookEvent(UUID id) implements Event {}
