package com.volodya.libproject.infrastructure.repositories;

import com.volodya.libproject.infrastructure.entites.ReaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReaderJpaRepository extends JpaRepository<ReaderEntity, UUID> {

    Optional<ReaderEntity> findByEmail(String email);

    Optional<ReaderEntity> findByPhone(String phone);
}
