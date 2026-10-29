package com.volodya.libproject.presentation.controllers;

import com.volodya.libproject.domain.dto.BookLoanDto;
import com.volodya.libproject.domain.dto.ReaderDto;
import com.volodya.libproject.domain.services.BookLoanService;
import com.volodya.libproject.presentation.requests.bookloans.CreateBookLoanRequest;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public final class BookLoanController {

    private final BookLoanService bookLoanService;

    @PostMapping
    public ResponseEntity<BookLoanDto> createBookLoan(
        @Valid @RequestBody CreateBookLoanRequest request
    ) {
        return new ResponseEntity<>(bookLoanService.createBookLoan(
            request.bookId(),
            request.readerId(),
            request.dueDate()
        ), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookLoanDto> getBookLoan(@PathParam("id") UUID id) {
        return ResponseEntity.ok(bookLoanService.getBookLoanDtoById(id));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BookLoanDto> returnBook(@PathParam("id") UUID id) {
        return ResponseEntity.ok(bookLoanService.returnBook(id));
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<BookLoanDto>> getReaderLoans(@PathParam("readerId") UUID readerId) {
        return ResponseEntity.ok(bookLoanService.getReaderLoans(readerId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<ReaderDto>> getDebtors() {
        return ResponseEntity.ok(bookLoanService.getDebtors());
    }
}
