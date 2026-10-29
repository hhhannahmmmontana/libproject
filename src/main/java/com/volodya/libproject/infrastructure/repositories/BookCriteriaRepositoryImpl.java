package com.volodya.libproject.infrastructure.repositories;

import com.volodya.libproject.domain.pagination.PaginationTokenDirecton;
import com.volodya.libproject.infrastructure.entites.BookEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookCriteriaRepositoryImpl implements BookCriteriaRepository {

    private final EntityManager em;
    private final static String DELIMITER = "\u001f";

    @Override
    public Pair<@Nullable String, List<BookEntity>> findBooksByFilters(
        @Nullable String title,
        @Nullable String author,
        @Nullable Year publicationYear,
        @Nullable Boolean isAvailable,
        int pageSize,
        @Nullable String cursor,
        PaginationTokenDirecton direction
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BookEntity> cq = cb.createQuery(BookEntity.class);
        Root<BookEntity> book = cq.from(BookEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (title != null) {
            predicates.add(cb.like(book.get("title"), title));
        }
        if (author != null) {
            predicates.add(cb.like(book.get("author"), author));
        }
        if (publicationYear != null) {
            predicates.add(cb.equal(book.get("publicationYear"), publicationYear));
        }
        if (isAvailable != null) {
            predicates.add(cb.gt(book.get("isAvailable"), 0));
        }

        if (cursor != null) {
            var splitCursor = cursor.split(DELIMITER, 2);

            Predicate predicate = onDirection(
                cb.and(
                    cb.greaterThan(book.get("author"), splitCursor[0]),
                    cb.or(
                        cb.equal(book.get("author"), splitCursor[0]),
                        cb.greaterThan(book.get("title"), splitCursor[1])
                    )
                ),
                cb.or(
                    cb.lessThan(book.get("author"), splitCursor[0]),
                    cb.and(
                        cb.equal(book.get("author"), splitCursor[0]),
                        cb.lessThanOrEqualTo(book.get("title"), splitCursor[1])
                    )
                ),
                direction
            );
            predicates.add(predicate);
        }

        Function<Expression<String>, Order> orderType = onDirection(
            cb::asc,
            cb::desc,
            direction
        );

        TypedQuery<BookEntity> q = em.createQuery(cq.where(predicates)
            .orderBy(
                orderType.apply(book.get("author")),
                orderType.apply(book.get("title"))
            )
        );

        var result = q.setMaxResults(pageSize).getResultList();
        log.trace("query completed, data length: {}", result.size());

        String newCursor = null;
        if (!result.isEmpty()) {
            BookEntity cursorEntity = onDirection(
                result.getLast(),
                result.getFirst(),
                direction
            );
            // db has unique constraints name + author so we don't need duplicates logic
            newCursor = cursorEntity.getAuthor() + DELIMITER + cursorEntity.getTitle();
            log.trace("created new cursor, result {}", newCursor);
        }

        return Pair.of(newCursor, result);
    }

    private static <T> T onDirection(
        T onForward,
        T onBackward,
        PaginationTokenDirecton direction
    ) {
        if (direction == PaginationTokenDirecton.FORWARD) {
            return onForward;
        } else if (direction == PaginationTokenDirecton.BACKWARD) {
            return onBackward;
        } else {
            throw new IllegalStateException("Unknown pagination direction");
        }
    }
}
