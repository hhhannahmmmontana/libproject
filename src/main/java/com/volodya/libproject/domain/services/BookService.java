package com.volodya.libproject.domain.services;

import com.volodya.libproject.domain.dto.BookDto;
import com.volodya.libproject.domain.entities.Isbn;
import com.volodya.libproject.domain.exceptions.EntityNotFoundException;
import com.volodya.libproject.domain.mappers.BookDtoEntityMapper;
import com.volodya.libproject.domain.pagination.PageData;
import com.volodya.libproject.domain.pagination.PaginationTokenDirecton;
import com.volodya.libproject.domain.pagination.PaginationTokenUtil;
import com.volodya.libproject.infrastructure.entites.BookEntity;
import com.volodya.libproject.infrastructure.repositories.BookJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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
        log.debug("creating book {} - {}, isbn {}", title, author, isbn);
        var book = new BookEntity(
            UUID.randomUUID(),
            title,
            author,
            isbn.value(),
            publicationYear,
            totalCopies
        );

        book = bookJpaRepository.save(book);
        log.info("book {} - {} created (id: {})", title, author, book.getId());
        return bookMapper.toDto(book);
    }

    BookEntity getBookEntityById(UUID id) {
        log.debug("searching for book with id {}", id);
        return bookJpaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
    }

    public BookDto getBookDtoById(UUID id) {
        return bookMapper.toDto(getBookEntityById(id));
    }

    public BookDto getBookDtoByIsbn(Isbn isbn) {
        log.debug("searching for book with isbn {}", isbn);
        var book = bookJpaRepository.findByIsbn(isbn.value())
            .map(bookMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, "ISBN", isbn.value()));
        log.debug("found book {} (from isbn {})", book.id(), isbn);
        return book;
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
        log.debug("patching book {}", id);
        var book = bookJpaRepository.findForUpdateById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));

        if (title != null) {
            book.setTitle(title);
            log.trace("{} book's title set to {}", id, title);
        }
        if (author != null) {
            book.setAuthor(author);
            log.trace("{} book's author set to {}", id, author);
        }
        if (isbn != null) {
            book.setIsbn(isbn.value());
            log.trace("{} book's isbn set to {}", id, isbn);
        }
        if (publicationYear != null) {
            book.setPublicationYear(publicationYear);
            log.trace("{} book's publication year set to {}", id, publicationYear);
        }
        if (totalCopies != null) {
            book.setTotalCopies(totalCopies);
            log.trace("{} book's totalCopies to {}", id, totalCopies);
        }
        if (availableCopies != null) {
            book.setAvailableCopies(availableCopies);
            log.trace("{} book's available copies to {}", id, availableCopies);
        }

        log.info("book {} patched", id);
        return bookMapper.toDto(book);
    }

    public void deleteBook(UUID id) {
        log.debug("book {} is being deleted", id);
        var book = getBookEntityById(id);
        bookJpaRepository.delete(book);
        log.info("book {} is deleted", id);
    }

    public PageData<BookDto> findBooksByFilters(
        @Nullable String title,
        @Nullable String author,
        @Nullable Year publicationYear,
        @Nullable Boolean isAvailable,
        int pageSize,
        PaginationTokenDirecton direction,
        @Nullable String token
    ) {
        log.debug(
            "searching for books, filters: title - {}, author - {}, publicationYear - {}, isAvailable - {}, pageSize - {}, direction - {}, token - {}",
            title, author, publicationYear, isAvailable, pageSize, direction, token
        );
        String cursor = null;
        if (token != null) {
            cursor = PaginationTokenUtil.decodeToken(token);
        }

        log.trace("executing findBooksByFilters repository method");
        var result = bookJpaRepository.findBooksByFilters(
            title,
            author,
            publicationYear,
            isAvailable,
            pageSize,
            cursor,
            direction
        );

        log.debug("books found: amount - {}, new token - {}", result.second().size(), result.first());
        return new PageData<>(
            result.second().size(),
            (result.first() != null) ? PaginationTokenUtil.encodeToken(result.first()) : null,
            result.second().stream().map(bookMapper::toDto).toList()
        );
    }
}
