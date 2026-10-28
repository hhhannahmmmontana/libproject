package com.volodya.libproject.presentation.controllers;

import com.volodya.libproject.domain.dto.BookDto;
import com.volodya.libproject.domain.entities.Isbn;
import com.volodya.libproject.domain.services.BookService;
import com.volodya.libproject.presentation.requests.books.CreateBookRequest;
import com.volodya.libproject.presentation.requests.books.PatchBookRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public final class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody CreateBookRequest request) {
        return new ResponseEntity<>(bookService.createBook(
            request.title(),
            request.author(),
            Isbn.of(request.isbn()),
            request.publicationYear(),
            request.totalCopies(),
            request.availableCopies()
        ), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathParam("id") UUID id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookById(
        @PathParam("isbn")
        @Size(min = 10, max = 13)
        String isbn
    ) {
        return ResponseEntity.ok(bookService.getBookByIsbn(Isbn.of(isbn)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> patchBook(
        @PathParam("id") UUID id,
        @Valid @RequestBody PatchBookRequest request
    ) {
        return ResponseEntity.ok(bookService.patchBook(
            id,
            request.title(),
            request.author(),
            Isbn.of(request.isbn()),
            request.publicationYear(),
            request.totalCopies(),
            request.availableCopies()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> patchBook(@PathParam("id") UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
