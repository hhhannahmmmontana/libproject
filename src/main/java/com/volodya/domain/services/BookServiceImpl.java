package com.volodya.domain.services;

import com.volodya.domain.entities.Book;
import com.volodya.domain.exceptions.BookNotFoundException;
import com.volodya.domain.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public final class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final TransactionService transactionService;

    @Override
    public Book create(
        String title,
        String author,
        String isbn,
        Year publicationYear,
        long totalCopies,
        long availableCopies
    ) {
        var book = new Book(
            title,
            author,
            isbn,
            publicationYear,
            totalCopies,
            availableCopies
        );
        return bookRepository.create(book);
    }

    @Override
    public Book getById(UUID id) {
        return bookRepository
            .findById(id)
            .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public List<Book> findByName(String name) {
        return bookRepository.findByName(name);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public Book getByIsbn(String isbn) {
        return bookRepository
            .findByIsbn(isbn)
            .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @Override
    public List<Book> filter(
        @Nullable Year minYear,
        @Nullable Year maxYear,
        @Nullable Long minAvailableCopies,
        @Nullable Long maxAvailableCopies
    ) {
        if (minAvailableCopies != null && maxAvailableCopies != null && minAvailableCopies > maxAvailableCopies) {
            throw new IllegalArgumentException("minAvailability should be less than maxAvailability");
        }
        return bookRepository.filter(
            minYear,
            maxYear,
            minAvailableCopies,
            maxAvailableCopies
        );
    }

    @Override
    public Book patch(UUID id, @Nullable Long totalCopies, @Nullable Long availableCopies) {
        return transactionService.supplyTransactional(() -> {
            var book = bookRepository
                .findByIdForUpdate(id)
                .orElseThrow(() -> new BookNotFoundException(id));

            if (totalCopies != null) {
                book = book.withTotalCopies(totalCopies);
            }
            if (availableCopies != null) {
                book = book.withAvailableCopies(availableCopies);
            }
            return book;
        });
    }

    @Override
    public void deleteById(UUID id) {
        bookRepository.deleteById(id);
    }
}
