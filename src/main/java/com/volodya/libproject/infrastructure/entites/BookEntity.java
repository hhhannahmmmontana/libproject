package com.volodya.libproject.infrastructure.entites;

import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class BookEntity {

    @Id
    @Setter(AccessLevel.PROTECTED)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    private Year publicationYear;

    @Column(nullable = false)
    private Long totalCopies;

    @Column(nullable = false)
    private Long availableCopies;

    @Version
    private Long version;

    public BookEntity(
        UUID id,
        String title,
        String author,
        String isbn,
        Year publicationYear,
        long totalCopies
    ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }
}
