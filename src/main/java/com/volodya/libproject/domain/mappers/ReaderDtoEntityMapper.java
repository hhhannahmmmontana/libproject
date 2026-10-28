package com.volodya.libproject.domain.mappers;

import com.volodya.libproject.domain.dto.ReaderDto;
import com.volodya.libproject.infrastructure.entites.ReaderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReaderDtoEntityMapper extends DtoEntityMapper<ReaderDto, ReaderEntity> {}
