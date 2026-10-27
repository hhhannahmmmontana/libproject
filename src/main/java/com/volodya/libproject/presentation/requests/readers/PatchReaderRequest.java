package com.volodya.libproject.presentation.requests.readers;

import com.volodya.libproject.presentation.annotations.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

public record PatchReaderRequest(

    @Nullable
    @Size(min = 1, max = 255)
    String firstName,

    @Nullable
    @Size(min = 1, max = 255)
    String lastName,

    @Nullable
    @Email
    String email,

    @Nullable
    @PhoneNumber
    String phone,

    @Nullable
    Instant registrationDate
) {}
