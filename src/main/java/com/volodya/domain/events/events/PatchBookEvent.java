package com.volodya.domain.events.events;

import com.volodya.domain.entities.Book;

public record PatchBookEvent(
    Book before,
    Book after
) implements Event {}
