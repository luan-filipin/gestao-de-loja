package com.gestaodeloja.estoque.mapper;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.dto.request.CategoriaRequestDto;
import com.gestaodeloja.estoque.dto.response.CategoriaResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(target = "id", ignore = true)
    Categoria toEntity(CategoriaRequestDto dto);

    CategoriaResponseDto toDto(Categoria categoria);
}
