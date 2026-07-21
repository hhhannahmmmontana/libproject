package com.volodya.infrastructure.driving.mappers;

import com.volodya.application.mappers.IntYearMapper;
import com.volodya.domain.entities.Book;
import com.volodya.infrastructure.driving.dto.BookDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = IntYearMapper.class)
public interface BookDtoMapper extends DtoMapper<Book, BookDto> {}
