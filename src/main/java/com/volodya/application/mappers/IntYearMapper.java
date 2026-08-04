package com.volodya.application.mappers;

import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public final class IntYearMapper {

    public int toInt(Year year) {
        return year.getValue();
    }

    public Year toYear(int intYear) {
        return Year.of(intYear);
    }
}
