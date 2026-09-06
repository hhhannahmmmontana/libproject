package com.volodya.application.exceptions;

import jakarta.servlet.http.HttpServletRequest;

public class EndpointNotFoundException extends RuntimeException {

    public final HttpServletRequest request;

    public EndpointNotFoundException(HttpServletRequest request) {
        super("Endpoint not found");
        this.request = request;
    }
}
