package com.volodya.libproject.domain.services;

import com.volodya.libproject.domain.dto.ReaderDto;
import com.volodya.libproject.domain.exceptions.EntityNotFoundException;
import com.volodya.libproject.domain.mappers.ReaderDtoEntityMapper;
import com.volodya.libproject.infrastructure.entites.ReaderEntity;
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

    public final static String ENTITY_NAME = "Reader";

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

    ReaderEntity getReaderEntityById(UUID id) {
        return readerJpaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
    }

    public ReaderDto getReaderDtoById(UUID id) {
        return readerMapper.toDto(getReaderEntityById(id));
    }

    public ReaderDto getReaderDtoByEmail(String email) {
        return readerJpaRepository.findByEmail(email)
            .map(readerMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, "email", email));
    }

    public ReaderDto getReaderDtoByPhone(String phone) {
        return readerJpaRepository.findByPhone(phone)
            .map(readerMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, "phone", phone));
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
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));

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
        var reader = getReaderEntityById(id);
        readerJpaRepository.delete(reader);
    }
}
