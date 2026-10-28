package com.volodya.libproject.domain.mappers;

import com.volodya.libproject.domain.dto.BookLoanDto;
import com.volodya.libproject.infrastructure.entites.BookLoanEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookDtoEntityMapper.class, ReaderDtoEntityMapper.class})
public interface BookLoanDtoEntityMapper extends DtoEntityMapper<BookLoanDto, BookLoanEntity> {}
