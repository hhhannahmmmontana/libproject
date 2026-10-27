package com.volodya.libproject.domain.entities;

public record Isbn(
    String value
) {

    public Isbn {
        if (value.length() != 10 && value.length() != 13) {
            throw new IllegalArgumentException("Invalid ISBN length");
        }
    }

    // i just like it that way))
    public static Isbn of(String value) {
        return new Isbn(value);
    }
}
