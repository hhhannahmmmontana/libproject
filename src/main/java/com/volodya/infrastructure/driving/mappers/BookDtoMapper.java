package com.volodya.infrastructure.driving.mappers;

import com.volodya.application.mappers.IntYearMapper;
import com.volodya.domain.entities.Book;
import com.volodya.infrastructure.driving.dto.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = IntYearMapper.class)
public interface BookDtoMapper extends DtoMapper<Book, BookDto> {

    @Override
    @Mapping(target = "withTotalCopies", ignore = true)
    @Mapping(target = "withAvailableCopies", ignore = true)
    Book toDomain(BookDto dtoEntity);
}
