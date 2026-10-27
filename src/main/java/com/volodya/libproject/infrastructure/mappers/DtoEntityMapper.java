package com.volodya.libproject.infrastructure.mappers;

public interface DtoEntityMapper<D, E> {

    D toDto(E entity);

    E toEntity(D dto);
}
