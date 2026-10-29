package com.volodya.libproject.domain.exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String serviceName;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
        EntityNotFoundException ex
    ) {
        return errorResponseFromException(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookOutOfStockException.class)
    public ResponseEntity<ErrorResponse> handleBookOutOfStockException(
        BookOutOfStockException ex
    ) {
        return errorResponseFromException(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException ex
    ) {
        return errorResponseFromException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex
    ) {
        return errorResponseFromException(
            ex,
            HttpStatus.BAD_REQUEST,
            "Invalid argument(s): " + Arrays.stream(ex.getDetailMessageArguments())
                .map(Object::toString)
                .filter(message -> !message.isBlank())
                .collect(Collectors.joining())
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
        DataIntegrityViolationException ex
    ) {
        return errorResponseFromException(ex, HttpStatus.CONFLICT, "Entity already exists");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return errorResponseFromException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> errorResponseFromException(Exception ex, HttpStatus httpStatus) {
        return errorResponseFromException(ex, httpStatus, ex.getLocalizedMessage());
    }

    private ResponseEntity<ErrorResponse> errorResponseFromException(
        Exception ex, HttpStatus httpStatus, String message
    ) {
        return new ResponseEntity<>(new ErrorResponse(
            serviceName,
            httpStatus.name(),
            ex.getClass().getSimpleName(),
            message,
            Instant.now()
        ), httpStatus);
    }
}
