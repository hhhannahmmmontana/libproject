package com.volodya.infrastructure.driving.dto.responses;

import com.volodya.infrastructure.driving.dto.BookDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FindBooksResponse(
    @NotNull List<BookDto> books
) {}
