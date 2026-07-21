package com.volodya.infrastructure.driving.dto;

import com.volodya.application.annotations.ExactSize;
import com.volodya.application.annotations.NotNegative;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BookDto(
    @NotNull UUID id,
    @NotBlank String title,
    @NotBlank String author,
    @NotBlank @ExactSize({10, 13}) String isbn,
    int publicationYear,
    @NotNegative long totalCopies,
    @NotNegative long availableCopies
) {}
