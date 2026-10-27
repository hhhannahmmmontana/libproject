package com.volodya.libproject.domain.exceptions;

public final class ReaderNotFoundException extends RuntimeException {

    public ReaderNotFoundException(String key, String value) {
        super("Reader not found (" + key + ": " + value + ")");
    }
}
