package com.volodya.libproject.infrastructure.repositories;

import com.volodya.libproject.infrastructure.entites.BookLoanEntity;
import com.volodya.libproject.infrastructure.entites.ReaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface BookLoanJpaRepository extends JpaRepository<BookLoanEntity, UUID> {

    List<BookLoanEntity> findByReader(ReaderEntity reader);

    List<BookLoanEntity> findByDueDateLessThan(Instant dueDate);
}
