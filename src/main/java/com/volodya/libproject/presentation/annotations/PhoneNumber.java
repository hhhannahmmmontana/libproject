package com.volodya.libproject.presentation.annotations;

import com.volodya.libproject.presentation.annotations.validators.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "Invalid phone number (example +79996663344)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
