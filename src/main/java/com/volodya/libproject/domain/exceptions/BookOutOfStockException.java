package com.volodya.libproject.domain.exceptions;

import com.volodya.libproject.infrastructure.entites.BookEntity;

public final class BookOutOfStockException extends RuntimeException {

    public BookOutOfStockException(BookEntity book) {
        super("Book " + book.getAuthor() + " - " + book.getTitle() + " out of available copies");
    }
}
