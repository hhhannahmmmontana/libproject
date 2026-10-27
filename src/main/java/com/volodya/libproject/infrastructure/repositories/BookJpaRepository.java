package com.volodya.libproject.infrastructure.repositories;

import com.volodya.libproject.infrastructure.entites.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookJpaRepository extends JpaRepository<BookEntity, UUID> {

    Optional<BookEntity> findByIsbn(String isbn);
}
