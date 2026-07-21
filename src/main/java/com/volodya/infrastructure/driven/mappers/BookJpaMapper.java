package com.volodya.infrastructure.driven.mappers;

import com.volodya.application.mappers.IntYearMapper;
import com.volodya.domain.entities.Book;
import com.volodya.infrastructure.driven.entities.BookJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = IntYearMapper.class)
public interface BookJpaMapper extends JpaMapper<Book, BookJpaEntity> {}
