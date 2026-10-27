package com.volodya.libproject.presentation.requests.readers;

import com.volodya.libproject.presentation.annotations.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CreateReaderRequest(

    @Size(min = 1, max = 255)
    String firstName,

    @Size(min = 1, max = 255)
    String lastName,

    @Email
    String email,

    @PhoneNumber
    String phone
) {}
