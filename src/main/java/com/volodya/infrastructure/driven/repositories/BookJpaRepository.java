package com.volodya.infrastructure.driven.repositories;

import com.volodya.infrastructure.driven.entities.BookJpaEntity;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookJpaRepository
    extends JpaRepository<@NonNull BookJpaEntity, @NonNull UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BookJpaEntity> findForUpdateById(UUID id);

    List<BookJpaEntity> findByTitle(String title);

    List<BookJpaEntity> findByAuthor(String author);

    Optional<BookJpaEntity> findByIsbn(String isbn);

    @Query("""
        SELECT b
        FROM BookJpaEntity b
        WHERE
            (:minYear IS NULL OR b.publicationYear >= :minYear) AND
            (:maxYear IS NULL OR b.publicationYear <= :maxYear) AND
            (:minAvailableCopies IS NULL OR b.availableCopies >= :minAvailableCopies) AND
            (:maxAvailableCopies IS NULL OR b.availableCopies <= :maxAvailableCopies)
        """)
    List<BookJpaEntity> findByFilters(
        @Nullable Integer minYear,
        @Nullable Integer maxYear,
        @Nullable Long minAvailableCopies,
        @Nullable Long maxAvailableCopies
    );
}
