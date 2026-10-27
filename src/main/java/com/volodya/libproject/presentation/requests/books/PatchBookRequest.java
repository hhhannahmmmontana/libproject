package com.volodya.libproject.presentation.requests.books;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

import java.time.Year;
import java.util.UUID;

public record PatchBookRequest(
    UUID id,

    @Nullable
    @Size(min = 1, max = 255)
    String title,

    @Nullable
    @Size(min = 1, max = 255)
    String author,

    @Nullable
    @Size(min = 10, max = 13)
    String isbn,

    @Nullable
    Year publicationYear,

    @Nullable
    @Min(0)
    Long totalCopies,

    @Nullable
    @Min(0)
    Long availableCopies
) {}
