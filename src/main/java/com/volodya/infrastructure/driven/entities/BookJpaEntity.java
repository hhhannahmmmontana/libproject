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
    UUID id;

    @Column(nullable = false)
    String name;
}
