package com.volodya.libproject.domain.mappers;

public interface DtoEntityMapper<D, E> {

    D toDto(E entity);

    E toEntity(D dto);
}
