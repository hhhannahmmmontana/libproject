package com.volodya.infrastructure.driven.repositories;

import com.volodya.infrastructure.driven.entities.BookJpaEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookJpaRepository
    extends JpaRepository<@NonNull BookJpaEntity, @NonNull UUID> {}
