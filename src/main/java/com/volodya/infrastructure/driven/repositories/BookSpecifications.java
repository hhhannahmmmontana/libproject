package com.volodya.infrastructure.driven.repositories;

import com.volodya.infrastructure.driven.entities.BookJpaEntity;
import jakarta.persistence.criteria.Predicate;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public final class BookSpecifications {

    public static Specification<BookJpaEntity> filter(
        @Nullable Year minYear,
        @Nullable Year maxYear,
        @Nullable Long minAvailableCopies,
        @Nullable Long maxAvailableCopies
    ) {
        return (root, _, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (minYear != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("publicationYear"),
                    minYear
                ));
            }

            if (maxYear != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("publicationYear"),
                    maxYear
                ));
            }

            if (minAvailableCopies != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("availableCopies"),
                    minAvailableCopies
                ));
            }

            if (maxAvailableCopies != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("availableCopies"),
                    maxAvailableCopies
                ));
            }

            return criteriaBuilder.and(predicates);
        };
    }
}
