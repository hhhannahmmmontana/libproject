package com.volodya.domain.exceptions;

import java.util.UUID;

public final class BookNotFoundException extends LibraryUncheckedException {

    public BookNotFoundException(UUID id) {
        super("Book with ID " + id + " not found");
    }

    public BookNotFoundException(String isbn) {
        super("Book with ISBN " + isbn + " not found");
    }
}
