package com.volodya.libproject.domain.configuration.properties;

public enum DataPopulationStrategy {
    /**
     * add up to specified data amount even if db has any data
     */
    APPEND_IF_LESS,

    /**
     * add only if database is empty
     */
    EMPTY_ONLY
}
