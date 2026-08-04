package com.volodya.domain.exceptions;

public abstract class LibraryUncheckedException extends RuntimeException {

    public LibraryUncheckedException(String message) {
        super(message);
    }
}
