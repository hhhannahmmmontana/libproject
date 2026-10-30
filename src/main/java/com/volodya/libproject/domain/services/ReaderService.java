package com.volodya.libproject.domain.services;

import com.volodya.libproject.domain.dto.ReaderDto;
import com.volodya.libproject.domain.exceptions.EntityNotFoundException;
import com.volodya.libproject.domain.mappers.ReaderDtoEntityMapper;
import com.volodya.libproject.extras.TimerRunner;
import com.volodya.libproject.infrastructure.entites.ReaderEntity;
import com.volodya.libproject.infrastructure.repositories.ReaderJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.volodya.libproject.domain.configuration.utils.MaskingUtil.maskData;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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
        log.debug(
            "creating reader {} {}, email: {}, phone: {}",
            maskData(firstName),
            maskData(lastName),
            maskData(email),
            maskData(phone)
        );
        var reader = new ReaderEntity(
            UUID.randomUUID(),
            firstName,
            lastName,
            email,
            phone,
            Instant.now()
        );
        reader = readerJpaRepository.save(reader);
        log.info("reader {} created", reader.getId());
        return readerMapper.toDto(reader);
    }

    ReaderEntity getReaderEntityById(UUID id) {
        log.debug("searching for reader with id {}", id);
        return readerJpaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
    }

    /**
     * only used in data population
     */
    public List<ReaderDto> getAllReaders() {
        log.debug("getting all readers");

        var result = TimerRunner.supply(() -> readerJpaRepository.findAll()
            .stream()
            .map(readerMapper::toDto)
            .toList());

        log.info(
            "got all books, current amount {} (took {} ms)",
            result.data().size(),
            result.timeMs()
        );
        return result.data();
    }

    /**
     * only used in data population
     */
    public long countReaders() {
        log.debug("counting readers");

        var result = TimerRunner.supply(readerJpaRepository::count);

        log.info(
            "readers counted, amount: {} (took {} ms)",
            result.data(),
            result.timeMs()
        );
        return result.data();
    }

    public ReaderDto getReaderDtoById(UUID id) {
        return readerMapper.toDto(getReaderEntityById(id));
    }

    public ReaderDto getReaderDtoByEmail(String email) {
        log.debug("searching for reader with email {}", maskData(email));
        var reader = readerJpaRepository.findByEmail(email)
            .map(readerMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, "email", email));
        log.debug("reader with email {} found, id {}", maskData(email), reader.id());
        return reader;
    }

    public ReaderDto getReaderDtoByPhone(String phone) {
        log.debug("searching for reader with phone {}", maskData(phone));
        var reader = readerJpaRepository.findByPhone(phone)
            .map(readerMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, "phone", phone));
        log.debug("reader with phone {} found, id {}", maskData(phone), reader.id());
        return reader;
    }

    public ReaderDto patchReader(
        UUID id,
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable String email,
        @Nullable String phone,
        @Nullable Instant registrationDate
    ) {
        log.debug("patching reader {}", id);
        var reader = getReaderEntityById(id);

        if (firstName != null) {
            reader.setFirstName(firstName);
            log.trace("{} reader's first name set to {}", id, maskData(firstName));
        }
        if (lastName != null) {
            reader.setLastName(lastName);
            log.trace("{} reader's last name set to {}", id, maskData(lastName));
        }
        if (email != null) {
            reader.setEmail(email);
            log.trace("{} reader's email set to {}", id, maskData(email));
        }
        if (phone != null) {
            reader.setPhone(phone);
            log.trace("{} reader's phone set to {}", id, maskData(phone));
        }
        if (registrationDate != null) {
            reader.setRegistrationDate(registrationDate);
            log.trace("{} reader's registration date set to {}", id, registrationDate);
        }

        log.info("reader {} patched", id);
        return readerMapper.toDto(reader);
    }

    public void deleteReader(UUID id) {
        log.debug("reader {} is being deleted", id);
        var reader = getReaderEntityById(id);
        readerJpaRepository.delete(reader);
        log.info("deleted reader {}", id);
    }
}
