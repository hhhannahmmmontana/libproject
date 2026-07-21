package com.volodya.domain.entities;

import lombok.NonNull;

import java.time.Year;
import java.util.UUID;

public record Book(
    @NonNull UUID id,
    @NonNull String title,
    @NonNull String author,
    @NonNull String isbn,
    @NonNull Year publicationYear,
    long totalCopies,
    long availableCopies
) {

    private static final int ISBN_OLD_LENGTH = 10;
    private static final int ISBN_LENGTH = 13;

    public Book {
        if (isbn.length() != ISBN_LENGTH && isbn.length() != ISBN_OLD_LENGTH) {
            throw new IllegalArgumentException("Invalid ISBN length");
        }
        if (title.isBlank()) {
            throw new IllegalArgumentException("Empty title");
        }
        if (author.isBlank()) {
            throw new IllegalArgumentException("Empty author");
        }
        if (totalCopies < 0) {
            throw new IllegalArgumentException("Negative total copies amount");
        }
        if (availableCopies < 0) {
            throw new IllegalArgumentException("Negative available copies amount");
        }
    }

    public Book(
        String title,
        String author,
        String isbn,
        Year publicationYear,
        long totalCopies,
        long availableCopies
    ) {
        this(UUID.randomUUID(), title, author, isbn, publicationYear, totalCopies, availableCopies);
    }

    public Book withTotalCopies(long totalCopies) {
        return new Book(
            id,
            title,
            author,
            isbn,
            publicationYear,
            totalCopies,
            availableCopies
        );
    }

    public Book withAvailableCopies(long availableCopies) {
        return new Book(
            id,
            title,
            author,
            isbn,
            publicationYear,
            totalCopies,
            availableCopies
        );
    }
}
