package com.volodya.libproject.infrastructure.repositories;

import com.volodya.libproject.domain.pagination.PaginationTokenDirecton;
import com.volodya.libproject.infrastructure.entites.BookEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;

import java.time.Year;
import java.util.List;

public interface BookCriteriaRepository {

    /**
     * @return new cursor + found data
     */
    Pair<@Nullable String, List<BookEntity>> findBooksByFilters(
        @Nullable String title,
        @Nullable String author,
        @Nullable Year publicationYear,
        @Nullable Boolean isAvailable,
        int pageSize,
        @Nullable String cursor,
        PaginationTokenDirecton direction
    );
}
