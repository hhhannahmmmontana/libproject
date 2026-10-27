package com.volodya.libproject.infrastructure.repositories;

import com.volodya.libproject.infrastructure.entites.BookEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, UUID> {

    Optional<BookEntity> findByIsbn(String isbn);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<BookEntity> findForUpdateById(UUID id);
}
