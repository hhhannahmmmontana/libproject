package com.volodya.libproject.infrastructure.entites;

import com.volodya.libproject.infrastructure.entites.enums.BookLoanStatus;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "book_loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class BookLoanEntity {

    @Id
    @Setter(AccessLevel.PROTECTED)
    private UUID id;

    @ManyToOne
    private BookEntity book;

    @ManyToOne
    private ReaderEntity reader;

    private Instant loanDate;

    private Instant dueDate;

    @Nullable
    private Instant returnDate;

    @Enumerated(EnumType.STRING)
    private BookLoanStatus status;

    public BookLoanEntity(
        UUID id,
        BookEntity book,
        ReaderEntity reader,
        Instant dueDate
    ) {
        this.id = id;
        this.book = book;
        this.reader = reader;
        this.loanDate = Instant.now();
        this.dueDate = dueDate;
        this.status = BookLoanStatus.RENTED;
    }
}
