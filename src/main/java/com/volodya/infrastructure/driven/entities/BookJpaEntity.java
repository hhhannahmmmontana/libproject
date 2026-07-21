package com.volodya.infrastructure.driven.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "books")
@Data
public class BookJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, length = 13)
    private String isbn;

    @Column(nullable = false)
    private Integer publicationYear;

    @Column(nullable = false)
    private Long totalCopies;

    @Column(nullable = false)
    private Long availableCopies;
}
