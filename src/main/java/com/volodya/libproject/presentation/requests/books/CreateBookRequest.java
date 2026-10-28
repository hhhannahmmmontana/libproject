package com.volodya.libproject.presentation.requests.books;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.Year;

public record CreateBookRequest(
    @Size(min = 1, max = 255)
    String title,

    @Size(min = 1, max = 255)
    String author,

    @Size(min = 10, max = 13)
    String isbn,

    Year publicationYear,

    @Min(0)
    long totalCopies
) {}
