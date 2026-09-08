package com.volodya.application.configuration;

import com.volodya.application.configurationproperties.ApplicationInfoConfigurationProperties;
import com.volodya.application.exceptions.EndpointNotFoundException;
import com.volodya.domain.exceptions.BookNotFoundException;
import com.volodya.domain.exceptions.IsbnAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
public final class GlobalExceptionHandler {

    private final ApplicationInfoConfigurationProperties applicationInfo;

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookNotFoundException(
        BookNotFoundException ex
    ) {
        return createErrorResponseFromException(ex);
    }

    @ExceptionHandler(IsbnAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIsbnAlreadyExistsException(
        IsbnAlreadyExistsException ex
    ) {
        return createErrorResponseFromException(ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException ex
    ) {
        return createErrorResponseFromException(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex
    ) {
        return createErrorResponseFromException(ex);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHandlerMethodValidationException(
        HandlerMethodValidationException ex
    ) {
        return createErrorResponseFromException(ex);
    }

    @ExceptionHandler(EndpointNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEndpointNotFoundException(
        EndpointNotFoundException ex
    ) {
        var request = ex.request;
        return handleNoHandlerFoundException(new NoHandlerFoundException(
            request.getMethod(),
            request.getRequestURI(),
            new ServletServerHttpRequest(request).getHeaders()
        ));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFoundException(
        NoHandlerFoundException ex
    ) {
        return createErrorResponseFromException(ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(
        Exception ex
    ) {
        return createErrorResponseFromException(ex);
    }

    private ErrorResponse createErrorResponseFromException(
        Exception ex
    ) {
        return ErrorResponse.fromException(applicationInfo.name(), ex);
    }
}
