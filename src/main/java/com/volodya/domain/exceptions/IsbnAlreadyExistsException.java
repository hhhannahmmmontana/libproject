package com.volodya.domain.exceptions;

public final class IsbnAlreadyExistsException extends RuntimeException {

    public IsbnAlreadyExistsException(String isbn) {
        super("Book with ISBN number \"" + isbn + "\" already exists");
    }
}
