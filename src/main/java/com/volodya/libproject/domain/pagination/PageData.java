package com.volodya.libproject.domain.pagination;

import org.jspecify.annotations.Nullable;

import java.util.List;

public record PageData<T>(int size, @Nullable String token, List<T> data) {}
