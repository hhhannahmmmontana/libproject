package com.volodya.libproject.domain.services;

import com.volodya.libproject.domain.dto.BookDto;
import com.volodya.libproject.domain.entities.Isbn;
import com.volodya.libproject.domain.exceptions.EntityNotFoundException;
import com.volodya.libproject.domain.mappers.BookDtoEntityMapper;
import com.volodya.libproject.infrastructure.entites.BookEntity;
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

    public final static String ENTITY_NAME = "Book";

    private final BookJpaRepository bookJpaRepository;
    private final BookDtoEntityMapper bookMapper;

    public BookDto createBook(
        String title,
        String author,
        Isbn isbn,
        Year publicationYear,
        long totalCopies
    ) {
        var book = new BookEntity(
            UUID.randomUUID(),
            title,
            author,
            isbn.value(),
            publicationYear,
            totalCopies
        );

        return bookMapper.toDto(bookJpaRepository.save(book));
    }

    BookEntity getBookEntityById(UUID id) {
        return bookJpaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
    }

    public BookDto getBookDtoById(UUID id) {
        return bookMapper.toDto(getBookEntityById(id));
    }

    public BookDto getBookDtoByIsbn(Isbn isbn) {
        return bookJpaRepository.findByIsbn(isbn.value())
            .map(bookMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, "ISBN", isbn.value()));
    }

    public BookDto patchBook(
        UUID id,
        @Nullable String title,
        @Nullable String author,
        @Nullable Isbn isbn,
        @Nullable Year publicationYear,
        @Nullable Long totalCopies,
        @Nullable Long availableCopies
    ) {
        var book = bookJpaRepository.findForUpdateById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));

        if (title != null) {
            book.setTitle(title);
        }
        if (author != null) {
            book.setAuthor(author);
        }
        if (isbn != null) {
            book.setIsbn(isbn.value());
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
        var book = getBookEntityById(id);
        bookJpaRepository.delete(book);
    }
}
