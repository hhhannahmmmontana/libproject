package com.volodya.libproject.infrastructure.mappers;

import com.volodya.libproject.domain.dto.BookDto;
import com.volodya.libproject.infrastructure.entites.BookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookDtoEntityMapper extends DtoEntityMapper<BookDto, BookEntity> {}
