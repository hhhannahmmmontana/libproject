package com.volodya.infrastructure.driving.mappers;

public interface DtoMapper<T, U> {

    T toDomain(U dtoEntity);

    U toDto(T domainEntity);
}
