package com.volodya.libproject.presentation.annotations.validators;

import com.volodya.libproject.presentation.annotations.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public final class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || Pattern.matches("^[+]?\\d{6,15}$", value);
    }
}
