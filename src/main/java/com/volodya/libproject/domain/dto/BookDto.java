package com.volodya.libproject.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.Year;
import java.util.UUID;

public record BookDto(
    UUID id,

    @Size(min = 1, max = 255)
    String title,

    @Size(min = 1, max = 255)
    String author,

    @Size(min = 10, max = 13)
    String isbn,

    Year publicationYear,

    @Min(0)
    long totalCopies,

    @Min(0)
    long availableCopies
) {}
