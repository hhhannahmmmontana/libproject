package com.volodya.libproject.domain.dto;

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
    String status
) {}
