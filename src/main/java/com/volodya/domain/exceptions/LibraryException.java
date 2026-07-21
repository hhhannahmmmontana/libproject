package com.volodya.domain.exceptions;

public abstract class LibraryException extends RuntimeException {

    public LibraryException(String message) {
        super(message);
    }
}
