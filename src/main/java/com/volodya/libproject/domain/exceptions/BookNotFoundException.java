package com.volodya.libproject.domain.exceptions;

public final class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String key, String value) {
        super("Book not found (" + key + ": " + value + ")");
    }
}
