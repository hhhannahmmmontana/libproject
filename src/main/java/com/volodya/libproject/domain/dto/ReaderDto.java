package com.volodya.libproject.domain.dto;

import com.volodya.libproject.presentation.annotations.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record ReaderDto(

    UUID id,

    @Size(min = 1, max = 255)
    String firstName,

    @Size(min = 1, max = 255)
    String lastName,

    @Email
    String email,

    @PhoneNumber
    String phone,

    Instant registrationDate
) {}
