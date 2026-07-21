package com.volodya.infrastructure.driven.mappers;

import com.volodya.application.mappers.IntYearMapper;
import com.volodya.domain.entities.Book;
import com.volodya.infrastructure.driven.entities.BookJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = IntYearMapper.class)
public interface BookJpaMapper extends JpaMapper<Book, BookJpaEntity> {

    @Override
    @Mapping(target = "withTotalCopies", ignore = true)
    @Mapping(target = "withAvailableCopies", ignore = true)
    Book toDomain(BookJpaEntity jpaEntity);
}
