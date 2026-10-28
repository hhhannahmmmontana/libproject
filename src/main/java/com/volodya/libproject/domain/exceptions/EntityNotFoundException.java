package com.volodya.libproject.domain.exceptions;

import java.util.UUID;

public final class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entity, String key, String value) {
        super(entity + " not found (" + key + ": " + value + ")");
    }

    public EntityNotFoundException(String entity, UUID id) {
        this(entity, "id", id.toString());
    }
}
