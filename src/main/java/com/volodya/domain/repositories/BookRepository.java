package com.volodya.domain.repositories;

import com.volodya.domain.entities.Book;
import org.jspecify.annotations.Nullable;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {

    Book create(Book book);

    Optional<Book> findById(UUID id);

    Optional<Book> findByIdForUpdate(UUID id);

    List<Book> findByName(String name);

    List<Book> findByAuthor(String author);

    Optional<Book> findByIsbn(String isbn);

    List<Book> filter(
        @Nullable Year minYear,
        @Nullable Year maxYear,
        @Nullable Long minAvailableCopies,
        @Nullable Long maxAvailableCopies
    );

    Book update(Book book);

    void deleteById(UUID id);
}
