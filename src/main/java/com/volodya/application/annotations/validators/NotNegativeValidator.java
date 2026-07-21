package com.volodya.application.annotations.validators;

import com.volodya.application.annotations.NotNegative;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class NotNegativeValidator implements ConstraintValidator<NotNegative, Number> {

    @Override
    public boolean isValid(Number number, ConstraintValidatorContext constraintValidatorContext) {
        if (number == null) {
            return true;
        }
        return new BigDecimal(number.toString()).compareTo(BigDecimal.ZERO) >= 0;
    }
}
