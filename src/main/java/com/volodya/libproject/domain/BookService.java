package com.volodya.libproject.domain;

import com.volodya.libproject.domain.dto.BookDto;
import com.volodya.libproject.domain.exceptions.BookNotFoundException;
import com.volodya.libproject.infrastructure.entites.BookEntity;
import com.volodya.libproject.infrastructure.mappers.BookDtoEntityMapper;
import com.volodya.libproject.infrastructure.repositories.BookJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookJpaRepository bookJpaRepository;
    private final BookDtoEntityMapper bookMapper;

    public BookDto createBook(
        String title,
        String author,
        String isbn,
        Year publicationYear,
        long totalCopies,
        long availableCopies
    ) {
        checkIsbn(isbn);

        var book = new BookEntity(
            UUID.randomUUID(),
            title,
            author,
            isbn,
            publicationYear,
            totalCopies,
            availableCopies
        );

        return bookMapper.toDto(bookJpaRepository.save(book));
    }

    public BookDto getBookById(UUID id) {
        return bookJpaRepository.findById(id)
            .map(bookMapper::toDto)
            .orElseThrow(() -> new BookNotFoundException("id", id.toString()));
    }

    public BookDto getBookByIsbn(String isbn) {
        checkIsbn(isbn);
        return bookJpaRepository.findByIsbn(isbn)
            .map(bookMapper::toDto)
            .orElseThrow(() -> new BookNotFoundException("ISBN", isbn));
    }

    public BookDto patchBook(
        UUID id,
        @Nullable String title,
        @Nullable String author,
        @Nullable String isbn,
        @Nullable Year publicationYear,
        @Nullable Long totalCopies,
        @Nullable Long availableCopies
    ) {
        if (isbn != null) {
            checkIsbn(isbn);
        }

        var book = bookJpaRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("id", id.toString()));

        if (title != null) {
            book.setTitle(title);
        }
        if (author != null) {
            book.setAuthor(author);
        }
        if (isbn != null) {
            book.setIsbn(isbn);
        }
        if (publicationYear != null) {
            book.setPublicationYear(publicationYear);
        }
        if (totalCopies != null) {
            book.setTotalCopies(totalCopies);
        }
        if (availableCopies != null) {
            book.setAvailableCopies(availableCopies);
        }

        return bookMapper.toDto(book);
    }

    public void deleteBook(UUID id) {
        var book = bookJpaRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("id", id.toString()));
        bookJpaRepository.delete(book);
    }

    private void checkIsbn(String isbn) {
        if (isbn.length() != 10 && isbn.length() != 13) {
            throw new IllegalArgumentException("ISBN length is invalid");
        }
    }
}
