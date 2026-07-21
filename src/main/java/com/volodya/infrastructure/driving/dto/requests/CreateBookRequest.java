package com.volodya.infrastructure.driving.dto.requests;

import com.volodya.application.annotations.ExactSize;
import com.volodya.application.annotations.NotNegative;
import jakarta.validation.constraints.NotBlank;

public record CreateBookRequest(
    @NotBlank String title,
    @NotBlank String author,
    @NotBlank @ExactSize({10, 13}) String isbn,
    int publicationYear,
    @NotNegative long totalCopies,
    @NotNegative long availableCopies
) {}
