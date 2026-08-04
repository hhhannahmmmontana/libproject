package com.volodya.domain.events.events;

import com.volodya.domain.entities.Book;

public record CreateBookEvent(Book book) implements Event {}
