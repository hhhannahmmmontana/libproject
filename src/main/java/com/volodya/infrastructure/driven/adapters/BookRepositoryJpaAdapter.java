package com.volodya.infrastructure.driven.adapters;

import com.volodya.application.mappers.IntYearMapper;
import com.volodya.domain.entities.Book;
import com.volodya.domain.exceptions.BookNotFoundException;
import com.volodya.domain.exceptions.IsbnAlreadyExistsException;
import com.volodya.domain.repositories.BookRepository;
import com.volodya.infrastructure.driven.mappers.BookJpaMapper;
import com.volodya.infrastructure.driven.repositories.BookJpaRepository;
import com.volodya.infrastructure.driven.repositories.BookSpecifications;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public final class BookRepositoryJpaAdapter implements BookRepository {

    private final BookJpaRepository jpaRepository;
    private final BookJpaMapper mapper;
    private final IntYearMapper intYearMapper;

    @Override
    public Book create(Book book) {
        var entity = mapper.toJpa(book);
        try {
            return mapper.toDomain(jpaRepository.save(entity));
        } catch (DataIntegrityViolationException ex) {
            throw new IsbnAlreadyExistsException(book.isbn());
        }
    }

    @Override
    public Optional<Book> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Book> findByIdForUpdate(UUID id) {
        return jpaRepository.findForUpdateById(id).map(mapper::toDomain);
    }

    @Override
    public List<Book> findByName(String name) {
        return jpaRepository.findByTitle(name)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return jpaRepository.findByAuthor(author)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return jpaRepository.findByIsbn(isbn).map(mapper::toDomain);
    }

    @Override
    public List<Book> filter(
        @Nullable Year minYear,
        @Nullable Year maxYear,
        @Nullable Long minAvailableCopies,
        @Nullable Long maxAvailableCopies
    ) {
        var specifications = BookSpecifications.filter(
            minYear,
            maxYear,
            minAvailableCopies,
            maxAvailableCopies
        );
        return jpaRepository.findAll(specifications)
            .stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public Book update(Book book) {
        var entity = mapper.toJpa(book);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        try {
            jpaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new BookNotFoundException(id);
        }
    }
}
