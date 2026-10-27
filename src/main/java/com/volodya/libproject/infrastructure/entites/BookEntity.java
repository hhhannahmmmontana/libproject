package com.volodya.libproject.infrastructure.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;

import java.time.Year;
import java.util.UUID;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
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
        long totalCopies,
        long availableCopies
    ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }
}
