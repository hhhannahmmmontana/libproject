package com.volodya.libproject.presentation.requests.bookloans;

import jakarta.validation.constraints.Future;

import java.time.Instant;
import java.util.UUID;

public record CreateBookLoanRequest(
    UUID bookId,
    UUID readerId,
    @Future Instant dueDate
) {}
