package com.volodya.libproject.domain.services;

import com.volodya.libproject.domain.dto.ReaderDto;
import com.volodya.libproject.domain.exceptions.ReaderNotFoundException;
import com.volodya.libproject.infrastructure.entites.ReaderEntity;
import com.volodya.libproject.infrastructure.mappers.ReaderDtoEntityMapper;
import com.volodya.libproject.infrastructure.repositories.ReaderJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReaderService {

    private final ReaderJpaRepository readerJpaRepository;
    private final ReaderDtoEntityMapper readerMapper;

    public ReaderDto createReader(
        String firstName,
        String lastName,
        String email,
        String phone
    ) {
        var reader = new ReaderEntity(
            UUID.randomUUID(),
            firstName,
            lastName,
            email,
            phone,
            Instant.now()
        );
        return readerMapper.toDto(readerJpaRepository.save(reader));
    }

    public ReaderDto getReaderById(UUID id) {
        return readerJpaRepository.findById(id)
            .map(readerMapper::toDto)
            .orElseThrow(() -> new ReaderNotFoundException("id", id.toString()));
    }

    public ReaderDto getReaderByEmail(String email) {
        return readerJpaRepository.findByEmail(email)
            .map(readerMapper::toDto)
            .orElseThrow(() -> new ReaderNotFoundException("email", email));
    }

    public ReaderDto getReaderByPhone(String phone) {
        return readerJpaRepository.findByPhone(phone)
            .map(readerMapper::toDto)
            .orElseThrow(() -> new ReaderNotFoundException("phone", phone));
    }

    public ReaderDto patchReader(
        UUID id,
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String email,
        @Nullable String phone,
        @Nullable Instant registrationDate
    ) {
        var reader = readerJpaRepository.findById(id)
            .orElseThrow(() -> new ReaderNotFoundException("id", id.toString()));

        if (firstName != null) {
            reader.setFirstName(firstName);
        }
        if (lastName != null) {
            reader.setLastName(lastName);
        }
        if (email != null) {
            reader.setEmail(email);
        }
        if (phone != null) {
            reader.setPhone(phone);
        }
        if (registrationDate != null) {
            reader.setRegistrationDate(registrationDate);
        }

        return readerMapper.toDto(reader);
    }

    public void deleteReader(UUID id) {
        var reader = readerJpaRepository.findById(id)
            .orElseThrow(() -> new ReaderNotFoundException("id", id.toString()));
        readerJpaRepository.delete(reader);
    }
}
