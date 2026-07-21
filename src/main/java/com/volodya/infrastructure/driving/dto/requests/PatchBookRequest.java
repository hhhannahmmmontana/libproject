package com.volodya.infrastructure.driving.dto.requests;

import com.volodya.application.annotations.NotNegative;
import org.jspecify.annotations.Nullable;

public record PatchBookRequest(
    @Nullable @NotNegative Long totalCopies,
    @Nullable @NotNegative Long availableCopies
) {}
