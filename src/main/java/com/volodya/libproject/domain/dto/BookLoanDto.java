package com.volodya.libproject.domain.dto;

import com.volodya.libproject.infrastructure.entites.enums.BookLoanStatus;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public record BookLoanDto(
    UUID id,
    BookDto book,
    ReaderDto reader,
    Instant loanDate,
    Instant dueDate,
    @Nullable Instant returnDate,
    BookLoanStatus status
) {}
