package com.volodya.infrastructure.driven.mappers;

public interface JpaMapper<T, U> {

    T toDomain(U jpaEntity);

    U toJpa(T domainEntity);
}
