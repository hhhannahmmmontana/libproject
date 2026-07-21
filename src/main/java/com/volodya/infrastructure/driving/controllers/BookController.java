package com.volodya.infrastructure.driving.controllers;

import com.volodya.application.annotations.NotNegative;
import com.volodya.application.mappers.IntYearMapper;
import com.volodya.domain.services.BookService;
import com.volodya.infrastructure.driving.dto.BookDto;
import com.volodya.infrastructure.driving.dto.requests.CreateBookRequest;
import com.volodya.infrastructure.driving.dto.requests.PatchBookRequest;
import com.volodya.infrastructure.driving.dto.responses.FindBooksResponse;
import com.volodya.infrastructure.driving.mappers.BookDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookDtoMapper mapper;
    private final IntYearMapper intYearMapper;

    @PostMapping
    public ResponseEntity<@NonNull BookDto> create(
        @Valid @RequestBody CreateBookRequest request
    ) {
        var book = bookService.create(
            request.title(),
            request.author(),
            request.isbn(),
            intYearMapper.toYear(request.publicationYear()),
            request.totalCopies(),
            request.availableCopies()
        );
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(mapper.toDto(book));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull BookDto> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(mapper.toDto(bookService.getById(id)));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<@NonNull FindBooksResponse> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(new FindBooksResponse(
            bookService.findByName(name)
                .stream()
                .map(mapper::toDto)
                .toList()
        ));
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<@NonNull FindBooksResponse> findByAuthor(@PathVariable("author") String author) {
        return ResponseEntity.ok(new FindBooksResponse(
            bookService.findByAuthor(author)
                .stream()
                .map(mapper::toDto)
                .toList()
        ));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<@NonNull BookDto> getByIsbn(@PathVariable("isbn") String isbn) {
        return ResponseEntity.ok(mapper.toDto(bookService.getByIsbn(isbn)));
    }

    @GetMapping
    public ResponseEntity<@NonNull FindBooksResponse> filter(
        @Nullable Integer minYear,
        @Nullable Integer maxYear,
        @Nullable @NotNegative Long minAvailableCopies,
        @Nullable @NotNegative Long maxAvailableCopies
    ) {
        return ResponseEntity.ok(new FindBooksResponse(
            bookService.filter(
                    minYear != null ? intYearMapper.toYear(minYear) : null,
                    maxYear != null ? intYearMapper.toYear(maxYear) : null,
                    minAvailableCopies,
                    maxAvailableCopies
                )
                .stream()
                .map(mapper::toDto)
                .toList()
        ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<@NonNull BookDto> patch(
        @PathVariable("id") UUID id,
        @Valid @RequestBody PatchBookRequest request
    ) {
        return ResponseEntity.ok(
            mapper.toDto(bookService.patch(
                id,
                request.totalCopies(),
                request.availableCopies())
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull Void> deleteById(@PathVariable("id") UUID id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
