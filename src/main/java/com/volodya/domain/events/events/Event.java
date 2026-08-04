package com.volodya.domain.events.events;

public sealed interface Event permits
    CreateBookEvent,
    DeleteBookEvent,
    PatchBookEvent {}
