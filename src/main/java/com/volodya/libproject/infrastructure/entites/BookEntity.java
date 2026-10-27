package com.volodya.libproject.infrastructure.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Year;
import java.util.UUID;

@Entity
@Table(name = "books")
@AllArgsConstructor
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
}
