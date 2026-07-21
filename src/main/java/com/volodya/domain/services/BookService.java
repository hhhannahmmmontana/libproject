package com.volodya.domain.services;

import com.volodya.domain.entities.Book;
import org.jspecify.annotations.Nullable;

import java.time.Year;
import java.util.List;
import java.util.UUID;

public interface BookService {

    Book create(
        String title,
        String author,
        String isbn,
        Year publicationYear,
        long totalCopies,
        long availableCopies
    );

    Book getById(UUID id);

    List<Book> findByName(String name);

    List<Book> findByAuthor(String author);

    Book getByIsbn(String isbn);

    List<Book> filter(
        @Nullable Year minYear,
        @Nullable Year maxYear,
        @Nullable Long minAvailableCopies,
        @Nullable Long maxAvailableCopies
    );

    Book patch(UUID id, @Nullable Long totalCopies, @Nullable Long availableCopies);

    void deleteById(UUID id);
}
