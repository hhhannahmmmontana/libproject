package com.volodya.application.annotations.validators;

import com.volodya.application.annotations.ExactSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ExactSizeValidator implements ConstraintValidator<ExactSize, String> {

    private int[] requiredLength;

    @Override
    public void initialize(ExactSize constraintAnnotation) {
        this.requiredLength = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.stream(requiredLength).anyMatch(it -> it == value.length());
    }
}